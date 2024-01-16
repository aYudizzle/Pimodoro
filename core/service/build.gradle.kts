import org.gradle.kotlin.dsl.projects

@Suppress("DSL_SCOPE_VIOLATION") // TODO: Remove once KTIJ-19369 is fixed
plugins {
    alias(libs.plugins.ayupi.android.library)
    alias(libs.plugins.ayupi.android.hilt)
}

android {
    namespace = "dev.ayupi.pimodoro.core.service"
}

dependencies {
    implementation(projects.core.data)
    implementation(projects.core.manager)

    implementation(libs.androidx.core.ktx)
    implementation(projects.core.common)
    implementation(projects.core.model)
}