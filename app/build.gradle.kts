plugins {
    alias(libs.plugins.androidApplication)
    alias(libs.plugins.jetbrainsKotlinAndroid)
    id("com.google.gms.google-services")
}

android {
    namespace = "ar.edu.uade.municipio_frontend"
    compileSdk = 34

    defaultConfig {
        applicationId = "ar.edu.uade.municipio_frontend"
        minSdk = 23
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
}

dependencies {

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    implementation(libs.androidx.activity)
    implementation(libs.androidx.constraintlayout)

    //Dependencias para endpoints

    implementation(libs.retrofit)
    implementation(libs.converter.gson)

    //Dependencia JWT

    implementation(libs.java.jwt)
    implementation(libs.play.services.maps)

    //Dependencia database
    implementation(libs.osmdroid.android)
    implementation(libs.androidx.preference)

    implementation(libs.okhttp)
    implementation(libs.logging.interceptor)
    implementation(libs.firebase.messaging)

    //notis
    implementation(libs.retrofit.v290)
    implementation(libs.converter.gson.v290)

    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)

    implementation(libs.glide)
    annotationProcessor(libs.compiler)

    implementation (libs.picasso)
    implementation(platform(libs.firebase.bom))
    //noinspection UseTomlInstead
    implementation("com.google.firebase:firebase-analytics")
    //noinspection UseTomlInstead
    implementation ("com.google.firebase:firebase-messaging")

    //noinspection UseTomlInstead
    implementation ("org.jetbrains.kotlin:kotlin-stdlib")
}
java {
    sourceCompatibility = JavaVersion.VERSION_1_8
    targetCompatibility = JavaVersion.VERSION_1_8
}