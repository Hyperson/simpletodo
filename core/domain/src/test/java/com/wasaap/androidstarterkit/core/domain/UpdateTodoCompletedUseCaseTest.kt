package com.wasaap.androidstarterkit.core.domain

import com.wasaap.androidstarterkit.core.domain.common.Result
import com.wasaap.androidstarterkit.core.model.NewTodo
import com.wasaap.androidstarterkit.core.model.UpdateTodo
import com.wasaap.androidstarterkit.core.testing.repository.TestTodoRepository
import com.wasaap.androidstarterkit.core.testing.util.MainDispatcherRule
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.Rule
import org.junit.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue


class UpdateTodoCompletedUseCaseTest {

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    private val repository = TestTodoRepository()
    private val addTodoUseCase = AddTodoUseCase(repository)
    private val updateTodoCompletedUseCase = UpdateTodoCompletedUseCase(repository)
    private val getTodoByIdUseCase = GetTodoByIdUseCase(repository)

    @Test
    fun whenTodoIsUpdated_doneStateIsChanged() = runTest {
        val addedTodo = (addTodoUseCase(NewTodo("Write docs", false)).first() as Result.Success).data

        val update = UpdateTodo(id = addedTodo.id, name = addedTodo.name, done = true)
        val updateResult = updateTodoCompletedUseCase(update).first()
        assertTrue(updateResult is Result.Success)

        val updatedTodo = (getTodoByIdUseCase(addedTodo.id).first() as Result.Success).data
        assertEquals(true, updatedTodo.done)
    }

    @Test
    fun whenTodoIsUpdated_nameIsChanged() = runTest {
        val addedTodo = (addTodoUseCase(NewTodo("Initial name", false)).first() as Result.Success).data

        val update = UpdateTodo(id = addedTodo.id, name = "Updated name", done = addedTodo.done)
        val updateResult = updateTodoCompletedUseCase(update).first()
        assertTrue(updateResult is Result.Success)

        val updatedTodo = (getTodoByIdUseCase(addedTodo.id).first() as Result.Success).data
        assertEquals("Updated name", updatedTodo.name)
    }
}