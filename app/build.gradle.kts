plugins {
    id ("com.android.application")
    kotlin("android")
    kotlin("kapt")
    id ("kotlin-parcelize")
    id ("dagger.hilt.android.plugin")
}


android {
    namespace = "com.mj.naversearch"

    compileSdk = Versions.COMPILE_SDK
    buildToolsVersion = Versions.BUILD_TOOLS

    defaultConfig {
        applicationId = "com.mj.naversearch"
        minSdk = 24
        targetSdk = Versions.TARGET_SDK
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
        jvmTarget = JavaVersion.VERSION_1_8.toString()
    }

    buildFeatures {
        dataBinding = true
    }
}

dependencies {

    implementation(project(":domain"))
    implementation(project(":data"))

    implementation ("androidx.core:core-ktx:${Versions.CORE}")
    implementation ("androidx.appcompat:appcompat:${Versions.APP_COMPAT}")
    implementation ("androidx.constraintlayout:constraintlayout:${Versions.CONSTRAINT_LAYOUT}")
    implementation ("androidx.activity:activity-ktx:${Versions.ACTIVITY}")
    implementation ("androidx.fragment:fragment-ktx:${Versions.FRAGMENT}")
    implementation ("androidx.lifecycle:lifecycle-runtime-ktx:${Versions.LIFECYCLE}")
    implementation ("androidx.recyclerview:recyclerview:${Versions.RECYCLERVIEW}")
    implementation ("androidx.paging:paging-runtime-ktx:${Versions.PAGING}")

    implementation ("com.google.android.material:material:${Versions.MATERIAL}")

    //Retrofit2
    implementation ("com.squareup.retrofit2:retrofit:${Versions.RETROFIT}")
    implementation ("com.squareup.retrofit2:converter-gson:${Versions.RETROFIT}")
    implementation ("com.squareup.okhttp3:logging-interceptor:${Versions.RETROFIT_LOGGING}")

    //hilt
    implementation ("com.google.dagger:hilt-android:${Versions.HILT}")
    testImplementation(project(mapOf("path" to ":data")))
    kapt ("com.google.dagger:hilt-compiler:${Versions.HILT}")

    //Coroutine
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:${Versions.COROUTINE}")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-android:${Versions.COROUTINE}")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-guava:${Versions.COROUTINE}")

    //test
    testImplementation ("junit:junit:4.13.2")
    testImplementation ("androidx.arch.core:core-testing:2.1.0")
    androidTestImplementation ("androidx.test.ext:junit:1.1.3")
    androidTestImplementation ("androidx.test.espresso:espresso-core:3.4.0")
    testImplementation ("org.jetbrains.kotlinx:kotlinx-coroutines-test:${Versions.COROUTINE}")
    testImplementation ("com.squareup.retrofit2:retrofit-mock:2.7.1")

    //glide
    implementation ("com.github.bumptech.glide:glide:${Versions.GLIDE}")
    annotationProcessor ("com.github.bumptech.glide:compiler:${Versions.GLIDE}")

    //Log
    implementation("com.jakewharton.timber:timber:${Versions.TIMBER}")
}