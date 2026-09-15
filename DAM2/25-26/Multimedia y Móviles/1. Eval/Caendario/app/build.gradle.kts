plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
}

android {
    namespace = "com.example.caendario"
    compileSdk = 36

    defaultConfig {
        applicationId = "com.example.caendario"
        minSdk = 24
        targetSdk = 36
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
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
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    kotlinOptions {
        jvmTarget = "11"
    }

    // Evitar warning de META-INF/INDEX.LIST
    packagingOptions {
        resources {
            excludes += listOf(
                "/META-INF/INDEX.LIST",
                "/META-INF/DEPENDENCIES"
            )
        }
    }
}


    dependencies {

    // Google Sign-In
    implementation("com.google.android.gms:play-services-auth:20.7.0")

    // Google API Client (para parsing JSON si quieres)
    implementation("com.google.api-client:google-api-client-android:1.34.0")
    implementation("com.google.api-client:google-api-client-gson:1.34.0")

    // Coroutines opcional para llamadas REST en background
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-android:1.7.3")

        implementation("com.squareup.okhttp3:okhttp:4.11.0")

    // AndroidX
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    implementation(libs.androidx.activity)
    implementation(libs.androidx.constraintlayout)
    implementation("androidx.recyclerview:recyclerview:1.3.1") // RecyclerView

    // Tests
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
}
