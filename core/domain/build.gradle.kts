@Suppress("DSL_SCOPE_VIOLATION") // TODO: Remove once KTIJ-19369 is fixed
plugins {
    alias(libs.plugins.ayupi.android.library)
    alias(libs.plugins.ayupi.android.library.jacoco)
    id("com.google.devtools.ksp")
}

android {
    namespace = "dev.ayupi.core.domain"
}

dependencies {
    implementation(projects.core.data)
    implementation(projects.core.model)
    implementation(libs.hilt.android)
    implementation(libs.kotlinx.coroutines.android)
    implementation(libs.kotlinx.datetime)

    ksp(libs.hilt.compiler)
}