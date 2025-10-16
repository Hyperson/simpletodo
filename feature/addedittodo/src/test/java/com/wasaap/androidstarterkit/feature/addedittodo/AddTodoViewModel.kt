package com.wasaap.androidstarterkit.feature.addedittodo

import com.wasaap.androidstarterkit.core.domain.usecase.todo.AddTodoUseCase
import com.wasaap.androidstarterkit.core.domain.usecase.todo.GetTodoByIdUseCase
import com.wasaap.androidstarterkit.core.domain.usecase.todo.UpdateTodoCompletedUseCase
import com.wasaap.androidstarterkit.core.domain.common.Result
import com.wasaap.androidstarterkit.core.model.Todo
import com.wasaap.androidstarterkit.core.testing.data.todosTestData
import com.wasaap.androidstarterkit.core.testing.repository.TestTodoRepository
import com.wasaap.androidstarterkit.core.testing.util.MainDispatcherRule
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertIs
import kotlin.test.assertNull
import kotlin.test.assertTrue


class AddTodoViewModelTest {

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    private val todoRepository = TestTodoRepository().apply {
        addTodos(todosTestData)
    }

    private val addTodoUseCase = AddTodoUseCase(todoRepository)
    private val getTodoByIdUseCase = GetTodoByIdUseCase(todoRepository)
    private val updateTodoCompletedUseCase = UpdateTodoCompletedUseCase(todoRepository)

    private lateinit var viewModel: AddTodoViewModel

    @Before
    fun setup() {
        viewModel = AddTodoViewModel(
            getTodoByIdUseCase = getTodoByIdUseCase,
            addTodoUseCase = addTodoUseCase,
            updateTodoCompletedUseCase = updateTodoCompletedUseCase,
            todoId = null
        )
    }

    @Test
    fun stateIsInitializedInAddMode() = runTest {
        val state = viewModel.uiState.value
        assertFalse(state.isLoading)
        assertEquals("", state.name)
        assertFalse(state.done)
        assertFalse(state.isEditMode)
        assertNull(state.error)
    }

    @Test
    fun stateIsLoadedInEditMode() = runTest {
        val existing = todosTestData.first()
        val editVm = AddTodoViewModel(
            getTodoByIdUseCase = getTodoByIdUseCase,
            addTodoUseCase = addTodoUseCase,
            updateTodoCompletedUseCase = updateTodoCompletedUseCase,
            todoId = existing.id
        )

        backgroundScope.launch(UnconfinedTestDispatcher()) { editVm.uiState.collect {} }

        advanceUntilIdle()

        val state = editVm.uiState.value
        assertFalse(state.isLoading)
        assertTrue(state.isEditMode)
        assertEquals(existing.name, state.name)
        assertEquals(existing.done, state.done)
    }

    @Test
    fun onNameChangedUpdatesState() = runTest {
        viewModel.onNameChanged("New Todo Name")
        assertEquals("New Todo Name", viewModel.uiState.value.name)
    }

    @Test
    fun onDoneChangedUpdatesState() = runTest {
        viewModel.onDoneChanged(true)
        assertTrue(viewModel.uiState.value.done)
    }

    @Test
    fun onSaveAddsNewTodoSuccessfully_andEmitsAddSuccess() = runTest {
        val collectedEvents = mutableListOf<AddTodoUiEvent>()
        val job = backgroundScope.launch(UnconfinedTestDispatcher()) {
            viewModel.events.collect { collectedEvents.add(it) }
        }

        viewModel.onNameChanged("Newly Added Todo")
        viewModel.onDoneChanged(false)
        viewModel.onSave()

        advanceUntilIdle()
        job.cancel()

        val lastEvent = collectedEvents.lastOrNull()
        assertIs<AddTodoUiEvent.AddSuccess>(lastEvent)

        val listResult = todoRepository.getTodos().first()
        assertIs<Result.Success<List<Todo>>>(listResult)
        assertTrue(listResult.data.any { it.name == "Newly Added Todo" && !it.done })
    }

    @Test
    fun onSaveUpdatesExistingTodoSuccessfully_andEmitsUpdateSuccess() = runTest {
        val existing = todosTestData.first()
        val editVm = AddTodoViewModel(
            getTodoByIdUseCase = getTodoByIdUseCase,
            addTodoUseCase = addTodoUseCase,
            updateTodoCompletedUseCase = updateTodoCompletedUseCase,
            todoId = existing.id
        )

        val collectedEvents = mutableListOf<AddTodoUiEvent>()
        val job = backgroundScope.launch(UnconfinedTestDispatcher()) {
            editVm.events.collect { collectedEvents.add(it) }
        }

        backgroundScope.launch(UnconfinedTestDispatcher()) { editVm.uiState.collect {} }
        advanceUntilIdle()

        editVm.onNameChanged("Updated Todo Name")
        editVm.onDoneChanged(true)
        editVm.onSave()

        advanceUntilIdle()
        job.cancel()

        val lastEvent = collectedEvents.lastOrNull()
        assertIs<AddTodoUiEvent.UpdateSuccess>(lastEvent)

        val result = todoRepository.getTodoById(existing.id).first()
        assertIs<Result.Success<Todo>>(result)
        assertEquals("Updated Todo Name", result.data.name)
        assertTrue(result.data.done)
    }
}