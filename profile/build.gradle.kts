plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.kotlin)
}

apply<DemoPlugin>()

android {
    println("peter ")
    namespace = "com.cvopa.peter.profile"
    defaultConfig{
        minSdk = 26
        targetSdk = 34
    }

    kotlinOptions {
        jvmTarget = "1.8"
    }
}

dependencies {
    compileOnly(libs.gradle)
    implementation(libs.core)
    implementation(libs.appcompat)
    implementation(libs.material)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.test.ext.junit)
    androidTestImplementation(libs.espresso.core)
}