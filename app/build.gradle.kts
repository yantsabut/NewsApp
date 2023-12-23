plugins {
    alias(libs.plugins.androidApplication)
    alias(libs.plugins.jetbrainsKotlinAndroid)
    id("kotlin-kapt")
    id("kotlin-parcelize")
}

android {
    namespace = "com.aston.news"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.aston.news"
        minSdk = 24
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
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
    }
}

dependencies {

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    implementation(libs.lottie)
    kapt (libs.moxy.compiler)
    implementation (libs.dagger)
    kapt (libs.dagger.compiler)
    implementation (libs.kirich1409.viewbindingpropertydelegate.noreflection)
    implementation (libs.glide)

    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    implementation(libs.androidx.swiperefreshlayout)


    //arch
    implementation (libs.androidx.core.splashscreen)
    implementation (libs.moxy)
    implementation (libs.moxy.androidx)
    implementation(libs.androidx.activity)
    implementation(libs.androidx.constraintlayout)
    implementation (libs.androidx.fragment.ktx)
    implementation (libs.androidx.lifecycle.extensions)

    //network
    implementation (libs.retrofit)
    implementation(libs.okhttp)
    implementation(libs.logging.interceptor)
    implementation (libs.gson)
    implementation(libs.adapter.rxjava2)
    implementation (libs.converter.gson)

    //threads
    implementation(libs.rxjava)
    implementation(libs.rxandroid)

    //database
    implementation(libs.androidx.room.runtime)
    implementation(libs.androidx.room.ktx)
    kapt(libs.androidx.room.compiler)

    //service
    implementation(libs.androidx.work.runtime.ktx)
}