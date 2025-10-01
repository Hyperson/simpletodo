package com.wasaap.androidstarterkit.core.data.repository

import com.wasaap.androidstarterkit.common.core.network.Dispatcher
import com.wasaap.androidstarterkit.common.core.network.TodoDispatchers
import com.wasaap.androidstarterkit.core.domain.repository.TodoRepository
import com.wasaap.androidstarterkit.core.domain.result.Result
import com.wasaap.androidstarterkit.core.domain.result.asResult
import com.wasaap.androidstarterkit.core.model.NewTodo
import com.wasaap.androidstarterkit.core.model.Todo
import com.wasaap.androidstarterkit.core.model.UpdateTodo
import com.wasaap.androidstarterkit.core.network.TodoNetworkDataSource
import com.wasaap.androidstarterkit.core.network.model.NetworkTodoWrite
import com.wasaap.androidstarterkit.core.network.model.asExternalModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

class TodoRepositoryImpl @Inject constructor(
    private val remote: TodoNetworkDataSource,
    @param:Dispatcher(TodoDispatchers.IO) private val ioDispatcher: CoroutineDispatcher
) : TodoRepository {
    override fun getTodos(): Flow<Result<List<Todo>>> =
        flow {
            val todos = remote.getTodos()
            emit(todos.map { it.asExternalModel() })
        }
            .asResult()
            .flowOn(ioDispatcher)

    override fun getTodoById(id: String): Flow<Result<Todo>> =
        flow {
            val todo = remote.getTodoById(id)
            emit(todo.asExternalModel())
        }
            .asResult()
            .flowOn(ioDispatcher)

    override fun addTodo(newTodo: NewTodo): Flow<Result<Todo>> = flow {
        val networkTodoWrite = NetworkTodoWrite(
            name = newTodo.name,
            done = newTodo.done
        )
        val todo = remote.addTodo(todo = networkTodoWrite)
        emit(todo.asExternalModel())
    }
        .asResult()
        .flowOn(ioDispatcher)

    override fun updateTodo(updateTodo: UpdateTodo): Flow<Result<Unit>> = flow {
        val body = NetworkTodoWrite(name = updateTodo.name, done = updateTodo.done)
        remote.updateTodo(updateTodo.id, body)
        emit(Unit)
    }.asResult()
        .flowOn(ioDispatcher)


    override fun deleteTodo(id: String): Flow<Result<Unit>> =
        flow {
            remote.deleteTodo(id)
            emit(Unit)
        }.asResult()
            .flowOn(ioDispatcher)
}