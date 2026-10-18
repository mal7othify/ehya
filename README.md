# Ehya | إحياء

Available on [Google Play](https://play.google.com/store/apps/details?id=com.eillia.ehya)

Ehya shows forgotten sunan so we can revive them. It is a single Kotlin Multiplatform code base
that ships to **Android**, **iOS** and the **web** from one Compose Multiplatform UI.

## Project layout

| Module / folder | What it is |
|-----------------|------------|
| `shared/`       | All app code: Compose Multiplatform UI, Navigation 3, ViewModels, Room 3 database, Koin modules. Targets Android, iOS (arm64 + simulator) and Wasm. |
| `androidApp/`   | Android application shell: `Application`, `MainActivity`, Firebase Cloud Messaging, splash screen, signing. |
| `iosApp/`       | Xcode project. SwiftUI hosts the shared Compose UI through `MainViewController()`. |
| `webApp/`       | Browser entry point (Kotlin/Wasm). Produces a static site. |
| `shared/webWorker/` | Local npm package with the SQLite Web Worker used by Room on the web. |
| `db-script/`    | The source `sunan.json` and the Python script that once built the SQLite asset. The app now seeds from the bundled JSON. |

## Stack

* [Compose Multiplatform](https://www.jetbrains.com/compose-multiplatform/) with Material 3.
* [Navigation 3](https://developer.android.com/guide/navigation/navigation-3): typed, serializable routes and an app-owned back stack.
* Material 3 Adaptive: `NavigationSuiteScaffold` (bottom bar / rail / drawer by window size) and a
  list-detail scene for the sunan list on tablets, foldables, desktops and wide browser windows.
* [Room 3](https://developer.android.com/jetpack/androidx/releases/room3) on every platform. Android and iOS use the bundled SQLite driver,
  the web uses `WebWorkerSQLiteDriver` with a worker that stores the database in the browser's
  Origin Private File System.
* [Koin](https://insert-koin.io/) for dependency injection on Android, iOS and the web. Shared
  bindings live in `shared/src/commonMain/.../di/AppModules.kt`; each platform adds a small module
  with its Room driver, share service and app info, and screens get their ViewModels with
  `koinViewModel()`.
* Kotlin coroutines, Flow and kotlinx.serialization.
* Compose resources for strings, fonts, drawables and the bundled `sunan.json`.
* Firebase Cloud Messaging, Analytics and Crashlytics on Android.
* Kermit for logging.

## Features

* Browse all sunan, filter them by category and share one.
* Open a sunnah to read the full hadith, its grading and how to do it.
* Play: swipe right to mark a sunnah as tried, swipe left to pass; passed cards come back later in the round.
* Share the app.
* Adaptive layouts for phones, tablets, foldables and the browser.
* A seamless launch: the system splash hands over to the in-app logo at the same size and spot,
  which then springs to life while the data loads.

## Screenshots

<img src="/screenshots/1.jpg" width="260">&emsp;<img src="/screenshots/2.jpg" width="260">&emsp;<img src="/screenshots/3.jpg" width="260">

<img src="/screenshots/4.jpg" width="260">&emsp;<img src="/screenshots/5.jpg" width="260">&emsp;<img src="/screenshots/6.jpg" width="260">

## Build and run

Requirements: JDK 17 or newer, Android Studio (or the Android SDK), Xcode 16+ for iOS.
On Apple silicon make sure `JAVA_HOME` points at an arm64 JDK; an x86_64 one makes
Kotlin/Native treat the host as Intel.

### Android

```shell
./gradlew :androidApp:installDebug                       # device or emulator must be connected
adb shell am start -n com.eillia.ehya/.MainActivity
```

Create a Firebase project and put its `google-services.json` in `androidApp/`.
Release signing reads `keystore.properties` in the project root (see `androidApp/build.gradle.kts`);
without the file the release build is simply unsigned.

### iOS

Open `iosApp/iosApp.xcodeproj` in Xcode and run the `iosApp` scheme. Set your team in
`iosApp/Configuration/Config.xcconfig`. The Kotlin framework is built by the
"Compile Kotlin Framework" build phase, which runs Gradle. Xcode starts scripts with a bare
environment, so the phase picks the first installed JDK 17-25 that runs natively; set `JAVA_HOME`
in Xcode's scheme environment if you want a specific one.

The app icon is an Icon Composer package, `iosApp/iosApp/AppIcon.icon`, built from the same
artwork as `shared/src/commonMain/composeResources/files/ehya-icon.svg`. Xcode renders the
light, dark, clear and tinted variants from its layers on iOS 26 and newer and flattens a
fallback for older versions, so there is no icon set to maintain by hand. Open the package in
Icon Composer (bundled with Xcode 26+) to adjust depth, glass and shadow.

From the terminal, build for a booted simulator, install and launch:

```shell
xcrun simctl boot "iPhone 15 Pro" && open -a Simulator
xcodebuild -project iosApp/iosApp.xcodeproj -scheme iosApp -configuration Debug \
  -destination 'platform=iOS Simulator,name=iPhone 15 Pro' -derivedDataPath build/ios build
xcrun simctl install booted build/ios/Build/Products/Debug-iphonesimulator/Ehya.app
xcrun simctl launch booted com.eillia.ehya
```

The launch screen is a plain image, `iosApp/iosApp/Assets.xcassets/LaunchLogo.imageset`, rendered
from `ehya-icon.svg` at 1x, 2x and 3x with a transparent background. iOS caches launch screens, so
after changing it delete the app and restart the device.

### Web

```shell
./gradlew :webApp:wasmJsBrowserDevelopmentRun   # dev server with hot reload
./gradlew :webApp:wasmJsBrowserDistribution     # static site in webApp/build/dist/wasmJs/productionExecutable
```

The web app needs a browser with WasmGC support (Chrome 119+, Firefox 120+, Safari 18.2+).

### Tests

```shell
./gradlew :shared:testAndroidHostTest        # common tests on the JVM
./gradlew :shared:iosSimulatorArm64Test      # common tests on the iOS simulator
./gradlew :shared:wasmJsBrowserTest          # common tests in a headless browser
./gradlew spotlessCheck                      # formatting (ktlint)
```

## Contribute

Please, feel free to contribute using the following approaches -other approaches are welcome too!
😻-:

### By pushing PRs to this repo

TBA

### By joining the testers list

Send your Google Play email to me at: ehyaapplication@gmail.com

## License

Ehya is released under the [MIT License](LICENSE).

```
Copyright (c) 2022-2026 Maryam Alhuthayfi
SPDX-License-Identifier: MIT
```
