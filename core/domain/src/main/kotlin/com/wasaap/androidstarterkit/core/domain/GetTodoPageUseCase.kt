package com.wasaap.androidstarterkit.core.domain

import com.wasaap.androidstarterkit.core.domain.repository.TodoRepository
import com.wasaap.androidstarterkit.core.model.Todo
import com.wasaap.androidstarterkit.core.domain.common.Result

import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetTodosPageUseCase @Inject constructor(
    private val repository: TodoRepository,
) {
    operator fun invoke(): Flow<Result<List<Todo>>> = repository.getTodos()
}