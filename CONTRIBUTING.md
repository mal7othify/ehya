# Contributing to Ehya

Thank you for helping. This guide is deliberately short; the one rule that matters most is that
Ehya is one code base for three platforms, and every change has to keep all of them working.

## Reporting issues

Open an issue at https://github.com/mal7othify/ehya-android/issues and include:

- the platform (Android, iOS or web) and its version,
- the app version, from the info screen,
- steps to reproduce, what you expected and what happened,
- a screenshot or recording for anything visual.

Feature ideas are welcome as issues too. Please check for an existing one before opening a new
issue.

## Proposing changes

1. Fork the repository and create a branch from `main`.
2. Make the change. App code belongs in `shared/`; use `expect`/`actual` or a small Koin module in
   the platform source set only when a platform really needs its own implementation.
3. Format and test:

   ```shell
   ./gradlew spotlessApply
   ./gradlew :shared:testAndroidHostTest
   ```

4. Run the app on every platform you can, and at least build the ones you cannot run. The README
   has the commands for Android, iOS and the web.
5. Open a pull request against `main`. Fill in the template, link the issue it addresses and add
   screenshots for UI changes.

## What a pull request needs

- It builds for Android, iOS and the web. A change that works on one platform only is not ready.
- Formatting passes (`spotlessCheck`) and the tests pass.
- Behaviour, strings and layout stay consistent across platforms; the app is Arabic and
  right-to-left everywhere.
- New data or resources go through Compose resources in `shared/`, not platform-specific assets.
- Commits are focused, with messages that say what changed and why.

## Review

Maintainers review pull requests as time allows. Small, focused pull requests are merged faster
than large ones, so split unrelated changes into separate pull requests.
