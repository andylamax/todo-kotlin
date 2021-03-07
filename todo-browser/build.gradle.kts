plugins {
    kotlin("js")
    id("tz.co.asoft.applikation")
}

applikation {
    debug()
    staging()
    release()
}

kotlin {
    js(IR) { application() }

    sourceSets {
        val main by getting {
            dependencies {
                implementation(project(":todo-core"))
                implementation(asoft("viewmodel-react", vers.asoft.viewmodel))
                implementation(asoft("reakt-media", vers.asoft.reakt))
                implementation(asoft("reakt-text", vers.asoft.reakt))
                implementation(asoft("form-react", vers.asoft.form))
                implementation(asoft("reakt-buttons", vers.asoft.reakt))
            }
        }
    }
}
