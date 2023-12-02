plugins {
    id("com.android.application")
    id("com.google.gms.google-services")
}


android {
    namespace = "com.example.myapplication"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.example.myapplication"
        minSdk = 26
        targetSdk = 33
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

    implementation("androidx.appcompat:appcompat:1.6.1")
    implementation("com.google.android.material:material:1.10.0")
    implementation("androidx.constraintlayout:constraintlayout:2.1.4")
    implementation(platform("com.google.firebase:firebase-bom:32.5.0"))
    implementation("com.google.firebase:firebase-firestore")
    implementation("androidx.annotation:annotation:1.2.0")
    implementation("com.google.firebase:firebase-auth:22.2.0") // Use the latest version
    implementation("junit:junit:4.13.2")
    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.1.5")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")
    androidTestImplementation("androidx.test:runner:1.4.0")
    androidTestImplementation("androidx.test:rules:1.4.0")
    androidTestImplementation("com.google.android.material:material:1.10.0")
//    androidTestImplementation("androidx.test.espresso:espresso-contrib:3.5.1"){
//
//        // IMPORTANT
//        // !!! If using espresso-contrib,
//        // !!! This module needs to be excluded for our UI errors to go away //
//        /**
//         * https://stackoverflow.com/questions/72558160/why-am-i-getting-internal-error-in-cloud-firestore-24-1-2-in-my-android-app-o
//         * https://stackoverflow.com/questions/66338416/internal-error-in-cloud-firestore-22-1-0-when-running-instrumentation-test
//         */
//         exclude(module = "protobuf-lite")
//    }
    implementation(platform("com.google.firebase:firebase-bom:32.3.1"))
    implementation("com.google.firebase:firebase-firestore")
    androidTestImplementation("androidx.test.espresso:espresso-intents:3.5.1")
}
