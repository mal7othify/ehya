plugins {
  alias(libs.plugins.android.application) apply false
  alias(libs.plugins.android.kotlin.multiplatform.library) apply false
  alias(libs.plugins.kotlin.multiplatform) apply false
  alias(libs.plugins.kotlin.compose) apply false
  alias(libs.plugins.kotlin.serialization) apply false
  alias(libs.plugins.compose.multiplatform) apply false
  alias(libs.plugins.ksp) apply false
  alias(libs.plugins.room3) apply false
  alias(libs.plugins.google.services) apply false
  alias(libs.plugins.firebase.crashlytics) apply false
  alias(libs.plugins.spotless)
}

spotless {
  kotlin {
    target("**/*.kt")
    targetExclude("**/build/**/*.kt", "spotless/**")
    ktlint(libs.versions.ktlint.get())
      .editorConfigOverride(
        mapOf("ktlint_function_naming_ignore_when_annotated_with" to "Composable")
      )
    licenseHeaderFile(rootProject.file("spotless/copyright.kt"))
  }
  kotlinGradle {
    target("**/*.gradle.kts")
    targetExclude("**/build/**")
    ktlint(libs.versions.ktlint.get())
  }
  format("javascript") {
    target("shared/webWorker/*.js")
    // Header ends at the first line that is not part of a block comment.
    licenseHeaderFile(rootProject.file("spotless/copyright.kt"), "(^(?![\\/ ]\\*).*$)")
  }
}
