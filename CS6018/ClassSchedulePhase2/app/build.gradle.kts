plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.compose)
    id("org.jetbrains.kotlin.plugin.serialization") version "1.9.10"
}

android {
    namespace = "com.example.classschedule"
    compileSdk = 37

    defaultConfig {
        applicationId = "com.example.classschedule"
        minSdk = 24
        targetSdk = 37
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        release {
            isMinifyEnabled = false
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    buildFeatures {
        compose = true
    }
}

dependencies {
    // Existing dependencies...
//    implementation("androidx.core:core-ktx:1.10.1")
//    implementation("androidx.activity:activity-compose:1.7.2")
//    implementation("androidx.compose.ui:ui:1.5.3")
//    implementation("androidx.compose.material3:material3:1.1.1")
//    implementation("androidx.lifecycle:lifecycle-viewmodel-compose:2.6.1")
//
//    // KTOR Client (HTTP)
//    implementation("io.ktor:ktor-client-core:2.3.0")
//    implementation("io.ktor:ktor-client-android:2.3.0")
//    implementation("io.ktor:ktor-client-content-negotiation:2.3.0")
//
//    // Kotlinx Serialization
//    implementation("io.ktor:ktor-serialization-kotlinx-json:2.3.0")
//    implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.6.0")
//
//    implementation("androidx.activity:activity-ktx:1.10.0")
//
//    // Core coroutines library (includes StateFlow)
//    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.8.0")
//
//    // If you are developing for Android, also add:
//    implementation("androidx.lifecycle:lifecycle-viewmodel-ktx:2.8.0")
//    implementation("androidx.lifecycle:lifecycle-runtime-ktx:2.8.0")
//
//    // Compose UI Testing
//    androidTestImplementation("androidx.compose.ui:ui-test-junit4:1.5.3")
//    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")

//    testImplementation(kotlin("test"))
   // testImplementation("org.jetbrains.kotlin:kotlin-test:1.9.10")
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.activity.compose)
    implementation(libs.androidx.compose.material3)
    implementation(libs.androidx.compose.ui)
    implementation(libs.androidx.compose.ui.graphics)
    implementation(libs.androidx.compose.ui.tooling.preview)
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.firebase.crashlytics.buildtools)
    testImplementation(libs.junit)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.compose.ui.test.junit4)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(libs.androidx.junit)
    debugImplementation(libs.androidx.compose.ui.test.manifest)
    debugImplementation(libs.androidx.compose.ui.tooling)
}