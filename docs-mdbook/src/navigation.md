# Navigation Between Activities

You can think of _activities_ in Android as the basic building blocks for apps. They group together multiple related "screens" (called _fragments_). Activities are heavier and costlier than fragments, and most apps do not require many activities (in fact many Android applications are single-activity applications).

Our wallet has 3 activities:
1. A `Dispatch` activity, which is the entry point of the app. The purpose of the dispatch activity is to launch the user into the proper following activity (either `Wallet` or `WalletChoice`). The dispatch activity checks if the user already has a wallet saved, and if so, launches the wallet directly. If not, it launches the `WalletChoice` activity.
2. The `WalletChoice` activity. This is where users can either create a new wallet or recover one from a BIP39 seedphrase. Once a wallet is created (or recovered), it launches the `Wallet` activity.
3. The `Wallet` activity is where the bulk of the application lives.

We start activities using `Intent`s, as in the following codeblock, which launches the `WalletActivity`:
```kotlin
// WalletChoiceActivity.kt
val intent: Intent = Intent(this, WalletActivity::class.java)
startActivity(intent)
```