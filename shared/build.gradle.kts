import org.jetbrains.kotlin.gradle.ExperimentalWasmDsl
import org.jetbrains.kotlin.gradle.dsl.JvmTarget

plugins {
  alias(libs.plugins.kotlin.multiplatform)
  alias(libs.plugins.android.kotlin.multiplatform.library)
  alias(libs.plugins.compose.multiplatform)
  alias(libs.plugins.kotlin.compose)
  alias(libs.plugins.kotlin.serialization)
  alias(libs.plugins.ksp)
  alias(libs.plugins.room3)
}

kotlin {
  android {
    namespace = "com.eillia.ehya.shared"
    compileSdk =
      libs.versions.android.compileSdk
        .get()
        .toInt()
    minSdk =
      libs.versions.android.minSdk
        .get()
        .toInt()
    androidResources.enable = true
    withHostTestBuilder {}.configure {
      isIncludeAndroidResources = true
    }
    compilerOptions {
      jvmTarget.set(JvmTarget.JVM_17)
    }
  }

  listOf(
    iosArm64(),
    iosSimulatorArm64()
  ).forEach { iosTarget ->
    iosTarget.binaries.framework {
      baseName = "Shared"
      isStatic = true
    }
  }

  @OptIn(ExperimentalWasmDsl::class)
  wasmJs {
    browser()
  }

  compilerOptions {
    freeCompilerArgs.add("-Xexpect-actual-classes")
    optIn.addAll(
      "kotlinx.coroutines.ExperimentalCoroutinesApi",
      "androidx.compose.material3.ExperimentalMaterial3Api",
      "androidx.compose.material3.adaptive.ExperimentalMaterial3AdaptiveApi",
      "org.jetbrains.compose.resources.ExperimentalResourceApi"
    )
  }

  sourceSets {
    commonMain.dependencies {
      implementation(libs.compose.runtime)
      implementation(libs.compose.foundation)
      implementation(libs.compose.ui)
      implementation(libs.compose.components.resources)
      implementation(libs.compose.ui.tooling.preview)
      implementation(libs.compose.material3)
      implementation(libs.compose.material3.windowsize)
      implementation(libs.compose.material3.adaptive.navigation.suite)
      implementation(libs.compose.material3.adaptive)
      implementation(libs.compose.material3.adaptive.layout)
      implementation(libs.compose.material3.adaptive.navigation3)
      implementation(libs.compose.material.icons.core)
      implementation(libs.compose.material.icons.extended)

      implementation(libs.jetbrains.lifecycle.viewmodel)
      implementation(libs.jetbrains.lifecycle.viewmodel.compose)
      implementation(libs.jetbrains.lifecycle.runtime.compose)
      implementation(libs.jetbrains.lifecycle.viewmodel.navigation3)
      implementation(libs.jetbrains.navigation3.ui)

      implementation(libs.kotlinx.coroutines.core)
      implementation(libs.kotlinx.serialization.json)

      implementation(libs.room3.runtime)
      implementation(libs.sqlite.async)
      implementation(libs.koin.core)
      implementation(libs.koin.core.viewmodel)
      implementation(libs.koin.compose)
      implementation(libs.koin.compose.viewmodel)
      implementation(libs.kermit)
    }
    commonTest.dependencies {
      implementation(libs.kotlin.test)
      implementation(libs.kotlinx.coroutines.test)
    }
    getByName("androidHostTest").dependencies {
      implementation(libs.koin.test)
    }
    androidMain.dependencies {
      implementation(libs.sqlite.bundled)
      implementation(libs.kotlinx.coroutines.android)
      implementation(libs.androidx.activity.compose)
      implementation(libs.androidx.core.ktx)
    }
    iosMain.dependencies {
      implementation(libs.sqlite.bundled)
    }
    wasmJsMain.dependencies {
      implementation(libs.sqlite.web)
      implementation(libs.kotlinx.browser)
      implementation(npm("ehya-sqlite-worker", layout.projectDirectory.dir("webWorker").asFile))
    }
  }
}

/** The web app has no package manager or bundle to read a version from, so Gradle writes one. */
val generateBuildInfo =
  tasks.register("generateBuildInfo") {
    val versionName =
      libs.versions.app.versionName
        .get()
    val outputDir = layout.buildDirectory.dir("generated/buildInfo/wasmJsMain/kotlin")
    inputs.property("versionName", versionName)
    outputs.dir(outputDir)
    doLast {
      val file = outputDir.get().file("com/eillia/ehya/BuildInfo.kt").asFile
      file.parentFile.mkdirs()
      file.writeText(
        """
      |package com.eillia.ehya
      |
      |internal object BuildInfo {
      |  const val VERSION_NAME = "$versionName"
      |}
      |
        """.trimMargin()
      )
    }
  }

kotlin.sourceSets.named("wasmJsMain") {
  kotlin.srcDir(generateBuildInfo)
}

compose.resources {
  publicResClass = false
  packageOfResClass = "com.eillia.ehya.resources"
  generateResClass = always
}

room3 {
  schemaDirectory(layout.projectDirectory.dir("schemas"))
}

dependencies {
  add("kspAndroid", libs.room3.compiler)
  add("kspIosArm64", libs.room3.compiler)
  add("kspIosSimulatorArm64", libs.room3.compiler)
  add("kspWasmJs", libs.room3.compiler)
  androidRuntimeClasspath(libs.compose.ui.tooling)
}
