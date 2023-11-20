import java.text.SimpleDateFormat
import java.util.Date

plugins {
    alias(libs.plugins.kotlin.multiplatform)
    kotlin("native.cocoapods")
    alias(libs.plugins.android.library)
    alias(libs.plugins.compose)
    alias(libs.plugins.ksp)
    alias(libs.plugins.buildConfig)
    alias(libs.plugins.kotlin.serialization)
}

@OptIn(org.jetbrains.kotlin.gradle.ExperimentalKotlinGradlePluginApi::class)
kotlin {
    targetHierarchy.default()
    androidTarget()

    listOf(
//        iosX64(),
        iosArm64(),
        iosSimulatorArm64()
    ).forEach { target ->
        target.binaries.framework {
            isStatic = true
        }
    }

    cocoapods {
        version = "1.0.0"
        summary = "Some description for the Shared Module"
        homepage = "Link to the Shared Module homepage"
        ios.deploymentTarget = "14.1"
        podfile = project.file("../ios/Podfile") // why doesn't it load the cocoapods from the iosApp podfile?

        // Must define the pods that are in the Podfile (unknown why?)
        pod("GoogleMaps") {
            version = "8.2.0"
            extraOpts += listOf("-compiler-option", "-fmodules")
        }
    }

    sourceSets {
        commonMain {
            dependencies {
                implementation(compose.runtime)
                implementation(compose.runtimeSaveable)
                implementation(compose.foundation)
                implementation(compose.material3)
                implementation(compose.material)
                implementation(compose.materialIconsExtended)
                implementation(compose.ui)
                implementation(compose.animation)
                @OptIn(org.jetbrains.compose.ExperimentalComposeLibrary::class)
                implementation(compose.components.resources)
                implementation(libs.voyager.navigator)
                implementation(libs.voyager.transitions)
                implementation(libs.voyager.navigator.tabs)
                implementation(libs.kotlin.inject.runtime)
                implementation(libs.kotlin.viewmodel.core)
                implementation(libs.kotlinx.serialization.json)
                implementation(libs.kotlinx.datetime)
                implementation(libs.imageloader)
                implementation(libs.logging)
                implementation(libs.orbital)
                implementation(libs.settings)
                implementation(libs.settings.coroutines)
                implementation(libs.webview)
                implementation(libs.uuid)
            }
        }
        val androidMain by getting {
            dependencies {
                api(libs.androidx.activity.compose)
                api(libs.androidx.core)
                api(libs.androidx.appcompat)
                api(libs.android.threeten)
                api(libs.google.play.location)
                api(libs.google.maps.compose)
                api(libs.google.maps.compose.utils)
            }
        }
    }
}

android {
    namespace = "${(findProperty("project.namespace") as String)}.common"
    compileSdk = libs.versions.android.compileSdk.get().toInt()

    sourceSets["main"].manifest.srcFile("src/androidMain/AndroidManifest.xml")
    sourceSets["main"].res.srcDirs("src/androidMain/res")
    // loaded for android fonts
    sourceSets["main"].res.srcDirs("src/commonMain/res")
    sourceSets["main"].resources.srcDirs("src/commonMain/resources")

    defaultConfig {
        minSdk = libs.versions.android.minSdk.get().toInt()
    }


    compileOptions {
        // Enable support for the new language APIs
        isCoreLibraryDesugaringEnabled = true
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }

    kotlin {
        jvmToolchain(17)
    }
}

buildConfig {
    buildConfigField(
        "String",
        "APP_VERSION",
        provider {
            "\"${libs.versions.app.version.name.get()} " +
                    "(${SimpleDateFormat("MMddyyyy").format(Date())})\""
        }
    )
    useKotlinOutput()
}


dependencies {
    // KSP will eventually have better multiplatform support and we'll be able to simply have
    // `ksp libs.kotlinInject.compiler` in the dependencies block of each source set
    // https://github.com/google/ksp/pull/1021
//    add("kspIosX64", libs.kotlin.inject.compiler)
    add("kspIosArm64", libs.kotlin.inject.compiler)
    add("kspIosSimulatorArm64", libs.kotlin.inject.compiler)
    add("kspAndroid", libs.kotlin.inject.compiler)
    add("coreLibraryDesugaring", "com.android.tools:desugar_jdk_libs:2.0.4")
}