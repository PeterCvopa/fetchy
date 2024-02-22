import org.gradle.api.Plugin
import org.gradle.api.Project
import com.android.build.gradle.LibraryExtension
import org.gradle.api.JavaVersion

class DemoPlugin : Plugin<Project> {

    override fun apply(project: Project) {

        println("Peter")
        project.plugins.apply("kotlin-android")
        project.plugins.apply("com.android.library")

        project.task("hellotask") {
            doLast {
                println("Hello from the GreetingPlugin")
            }
        }
    }

    private fun applyPlugins(project: Project) {



  /*      project.apply {
            plugin("android-library")
            plugin("dagger.hilt.android.plugin")
        }*/
    }

/*    private fun setProjectConfig(project: Project) {
        project.android().apply {
            compileSdk = Versions.COMPILE_SDK

            defaultConfig {
                targetSdk = Versions.TARGET_SDK
                minSdk = Versions.MIN_SDK
                testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
            }

            compileOptions {
                sourceCompatibility = JavaVersion.VERSION_18
                targetCompatibility = JavaVersion.VERSION_18
            }
        }
    }

    private fun Project.android(): LibraryExtension {
        return extensions.getByType(LibraryExtension::class.java)
    }*/
}