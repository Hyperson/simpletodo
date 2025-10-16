package com.wasaap.androidstarterkit.core.domain

import com.wasaap.androidstarterkit.core.domain.common.Result
import com.wasaap.androidstarterkit.core.domain.usecase.todo.AddTodoUseCase
import com.wasaap.androidstarterkit.core.domain.usecase.todo.GetTodoByIdUseCase
import com.wasaap.androidstarterkit.core.model.NewTodo
import com.wasaap.androidstarterkit.core.testing.repository.TestTodoRepository
import com.wasaap.androidstarterkit.core.testing.util.MainDispatcherRule
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.Rule
import org.junit.Test
import kotlin.test.assertEquals

class GetTodoByIdUseCaseTest {

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    private val repository = TestTodoRepository()
    private val addTodoUseCase = AddTodoUseCase(repository)
    private val getTodoByIdUseCase = GetTodoByIdUseCase(repository)

    @Test
    fun whenTodoExists_itIsReturnedById() = runTest {
        val addedTodo = (addTodoUseCase(NewTodo("Write docs", false)).first() as Result.Success).data
        val fetchedTodo = (getTodoByIdUseCase(addedTodo.id).first() as Result.Success).data

        assertEquals(addedTodo, fetchedTodo)
    }

    @Test
    fun whenTodoDoesNotExist_errorIsReturned() = runTest {
        val result = getTodoByIdUseCase("invalid_id").first()

        assert(result is Result.Error)
    }
}