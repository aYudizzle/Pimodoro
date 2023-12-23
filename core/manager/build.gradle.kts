@Suppress("DSL_SCOPE_VIOLATION") // TODO: Remove once KTIJ-19369 is fixed
plugins {
    alias(libs.plugins.ayupi.android.library)
    alias(libs.plugins.ayupi.android.hilt)
    id("kotlinx-serialization")
}

android {
    namespace = "dev.ayupi.core.manager"
}

dependencies {
    implementation(projects.core.common)
    implementation(projects.core.model)
    implementation(projects.core.datastore)
//    implementation(projects.core.data)
}