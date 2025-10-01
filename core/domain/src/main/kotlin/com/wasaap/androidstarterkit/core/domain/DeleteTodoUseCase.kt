package com.wasaap.androidstarterkit.core.domain

import com.wasaap.androidstarterkit.core.domain.result.Result
import com.wasaap.androidstarterkit.core.domain.repository.TodoRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class DeleteTodoUseCase @Inject constructor(
    private val repository: TodoRepository,
) {
    operator fun invoke(id: String) : Flow<Result<Unit>> = repository.deleteTodo(id)
}