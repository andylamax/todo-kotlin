plugins {
    id("com.android.library") version vers.agp apply false
    kotlin("multiplatform") version vers.kotlin apply false
    id("tz.co.asoft.library") version vers.asoft.builders apply false
    id("io.codearte.nexus-staging") version vers.nexus_staging apply false
}