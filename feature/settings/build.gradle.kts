@Suppress("DSL_SCOPE_VIOLATION") // TODO: Remove once KTIJ-19369 is fixed
plugins {
    alias(libs.plugins.ayupi.android.feature)
    alias(libs.plugins.ayupi.android.library.compose)
    alias(libs.plugins.ayupi.android.library.jacoco)
}

android {
    namespace = "dev.ayupi.pimodoro.feature.settings"
}

dependencies {
    implementation(libs.kotlinx.datetime)
    implementation(libs.androidx.activity.compose)
}
