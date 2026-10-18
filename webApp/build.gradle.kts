import org.jetbrains.kotlin.gradle.ExperimentalWasmDsl

plugins {
  alias(libs.plugins.kotlin.multiplatform)
  alias(libs.plugins.compose.multiplatform)
  alias(libs.plugins.kotlin.compose)
}

kotlin {
  @OptIn(ExperimentalWasmDsl::class)
  wasmJs {
    outputModuleName.set("ehya")
    browser {
      commonWebpackConfig {
        outputFileName = "ehya.js"
      }
    }
    binaries.executable()
  }

  sourceSets {
    wasmJsMain.dependencies {
      implementation(projects.shared)
      implementation(libs.compose.runtime)
      implementation(libs.compose.ui)
      implementation(libs.kotlinx.browser)
    }
  }
}
