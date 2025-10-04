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

    private val _uiState = MutableStateFlow<TodoUiState>(TodoUiState.Loading)
    val uiState: StateFlow<TodoUiState> = _uiState.asStateFlow()

    private val _events = MutableSharedFlow<TodoUiEvent>()
    val events: SharedFlow<TodoUiEvent> = _events


    init {
        refreshTodos()
    }

    fun refreshTodos() {
        viewModelScope.launch {
            getTodosUseCase().collect { result ->
                when (result) {
                    is Result.Success -> {
                        _uiState.value = TodoUiState.Success(result.data.map { it.toUiModel() })
                    }

                    is Result.Error -> {
                        _uiState.value = TodoUiState.Error(
                            result.exception.message
                        )
                    }

                    is Result.Loading -> {
                        _uiState.value = TodoUiState.Loading
                    }
                }
            }
        }
    }

    fun deleteTodo(todo: TodoUiModel) {
        viewModelScope.launch {
            deleteTodoUseCase(todo.id).collect { result ->
                when (result) {
                    is Result.Success -> {
                        //TODO add strings xml
                        _events.emit(TodoUiEvent.ShowMessage("Todo deleted"))
                    }

                    is Result.Error -> {
                        _events.emit(TodoUiEvent.ShowMessage("Failed to delete: ${result.exception.message}"))
                    }

                    is Result.Loading -> Unit
                }
            }
        }
    }
}

sealed interface TodoUiState {
    object Loading : TodoUiState
    data class Success(val todos: List<TodoUiModel>) : TodoUiState
    data class Error(val message: String?) : TodoUiState
}

sealed interface TodoUiEvent {
    data class ShowMessage(val message: String) : TodoUiEvent
}

data class TodoUiModel(val id: String, val name: String, val isDone: Boolean)

fun Todo.toUiModel(): TodoUiModel = TodoUiModel(
    id = id,
    name = name,
    isDone = done
)