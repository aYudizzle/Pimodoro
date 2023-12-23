package dev.ayupi

import com.android.build.api.dsl.ApplicationExtension
import org.gradle.api.Project

internal fun Project.configureAndroidApplication(
    extension: ApplicationExtension,
) {
    extension.apply {
        defaultConfig {
            targetSdk = 34
        }
    }
}