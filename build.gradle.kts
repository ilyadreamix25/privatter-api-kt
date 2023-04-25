plugins {
    alias(libs.plugins.kotlin.jvm)
    alias(libs.plugins.kotlin.serialization)
    alias(libs.plugins.kotlin.spring)
    alias(libs.plugins.kotlin.jpa)
    alias(libs.plugins.spring.framework.boot)
    alias(libs.plugins.spring.dependency.managment)
}

group = "com.privatter.api"
version = "1.1.1"

repositories {
    mavenCentral()
}

dependencies {
    implementation(libs.kotlin.reflect)
    implementation(libs.kotlinx.serialization.json)
    implementation(libs.kotlinx.coroutines.core)
    implementation(libs.kotlinx.coroutines.reactor)
    implementation(libs.springboot.starter.data.jpa)
    implementation(libs.springboot.starter.webflux)
    implementation(libs.springboot.starter.web)
    implementation(libs.springboot.starter.mail)
    runtimeOnly(libs.postgresql)
}

allOpen {
    annotation("com.privatter.api.v2.annotation.Open")
}
