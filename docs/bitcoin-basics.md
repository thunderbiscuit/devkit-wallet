---
layout: default
title: Basic Wallet
nav_order: 3
description: A testnet only wallet using the bitcoindevkit
permalink: /basic-wallet
---

# [Task 5](): Add Wallet and Repository objects
This is where things get interesting on the bitcoin side of things. This task introduces 2 new objects: the `Wallet` object and the `Repository` object.

Both are initialized on startup by the `SobiWalletApplication` class, with some properties they need to function (wallet path and shared preferences respectively).

## Wallet object
The `Wallet` class is our window to the bitcoindevkit. It's the only class that interacts with the bitcoindevkit direclty and you'll find in there most of the API. Methods like `createWallet()`, `loadExistingWallet()`, and `recoverWal`let()` allow you to generate/recover wallets on startup, and methods like `sync()`, `getNewAddress()`, and `getBalance()` provide the necessary interactions one would expect from a bitcoin library.

Note that because the bitcoindevkit is a native library (it is not written in Kotlin/Java and is provided as binaries to the OS), the library get "loaded" on initialization through the `init` block:
```kotlin
object Wallet {
    private val lib: Lib
    
    init {
        // load bitcoindevkit native library
        Lib.load()
        lib = Lib()
    }
    // ...
}
```
The library is then accessible throughout the class, and most methods use it like so:
```kotlin
fun getNewAddress(): String {
    return lib.get_new_address(walletPtr)
}

fun getBalance(): Long {
    return lib.get_balance(walletPtr)
}
```

The library comes with a few types (`ExtendedKey`, `CreateTxResponse`, `SignResponse`, etc.) which can be investigated by looking at the source code [here](https://github.com/bitcoindevkit/bdk-jni/tree/master/library/src/main/java/org/bitcoindevkit/bdkjni).

## Repository object
The _Repository_ design pattern is very common in Android applications. The idea is to create a layer of separation between the UI (activities, fragments) and the data they need to function. A `Repository` class is often used as the bridge between the two. For example, a fragment might need to query a list of friends the user has, and that list might be available from different locations (say a ping to a microservice, or a lookup in a local cache). It's important to pull that sort of decision/code away from UI fragments. This is typically the sort of thing that the Repository will do; make decisions as to where and how to get data for the UI fragments that request it. 

For us this shows up when the `DispatchActivity` tries to decide if the user already has a wallet initialized upon launch. In this case the activity simply asks the `Repository` the question
```kotlin
Repository.doesWalletExist()
```
and doesn't care how the Repository knows (in this example the repository uses a boolean value stored in [shared preferences](https://developer.android.com/training/data-storage/shared-preferences)). Shared preferences are a way to store small amounts of data quickly without requiring a database. Common use cases are small strings and booleans (like choice of color theme, whether something has been completed, etc.).

## Using the bitcoindevkit
We can see the library in action through the logs, for example when creating a new wallet, or when pressing the new `generateNewAddressButton` on the receive fragment:
```kotlin
binding.generateNewAddressButton.setOnClickListener {
    Log.i("SobiWallet", "${Wallet.getNewAddress()}")
}
```

# [Task 6](): Implement receive and sync
It's now time to connect the `Wallet` object to the user interface. Note how the `generateNewAddressButton` has on `onClickListener` that triggers the `displayNewAddress()` method:
```kotlin
// ReceiveFragment.kt

override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    super.onViewCreated(view, savedInstanceState)

    val navController = Navigation.findNavController(view)
    binding.receiveToWalletButton.setOnClickListener {
        navController.navigate(R.id.action_receiveFragment_to_walletFragment)
    }
    binding.generateNewAddressButton.setOnClickListener {
        displayNewAddress()
    }
}

private fun displayNewAddress() {
    val newGeneratedAddress: String = Wallet.getNewAddress()
    Log.i("SobiWallet", "New deposit address is $newGeneratedAddress")

    val qrgEncoder: QRGEncoder = QRGEncoder(newGeneratedAddress, null, QRGContents.Type.TEXT, 1000)
    qrgEncoder.colorBlack = ContextCompat.getColor(requireContext(), R.color.night_1)
    qrgEncoder.colorWhite = ContextCompat.getColor(requireContext(), R.color.snow_1)
    try {
        val bitmap = qrgEncoder.bitmap
        binding.qrCode.setImageBitmap(bitmap)
    } catch (e: Throwable) {
        Log.i("SobiWallet", "Error with QRCode generator, ${e.toString()}")
    }
    binding.receiveAddress.text = newGeneratedAddress
}
```
The `displayNewAddress()` method calls `Wallet.getNewAddress()` and uses the bindings on the `qrCode` (an image) and `receiveAddress` (text) views to populate the screen with the proper address.

## QR codes
QR codes are generated using a library called zxing (you'll find the new dependency in the `/app/build.gradle.kts` file).

## Sync
The sync functionality is very simple (a simple `Wallet.sync()` will do). But note that we wish to update the UI to reflect the current balance upon sync. This is done using something called the viewmodel, a very common pattern in Android applications. ViewModels are a way to implement the [observer pattern](https://www.youtube.com/watch?v=_BpmfnqjgzQ).

Take a look at the `WalletViewModel` class:
```kotlin
class WalletViewModel(application: Application) : AndroidViewModel(application) {

    public var balance: MutableLiveData<Long> = MutableLiveData(0)

    public fun updateBalance() {
        Wallet.sync(100)
        val newBalance = Wallet.getBalance()
        Log.i("SobiWallet", "New balance is $newBalance")
        balance.postValue(newBalance)
    }
}
```

Fragment and activities can simply "observe" (subscribe to) particular variables in our ViewModel, and the ViewModel will update them as this value changes. Notice the `balance.postValue(newBalance)` call (this triggers all observers to pull the new data).

The code from the fragment looks like this:
```kotlin
viewModel.balance.observe(viewLifecycleOwner, {
    val balanceInBitcoin: Float
    if (it == 0L) {
        balanceInBitcoin = 0F
    } else {
        balanceInBitcoin = it.toFloat().div(100_000_000)
    }
    val humanReadableBalance = DecimalFormat("0.00000000").format(balanceInBitcoin)
    binding.balance.text = humanReadableBalance
})
```
This ensures that the balance displayed in the `balance` view is always up to date with the balance in the `WalletViewModel`. Easy Peasy Bitcoineesy.

<center>
  <img class="screenshot" src="./images/screenshots/task-6.gif" width="300px" />
</center>

# [Task 7](): Implement send
Sending bitcoin is a slightly more involved operation.

The bitcoindevkit workflow for this operation is as follows:
1. Create a transaction with proper data (amount, fee rate, adressees)
2. Sign the transaction
3. Extract the raw transaction
4. Broadcast it

Note that all 4 of those steps are accomplished by the `broadcastTransaction()` method of the `SendFragment`:
```kotlin
private fun broadcastTransaction() {
    try {
        // build required transaction information from text inputs
        val feeRate = 1F
        val sendToAddress: String = binding.sendToAddress.text.toString().trim()
        val sendAmount: String = binding.sendAmount.text.toString().trim()
        val addressAndAmount: List<Pair<String, String>> = listOf(Pair(sendToAddress, sendAmount))

        val transactionDetails: CreateTxResponse = Wallet.createTransaction(feeRate, addressAndAmount, false, null, null, null)
        val signResponse: SignResponse = Wallet.sign(transactionDetails.psbt)

        val rawTx: RawTransaction = Wallet.extractPsbt(signResponse.psbt)
        val txid: Txid = Wallet.broadcast(rawTx.transaction)

        Log.i("SobiWallet", "Transaction was broadcast! txid: $txid")
        showSnackbar(
            requireView(),
            SnackbarLevel.SUCCESS,
            "Transaction was broadcast successfully!"
        )
    } catch (e: Throwable) {
        Log.i("SobiWallet", "Broadcast error: ${e.message}")
        showSnackbar(
            requireView(),
            SnackbarLevel.ERROR,
            "Broadcast error: ${e.message}"
        )
    }
}
```

The other parts of this fragment are the `MaterialAlertDialog` (which we use as a confirmation step before broadcasting the transaction):
```kotlin
val broadcastTransactionDialog =
    MaterialAlertDialogBuilder(this@SendFragment.requireContext(), R.style.NordDialogTheme)
        .setTitle("Confirm transaction")
        .setMessage(buildConfirmTransactionMessage())
        .setPositiveButton("Broadcast") { _, _ ->
            Log.i("SobiWallet", "User is attempting to broadcast transaction")
            broadcastTransaction()
            navController.navigate(R.id.action_sendFragment_to_walletFragment)
        }
        .setNegativeButton("Go back") { _, _ ->
            Log.i("SobiWallet", "User is not broadcasting")
        }
broadcastTransactionDialog.show()
```

And the use of snackbars to let the user know whether the transaction has successfully been broadcast of if an error was thrown while attempting to broadcast it.
```kotlin
showSnackbar(
    requireView(),
    SnackbarLevel.ERROR,
    "Broadcast error: ${e.message}"
)
```
Take a look at `utilities/Snackbars.kt` to get a sense for how they work.

<center>
  <img class="screenshot" src="./images/screenshots/task-7.gif" width="300px" />
</center>

# [Task 8](): Add transaction history
Adding a list of transactions is a daunting task if one is to take it to a polished result. It involves using a database and keeping track on transactions, their state, and performing calculations on the raw material that the bitcoindevkit provides. This is slightly outside of the scope of this workshop. Simply displaying the list of transactions as one long string (with some small modifications), however, is quite easy, and this is what this wallet implements.

Note that the `transactionsView` is simply a `NestedScrollView` that displays a string built by the `transactionsList()` method. Creating the `confirmationTime` string variable is the most involved part of this whole endeavor, and is done using a neat Kotlin feature called _extension functions_, where we define a method on the bitcoindevkit type `ConfirmationTime` which returns a nicely formatted timestamp. Take a look at the `utilities/Timestamps.kt` file for more on this function. Building the string is otherwise a rather simple affair; the bitcoindevkit returns a list of `TransactionDetails` through the `listTransactions()` method, and we parse them one by one and pull the interesting things into a string template.

```kotlin
override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    super.onViewCreated(view, savedInstanceState)

    binding.transactionsView.text = transactionList()

    val navController = Navigation.findNavController(view)
    binding.transactionsToWalletButton.setOnClickListener {
        navController.navigate(R.id.action_transactionsFragment_to_walletFragment)
    }
}

private fun transactionList(): String {
    val rawList: List<TransactionDetails> = Wallet.listTransactions()
    var finalList: String = ""
    for (item in rawList) {
        Log.i("SobiWallet", "Transaction list item: $item")
        val confirmationTime: String = item.confirmation_time?.timestampToString() ?: "Pending"
        val transactionInfo: String =
            "Timestamp: ${confirmationTime}\nReceived: ${item.received}\nSent: ${item.sent}\nFees: ${item.fee}\nTxid: ${item.txid}"

        finalList = "$finalList\n$transactionInfo\n"
    }
    return finalList
}
```

<center>
  <img class="screenshot" src="./images/screenshots/transaction-history.png" width="300px" />
</center>

# [Task 9](): Display recovery phrase
Displaying the recovery phrase to the user is not a complicated task. Remember that we have stored the recovery phrase in shared preferences when creating the wallet
```kotlin
fun createWallet(): Unit {
    val keys: ExtendedKey = generateExtendedKey()
    val descriptor: String = createDescriptor(keys)
    val changeDescriptor: String = createChangeDescriptor(keys)
    initialize(
        descriptor = descriptor,
        changeDescriptor = changeDescriptor,
    )
    Repository.saveWallet(path, descriptor, changeDescriptor)
    Repository.saveMnemonic(keys.mnemonic)
}
```
Retreiving the recovery phrase is a simple call to the repository, which has a `getMnemonic()` method defined:
```kotlin
fun getMnemonic(): String {
    return sharedPreferences.getString("mnemonic", "No seed phrase saved") ?: "Seed phrase not there"
}
```

Upon creating the fragment, the `getMnemonic()` method is simply called to populate the recoveryPhrase text view:
```kotlin
// RecoveryPhraseFragment.kt
override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    super.onViewCreated(view, savedInstanceState)

    binding.recoveryPhrase.text = Repository.getMnemonic()
}
```

<center>
  <img class="screenshot" src="./images/screenshots/recovery-phrase.png" width="300px" />
</center>

# [Task 10](): Enable wallet recovery
Enabling wallet recovery is not complicated from the bitcoindevkit point of view, but does require a bit of work on the Android side of things. Note for example that so far, the `WalletChoiceActivity` does not contain any fragments. But here we'll need to add a screen for entering the 12 word recovery phrase, and so the first thing we need to do is create a `NavHostFragment` in the `WalletChoiceActivity`, complete with 2 fragments: our original screen and a wallet recovery screen. We also need to build a `nav_wallet_choice.xml` file, for navigating between the first and second fragments.

You'll note that the `fragment_recover.xml` layout file is a `ConstraintLayout` with a `NestedScrollView`, itself containing a `LinearLayout` which is the parent for all 12 `EditText` views where the user can input their mnemonic words. This allows for the list of words to be scrollable and ensures it shows well on all screen sizes.

The `RecoverWalletFragment` is one of our longest Kotlin file, but it really comes down to two methods used in the listener for the `recoverWalletButton`, namely `checkWords()` and `buildRecoveryPhrase()`:
```kotlin
// RecoverWalletFragment.kt
binding.recoverWalletButton.setOnClickListener {
    if (checkWords()) {
        val recoveryPhraseString = buildRecoveryPhrase()
        Wallet.recoverWallet(recoveryPhraseString)

        // launch home activity
        val intent: Intent = Intent(this@RecoverWalletFragment.context, WalletActivity::class.java)
        startActivity(intent)
    } else {
        Log.i("SobiWallet", "Recovery phrase was invalid")
    }
}
```

The `checkWords` method verifies whether the words provided are (a) not empty, and (b) part of the list of 2048 words defined in the English version of the BIP39 wordlist. It uses error snackbars to let the user know if any of the word inputs has any problems:
```kotlin
private fun checkWords(): Boolean {
    val mnemonicWordsTextViews: List<Int> = listOfNotNull<Int>(
        R.id.word1, R.id.word2, R.id.word3, R.id.word4, R.id.word5, R.id.word6,
        R.id.word7, R.id.word8, R.id.word9, R.id.word10, R.id.word11, R.id.word12,
    )

    for (word in 0..11) {
        val mnemonicWord: String = requireView().findViewById<TextView>(mnemonicWordsTextViews[word]).text.toString()
            .trim().lowercase(Locale.getDefault())
        Log.i("SobiWallet", "Verifying word $word: $mnemonicWord")

        when {
            mnemonicWord.isEmpty() -> {
                Log.i("SobiWallet", "Word #$word is empty!")
                showSnackbar(
                    requireView(),
                    SnackbarLevel.ERROR,
                    "Word #${word + 1} is empty!"
                )
                return false
            }
            mnemonicWord !in this.wordList -> {
                Log.i("SobiWallet", "Word #$word, $mnemonicWord, is not valid!")
                showSnackbar(
                    requireView(),
                    SnackbarLevel.ERROR,
                    "Word #${word + 1} is invalid!"
                )
                return false
            }
            else -> {
                Log.i("SobiWallet", "Word #$word, $mnemonicWord, is valid")
            }
        }
    }
    return true
}
```

The `buildRecoveryPhrase()` simply brings all the text inputs into one string, and returns it so it can be used by the `Wallet` class for the recovery:
```kotlin
// Wallet.kt
fun recoverWallet(mnemonic: String) {
    val keys: ExtendedKey = restoreExtendedKeyFromMnemonic(mnemonic)
    val descriptor: String = createDescriptor(keys)
    val changeDescriptor: String = createChangeDescriptor(keys)
    initialize(
        descriptor = descriptor,
        changeDescriptor = changeDescriptor,
    )
    Repository.saveWallet(path, descriptor, changeDescriptor)
    Repository.saveMnemonic(keys.mnemonic)
}
```

<center>
  <img class="screenshot" src="./images/screenshots/recover.png" width="300px" />
</center>
