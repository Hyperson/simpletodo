package com.wasaap.androidstarterkit.core.domain

import com.wasaap.androidstarterkit.core.domain.repository.TodoRepository
import com.wasaap.androidstarterkit.core.model.UpdateTodo
import kotlinx.coroutines.flow.Flow
import com.wasaap.androidstarterkit.core.domain.common.Result
import javax.inject.Inject

class UpdateTodoCompletedUseCase @Inject constructor(
    private val repository: TodoRepository,
) {
    operator fun invoke(updateTodo: UpdateTodo): Flow<Result<Unit>> =
        repository.updateTodo(updateTodo)
}