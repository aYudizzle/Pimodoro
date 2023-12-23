import com.android.build.api.dsl.LibraryExtension
import com.android.build.api.variant.LibraryAndroidComponentsExtension
import dev.ayupi.configureKotlinAndroid
import dev.ayupi.configureAndroidLibrary
import dev.ayupi.configurePrintApksTask
import dev.ayupi.disableUnnecessaryAndroidTests
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.configure
import org.gradle.kotlin.dsl.dependencies
import org.gradle.kotlin.dsl.kotlin

class AndroidLibraryConventionPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            with(pluginManager) {
                apply("com.android.library")
                apply("org.jetbrains.kotlin.android")
            }

            extensions.configure<com.android.build.gradle.LibraryExtension> {
                configureKotlinAndroid(this)
                defaultConfig.targetSdk = 34
//                configureGradleManagedDevices(this)
            }
            extensions.configure<LibraryAndroidComponentsExtension> {
                configurePrintApksTask(this)
                disableUnnecessaryAndroidTests(target)
            }
            dependencies {
                add("testImplementation", kotlin("test"))
                add("androidTestImplementation", kotlin("test"))
//                add("testImplementation", project(":core:testing"))
//                add("androidTestImplementation", project(":core:testing"))
            }
        }
    }
}

