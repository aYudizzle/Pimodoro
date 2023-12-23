@Suppress("DSL_SCOPE_VIOLATION") // TODO: Remove once KTIJ-19369 is fixed
plugins {
    alias(libs.plugins.ayupi.android.library)
    alias(libs.plugins.ayupi.android.hilt)
    id("kotlinx-serialization")
}

android {
    defaultConfig {
        consumerProguardFiles("consumer-proguard-rules.pro")
    }
    namespace = "dev.ayupi.pimodoro.core.datastore"

    testOptions {
        unitTests {
            isReturnDefaultValues = true
        }
    }
}

dependencies {
    implementation(projects.core.common)
    implementation(projects.core.model)

    implementation(libs.androidx.dataStore.core)
    implementation(libs.kotlinx.coroutines.android)
    implementation(libs.protobuf.kotlin.lite)

    implementation(libs.kotlinx.datetime)
    implementation(libs.kotlinx.serialization.json)
}