plugins {
    alias(libs.plugins.androidApplication)
    alias(libs.plugins.jetbrainsKotlinAndroid)
    alias(libs.plugins.parcelize)
    alias(libs.plugins.safeargs)
    alias(libs.plugins.hilt)
    id ("kotlin-kapt")
}

android {
    namespace = "com.example.furnitureapp"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.example.furnitureapp"
        minSdk = 24
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
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
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    kotlinOptions {
        jvmTarget = "1.8"
    }
}

dependencies {

    //loading button
    implementation(libs.loading.button)

    //glide
    implementation(libs.glide)

    //circle image view
    implementation(libs.circle.image.view)

    //viewpager2 indicator
    implementation(libs.viewpager2)

    //stepview
    implementation(libs.stepview)

    //navigation fragments
    implementation(libs.navigation.fragments)

    //hilt / compiler
    implementation(libs.hilt.android)
    kapt(libs.hilt.compiler)

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    implementation(libs.androidx.activity)
    implementation(libs.androidx.constraintlayout)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
}