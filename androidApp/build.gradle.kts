import java.util.Properties
import org.jetbrains.kotlin.gradle.dsl.JvmTarget

plugins {
  alias(libs.plugins.android.application)
  alias(libs.plugins.kotlin.compose)
  alias(libs.plugins.google.services)
  alias(libs.plugins.firebase.crashlytics)
}

val keystoreProperties =
  Properties().apply {
    val file = rootProject.file("keystore.properties")
    if (file.exists()) file.inputStream().use { load(it) }
  }

android {
  namespace = "com.eillia.ehya"
  compileSdk =
    libs.versions.android.compileSdk
      .get()
      .toInt()

  defaultConfig {
    applicationId = "com.eillia.ehya"
    minSdk =
      libs.versions.android.minSdk
        .get()
        .toInt()
    targetSdk =
      libs.versions.android.targetSdk
        .get()
        .toInt()
    versionCode =
      libs.versions.app.versionCode
        .get()
        .toInt()
    versionName =
      libs.versions.app.versionName
        .get()
    testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
  }

  signingConfigs {
    if (keystoreProperties.containsKey("prodKeystoreFile")) {
      create("release") {
        storeFile = file(keystoreProperties["prodKeystoreFile"] as String)
        storePassword = keystoreProperties["prodKeystorePassword"] as String
        keyAlias = keystoreProperties["prodKeyAlias"] as String
        keyPassword = keystoreProperties["prodKeyPassword"] as String
      }
    }
    if (keystoreProperties.containsKey("debugKeystoreFile")) {
      getByName("debug") {
        storeFile = file(keystoreProperties["debugKeystoreFile"] as String)
        storePassword = keystoreProperties["debugKeystorePassword"] as String
        keyAlias = keystoreProperties["debugKeyAlias"] as String
        keyPassword = keystoreProperties["debugKeyPassword"] as String
      }
    }
  }

  buildTypes {
    release {
      isMinifyEnabled = true
      isShrinkResources = true
      proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
      signingConfigs.findByName("release")?.let { signingConfig = it }
    }
  }

  buildFeatures {
    buildConfig = true
  }

  compileOptions {
    sourceCompatibility = JavaVersion.VERSION_17
    targetCompatibility = JavaVersion.VERSION_17
  }

  packaging {
    resources {
      excludes += "/META-INF/{AL2.0,LGPL2.1}"
    }
  }
}

kotlin {
  compilerOptions {
    jvmTarget.set(JvmTarget.JVM_17)
  }
}

dependencies {
  implementation(projects.shared)

  implementation(libs.androidx.activity.compose)
  implementation(libs.androidx.core.ktx)
  implementation(libs.androidx.core.splashscreen)
  implementation(libs.compose.ui)
  implementation(libs.compose.ui.tooling.preview)
  implementation(libs.kermit)

  implementation(platform(libs.firebase.bom))
  implementation(libs.firebase.messaging)
  implementation(libs.firebase.analytics)
  implementation(libs.firebase.crashlytics)

  debugImplementation(libs.compose.ui.tooling)

  testImplementation(libs.junit)
  androidTestImplementation(libs.androidx.test.ext.junit)
  androidTestImplementation(libs.androidx.test.runner)
}
