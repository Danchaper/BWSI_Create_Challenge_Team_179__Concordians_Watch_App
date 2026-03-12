plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)
}

android {
    namespace = "com.example.bwsicreatechallenge179_watchapp"
    compileSdk {
        version = release(36)
    }

    defaultConfig {
        applicationId = "com.example.bwsicreatechallenge179_watchapp"
        minSdk = 30
        targetSdk = 36
        versionCode = 1
        versionName = "1.0"

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
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    kotlinOptions {
        jvmTarget = "11"
    }
    useLibrary("wear-sdk")
    buildFeatures {
        compose = true
    }
}

dependencies {
    implementation("androidx.wear.compose:compose-material:1.3.0")
    implementation("androidx.compose.material3:material3:1.2.1")
    implementation(libs.play.services.wearable)
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.compose.ui)
    implementation(libs.androidx.compose.ui.graphics)
    implementation(libs.androidx.compose.ui.tooling.preview)
    implementation(libs.androidx.compose.material)
    implementation(libs.androidx.compose.foundation)
    implementation(libs.androidx.wear.tooling.preview)
    implementation(libs.androidx.activity.compose)
    implementation(libs.androidx.core.splashscreen)
    implementation(libs.androidx.compose.foundation.layout)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.compose.ui.test.junit4)
    debugImplementation(libs.androidx.compose.ui.tooling)
    debugImplementation(libs.androidx.compose.ui.test.manifest)

    val composeBom = platform("androidx.compose:compose-bom:2026.02.01")

    // General compose dependencies
    implementation(composeBom)
    // Other compose dependencies

    // Compose for Wear OS dependencies
    implementation("androidx.wear.compose:compose-material3:1.5.6")

    // Foundation is additive, so you can use the mobile version in your Wear OS app.
    implementation("androidx.wear.compose:compose-foundation:1.5.6")

    // Wear OS preview annotations
    implementation("androidx.wear.compose:compose-ui-tooling:1.5.6")

    // If you are using Compose Navigation, use the Wear OS version (NOT THE MOBILE VERSION).
    // Uncomment the line below and update the version number.
    // implementation("androidx.wear.compose:compose-navigation:1.5.6")

    // Testing
    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.1.3")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.4.0")
    androidTestImplementation("androidx.compose.ui:ui-test-junit4:1.0.3")
    debugImplementation("androidx.compose.ui:ui-tooling:1.4.1")
}