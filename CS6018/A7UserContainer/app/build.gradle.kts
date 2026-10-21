// Module-level build file: app/build.gradle.kts
// Note: plugins are APPLIED here (no "apply false"). "apply false" belongs only in the root build file.
plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.compose)
    alias(libs.plugins.kotlin.serialization)
}

android {
    namespace = "com.example.augmentedreality"
    compileSdk {
        version = release(37)
    }

    defaultConfig {
        applicationId = "com.example.augmentedreality"
        minSdk = 24
        // 36, not 37: apps targeting 37 (Android 17) are blocked from reaching local-network
        // addresses (the emulator's 10.0.2.2, a LAN IP) unless they request the
        // ACCESS_LOCAL_NETWORK runtime permission. Apps targeting 36 keep access automatically.
        targetSdk = 36
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        release {
            optimization {
                enable = false
            }
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    buildFeatures {
        compose = true
    }
    androidResources {
        noCompress += "tflite"
    }
}

dependencies {
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.activity.compose)
    implementation(libs.androidx.camera.core)
    implementation(libs.androidx.camera.lifecycle)
    implementation(libs.androidx.compose.material3)
    implementation(libs.androidx.compose.ui)
    implementation(libs.androidx.compose.ui.graphics)
    implementation(libs.androidx.compose.ui.tooling.preview)
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.camera.compose)
    implementation(libs.androidx.camera.camera2)
    implementation(libs.mlkit.objectdetection)
    implementation(libs.mediapipe.vision)

    // Talking to the photo server
    implementation(libs.ktor.client.core)
    implementation(libs.ktor.client.okhttp)
    implementation(libs.ktor.client.content.negotiation)
    implementation(libs.ktor.serialization.kotlinx.json)
}