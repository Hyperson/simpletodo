package com.wasaap.androidstarterkit.core.domain

import com.wasaap.androidstarterkit.core.domain.common.Result
import com.wasaap.androidstarterkit.core.model.NewTodo
import com.wasaap.androidstarterkit.core.testing.repository.TestTodoRepository
import com.wasaap.androidstarterkit.core.testing.util.MainDispatcherRule
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.Rule
import org.junit.Test
import kotlin.test.assertTrue

class DeleteTodoUseCaseTest {

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    private val repository = TestTodoRepository()
    private val addTodoUseCase = AddTodoUseCase(repository)
    private val deleteTodoUseCase = DeleteTodoUseCase(repository)

    @Test
    fun whenTodoIsDeleted_itIsRemovedFromRepository() = runTest {
        val todo1 = (addTodoUseCase(NewTodo("Write docs", false)).first() as Result.Success).data
        val todo2 = (addTodoUseCase(NewTodo("Read spec", false)).first() as Result.Success).data

        val deleteResult = deleteTodoUseCase(todo1.id).first()
        assertTrue(deleteResult is Result.Success)

        val allTodosResult = repository.getTodos().first()
        val allTodos = (allTodosResult as Result.Success).data

        assertTrue(allTodos.none { it.id == todo1.id })
        assertTrue(allTodos.any { it.id == todo2.id })
    }
}