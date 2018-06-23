import org.jetbrains.kotlin.config.KotlinCompilerVersion

plugins {
    id("java-library")
    id("kotlin")
}

object Versions {
    const val supportLibVersion = "27.0.0"
    const val runnerVersion = "1.0.1"
    const val rulesVersion = "1.0.1"
    const val espressoVersion = "3.0.1"
    const val archLifecycleVersion = "1.1.1"
    const val archRoomVersion = "1.1.0"
    const val pagingVersion = "1.0.0"
    const val retrofitVersion = "2.4.0"
    const val daggerVersion = "2.16"
}



dependencies {
    implementation(kotlin("stdlib", KotlinCompilerVersion.VERSION))
    implementation(project(":model"))

    api("com.squareup.retrofit2:retrofit:${Versions.retrofitVersion}")
    api("com.squareup.retrofit2:converter-gson:${Versions.retrofitVersion}")

    testImplementation("com.nhaarman:mockito-kotlin:1.5.0")
    testImplementation("android.arch.core:core-testing:${Versions.archLifecycleVersion}")
    testImplementation("org.mockito:mockito-core:2.19.0")
    testImplementation("junit:junit:4.12")
}

java{
    sourceSets {
        getByName("main").java.srcDirs("src/main/kotlin")
        getByName("test").java.srcDirs("src/test/kotlin")
    }

    sourceCompatibility = JavaVersion.VERSION_1_7
    targetCompatibility = JavaVersion.VERSION_1_7
}




repositories {
    mavenCentral()
}