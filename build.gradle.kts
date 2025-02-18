plugins {
    kotlin("jvm") version "2.0.21" apply false
    alias(libs.plugins.kotlinMultiplatform) apply false
}

group = "ru.morozovit"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}