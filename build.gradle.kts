plugins {
    alias(libs.plugins.kotlin.jvm)
    alias(libs.plugins.kotlin.serialization)
    alias(libs.plugins.kotlin.spring)
    alias(libs.plugins.kotlin.jpa)
    alias(libs.plugins.spring.framework.boot)
    alias(libs.plugins.spring.dependency.managment)
}

group = "com.privatter.api"
version = "1.0.5"

repositories {
    mavenCentral()
}

dependencies {
    implementation(libs.kotlin.reflect)
    implementation(libs.kotlinx.serialization.json)
    implementation(libs.springboot.starter.data.jpa)
    implementation(libs.springboot.starter.web)
    implementation(libs.springboot.starter.mail)
    runtimeOnly(libs.postgresql)
}
