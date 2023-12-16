buildscript {
    dependencies {
        classpath("com.google.gms:google-services:4.4.0")
        classpath("com.squareup.sqldelight:gradle-plugin:1.5.5")
    }
}
plugins {
    // this is necessary to avoid the plugins to be loaded multiple times
    // in each subproject's classloader
    kotlin("multiplatform").apply(false)
    id("com.android.application").apply(false)
    id("com.android.library").apply(false)
    id("org.jetbrains.compose").apply(false)
    id("com.google.gms.google-services").version("4.3.14").apply(false)

}
