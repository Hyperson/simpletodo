package com.wasaap.androidstarterkit.core.domain

import com.wasaap.androidstarterkit.core.domain.repository.TodoRepository
import com.wasaap.androidstarterkit.core.model.NewTodo
import com.wasaap.androidstarterkit.core.model.Todo
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import com.wasaap.androidstarterkit.core.domain.result.Result


class AddTodoUseCase @Inject constructor(
    private val repository: TodoRepository,
) {
    operator fun invoke(newTodo: NewTodo): Flow<Result<Todo>> =
        repository.addTodo(newTodo)
}