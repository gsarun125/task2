plugins {
    id ("dagger.hilt.android.plugin")
    id("com.android.application")

}

android {
    namespace = "com.ka.task1"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.ka.task1"
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
}

dependencies {
    implementation ("androidx.paging:paging-runtime:3.0.1")


    implementation ("com.squareup.retrofit2:retrofit:2.9.0")
    implementation ("com.squareup.retrofit2:converter-gson:2.9.0")
    implementation ("com.github.bumptech.glide:glide:4.12.0")
    implementation ("androidx.lifecycle:lifecycle-extensions:2.2.0")

    implementation ("com.google.dagger:hilt-android:2.49")
    implementation("androidx.drawerlayout:drawerlayout:1.2.0")
    annotationProcessor ("com.google.dagger:hilt-compiler:2.49")
    implementation ("com.google.dagger:hilt-android:2.49")

    androidTestImplementation  ("com.google.dagger:hilt-android-testing:2.49")

    implementation("androidx.appcompat:appcompat:1.6.1")
    implementation("com.google.android.material:material:1.11.0")
    implementation("androidx.constraintlayout:constraintlayout:2.1.4")
    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.1.5")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")
}