plugins {
    id("com.android.application")
    kotlin("android")
    id("tz.co.asoft.applikation")
}

android {
    configureAndroid("src/main")
    defaultConfig {
        minSdk = 21
    }
}

applikation {
    debug()
    release()
}

kotlin {
    target { targetJava("1.8") }
    sourceSets {
        val main by getting {
            dependencies {
                implementation(project(":todo-core"))
                implementation("androidx.appcompat:appcompat:1.2.0")
                implementation("androidx.constraintlayout:constraintlayout:2.0.4")
                implementation("com.google.android.material:material:1.4.0-alpha01")
                // Stripe Android SD
            }
        }

        val test by getting {
            dependencies {
                implementation(asoft("test-coroutines", vers.asoft.test))
                implementation(asoft("expect-core", vers.asoft.expect))
            }
        }
    }
}