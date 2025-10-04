package com.wasaap.androidstarterkit.core.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.wasaap.androidstarterkit.core.database.model.TodoEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface TodoDao {

    @Query("SELECT * FROM todos")
    fun getTodosStream(): Flow<List<TodoEntity>>

    @Query("SELECT * FROM todos WHERE isSynced = 0")
    suspend fun getPendingTodos(): List<TodoEntity>

    @Query("SELECT * FROM todos WHERE localId = :id LIMIT 1")
    fun getTodoByIdStream(id: String): Flow<TodoEntity?>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertOrReplace(todos: List<TodoEntity>)

    @Query("SELECT * FROM todos WHERE localId = :id")
    suspend fun getTodoById(id: String): TodoEntity?

    @Update
    suspend fun updateTodos(todos: List<TodoEntity>)

    @Query("DELETE FROM todos WHERE localId = :localId")
    suspend fun deleteByLocalId(localId: String)

    @Query("SELECT * FROM todos ORDER BY localId ASC")
    suspend fun getTodos(): List<TodoEntity>
}

