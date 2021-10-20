# Basic Android Activity

<center>
  <img class="screenshot" src="./images/task-1.png" width="300px" style="border: solid 2px #ababab;" />
</center>

Our first tag is the easiest to accomplish yet one of the more complicated one to understand if you are new to Android. While the goal of this tutorial is not to teach the Android framework in depth, we nonetheless need to go over the main pieces of the puzzle at play here. Note that almost all of the code at this point was generated automatically by Android Studio (you can created similar empty shell apps by choosing `New Project -> Empty Activity -> [Choose options...]`).

Some of the important files and directories at this point are:

### 1. The `build.gradle.kts` files
[Gradle](https://docs.gradle.org/current/userguide/userguide.html) is the build tool used by Android to describe the compilation steps for your app. The `build.gradle.kts` files use a Kotlin Domain Specific Language (DSL) to describe those steps, and some of the configuration options.

### 2. Files in the `app/src/main/`
The `main` directory breaks into two major parts: the Kotlin source code files and the resources files. The Kotlin source file define behavior on the application, whereas the resouces are files like layouts, colors, strings, themes, images, icons, etc.)

### 3. The `app/src/main/AndroidManifest.xml` file
The Android Manifest file describes the activities that are registered for the app, the permissions that the app will requires (internet, camera, etc.), as well as some other metadata information necessary for the OS to start your application.
