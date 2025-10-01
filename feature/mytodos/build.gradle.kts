plugins {
    alias(libs.plugins.starter.kit.android.feature)
    alias(libs.plugins.starter.kit.android.library.compose)
}

android {
    namespace = "com.wasaap.androidstarterkit.feature.mytodos"
}

dependencies {
    implementation(projects.core.data)
    implementation(projects.core.domain)

    testImplementation(libs.hilt.android.testing)
    testImplementation(projects.core.testing)

    androidTestImplementation(libs.bundles.androidx.compose.ui.test)
    androidTestImplementation(projects.core.testing)
}
