# Building the UI
This page is the UI part of a walkthrough tutorial of the [DevKit Wallet codebase](https://github.com/thunderbiscuit/devkit-wallet).

Note that this page concerns itself with the `ui` branch of the repository.

The user interface for the Devkit Wallet is built using [Jetpack Compose](https://developer.android.com/jetpack/compose), the new, modern way to build user interfaces on Android. Going over the how Compose works is outside the scope of this documentation, but we'll point out the important parts that should help you understand how it all comes together on the UI side of things.

Some of the important files and directories at this point are:
### 1. The `build.gradle.kts` files
Gradle is the build tool used by Android to describe the compilation steps for your app. The `build.gradle.kts` files use a Kotlin Domain Specific Language (DSL) to describe those steps, and some of the configuration options.

### 2. Files in the `app/src/main/` directory
The main directory breaks into two major parts: the Kotlin source code files and the resources files. The Kotlin source files define look and behavior on the application, whereas the resources are files are static things like strings, images, icons, etc.

### 3. The `app/src/main/AndroidManifest.xml` file
The Android Manifest file describes the activities that are registered for the app, the permissions that the app will requires (internet, camera, etc.), as well as some other metadata information necessary for the OS to start your application.

<br/>

# Part 1: Activity and Navigation
The Devkit Wallet is what is known as a _single activity application_. All screens are contained in this one activity (a low-level Android construct), and we navigate between the screens using a navigation component. Take a look at the [`HomeNavigation`](https://github.com/thunderbiscuit/devkit-wallet/blob/ui/app/src/main/java/com/goldenraven/devkitwallet/ui/HomeNavigation.kt) composable and notice how it allows you to navigate to 3 different screens: the `WalletScreen`, the `AboutScreen`, and the `RecoveryPhraseScreen`.

The wallet is broken into 3 navigation graphs (`CreateWallet`, `Home`, and `Wallet`). Upon launch, the app will identify whether a wallet has already been created/loaded, and if not, fire up the `CreateWalletNavigation` graph (2 screens: create a new wallet and recover a existing wallet).

<center>
  <video height="600" controls>
    <source src="../images/videos/nav1.mp4" type="video/mp4">
    Your browser does not support the video tag.
  </video>
</center>

If a wallet already exists on the device, the `HomeNavigation` graph is launched. This graph contains the home wallet screen with the drawer (containing the About and Recovery Phrase screens).

<center>
  <video height="600" controls>
    <source src="../images/videos/nav2.mp4" type="video/mp4">
    Your browser does not support the video tag.
  </video>
</center>


The home wallet screen itself is simply a container for the `WalletNavigation` graph, which handles the core Home, Receive, Send, and Transactions screens.

<center>
  <video height="600" controls>
    <source src="../images/videos/nav3.mp4" type="video/mp4">
    Your browser does not support the video tag.
  </video>
</center>

The screens themselves are built using what are known as _composables_, functions that build UI using the declarative paradigm for building user interfaces. You'll find them fairly intuitive at a glance; they mostly compose together a series of rows, columns, texts, buttons, and other common UI components into complete screens.
