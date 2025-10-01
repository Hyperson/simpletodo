package com.wasaap.androidstarterkit.core.data.model

import com.wasaap.androidstarterkit.core.database.model.TodoEntity
import com.wasaap.androidstarterkit.core.model.Todo
import com.wasaap.androidstarterkit.core.network.model.NetworkTodo

fun NetworkTodo.asEntity() = TodoEntity(
    id = id ?: "-1",
    name = name,
    done = done,
)

fun TodoEntity.asExternalModel() = Todo(
    id = id,
    name = name,
    done = done,
)