import com.android.build.gradle.LibraryExtension
import dev.ayupi.libs
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.configure
import org.gradle.kotlin.dsl.dependencies
import org.gradle.kotlin.dsl.kotlin

class AndroidFeatureConventionPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            pluginManager.apply {
                apply("ayupi.android.library")
                apply("ayupi.android.hilt")
            }
            extensions.configure<LibraryExtension> {
                defaultConfig {
                    testInstrumentationRunner =
                        ""
                }
//                configureGradleManagedDevices(this)
            }

            dependencies {
                add("implementation", project(":core:model"))
                add("implementation", project(":core:common"))
                add("implementation", project(":core:designsystem"))
                add("implementation", project(":core:data"))
                add("implementation", project(":core:manager"))

//                add("implementation", project(":core:ui"))
                add("implementation", project(":core:domain"))
//                add("implementation", project(":core:analytics"))

                add("testImplementation", kotlin("test"))
//                add("testImplementation", project(":core:testing"))
                add("androidTestImplementation", kotlin("test"))
//                add("androidTestImplementation", project(":core:testing"))

                add("implementation", libs.findLibrary("coil.kt").get())
                add("implementation", libs.findLibrary("coil.kt.compose").get())

                add("implementation", libs.findLibrary("androidx.hilt.navigation.compose").get())
                add("implementation", libs.findLibrary("androidx.lifecycle.runtimeCompose").get())
                add("implementation", libs.findLibrary("androidx.lifecycle.viewModelCompose").get())

                add("implementation", libs.findLibrary("androidx.compose.ui.tooling").get())
                add("debugImplementation", libs.findLibrary("androidx.compose.ui.tooling.preview").get())

//                implementation("androidx.compose.ui:ui-tooling-preview:1.1.1")
//                debugImplementation("androidx.compose.ui:ui-tooling:1.1.1")
                add("implementation", libs.findLibrary("kotlinx.coroutines.android").get())
            }
        }
    }
}