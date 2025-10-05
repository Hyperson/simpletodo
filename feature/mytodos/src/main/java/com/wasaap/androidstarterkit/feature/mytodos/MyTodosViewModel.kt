package com.wasaap.androidstarterkit.feature.mytodos

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.wasaap.androidstarterkit.core.domain.DeleteTodoUseCase
import com.wasaap.androidstarterkit.core.domain.GetTodosPageUseCase
import com.wasaap.androidstarterkit.core.domain.common.Result
import com.wasaap.androidstarterkit.core.model.Todo
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MyTodosViewModel @Inject constructor(
    private val getTodosUseCase: GetTodosPageUseCase,
    private val deleteTodoUseCase: DeleteTodoUseCase
) : ViewModel() {

    private val _state = MutableStateFlow<TodoState>(TodoState.Loading)
    val state: StateFlow<TodoState> = _state.asStateFlow()

    private val _effect = MutableSharedFlow<TodoEffect>()
    val effect: SharedFlow<TodoEffect> = _effect

    private val _intent = MutableSharedFlow<TodoIntent>()

    init {
        processIntents()

        sendIntent(TodoIntent.LoadTodos)
    }

    fun sendIntent(intent: TodoIntent) {
        viewModelScope.launch { _intent.emit(intent) }
    }

    private fun processIntents() {
        viewModelScope.launch {
            _intent.collect { intent ->
                when (intent) {
                    is TodoIntent.LoadTodos -> loadTodos()
                    is TodoIntent.DeleteTodo -> deleteTodo(intent.todo)
                }
            }
        }
    }

    private fun loadTodos() {
        viewModelScope.launch {
            getTodosUseCase().collect { result ->
                when (result) {
                    is Result.Loading -> _state.value = TodoState.Loading
                    is Result.Success -> _state.value =
                        TodoState.Success(result.data.map { it.toUiModel() })

                    is Result.Error -> _state.value =
                        TodoState.Error(result.exception.message)
                }
            }
        }
    }

    private fun deleteTodo(todo: TodoUiModel) {
        viewModelScope.launch {
            deleteTodoUseCase(todo.id).collect { result ->
                when (result) {
                    is Result.Success -> {
                        _effect.emit(TodoEffect.ShowMessage("Todo deleted"))
                    }

                    is Result.Error -> {
                        _effect.emit(TodoEffect.ShowMessage("Failed: ${result.exception.message}"))
                    }

                    is Result.Loading -> Unit
                }
            }
        }
    }
}

sealed interface TodoEffect {
    data class ShowMessage(val message: String) : TodoEffect
}

sealed interface TodoState {
    object Loading : TodoState
    data class Success(val todos: List<TodoUiModel>) : TodoState
    data class Error(val message: String?) : TodoState
}

sealed interface TodoIntent {
    object LoadTodos : TodoIntent
    data class DeleteTodo(val todo: TodoUiModel) : TodoIntent
}

data class TodoUiModel(val id: String, val name: String, val isDone: Boolean)

fun Todo.toUiModel(): TodoUiModel = TodoUiModel(
    id = id,
    name = name,
    isDone = done
)