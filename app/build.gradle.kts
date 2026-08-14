import java.util.Properties
plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.compose)
    alias(libs.plugins.hilt)
    alias(libs.plugins.ksp)
}

android {
    namespace = "com.shetty.mapbooking"

    compileSdk {
        version = release(37)
    }

    val localProperties = Properties()
    val localPropertiesFile = rootProject.file("local.properties")
    if (localPropertiesFile.exists()) {
        localPropertiesFile.inputStream().use {
            localProperties.load(it)
        }
    }
    val mapsApiKey =
        localProperties.getProperty("MAPS_API_KEY", "")

    val aqiApiKey =
        localProperties.getProperty("AQI_API_KEY", "")

    defaultConfig {
        applicationId = "com.shetty.mapbooking"
        minSdk = 24
        targetSdk = 37

        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"

        buildConfigField(
            "String",
            "AQI_API_KEY",
            "\"$aqiApiKey\""
        )

        buildConfigField(
            "String",
            "MAPS_API_KEY",
            "\"$mapsApiKey\""
        )
        manifestPlaceholders["MAPS_API_KEY"] = mapsApiKey
    }

    buildTypes {
        release {
            optimization {
                enable = false
            }
        }
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }

    buildFeatures {
        compose = true
        buildConfig = true
    }
}

dependencies {

    // -------------------------
    // Compose
    // -------------------------

    implementation(platform(libs.androidx.compose.bom))

    implementation(libs.androidx.activity.compose)

    implementation(libs.androidx.compose.material3)

    implementation(libs.androidx.compose.ui)

    implementation(libs.androidx.compose.ui.graphics)

    implementation(libs.androidx.compose.ui.tooling.preview)

    debugImplementation(libs.androidx.compose.ui.tooling)


    // -------------------------
    // Android
    // -------------------------

    implementation(libs.androidx.core.ktx)

    implementation(libs.androidx.lifecycle.runtime.ktx)

    implementation(libs.androidx.lifecycle.viewmodel.compose)


    // -------------------------
    // Navigation
    // -------------------------

    implementation(libs.androidx.navigation.compose)


    // -------------------------
    // Coroutines
    // -------------------------

    implementation(libs.kotlinx.coroutines.android)


    // -------------------------
    // Hilt
    // -------------------------

    implementation(libs.hilt.android)

    implementation(libs.androidx.hilt.navigation.compose)

    ksp(libs.hilt.compiler)


    // -------------------------
    // Retrofit
    // -------------------------

    implementation(libs.retrofit)

    implementation(libs.retrofit.converter.gson)


    // -------------------------
    // OkHttp
    // -------------------------

    implementation(libs.okhttp)

    implementation(libs.okhttp.logging.interceptor)


    // -------------------------
    // Google Maps
    // -------------------------

    implementation(libs.maps.compose)

    implementation(libs.play.services.location)


    // -------------------------
    // Unit Tests
    // -------------------------

    testImplementation(libs.junit)


    // -------------------------
    // Android Tests
    // -------------------------

    androidTestImplementation(platform(libs.androidx.compose.bom))

    androidTestImplementation(libs.androidx.compose.ui.test.junit4)

    androidTestImplementation(libs.androidx.espresso.core)

    androidTestImplementation(libs.androidx.junit)

    debugImplementation(libs.androidx.compose.ui.test.manifest)
}