import com.android.build.gradle.internal.cxx.configure.gradleLocalProperties
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    alias(libs.plugins.androidApplication)
    alias(libs.plugins.kotlinAndroid)
    alias(libs.plugins.ksp)
    alias(libs.plugins.hilt)
    alias(libs.plugins.kotlin.serialization)
    alias(libs.plugins.google.services)
    id("kotlin-kapt")
    id("com.google.android.libraries.mapsplatform.secrets-gradle-plugin")
}

val vasttrafikApiKey: String = gradleLocalProperties(rootDir, providers).getProperty("VASTTRAFIK_API_KEY")

android {
    namespace = "com.mazarini.resa"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.mazarini.resa"
        minSdk = 26
        targetSdk = 34
        versionCode = 102
        versionName = "1.02"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        vectorDrawables {
            useSupportLibrary = true
        }
        buildConfigField("String", "VASTTRAFIK_API_KEY", vasttrafikApiKey)
        signingConfig = signingConfigs.getByName("debug")

        val locales = arrayOf("en", "sv", "pt", "peo", "tr")

        // Convert the array to a format suitable for buildConfigField
        val localesString = locales.joinToString(prefix = "{", postfix = "}", separator = ", ") { "\"$it\"" }

        // Add the locale codes to buildConfigField
        buildConfigField("String[]", "TRANSLATED_LOCALES", localesString)
    }

    buildTypes {
        debug {
            isMinifyEnabled = true
            isShrinkResources = true
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
        release {
            isMinifyEnabled = true
            isShrinkResources = true
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
    kotlinOptions {
        jvmTarget = "17"
    }
    kotlin {
        jvmToolchain(17)
    }
    buildFeatures {
        compose = true
        buildConfig = true
    }
    composeOptions {
        kotlinCompilerExtensionVersion = "1.4.3"
    }
    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
            excludes += "/META-INF/*"
        }
    }
}

secrets {
    // Optionally specify a different file name containing your secrets.
    // The plugin defaults to "local.properties"
    propertiesFileName = "secrets.properties"

    // A properties file containing default secret values. This file can be
    // checked in version control.
    defaultPropertiesFileName = "local.defaults.properties"

    // Configure which keys should be ignored by the plugin by providing regular expressions.
    // "sdk.dir" is ignored by default.
    ignoreList.add("keyToIgnore") // Ignore the key "keyToIgnore"
    ignoreList.add("sdk.*")       // Ignore all keys matching the regexp "sdk.*"
}

dependencies {

    /* KotlinX */
    implementation(libs.core.ktx)
    implementation(libs.lifecycle.runtime.ktx)

    /* Compose */
    implementation(platform(libs.compose.bom))
    implementation(libs.compose.ui)
    implementation(libs.compose.material3)
    implementation(libs.material3.pullrefresh)
    implementation(libs.compose.ui.tooling)
    implementation(libs.compose.ui.tooling.preview)
    implementation(libs.compose.ui.util)
    implementation(libs.compose.viewmodel)
    implementation(libs.compose.runtime)
    implementation(libs.compose.ui.graphics)
    implementation(libs.activity.compose)
    implementation(libs.activity)
    implementation(libs.compose.navigation)
    implementation(libs.kotlinx.serialization.json)
    implementation(libs.compose.constraintlayout)

    /* DI */
    implementation(libs.hilt)
    implementation(libs.hilt.navigation.compose)
    implementation(libs.graphics.shapes.android)
    kapt(libs.hilt.compiler)

    /* Storage - Room */
    implementation(libs.datastore)
    annotationProcessor(libs.room.compiler)
    ksp(libs.room.compiler)
    implementation(libs.room.runtime)
    implementation(libs.room.ktx)

    /* Images */
    implementation(libs.landscapist.glide)
    implementation(libs.lottie.compose)

    /* Retrofit */
    implementation(libs.retrofit)
    implementation(libs.retrofit.moshi)
    implementation(libs.moshi.kotlin)
    ksp(libs.moshi.codegen)
    implementation(libs.okhttp.interceptor)
    implementation(libs.paging)
    implementation(libs.paging.compose)

    /* Google Services */
    implementation(libs.google.location)
    implementation(libs.google.maps.compose)
    implementation(libs.google.mlkit.translate)

    /* Firebase */
    implementation(platform(libs.firebase.bom))
    implementation(libs.firebase.analytics)

    /* Unit Test */
    testImplementation(libs.mockk)
    testImplementation(libs.jupiter.api)
    testImplementation(libs.jupiter.params)
    testRuntimeOnly(libs.jupiter.engine)
    testImplementation(libs.coroutines.test)

    /* UI Test */
    androidTestImplementation(platform(libs.compose.bom))
    androidTestImplementation(libs.test.ext.junit)
    androidTestImplementation(libs.espresso.core)
    androidTestImplementation(libs.compose.ui.test)
    androidTestImplementation(libs.test.rules)
    androidTestImplementation(libs.test.core.ktx)
    androidTestImplementation(libs.mockk.android)

    /* Debug */
    debugImplementation(libs.ui.test.manifest)
    debugImplementation(libs.leakcanary)
//    debugImplementation(libs.flipper)
//    debugImplementation(libs.soloader)
//
//    releaseImplementation(libs.flipper.noop)
}

kapt {
    correctErrorTypes = true
}