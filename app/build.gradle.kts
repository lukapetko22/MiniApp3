plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
    id("org.jetbrains.kotlin.kapt")
}

android {
    namespace = "si.uni_lj.fri.pbd.miniapp3"
    compileSdk = 34

    defaultConfig {
        applicationId = "si.uni_lj.fri.pbd.miniapp3"
        minSdk = 26
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

    buildFeatures {
        compose = true
    }

    composeOptions {
        kotlinCompilerExtensionVersion = "1.5.2"
    }
}

dependencies {

    implementation("androidx.lifecycle:lifecycle-extensions:2.2.0")
    implementation("androidx.lifecycle:lifecycle-viewmodel-ktx:2.4.1")
    implementation(libs.androidx.coordinatorlayout)
    implementation(libs.androidx.swiperefreshlayout)
    implementation(libs.androidx.ui.android)
    implementation(libs.androidx.material3.android)
    val room_version = "2.6.1"
    implementation("androidx.room:room-runtime:$room_version")
    annotationProcessor("androidx.room:room-compiler:$room_version")
    implementation("androidx.room:room-ktx:$room_version")//KTX Extensions/Coroutines for Room
    annotationProcessor("androidx.room:room-compiler:$room_version")
    kapt("androidx.room:room-compiler:$room_version")

    implementation("com.google.code.gson:gson:2.10.1")
    implementation("com.squareup.retrofit2:retrofit:2.11.0")
    implementation("com.squareup.retrofit2:converter-gson:2.11.0")

    implementation("com.github.amitshekhariitbhu.Android-Debug-Database:debug-db:v1.0.6")
    implementation("me.zhanghai.android.materialprogressbar:library:1.6.1")

    implementation("androidx.lifecycle:lifecycle-viewmodel-ktx:2.6.0")
    implementation("androidx.lifecycle:lifecycle-livedata-ktx:2.6.0")

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    implementation(libs.androidx.activity)
    implementation(libs.androidx.constraintlayout)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)

    val composeBom = platform("androidx.compose:compose-bom:2024.04.01")
    implementation(composeBom)
    androidTestImplementation(composeBom)
// Choose one of the following:
// Material Design 3
    implementation("androidx.compose.material3:material3")
// or Material Design 2
    implementation("androidx.compose.material:material")
// or skip Material Design and build directly on top of foundational components
    implementation("androidx.compose.foundation:foundation")
// or only import the main APIs for the underlying toolkit systems,
// such as input and measurement/layout
    implementation("androidx.compose.ui:ui")
// Android Studio Preview support
    implementation("androidx.compose.ui:ui-tooling-preview")
    debugImplementation("androidx.compose.ui:ui-tooling")
// UI Tests
    androidTestImplementation("androidx.compose.ui:ui-test-junit4")
    debugImplementation("androidx.compose.ui:ui-test-manifest")
// Optional - Included automatically by material, only add when you need
// the icons but not the material library (e.g. when using Material3 or a
// custom design system based on Foundation)
    implementation("androidx.compose.material:material-icons-core")
// Optional - Add full set of material icons
    implementation("androidx.compose.material:material-icons-extended")
// Optional - Add window size utils
    implementation("androidx.compose.material3:material3-window-size-class")
// Optional - Integration with activities
    implementation("androidx.activity:activity-compose:1.8.2")
// Optional - Integration with ViewModels
    implementation("androidx.lifecycle:lifecycle-viewmodel-compose:2.6.1")
// Optional - Integration with LiveData
    implementation("androidx.compose.runtime:runtime-livedata")
// Optional - Integration with RxJava
    implementation("androidx.compose.runtime:runtime-rxjava2")
    implementation("io.coil-kt:coil-compose:2.0.0-rc01")

    implementation("androidx.fragment:fragment-ktx:1.3.6")
    implementation("androidx.viewpager2:viewpager2:1.0.0")
    implementation("androidx.compose.ui:ui:1.0.0")
    implementation("androidx.compose.material:material:1.0.0")
    implementation("androidx.compose.ui:ui-tooling-preview:1.0.0")
    implementation("com.google.accompanist:accompanist-swiperefresh:0.23.1")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.6.4")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-android:1.6.4")
}