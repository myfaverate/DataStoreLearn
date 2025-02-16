import com.android.tools.r8.internal.id
import com.google.protobuf.gradle.GenerateProtoTask
import org.jetbrains.kotlin.gradle.dsl.JvmTarget
import com.google.protobuf.gradle.id
import com.google.protobuf.gradle.proto

plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)
    alias(libs.plugins.com.google.protobuf)
}

android {
    namespace = "edu.tyut.datastorelearn"
    compileSdk = 35

    defaultConfig {
        applicationId = "edu.tyut.datastorelearn"
        minSdk = 24
        targetSdk = 35
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
        sourceCompatibility = JavaVersion.VERSION_21
        targetCompatibility = JavaVersion.VERSION_21
    }
    kotlinOptions {
        jvmTarget = JvmTarget.JVM_21.target
    }
    buildFeatures {
        compose = true
        buildConfig = true
    }
    sourceSets {
        getByName("main") {
            proto{
                srcDir("src/main/proto")
            }
        }
    }
}

protobuf {
    protoc {
        // The artifact spec for the Protobuf Compiler
        // artifact = "com.google.protobuf:protoc:4.29.3"
        artifact = "${libs.protobuf.compiler.get()}"
    }
    generateProtoTasks {
        all().forEach { task ->
            task.plugins{
                create("java"){
                    option("lite")
                }
                create("kotlin") {
                    option("lite")
                }
            }
        }
    }
}

dependencies {

    // MMKv
    implementation(libs.com.tencent.mmkv)

    // datastore-preferences
    implementation(libs.datastore.preferences)
    // datastore
    implementation(libs.androidx.datastore)


    // protobuf-javalite
    implementation(libs.protobuf.javalite)
    // protobuf-kotlin-lite
    implementation(libs.protobuf.kotlin.lite)

    // =================================================

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.activity.compose)
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.ui)
    implementation(libs.androidx.ui.graphics)
    implementation(libs.androidx.ui.tooling.preview)
    implementation(libs.androidx.material3)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.ui.test.junit4)
    debugImplementation(libs.androidx.ui.tooling)
    debugImplementation(libs.androidx.ui.test.manifest)
}