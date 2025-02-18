plugins {
    alias(libs.plugins.kotlinMultiplatform)
}

group = "ru.morozovit"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

kotlin {
    arrayOf(
        linuxX64(),
        mingwX64(),
        macosX64()
    ).forEach {
        it.binaries {
            executable {
                entryPoint = "ru.morozovit.jnistringencryptor.main"
            }
        }
    }

    sourceSets {
        val commonMain by getting {
            dependencies {
                implementation(project(":backend"))
            }
        }
    }
}

repositories {
    mavenCentral()
}

tasks.configureEach {
    if (this is BaseExecSpec) {
        standardInput = System.`in`
    }
}