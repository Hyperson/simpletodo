package com.wasaap.androidstarterkit.core.network.model

import com.wasaap.androidstarterkit.core.model.Todo
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class NetworkTodo(
    @SerialName("_id") val id: String? = null,
    val name: String,
    val done: Boolean,
)

/**
 * Convert network model to external (domain) model
 */
fun NetworkTodo.asExternalModel(): Todo =
    Todo(
        id = id ?: "-1",
        name = name,
        done = done,
    )