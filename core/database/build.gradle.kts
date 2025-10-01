plugins {
    alias(libs.plugins.starter.kit.android.library)
    alias(libs.plugins.starter.kit.android.room)
    alias(libs.plugins.starter.kit.hilt)
}

android {
    namespace = "com.wasaap.androidstarterkit.core.database"
}

dependencies {
    api(projects.core.model)

    implementation(libs.kotlinx.datetime)

    androidTestImplementation(libs.androidx.test.core)
    androidTestImplementation(libs.androidx.test.runner)
    androidTestImplementation(libs.kotlinx.coroutines.test)
}
