package com.wasaap.androidstarterkit.core.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.wasaap.androidstarterkit.core.database.model.TodoEntity
import com.wasaap.androidstarterkit.core.database.dao.TodoDao

@Database(
    entities = [TodoEntity::class],
    version = 1,
    exportSchema = true,
)
abstract class TodoDatabase : RoomDatabase() {
    abstract fun todoDao(): TodoDao
}