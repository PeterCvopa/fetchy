plugins {
    alias(libs.plugins.gradle)
    alias(libs.plugins.kotlin)
    alias(libs.plugins.ksp)
    alias(libs.plugins.hilt)
}

android {
    namespace = "com.cvopa.peter.fetchy"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.cvopa.peter.fetchy"
        minSdk = 26
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        vectorDrawables {
            useSupportLibrary = true
        }
    }

    buildTypes {
        debug {
            isMinifyEnabled = false
            buildConfigField( "String",     "BASEURL", "\"https://mobility.cleverlance.com/\"")
        }

        release {
            isMinifyEnabled = false
            buildConfigField( "String", "BASEURL", "\"https://mobility.cleverlance.com/\"")
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
    buildFeatures {
        compose = true
        buildConfig = true
    }
    composeOptions {
        kotlinCompilerExtensionVersion = "1.5.4"
    }
    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
}

dependencies {

    //timber
    implementation(libs.timber)

    // lc
    implementation(libs.lifecycle.compose)
    implementation(libs.lifecycle.ktx)
    implementation(libs.lifecycle.livedata.ktx)
    implementation(libs.lifecycle.runtime.ktx)
    implementation(libs.lifecycle.runtime.compose)
    implementation(libs.lifecycle.savestate)
    ksp(libs.lifecycle.compiler)

    //hilt
    implementation(libs.hilt)
    ksp(libs.hilt.compiler)
    ksp(libs.hilt.lifecycle)
    implementation(libs.hilt.compose)


    // dagger
    implementation(libs.dagger.base)
    implementation(libs.dagger.android)
    implementation(libs.dagger.android.support)

    //core
    implementation(libs.core)

    //retrofit
    implementation(libs.retrofit.base)
    implementation(libs.retrofit.moshi)
    implementation(libs.retrofit.interceptor)

    // guava
    implementation(libs.guava)

    //compose
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.compose.foundation)
    implementation(libs.androidx.compose.ui)
    implementation(libs.androidx.compose.ui.preview)
    implementation(libs.androidx.compose.material3)

    //test
    testImplementation(libs.mockk)
    testImplementation(libs.junit)
    testImplementation(libs.coroutine.test)
    testImplementation(libs.turbine)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.junit.compose)
    debugImplementation(libs.androidx.compose.ui.preview)
    debugImplementation(libs.junit.compose.ui.manifest)
}