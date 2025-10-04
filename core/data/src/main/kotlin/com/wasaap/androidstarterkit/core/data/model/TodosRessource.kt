package com.wasaap.androidstarterkit.core.data.model

import com.wasaap.androidstarterkit.core.database.model.TodoEntity
import com.wasaap.androidstarterkit.core.model.Todo
import com.wasaap.androidstarterkit.core.network.model.NetworkTodo
import java.util.UUID

fun NetworkTodo.asEntity(): TodoEntity = TodoEntity(
    localId = UUID.randomUUID().toString(),
    remoteId = id,
    name = name,
    done = done,
    isSynced = true,
    isDeleted = false
)

fun TodoEntity.asExternalModel(): Todo = Todo(
    id = remoteId ?: localId,
    name = name,
    done = done
)