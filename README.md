# Bitcoindevkit Android Demo Wallet

<div align="center">
    <img src="./images/screenshots.png" width="600" />
</div>

The _Android Devkit Demo Bitcoin Wallet_ (we're calling it _Devkit Wallet_ for short) is a simple testnet Bitcoin wallet built as a reference app for the [bitcoindevkit](https://github.com/bitcoindevkit) on Android. It is purposely lean on Android-specific bells and whistles in order to keep the focus on bitcoin fundamentals and the bitcoindevkit API.

The repository is built to help newcomers to the bitcoindevkit by layering complexity slowly while not adding too much UI polish to the app.

The repository works in the following way: multiple branches are maintained in parallel, each of them focusing on a specific version of the app.
1. the default branch is the `simple-wallet` branch. It builds a simple bitcoin wallet which implements the core functionality one would expect from a wallet: create addresses, send, receive, display transaction history, and BIP84-compatible wallet recovery.
2. The `advanced-features` branch showcases the more advanced features of the library
3. the `ui` branch builds the basic design and UI without any functionality
4. the `docs` branch is mostly empty and is used for docs

<br/>

## Feature list
### `simple-wallet` branch
- [x] Receive
- [x] Send
- [x] Sync wallet using Electrum
- [x] Retrieve transaction history
- [x] Wallet recovery using BIP39 recovery phrases

### `advanced-features` branch
- [ ] Send to multiple recipients
- [ ] "Send All" functionality
- [ ] Replace-by-Fee (BIP125)
- [ ] Choose custom Electrum server
- [ ] Partial signatures on PSBTs
- [ ] Create OP_RETURN outputs

<br/>

## Design Choices
The Devkit Wallet makes rather opinionated choices regarding its design and architecture, all with the goal of being as helpful to potential bitcoindevkit developers exploring the library and looking for a working example. The end users of this application are developers, not real users. Here are some of these choices:
1. Be lean on Android-specific bells and whistles.
2. Return types are explicitly stated for all code that touches bitcoin, even when the IDE doesn't like it.
3. Some bugs are left in and considered acceptable if squashing them requires a lot of polish/boilerplate code that does not add to the understanding of the Bitcoin Dev Kit.
4. Errors are not always communicated back to the user on-screen (for example through polished snackbars) for similar reasons as (3); if something is not working, take a look a the logcat output.
5. The wallet does not make use of string resources. This makes the codebase easier to search and simpler to parse on GitHub and platforms where the IDE doesn't show inline replacements of the string resources. In short, string resources, while a best practice, do not improve readability, the prime focus of this sample application.

<br/>

## Companion Documentation Website

<div align="center">
    <img src="./images/docs.png" width="500" />
</div>

You can find the companion tutorial website for this repository [here](https://thunderbiscuit.github.io/devkit-wallet/). To learn more about the bitcoindevkit, check out [the shiny new docs here](https://bitcoindevkit.org/).
