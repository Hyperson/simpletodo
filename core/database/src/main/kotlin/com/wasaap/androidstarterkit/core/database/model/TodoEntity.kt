package com.wasaap.androidstarterkit.core.database.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.wasaap.androidstarterkit.core.model.Todo

@Entity(tableName = "todos")
data class TodoEntity(
    @PrimaryKey
    val id: String,

    val name: String,

    val done: Boolean,
)

fun TodoEntity.asExternalModel() = Todo(
    id = id,
    name = name,
    done = done,
)