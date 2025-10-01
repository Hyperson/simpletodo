package com.wasaap.androidstarterkit

/**
 * This could be shared between :app and :another module to provide configurations type safety.
 */
enum class TodoBuildType(val applicationIdSuffix: String? = null) {
    DEBUG(".debug"),
    RELEASE,
}
