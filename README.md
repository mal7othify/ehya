# Ehya

Ehya (إحياء) presents forgotten sunan so they can be revived. It is a single Kotlin Multiplatform
code base with one Compose Multiplatform UI that ships to Android, iOS and the web.

The Android app is available on
[Google Play](https://play.google.com/store/apps/details?id=com.eillia.ehya).

## Features

- Browse all sunan, filter them by category and share any of them.
- Open a sunnah to read the full hadith, its grading and how to perform it.
- Play: swipe right to mark a sunnah as tried, swipe left to pass. Passed cards return later in
  the round.
- Share the app.
- Adaptive layouts for phones, tablets, foldables and browser windows.
- A seamless launch: the system splash hands over to the in-app logo at the same size and position,
  which then animates while the data loads.

## Screenshots

<img src="/screenshots/1.jpg" width="260">&emsp;<img src="/screenshots/2.jpg" width="260">&emsp;<img src="/screenshots/3.jpg" width="260">

<img src="/screenshots/4.jpg" width="260">&emsp;<img src="/screenshots/5.jpg" width="260">&emsp;<img src="/screenshots/6.jpg" width="260">

## Project layout

| Path                | Purpose |
|---------------------|---------|
| `shared/`           | All application code: Compose Multiplatform UI, Navigation 3, ViewModels, Room 3 database, Koin modules and resources. Targets Android, iOS (device and simulator) and Wasm. |
| `androidApp/`       | Android application shell: `Application`, `MainActivity`, Firebase Cloud Messaging, splash screen and signing. |
| `iosApp/`           | Xcode project. SwiftUI hosts the shared Compose UI through `MainViewController()`. |
| `webApp/`           | Browser entry point (Kotlin/Wasm) that produces a static site. |
| `shared/webWorker/` | Local npm package with the SQLite Web Worker used by Room on the web. |
| `db-script/`        | The source `sunan.json` and the script that once built the SQLite asset. The app now seeds from the bundled JSON. |

## Stack

- [Compose Multiplatform](https://www.jetbrains.com/compose-multiplatform/) with Material 3.
- [Navigation 3](https://developer.android.com/guide/navigation/navigation-3): typed, serializable
  routes and an app-owned back stack.
- Material 3 Adaptive: `NavigationSuiteScaffold` (bottom bar, rail or drawer by window size) and a
  list-detail scene for the sunan list on large windows.
- [Room 3](https://developer.android.com/jetpack/androidx/releases/room3) on every platform.
  Android and iOS use the bundled SQLite driver; the web uses `WebWorkerSQLiteDriver` with a worker
  that stores the database in the browser's Origin Private File System.
- [Koin](https://insert-koin.io/) for dependency injection. Shared bindings live in
  `shared/src/commonMain/kotlin/com/eillia/ehya/di/AppModules.kt`; each platform adds a small
  module with its Room driver, share service and app info, and screens obtain their ViewModels with
  `koinViewModel()`.
- Kotlin coroutines, Flow and kotlinx.serialization.
- Compose resources for strings, fonts, drawables and the bundled `sunan.json`.
- Firebase Cloud Messaging, Analytics and Crashlytics on Android.
- Kermit for logging.

## Build and run

Requirements: JDK 17 or newer, Android Studio or the Android SDK, and Xcode 16 or newer for iOS.
On Apple silicon, `JAVA_HOME` must point at an arm64 JDK; an x86_64 JDK makes Kotlin/Native treat
the host as Intel.

### Android

```shell
./gradlew :androidApp:installDebug                       # a device or emulator must be connected
adb shell am start -n com.eillia.ehya/.MainActivity
```

The build works out of the box. When the gitignored `androidApp/google-services.json` is missing,
it is created from `androidApp/google-services.placeholder.json` and Firebase stays offline. To
enable push notifications, analytics and crash reports, replace it with the file from your Firebase
project.

Release signing reads `keystore.properties` in the project root (see `androidApp/build.gradle.kts`).
Without that file the release build is unsigned.

### iOS

Open `iosApp/iosApp.xcodeproj` in Xcode and run the `iosApp` scheme. Set your team in
`iosApp/Configuration/Config.xcconfig`. The Kotlin framework is built by the "Compile Kotlin
Framework" build phase, which runs Gradle. Xcode starts scripts with a bare environment, so the
phase picks the first installed JDK 17 to 25 that runs natively; set `JAVA_HOME` in the scheme
environment to choose a specific one.

From the terminal, build for a booted simulator, then install and launch:

```shell
xcrun simctl boot "iPhone 15 Pro" && open -a Simulator
xcodebuild -project iosApp/iosApp.xcodeproj -scheme iosApp -configuration Debug \
  -destination 'platform=iOS Simulator,name=iPhone 15 Pro' -derivedDataPath build/ios build
xcrun simctl install booted build/ios/Build/Products/Debug-iphonesimulator/Ehya.app
xcrun simctl launch booted com.eillia.ehya
```

The app icon is an Icon Composer package, `iosApp/iosApp/AppIcon.icon`, built from the same
artwork as `shared/src/commonMain/composeResources/files/ehya-icon.svg`. Xcode renders the light,
dark, clear and tinted variants on iOS 26 and newer and flattens a fallback for older versions.

The launch screen is a plain image, `iosApp/iosApp/Assets.xcassets/LaunchLogo.imageset`, rendered
from `ehya-icon.svg` at 1x, 2x and 3x with a transparent background. iOS caches launch screens, so
after changing it delete the app and restart the device.

### Web

```shell
./gradlew :webApp:wasmJsBrowserDevelopmentRun   # development server with hot reload
./gradlew :webApp:wasmJsBrowserDistribution     # static site in webApp/build/dist/wasmJs/productionExecutable
```

The web app needs a browser with WasmGC support: Chrome 119, Firefox 120, Safari 18.2 or newer.

### Tests and checks

```shell
./gradlew :shared:testAndroidHostTest        # common tests on the JVM
./gradlew :shared:iosSimulatorArm64Test      # common tests on the iOS simulator
./gradlew :shared:wasmJsBrowserTest          # common tests in a headless browser
./gradlew spotlessCheck                      # formatting (ktlint); spotlessApply fixes it
```

## Contributing

Contributions are welcome. Please read the [contribution guide](CONTRIBUTING.md) first; it is short.

- Found a bug or have an idea? [Open an issue](https://github.com/mal7othify/ehya-android/issues)
  and describe the platform and the steps to reproduce.
- Want to change something? Open a pull request from a branch of your fork. Every change must keep
  Android, iOS and the web working, and the pull request template lists what to check.
- Want to help test the Android app before release? Send your Google Play email address to
  ehyaapplication@gmail.com.

## License

Ehya is released under the [MIT License](LICENSE).

```
Copyright (c) 2022-2026 Maryam Alhuthayfi
SPDX-License-Identifier: MIT
```
