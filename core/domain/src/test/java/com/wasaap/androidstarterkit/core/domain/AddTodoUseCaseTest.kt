package com.wasaap.androidstarterkit.core.domain

import com.wasaap.androidstarterkit.core.model.NewTodo
import com.wasaap.androidstarterkit.core.testing.repository.TestTodoRepository
import com.wasaap.androidstarterkit.core.testing.util.MainDispatcherRule
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import com.wasaap.androidstarterkit.core.domain.common.Result
import org.junit.Rule
import org.junit.Test
import kotlin.test.assertEquals

class AddTodoUseCaseTest {

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    private val repository = TestTodoRepository()
    private val useCase = AddTodoUseCase(repository)

    @Test
    fun whenNewTodoIsAdded_todoIsReturnedAndStored() = runTest {
        val newTodo = NewTodo("Write documentation", false)

        val todo = (useCase(newTodo).first() as Result.Success).data
        val storedTodo = (repository.getTodoById(todo.id).first() as Result.Success).data

        assertEquals(storedTodo, todo)
    }
}