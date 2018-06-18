import com.android.build.gradle.api.AndroidSourceSet
import org.gradle.api.internal.HasConvention
import org.jetbrains.kotlin.config.KotlinCompilerVersion
import org.jetbrains.kotlin.gradle.plugin.KotlinSourceSet

plugins {
    id("com.android.application")
    id("kotlin-android")
}

android {
    compileSdkVersion(27)
    defaultConfig {
        applicationId = "com.example.radek.movielist"
        minSdkVersion(15)
        targetSdkVersion(27)
        versionCode = 1
        versionName = "1.0"
        testInstrumentationRunner = "android.support.loadMoviesPage.runner.AndroidJUnitRunner"
    }
    buildTypes {
        getByName("release") {
            isMinifyEnabled = true
            proguardFiles(
                    getDefaultProguardFile("proguard-android.txt"),
                    "proguard-rules.pro",
                    "proguard-rules-retrofit.pro"
            )
        }
//        getByName("debug") {
//            isMinifyEnabled = true
//            proguardFiles(
//                    getDefaultProguardFile("proguard-android.txt"),
//                    "proguard-rules.pro",
//                    "proguard-rules-retrofit.pro",
//                    "proguard-rules-okhttp.pro"
//            )
//        }
    }

    sourceSets {
        getByName("main").java.srcDirs("src/main/kotlin")
    }
}

object Versions {
    const val supportLibVersion = "27.0.0"
    const val runnerVersion = "1.0.1"
    const val rulesVersion = "1.0.1"
    const val espressoVersion = "3.0.1"
    const val archLifecycleVersion = "1.1.1"
    const val archRoomVersion = "1.0.0"
    const val pagingVersion = "1.0.0"
    const val retrofitVersion = "2.4.0"
}

dependencies {
    implementation(kotlin("stdlib", KotlinCompilerVersion.VERSION))
    implementation("com.android.support:appcompat-v7:${Versions.supportLibVersion}")
    implementation("com.android.support:cardview-v7:${Versions.supportLibVersion}")
    implementation("com.android.support:recyclerview-v7:${Versions.supportLibVersion}")
    implementation("com.android.support:support-v4:${Versions.supportLibVersion}")

    implementation("android.arch.paging:runtime:${Versions.pagingVersion}")
    implementation("android.arch.lifecycle:extensions:${Versions.archLifecycleVersion}")
    implementation("android.arch.persistence.room:runtime:${Versions.archRoomVersion}")
    annotationProcessor("android.arch.lifecycle:compiler:${Versions.archLifecycleVersion}")
    annotationProcessor("android.arch.persistence.room:compiler:${Versions.archRoomVersion}")

    implementation("com.squareup.retrofit2:retrofit:${Versions.retrofitVersion}")
    implementation("com.squareup.retrofit2:converter-gson:${Versions.retrofitVersion}")

    testImplementation("junit:junit:4.12")

    val excludeSupportAnnotations: ExternalModuleDependency.() -> Unit = {
        exclude(module = "support-annotations", group = "com.android.support")
    }
    androidTestImplementation("com.android.support.loadMoviesPage.espresso:espresso-core:3.0.2", excludeSupportAnnotations)
}