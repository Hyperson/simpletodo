package com.wasaap.androidstarterkit.core.domain.repository

import com.wasaap.androidstarterkit.core.model.NewTodo
import com.wasaap.androidstarterkit.core.domain.common.Result
import com.wasaap.androidstarterkit.core.model.Todo
import com.wasaap.androidstarterkit.core.model.UpdateTodo
import kotlinx.coroutines.flow.Flow

interface TodoRepository {
    fun getTodos(): Flow<Result<List<Todo>>>
    fun getTodoById(id: String): Flow<Result<Todo>>
    fun addTodo(newTodo: NewTodo): Flow<Result<Todo>>
    fun updateTodo(updateTodo: UpdateTodo): Flow<Result<Unit>>
    fun deleteTodo(id: String): Flow<Result<Unit>>
}