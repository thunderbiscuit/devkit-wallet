# Bitcoindevkit Android Demo Wallet

The _Android Devkit Demo Bitcoin Wallet_ (we're calling it _AddBit Wallet_ for short) is a simple testnet Bitcoin wallet built as a reference app for the [bitcoindevkit](https://github.com/bitcoindevkit) on Android. It is purposely lean on Android-specific bells and whistles in order to keep the focus on bitcoin fundamentals and the bitcoindevkit API.

The repository is built to help newcomers to the bitcoindevkit by layering complexity slowly while not adding too much UI polish to the app.

The repository works in the following way: multiple branches are maintained in parallel, each of them focusing on a version of the app.
1. the default branch `docs` is mostly empty, and is used for docs
2. the `basic-ui` branch builds the basic design and UI without any functionality
3. the `onchain-basic` branch builds a simple bitcoin wallet which implements the core functionality one would expect from a wallet: create addresses, send, receive, display transaction history, and BIP84-compatible wallet recovery.

You can find the companion tutorial website [here](https://thunderbiscuit.github.io/bitcoindevkit-android-sample-app/).
