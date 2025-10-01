plugins {
    alias(libs.plugins.starter.kit.jvm.library)
    alias(libs.plugins.starter.kit.hilt)
}

dependencies {
    implementation(libs.kotlinx.coroutines.core)
    testImplementation(libs.kotlinx.coroutines.test)
    testImplementation(libs.turbine)
}