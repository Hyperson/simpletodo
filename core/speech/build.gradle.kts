plugins {
    alias(libs.plugins.starter.kit.android.library)
    alias(libs.plugins.starter.kit.hilt)
}

android {
    namespace = "com.wasaap.androidstarterkit.core.speech"
}

dependencies {
    api(projects.core.common)
}