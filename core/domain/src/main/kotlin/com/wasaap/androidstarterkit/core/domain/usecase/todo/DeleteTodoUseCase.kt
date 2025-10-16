package com.wasaap.androidstarterkit.core.domain.usecase.todo

import com.wasaap.androidstarterkit.core.domain.repository.TodoRepository
import kotlinx.coroutines.flow.Flow
import com.wasaap.androidstarterkit.core.domain.common.Result

import javax.inject.Inject

class DeleteTodoUseCase @Inject constructor(
    private val repository: TodoRepository,
) {
    operator fun invoke(id: String) : Flow<Result<Unit>> = repository.deleteTodo(id)
}