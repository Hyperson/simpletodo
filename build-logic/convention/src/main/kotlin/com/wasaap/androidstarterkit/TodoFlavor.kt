package com.wasaap.androidstarterkit

import com.android.build.api.dsl.ApplicationExtension
import com.android.build.api.dsl.ApplicationProductFlavor
import com.android.build.api.dsl.CommonExtension
import com.android.build.api.dsl.ProductFlavor

@Suppress("EnumEntryName")
enum class FlavorDimension {
    contentType
}

@Suppress("EnumEntryName")
enum class TodoFlavor(val dimension: FlavorDimension, val applicationIdSuffix: String? = null) {
    demo(FlavorDimension.contentType, applicationIdSuffix = ".demo"),
    prod(FlavorDimension.contentType),
}

fun configureFlavors(
    commonExtension: CommonExtension<*, *, *, *, *, *>,
    flavorConfigurationBlock: ProductFlavor.(flavor: TodoFlavor) -> Unit = {},
) {
    commonExtension.apply {
        FlavorDimension.values().forEach { flavorDimension ->
            flavorDimensions += flavorDimension.name
        }

        productFlavors {
            TodoFlavor.values().forEach { todoFlavor ->
                register(todoFlavor.name) {
                    dimension = todoFlavor.dimension.name
                    flavorConfigurationBlock(this, todoFlavor)
                    if (this@apply is ApplicationExtension && this is ApplicationProductFlavor) {
                        if (todoFlavor.applicationIdSuffix != null) {
                            applicationIdSuffix = todoFlavor.applicationIdSuffix
                        }
                    }
                }
            }
        }
    }
}
