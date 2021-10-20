---
layout: default
title: Getting Started
nav_order: 1
description: A testnet only wallet using the bitcoindevkit
permalink: /
---

<link rel="preconnect" href="https://fonts.googleapis.com">
<link rel="preconnect" href="https://fonts.gstatic.com" crossorigin>
<link href="https://fonts.googleapis.com/css2?family=JetBrains+Mono:wght@500&display=swap" rel="stylesheet"> 
<link rel="stylesheet" href="./styles.css">

<br/>
<!-- logos -->
<div style="display: flex; justify-content: space-evenly; align-items: center; margin-top: 1rem;">
  <!-- <p>➕</p> -->
  <img id="bitcoindevkit-logo" src="./images/header/bitcoindevkit.svg" width="120px" />
  <img id="plus-sign-1" src="./images/header/plus.png" width="30px" height="30px"/>
  <!-- <p>➕</p> -->
  <img id="android-logo" src="./images/header/android.svg" width="120px" />
</div>

<center>
  <h1 style="font-size: 42px !important; font-family: 'JetBrains Mono'; margin-top: 2rem">Bitcoindevkit Android<br>Demo Wallet</h1>
  <hr>
  <br/>
</center>

The _Android Bitcoindevkit Demo Wallet_ (we're calling it the _Devkit Wallet_ for short) is a simple testnet Bitcoin wallet built as a reference app for the [bitcoindevkit](https://github.com/bitcoindevkit) on Android. It is purposely lean on Android-specific bells and whistles in order to keep the focus on bitcoin fundamentals and the bitcoindevkit API.

The repository is built to help newcomers to the bitcoindevkit by layering complexity slowly while not adding too much UI polish to the app.

The repository works in the following way: multiple branches are maintained in parallel, each of them focusing on a version of the app.
1. the default branch `docs` is mostly empty, and is used for docs
2. the `ui` branch builds the basic design and UI without any functionality
3. the `simple-wallet` branch builds a simple bitcoin wallet which implements the core functionality one would expect from a wallet: create addresses, send, receive, display transaction history, and BIP84-compatible wallet recovery.

Each of those branches has a documentation page associated with it.

# Prerequisites
These tutorials assume you have a working Android development setup, including Android Studio 4.2.2 or above and a phone you can launch the app on or a working emulator on your development machine. It also requires you have downloaded or built the bitcoindevkit library for Android ([bdk-jni](https://github.com/bitcoindevkit/bdk-jni)). To ensure you can run through this tutorial without problems, you can build the `master` branch of the [test app](https://github.com/thunderbiscuit/android-bdk-development-testapp). The readme is comprehensive and should guide you through installing the proper software and testing the app.
