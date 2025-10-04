package com.wasaap.androidstarterkit.core.database.model

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import com.wasaap.androidstarterkit.core.model.Todo

@Entity(tableName = "todos",indices = [Index(value = ["remoteId"], unique = true)])

data class TodoEntity(
    @PrimaryKey
    val localId: String,

    val remoteId: String? = null,

    val name: String,

    val done: Boolean,

    val isSynced: Boolean = false,

    val isDeleted: Boolean = false
)

fun TodoEntity.asExternalModel() = Todo(
    id = localId,
    name = name,
    done = done
)