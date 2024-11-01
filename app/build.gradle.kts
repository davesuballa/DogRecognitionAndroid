plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
    id("com.google.gms.google-services")
}

android {
    namespace = "com.example.buddy"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.example.buddy"
        minSdk = 26
        targetSdk = 34
        versionCode = 1
        versionName = "1.0.10"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        vectorDrawables {
            useSupportLibrary = true
        }
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
    buildFeatures {
        compose = true
        viewBinding = true

    }
    composeOptions {
        kotlinCompilerExtensionVersion = "1.5.1"
    }
    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
}

dependencies {


    implementation ("com.google.zxing:core:3.5.0")
    implementation ("com.google.android.gms:play-services-vision:20.1.3")
    implementation ("com.google.android.material:material:1.11.0")
    implementation("com.github.yuriy-budiyev:code-scanner:2.3.0")
    implementation ("com.jakewharton.threetenabp:threetenabp:1.3.0")
    implementation("androidx.fragment:fragment-ktx:1.6.2")
    implementation ("androidx.recyclerview:recyclerview:1.3.2")
    implementation("androidx.core:core-ktx:1.12.0")
    implementation("androidx.lifecycle:lifecycle-runtime-ktx:2.7.0")
    implementation("androidx.activity:activity-compose:1.8.2")
    implementation(platform("androidx.compose:compose-bom:2023.08.00"))
    implementation("androidx.compose.ui:ui")
    implementation("androidx.compose.ui:ui-graphics")
    implementation("androidx.compose.ui:ui-tooling-preview")
    implementation("androidx.compose.material3:material3")
    implementation ("androidx.cardview:cardview:1.0.0")
    implementation("androidx.appcompat:appcompat:1.6.1")
    implementation("com.google.android.material:material:1.11.0")
    implementation("androidx.constraintlayout:constraintlayout:2.1.4")
    implementation("com.github.qamarelsafadi:CurvedBottomNavigation:0.1.3")
    implementation(platform("com.google.firebase:firebase-bom:32.7.2"))
    implementation("com.google.firebase:firebase-analytics")
    implementation("com.google.firebase:firebase-auth:22.3.1")
    implementation("com.google.firebase:firebase-database:20.3.1")
    implementation("androidx.core:core-splashscreen:1.0.1")
    // camerax dependencies
    implementation("androidx.camera:camera-camera2:1.0.0-beta07")
    implementation("androidx.camera:camera-lifecycle:1.0.0-beta07")
    implementation("androidx.camera:camera-view:1.0.0-alpha14")


// circleimageview library
    implementation("de.hdodenhof:circleimageview:3.1.0")
    implementation("com.github.yuriy-budiyev:circular-progress-bar:1.2.3")

    // Retrofit for api implementation
    implementation("com.squareup.retrofit2:retrofit:2.9.0")





    implementation ("com.google.android.material:material")
    implementation ("com.prolificinteractive:material-calendarview:1.4.3")


    // Gson Converter (for JSON serialization/deserialization)
    implementation("com.squareup.retrofit2:converter-gson:2.9.0")
    implementation("com.squareup.okhttp3:okhttp:4.9.1")
    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.1.5")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")
    androidTestImplementation("androidx.compose.ui:ui-test-junit4")
    androidTestImplementation(platform("androidx.compose:compose-bom:2023.08.00"))
    debugImplementation("androidx.compose.ui:ui-tooling")
    debugImplementation("androidx.compose.ui:ui-test-manifest")

}