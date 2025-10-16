package com.wasaap.androidstarterkit.core.domain

import com.wasaap.androidstarterkit.core.domain.common.Result
import com.wasaap.androidstarterkit.core.domain.usecase.todo.AddTodoUseCase
import com.wasaap.androidstarterkit.core.domain.usecase.todo.GetTodosPageUseCase
import com.wasaap.androidstarterkit.core.model.NewTodo
import com.wasaap.androidstarterkit.core.testing.repository.TestTodoRepository
import com.wasaap.androidstarterkit.core.testing.util.MainDispatcherRule
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.Rule
import org.junit.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class GetTodosPageUseCaseTest {

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    private val repository = TestTodoRepository()
    private val addTodoUseCase = AddTodoUseCase(repository)
    private val getTodosPageUseCase = GetTodosPageUseCase(repository)

    @Test
    fun whenTodosExist_theyAreReturned() = runTest {
        val todo1 = (addTodoUseCase(NewTodo("Write docs", false)).first() as Result.Success).data
        val todo2 = (addTodoUseCase(NewTodo("Read specs", true)).first() as Result.Success).data

        val todos = (getTodosPageUseCase().first() as Result.Success).data

        assertEquals(listOf(todo1, todo2), todos)
    }

    @Test
    fun whenNoTodosExist_emptyListIsReturned() = runTest {
        val todos = (getTodosPageUseCase().first() as Result.Success).data
        assertTrue(todos.isEmpty())
    }
}