package com.wasaap.androidstarterkit.core.domain.usecase.todo

import com.wasaap.androidstarterkit.core.domain.repository.TodoRepository
import com.wasaap.androidstarterkit.core.model.Todo
import com.wasaap.androidstarterkit.core.domain.common.Result

import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetTodoByIdUseCase @Inject constructor(
    private val repository: TodoRepository,
) {
    operator fun invoke(id: String): Flow<Result<Todo>> = repository.getTodoById(id)
}