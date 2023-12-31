buildscript {

    repositories {
        google()
        mavenCentral()
    }

    dependencies {
        classpath("com.google.gms:google-services:4.4.0")
        classpath("com.squareup.sqldelight:gradle-plugin:1.5.5")
    }
}
plugins {
    // this is necessary to avoid the plugins to be loaded multiple times
    // in each subproject's classloader
    kotlin("multiplatform").version("1.9.20").apply(false)
    id("com.android.application").version("8.1.3").apply(false)
    id("com.android.library").apply(false)
    id("org.jetbrains.compose").apply(false)
    id("com.google.gms.google-services").version("4.4.0").apply(false)

}

repositories {
    mavenCentral()
}

