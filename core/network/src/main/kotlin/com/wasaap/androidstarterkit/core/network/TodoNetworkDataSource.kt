package com.wasaap.androidstarterkit.core.network

import com.wasaap.androidstarterkit.core.network.model.NetworkTodo
import com.wasaap.androidstarterkit.core.network.model.NetworkTodoWrite


interface TodoNetworkDataSource {
    suspend fun getTodos(): List<NetworkTodo>

    suspend fun getTodoById(id: String): NetworkTodo

    suspend fun addTodo(todo: NetworkTodoWrite): NetworkTodo

    suspend fun updateTodo(id: String, todo: NetworkTodoWrite)

    suspend fun deleteTodo(id: String)
}