package com.wasaap.androidstarterkit.feature.mytodos

import com.wasaap.androidstarterkit.core.domain.DeleteTodoUseCase
import com.wasaap.androidstarterkit.core.domain.GetTodosPageUseCase
import com.wasaap.androidstarterkit.core.testing.data.todosTestData
import com.wasaap.androidstarterkit.core.testing.repository.TestTodoRepository
import com.wasaap.androidstarterkit.core.testing.util.MainDispatcherRule
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import kotlin.test.assertEquals
import kotlin.test.assertIs

class MyTodosViewModelTest {

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    private val todoRepository = TestTodoRepository().apply {
        addTodos(todosTestData)
    }

    private val getTodosUseCase = GetTodosPageUseCase(todoRepository)
    private val deleteTodoUseCase = DeleteTodoUseCase(todoRepository)

    private lateinit var viewModel: MyTodosViewModel

    @Before
    fun setup() {
        viewModel = MyTodosViewModel(
            getTodosUseCase = getTodosUseCase,
            deleteTodoUseCase = deleteTodoUseCase
        )
    }

    @Test
    fun stateIsInitiallyLoading() = runTest {
        assertIs<TodoState.Loading>(viewModel.state.value)
    }

    @Test
    fun todosAreLoadedSuccessfully() = runTest {
        backgroundScope.launch(UnconfinedTestDispatcher()) { viewModel.state.collect {} }

        advanceUntilIdle()

        val state = viewModel.state.value
        assertIs<TodoState.Success>(state)
        assertEquals(todosTestData.size, state.todos.size)
        assertEquals(todosTestData.first().name, state.todos.first().name)
    }

    @Test
    fun deleteTodoEmitsShowMessageEffect() = runTest {
        val collectedEffects = mutableListOf<TodoEffect>()

        val job = backgroundScope.launch(UnconfinedTestDispatcher()) {
            viewModel.effect.collect { effect ->
                collectedEffects.add(effect)
            }
        }

        val todoToDelete = todosTestData.first().toUiModel()
        viewModel.sendIntent(TodoIntent.DeleteTodo(todoToDelete))

        advanceUntilIdle()

        job.cancel()

        val lastEffect = collectedEffects.lastOrNull()
        assertIs<TodoEffect.ShowMessage>(lastEffect)
        assertEquals("Todo deleted", lastEffect.message)
    }
}