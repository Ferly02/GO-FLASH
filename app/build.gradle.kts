plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
    id("com.google.gms.google-services")
}

android {
    namespace = "com.example.kurirkilat"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.example.kurirkilat"
        minSdk = 24
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        vectorDrawables.useSupportLibrary = true
    }

    buildFeatures {
        viewBinding = true
    }

    buildTypes {
        release {
            isMinifyEnabled = false
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
}

dependencies {

    implementation("androidx.core:core-ktx:1.9.0") // aman buat compileSdk 33
    implementation("androidx.appcompat:appcompat:1.6.1")
    implementation("com.google.android.material:material:1.8.0") // gak perlu compileSdk 35
    implementation("androidx.constraintlayout:constraintlayout:2.1.4")


    /*compose
    implementation("androidx.compose.animation:animation:1.4.3")
    implementation("androidx.compose.material:material:1.4.3")
    implementation("androidx.compose.animation:animation:1.4.3")
    implementation("androidx.compose.ui:ui:1.4.3")
    implementation("androidx.compose.foundation:foundation:1.4.3")
    implementation("androidx.navigation:navigation-compose:2.7.0")*/

    // Firebase versi stabil buat AGP 8.0 & Kotlin 1.8
    implementation("com.google.firebase:firebase-auth-ktx:21.1.0")

    // Kalau mau pake credentials API yang aman (versi awal)
    implementation("androidx.credentials:credentials:1.0.0-alpha02")
    implementation("androidx.credentials:credentials-play-services-auth:1.0.0-alpha02")

    // Google Sign In (Kalau mau support login Google Sign-In Play Services)
    implementation("com.google.android.gms:play-services-auth:20.7.0")

    // Activity compose versi stabil
    implementation("androidx.activity:activity-compose:1.6.1")
    implementation("androidx.core:core-animation:1.0.0")

    // Unit Test
    testImplementation("junit:junit:4.13.2")

    // Android Test
    androidTestImplementation("androidx.test.ext:junit:1.1.5")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")

    implementation("com.google.android.libraries.identity.googleid:googleid:1.1.1")
    implementation ("de.hdodenhof:circleimageview:3.1.0")

}
