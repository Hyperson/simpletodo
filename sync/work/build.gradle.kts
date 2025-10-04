plugins {
    alias(libs.plugins.starter.kit.android.library)
    alias(libs.plugins.starter.kit.hilt)
}

android {
    defaultConfig {
        testInstrumentationRunner = "com.wasaap.androidstarterkit.core.testing.TodoTestRunner"
    }
    namespace = "com.wasaap.androidstarterkit.sync.work"
}

dependencies {
    ksp(libs.hilt.ext.compiler)

    implementation(libs.androidx.work.ktx)
    implementation(libs.hilt.ext.work)
    implementation(projects.core.data)


    androidTestImplementation(libs.androidx.work.testing)
    androidTestImplementation(libs.hilt.android.testing)
    androidTestImplementation(projects.core.testing)
}
