import org.jetbrains.kotlin.gradle.dsl.KotlinJsCompile

plugins {
    alias(libs.plugins.kotlinMultiplatform)
}

group = "ru.morozovit"
version = "1.0.0"

kotlin {
    linuxX64()
    mingwX64()
    macosX64()
    js {
        browser()
        binaries.executable()
    }

    sourceSets {
        val commonTest by getting {
            dependencies {
                implementation(libs.kotlin.test)
            }
        }
    }
}

tasks.withType<KotlinJsCompile>().configureEach {
    compilerOptions {
        target = "es2015"
    }
}

repositories {
    mavenCentral()
}