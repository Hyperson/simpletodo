package com.wasaap.androidstarterkit.core.domain

import com.wasaap.androidstarterkit.core.domain.repository.TodoRepository
import com.wasaap.androidstarterkit.core.model.Todo
import com.wasaap.androidstarterkit.core.domain.result.Result
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject


class GetTodoByIdUseCase @Inject constructor(
    private val repository: TodoRepository,
) {
    operator fun invoke(id: String): Flow<Result<Todo>> = repository.getTodoById(id)
}