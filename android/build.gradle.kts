import java.text.SimpleDateFormat
import java.util.Date

plugins {
    alias(libs.plugins.kotlin.multiplatform)
    alias(libs.plugins.android.application)
    alias(libs.plugins.compose)
    alias(libs.plugins.ksp)
}

kotlin {
    androidTarget()
}

android {
    compileSdk = libs.versions.android.compileSdk.get().toInt()
    namespace = (findProperty("project.namespace") as String)

    sourceSets["main"].manifest.srcFile("src/androidMain/AndroidManifest.xml")

    defaultConfig {
        minSdk = libs.versions.android.minSdk.get().toInt()
        versionCode = libs.versions.app.version.code.get().toInt()
        versionName = "${libs.versions.app.version.name.get()} (${SimpleDateFormat("MMddyyyy").format(Date())})"
    }
    compileOptions {
        isCoreLibraryDesugaringEnabled = true
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
    kotlin {
        jvmToolchain(17)
    }
    buildFeatures {
        compose = true
    }
    composeOptions {
        kotlinCompilerExtensionVersion = libs.versions.compose.compiler.get()
    }
    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
            excludes += "/META-INF/versions/9/previous-compilation-data.bin"
        }
    }
    buildTypes {
        getByName("release") {
            isMinifyEnabled = false
        }
    }
}

dependencies {
    implementation(project(":shared"))
    implementation(libs.kotlin.inject.runtime)
    ksp(libs.kotlin.inject.compiler)
    coreLibraryDesugaring("com.android.tools:desugar_jdk_libs:2.0.4")
}