@Suppress("DSL_SCOPE_VIOLATION") // TODO: Remove once KTIJ-19369 is fixed
plugins {
    alias(libs.plugins.ayupi.android.feature)
    alias(libs.plugins.ayupi.android.library.compose)
}

android {
    namespace = "dev.ayupi.pimodoro.feature.timer"
}

dependencies {
    implementation(libs.kotlinx.datetime)
    implementation(libs.androidx.activity.compose)
}