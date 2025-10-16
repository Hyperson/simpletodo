plugins {
    alias(libs.plugins.starter.kit.android.library)
    alias(libs.plugins.starter.kit.hilt)
    id("kotlinx-serialization")
}

android {
    namespace = "com.wasaap.androidstarterkit.core.data"
    testOptions {
        unitTests {
            isIncludeAndroidResources = true
        }
    }
}

dependencies {
    api(projects.core.common)
    api(projects.core.datastore)

    api(projects.core.database)
    api(projects.core.network)
    api(projects.core.domain)
    api(projects.core.speech)

    testImplementation(libs.kotlinx.coroutines.test)
    testImplementation(libs.kotlinx.serialization.json)
    testImplementation(projects.core.testing)
}
