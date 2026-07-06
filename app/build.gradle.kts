plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
    id("org.jetbrains.kotlin.plugin.compose")
}

android {
    namespace = "com.seren.app"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.seren.app"
        minSdk = 26
        targetSdk = 35
        versionCode = 1
        versionName = "0.1.0-batch1"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        release {
            isMinifyEnabled = true
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }

    kotlinOptions {
        jvmTarget = "17"
    }

    buildFeatures {
        compose = true
    }

    // TFLite models go in assets/ — don't compress them
    aaptOptions {
        noCompress += "tflite"
    }
}

dependencies {
    // Compose BOM — single version for all Compose libs
    val composeBom = platform("androidx.compose:compose-bom:2024.12.01")
    implementation(composeBom)

    // Core Compose
    implementation("androidx.compose.ui:ui")
    implementation("androidx.compose.ui:ui-graphics")
    implementation("androidx.compose.ui:ui-tooling-preview")
    implementation("androidx.compose.material3:material3")
    implementation("androidx.activity:activity-compose:1.9.3")
    implementation("androidx.navigation:navigation-compose:2.8.5")

    // Core Android
    implementation("androidx.core:core-ktx:1.15.0")
    implementation("androidx.lifecycle:lifecycle-runtime-ktx:2.8.7")
    implementation("androidx.lifecycle:lifecycle-viewmodel-compose:2.8.7")

    // Room (local database — UserProfile, ScreeningSession, TaskResult, ConditionScore)
    val roomVersion = "2.6.1"
    implementation("androidx.room:room-runtime:$roomVersion")
    implementation("androidx.room:room-ktx:$roomVersion")
    // kapt or ksp will be added when we add Room entities in Mission 1

    // TensorFlow Lite (on-device ML inference — Mission 3+)
    // implementation("org.tensorflow:tensorflow-lite:2.16.1")
    // implementation("org.tensorflow:tensorflow-lite-support:0.4.4")

    // MediaPipe (eye-tracking, face mesh — Mission 3+)
    // implementation("com.google.mediapipe:tasks-vision:0.20241001")

    // CameraX (front camera for gaze/face — Mission 3+)
    // implementation("androidx.camera:camera-camera2:1.4.1")
    // implementation("androidx.camera:camera-lifecycle:1.4.1")
    // implementation("androidx.camera:camera-view:1.4.1")

    // Google Fonts (Inter for body text)
    implementation("androidx.compose.ui:ui-text-google-fonts")

    // Testing
    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.2.1")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.6.1")
    androidTestImplementation(composeBom)
    androidTestImplementation("androidx.compose.ui:ui-test-junit4")
    debugImplementation("androidx.compose.ui:ui-tooling")
    debugImplementation("androidx.compose.ui:ui-test-manifest")
}
