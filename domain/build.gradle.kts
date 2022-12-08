plugins {
    id("com.android.library")
    id("org.jetbrains.kotlin.android")
    kotlin("kapt")
    id ("kotlin-parcelize")
    id ("dagger.hilt.android.plugin")
}

android {
    namespace = "com.mj.naversearch.domain"

    compileSdk = Versions.COMPILE_SDK

    defaultConfig {
        minSdk = 24
        targetSdk = Versions.TARGET_SDK

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        consumerProguardFiles("consumer-rules.pro")
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
        jvmTarget = JavaVersion.VERSION_1_8.toString()
    }
}

dependencies {

    implementation ("androidx.core:core-ktx:${Versions.CORE}")
    implementation ("androidx.appcompat:appcompat:${Versions.APP_COMPAT}")
    implementation ("com.google.android.material:material:${Versions.MATERIAL}")
    implementation ("androidx.paging:paging-runtime-ktx:${Versions.PAGING}")
    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.1.3")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.4.0")

    //hilt
    implementation ("com.google.dagger:hilt-android:${Versions.HILT}")
    kapt ("com.google.dagger:hilt-compiler:${Versions.HILT}")

    //room
    implementation ("androidx.room:room-runtime:${Versions.ROOM}")
    implementation ("androidx.room:room-ktx:${Versions.ROOM}")
    kapt ("androidx.room:room-compiler:${Versions.ROOM}")

    //Coroutine
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:${Versions.COROUTINE}")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-android:${Versions.COROUTINE}")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-guava:${Versions.COROUTINE}")
}