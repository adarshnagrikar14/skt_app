import java.util.Properties
import java.io.FileInputStream

val properties = Properties().apply {
    load(FileInputStream(rootProject.file("local.properties")))
}

plugins {
    id("com.android.application")
    id("com.google.gms.google-services")
    id("com.google.firebase.crashlytics")
}

android {
    namespace = "com.skt.skillup"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.skt.skillup"
        minSdk = 24
        targetSdk = 33
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"

        buildConfigField("String", "API_KEY", "\"${properties.getProperty("API_KEY")}\"")
        buildConfigField("String", "API_AUTH", "\"${properties.getProperty("API_AUTH")}\"")
        buildConfigField("String", "API_VIDEO", "\"${properties.getProperty("API_VIDEO")}\"")
        buildConfigField("String", "API_PDF", "\"${properties.getProperty("API_PDF")}\"")
        buildConfigField(
            "String", "API_WORKINGCREDS", "\"${properties.getProperty("API_WORKINGCREDS")}\""
        )
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro"
            )
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }

    buildFeatures {
        buildConfig = true
    }

    viewBinding {
        enable = true
    }
}

dependencies {

    implementation("androidx.appcompat:appcompat:1.6.1")
    implementation("com.google.android.material:material:1.11.0")
    implementation("androidx.constraintlayout:constraintlayout:2.1.4")
    implementation("androidx.lifecycle:lifecycle-livedata-ktx:2.6.1")
    implementation("androidx.lifecycle:lifecycle-viewmodel-ktx:2.6.1")
    implementation("androidx.navigation:navigation-fragment:2.7.1")
    implementation("androidx.navigation:navigation-ui:2.7.1")

    implementation("de.hdodenhof:circleimageview:3.1.0")
    implementation("com.github.bumptech.glide:glide:4.14.2")
    implementation("com.airbnb.android:lottie:5.0.3")

    implementation("com.google.code.gson:gson:2.10.1")
    implementation("androidx.biometric:biometric:1.1.0")

    implementation(platform("org.jetbrains.kotlin:kotlin-bom:1.8.0"))
    implementation("com.google.firebase:firebase-auth-ktx:22.1.1")
    implementation("com.google.android.exoplayer:exoplayer:2.14.1")

    implementation("com.android.volley:volley:1.2.0")

    implementation("commons-net:commons-net:3.8.0")
    implementation("com.google.android.gms:play-services-auth:20.6.0")
    implementation("com.google.firebase:firebase-crashlytics:19.0.2")
    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.1.5")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")
}