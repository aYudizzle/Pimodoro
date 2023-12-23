@Suppress("DSL_SCOPE_VIOLATION") // TODO: Remove once KTIJ-19369 is fixed
plugins {
    alias(libs.plugins.ayupi.android.library)
    alias(libs.plugins.ayupi.android.hilt)
}

android {
    namespace = "dev.ayupi.pimodoro.core.common"

}

dependencies {
    implementation(libs.kotlinx.coroutines.android)
//    implementation(libs.appcompat)
//    implementation(libs.material)
//    testImplementation(libs.junit)
//    androidTestImplementation(libs.androidx.test.ext.junit)
//    androidTestImplementation(libs.espresso.core)
}