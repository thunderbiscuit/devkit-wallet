<!-- logos -->
<div style="display: flex; justify-content: space-evenly; align-items: center; margin-top: 1rem;">
  <!-- <p>➕</p> -->
  <img id="bitcoindevkit-logo" src="./images/header/bitcoindevkit.svg" width="120px" />
  <img id="plus-sign-1" src="./images/header/plus.png" width="30px" height="30px"/>
  <!-- <p>➕</p> -->
  <img id="android-logo" src="./images/header/android.svg" width="120px" />
</div>

<center>
  <h1 style="font-size: 42px !important; font-family: 'JetBrains Mono', monospace; margin-top: 2rem">Bitcoindevkit Android<br>Demo Wallet</h1>
  <hr>
  <br/>
</center>

The _Android Bitcoindevkit Demo Wallet_ (we're calling it the _Devkit Wallet_ for short) is a simple testnet Bitcoin wallet built as a reference app for the [bitcoindevkit](https://github.com/bitcoindevkit) on Android. It is purposely lean on Android-specific bells and whistles in order to keep the focus on bitcoin fundamentals and the bitcoindevkit API.

The repository is built to help newcomers to the bitcoindevkit by layering complexity slowly while not bloating the codebase with too much UI polish.

The repository works in the following way: multiple app are maintained in parallel, each of them focusing on showcasing different levels of integration between the bitcoin development kit and the applications.  

1. The **ui-only** app showcases the basic design and UI without any functionality.
2. The **simple-wallet** app showcases a simple bitcoin wallet which implements the core functionality one would expect from a wallet: create addresses, send, receive, display transaction history, and wallet recovery.
3. The **advanced-features** app showcases some of the more advanced features of the bitcoin development kit library, like sending to multiple recipients, replace-by-fee (BIP125), custom Electrum server, and creating OP_RETURN outputs.
4. The **kpm** apps showcase how to build Android and iOS apps using the same codebase and sharing common code for bitcoin-related logic while writing the user interface in the native languages (Kotlin for Android and Swift for iOS).

Each of those apps has a documentation page associated with it, accessible with the navbar above.

<br>
