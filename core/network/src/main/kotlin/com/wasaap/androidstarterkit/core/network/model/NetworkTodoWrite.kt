package com.wasaap.androidstarterkit.core.network.model

import kotlinx.serialization.Serializable

@Serializable
data class NetworkTodoWrite(
    val name: String,
    val done: Boolean,
)