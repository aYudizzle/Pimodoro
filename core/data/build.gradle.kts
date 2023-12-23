@Suppress("DSL_SCOPE_VIOLATION") // TODO: Remove once KTIJ-19369 is fixed
plugins {
    alias(libs.plugins.ayupi.android.library)
    alias(libs.plugins.ayupi.android.hilt)
    id("kotlinx-serialization")
}

android {
    namespace = "dev.ayupi.pimodoro.core.data"
}

dependencies {
    implementation(projects.core.common)
    implementation(projects.core.datastore)
    implementation(projects.core.manager)
    implementation(projects.core.model)
    implementation(libs.androidx.core.ktx)
    implementation(libs.kotlinx.coroutines.android)
    implementation(libs.kotlinx.serialization.json)

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    testImplementation(libs.junit4)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.test.espresso.core)
}