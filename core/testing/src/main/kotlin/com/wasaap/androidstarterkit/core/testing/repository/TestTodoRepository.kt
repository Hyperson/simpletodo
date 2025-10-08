package com.wasaap.androidstarterkit.core.testing.repository

import com.wasaap.androidstarterkit.core.domain.common.Result
import com.wasaap.androidstarterkit.core.domain.repository.TodoRepository
import com.wasaap.androidstarterkit.core.model.NewTodo
import com.wasaap.androidstarterkit.core.model.Todo
import com.wasaap.androidstarterkit.core.model.UpdateTodo
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.update
import org.jetbrains.annotations.TestOnly
import java.util.UUID


class TestTodoRepository : TodoRepository {

    private val cachedTodos = MutableStateFlow<List<Todo>>(emptyList())

    override fun getTodos(): Flow<Result<List<Todo>>> =
        cachedTodos.map { Result.Success(it) }

    override fun getTodoById(id: String): Flow<Result<Todo>> =
        cachedTodos.map { todos ->
            val todo = todos.find { it.id == id }
            if (todo != null) Result.Success(todo)
            else Result.Error(Exception("Todo not found"))
        }

    override fun addTodo(newTodo: NewTodo): Flow<Result<Todo>> = flow {
        val todo = Todo(
            id = UUID.randomUUID().toString(),
            name = newTodo.name,
            done = newTodo.done
        )
        cachedTodos.update { it + todo }
        emit(Result.Success(todo))
    }

    override fun updateTodo(updateTodo: UpdateTodo): Flow<Result<Unit>> = flow {
        val updatedList = cachedTodos.value.map {
            if (it.id == updateTodo.id) it.copy(
                name = updateTodo.name,
                done = updateTodo.done
            ) else it
        }
        cachedTodos.value = updatedList
        emit(Result.Success(Unit))
    }

    override fun deleteTodo(id: String): Flow<Result<Unit>> = flow {
        cachedTodos.update { list -> list.filterNot { it.id == id } }
        emit(Result.Success(Unit))
    }

    @TestOnly
    fun addTodos(todos: List<Todo>) = cachedTodos.update { it + todos }

    @TestOnly
    fun clearTodos() = cachedTodos.update { emptyList() }
}