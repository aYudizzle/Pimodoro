package dev.ayupi

import com.android.build.api.dsl.LibraryExtension
import org.gradle.api.Project

internal fun Project.configureAndroidLibrary(
    extension: LibraryExtension,
) {
    extension.apply {
        defaultConfig {
            targetSdk = 34
        }
    }
}