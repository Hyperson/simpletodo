plugins {
    alias(libs.plugins.starter.kit.android.library)
    id("com.google.devtools.ksp")
}

android {
    namespace = "com.wasaap.androidstarterkit.core.domain"
}

dependencies {
    api(projects.core.model)

    implementation(libs.kotlinx.coroutines.core)

    implementation(libs.javax.inject)

    testImplementation(projects.core.testing)
}