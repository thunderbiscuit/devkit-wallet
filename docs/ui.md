# Building the UI
This page is the UI part of a walkthrough tutorial of the [DevKit Wallet codebase](https://github.com/thunderbiscuit/devkit-wallet).

Note that this page concerns itself with the `ui` branch of the repository.

The user interface for the Devkit Wallet is built using Jetpack Compose, the new, modern way to build user interfaces on Android.

Some of the important files and directories at this point are:
### 1. The `build.gradle.kts` files
Gradle is the build tool used by Android to describe the compilation steps for your app. The `build.gradle.kts` files use a Kotlin Domain Specific Language (DSL) to describe those steps, and some of the configuration options.

### 3. Files in the `app/src/main/` directory
The main directory breaks into two major parts: the Kotlin source code files and the resources files. The Kotlin source files define look and behavior on the application, whereas the resources are files are static things like strings, images, icons, etc.

### 4. The `app/src/main/AndroidManifest.xml` file
The Android Manifest file describes the activities that are registered for the app, the permissions that the app will requires (internet, camera, etc.), as well as some other metadata information necessary for the OS to start your application.

<br/>

# Part 1: Activity and Navigation
The Devkit Wallet is what is known as a _single activity application_. All screens are contained in this one activity (a low-level Android construct), and we navigate between the screens using a navigation component. Take a look at the [`HomeNavigation`](https://github.com/thunderbiscuit/devkit-wallet/blob/ui/app/src/main/java/com/goldenraven/devkitwallet/ui/HomeNavigation.kt) composable and notice how it allows you to navigate to 3 different screens: the `WalletScreen`, the `AboutScreen`, and the `RecoveryPhraseScreen`.
