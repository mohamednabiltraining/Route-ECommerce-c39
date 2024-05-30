plugins {
    alias(libs.plugins.androidApplication)
    alias(libs.plugins.jetbrainsKotlinAndroid)
    alias(libs.plugins.kotlin.kapt)
    alias(libs.plugins.kotlin.parcelize)
    alias(libs.plugins.android.hilt)
    id("androidx.navigation.safeargs.kotlin")
    alias(libs.plugins.google.services)
    alias(libs.plugins.firebase.crashlytics)
}

android {
    namespace = "com.example.routeEcommerce"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.example.routeEcommerce"
        minSdk = 24
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"

        multiDexEnabled = true
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro",
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
    buildFeatures {
        viewBinding = true
        dataBinding = true
    }
    buildToolsVersion = "34.0.0"
    ndkVersion = "25.1.8937393"
}

dependencies {

    implementation(libs.androidx.multidex)

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    implementation(libs.androidx.activity)
    implementation(libs.androidx.constraintlayout)

    // viewPager2
    implementation(libs.androidx.viewpager2)

    // navigation component
    implementation(libs.androidx.navigation.fragment)
    implementation(libs.androidx.navigation.ui)

    // shimmer effect
    implementation(libs.shimmer.effect)
    // rounded image
    implementation(libs.roundedimage)

    // glide
    implementation(libs.glide)
    // picasso
    implementation(libs.picasso)

    // Pager Dots Indicator
    implementation(libs.dotsindicator)

    // touchImage
    implementation(libs.touch.imageview)

    // firebase

    implementation(platform(libs.firebase.bom))
    implementation(libs.bundles.firebase)

    implementation(project(":data"))
    implementation(project(":domain"))

    implementation(libs.androidx.lifecycle.livedata.ktx)
    implementation(libs.androidx.lifecycle.viewmodel.ktx)
    implementation(libs.androidx.fragment.ktx)

    implementation(libs.hilt.android.v25)
    implementation(libs.converter.gson)
    implementation(libs.androidx.legacy.support.v4)
    implementation(libs.androidx.recyclerview)
    kapt(libs.hilt.android.compiler)

    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    implementation(libs.androidx.security.crypto)
}
