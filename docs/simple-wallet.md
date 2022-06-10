# Building a Simple Testnet Wallet
This page is the second part of a walkthrough tutorial of the [DevKit Wallet codebase](https://github.com/thunderbiscuit/devkit-wallet).

Note that this page concerns itself with the `simple-wallet` branch of the repository.

We break the journey of building the wallet into 6 distinct steps:
1. Create a Wallet object with the Repository design pattern
2. Implement receive and sync functionalities
3. Implement send functionality
4. Query and display transaction history
5. Display recovery phrase
6. Implement wallet recovery from BIP39 words

# Task 1: Add Wallet and Repository objects
This is where things get interesting on the bitcoin side of things. This task introduces 2 new objects: the `Wallet` object and the `Repository` object.

Both are initialized on startup by the `DevkitWalletApplication` class, with some properties they need to function (wallet path and shared preferences respectively).

## Wallet object
The `Wallet` class is our window to the bitcoindevkit. It's the only class that interacts with the bitcoindevkit directly and you'll find in there most of the API. Methods like `createWallet()`, `loadExistingWallet()`, and `recoverWallet()` allow you to generate/recover wallets on startup, and methods like `sync()`, `getNewAddress()`, and `getBalance()` provide the necessary interactions one would expect from a bitcoin wallet library.

## Repository object
The _Repository_ design pattern is common in Android applications. The idea is to create a layer of separation between the UI (activities, fragments) and the data they need to function. A `Repository` class is often used as the bridge between the two. For example, a composable might need to display a list of friends the user has, and that list might be available from different locations (say a ping to a microservice, or a lookup in a local cache). It's important to pull that sort of decision/code away from UI components. This is typically the sort of thing that the repository will do; make decisions as to where and how to get data for the UI fragments that request it.

For us this shows up when the `DevkitWalletActivity` tries to decide if the user already has a wallet initialized upon launch. In this case the activity simply asks the `Repository` the question
```kotlin
Repository.doesWalletExist()
```
and doesn't care how the Repository knows (in this example the repository uses a boolean value stored in [shared preferences](https://developer.android.com/training/data-storage/shared-preferences)). Shared preferences are a way to store small amounts of data quickly without requiring a database. Common use cases are small strings and booleans (like choice of color theme, whether something has been completed, etc.).

## Using the bitcoindevkit
We can see the library in action through the logs, for example when creating a new wallet, generating new addresses, and syncing. Log statements are scattered through the app and look like this:
```kotlin
Log.i(TAG, "Loading existing wallet, descriptor is ${initialWalletData.descriptor}")
```
