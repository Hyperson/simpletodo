package com.wasaap.androidstarterkit.feature.addedittodo

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.wasaap.androidstarterkit.core.domain.common.Result
import com.wasaap.androidstarterkit.core.domain.usecase.speech.ObserveSpeechUseCase
import com.wasaap.androidstarterkit.core.domain.usecase.speech.StartSpeechListeningUseCase
import com.wasaap.androidstarterkit.core.domain.usecase.todo.AddTodoUseCase
import com.wasaap.androidstarterkit.core.domain.usecase.todo.GetTodoByIdUseCase
import com.wasaap.androidstarterkit.core.domain.usecase.todo.UpdateTodoCompletedUseCase
import com.wasaap.androidstarterkit.core.model.NewTodo
import com.wasaap.androidstarterkit.core.model.UpdateTodo
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

@HiltViewModel(assistedFactory = AddTodoViewModel.Factory::class)
class AddTodoViewModel @AssistedInject constructor(
    private val getTodoByIdUseCase: GetTodoByIdUseCase,
    private val addTodoUseCase: AddTodoUseCase,
    private val updateTodoCompletedUseCase: UpdateTodoCompletedUseCase,
    private val startSpeechListeningUseCase: StartSpeechListeningUseCase,
    private val observeSpeechUseCase: ObserveSpeechUseCase,
    @Assisted val todoId: String?,
) : ViewModel() {

    private val _uiState = MutableStateFlow(AddTodoUiState())
    val uiState: StateFlow<AddTodoUiState> = _uiState.asStateFlow()

    private val _events = MutableSharedFlow<AddTodoUiEvent>()
    val events: SharedFlow<AddTodoUiEvent> = _events

    init {
        observeSpeech()
        initializeTodoState()
    }

    private fun initializeTodoState() {
        if (todoId == null) {
            _uiState.value = AddTodoUiState(
                isLoading = false,
                name = "",
                done = false,
                isEditMode = false
            )
        } else {
            viewModelScope.launch {
                getTodoByIdUseCase(todoId).collect { result ->
                    when (result) {
                        is Result.Success -> {
                            val todo = result.data
                            _uiState.value = AddTodoUiState(
                                isLoading = false,
                                name = todo.name,
                                done = todo.done,
                                isEditMode = true
                            )
                        }

                        is Result.Error -> {
                            _uiState.value = _uiState.value.copy(
                                isLoading = false,
                                error = result.exception.message
                            )
                        }

                        is Result.Loading -> {
                            _uiState.value = _uiState.value.copy(isLoading = true)
                        }
                    }
                }
            }
        }
    }

    private fun observeSpeech() {
        viewModelScope.launch {
            observeSpeechUseCase().collect { speechState ->
                _uiState.update {
                    it.copy(
                        isRecording = speechState.isListening,
                        name = speechState.recognizedText.ifBlank { it.name }
                    )
                }
            }
        }
    }

    fun onNameChanged(newName: String) {
        _uiState.update { it.copy(name = newName) }
    }

    fun onDoneChanged(newDone: Boolean) {
        _uiState.update { it.copy(done = newDone) }
    }

    fun onSave() {
        val current = _uiState.value
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, error = null) }

            if (current.isEditMode) {
                val updateTodo = UpdateTodo(todoId ?: "", current.name, current.done)
                updateTodoCompletedUseCase(updateTodo).collect { result ->
                    handleResult(result, isEdit = true)
                }
            } else {
                val newTodo = NewTodo(current.name, current.done)
                addTodoUseCase(newTodo).collect { result ->
                    handleResult(result, isEdit = false)
                }
            }
        }
    }

    fun startVoiceInput() {
        viewModelScope.launch {
            startSpeechListeningUseCase()
        }
    }

    private suspend fun handleResult(result: Result<*>, isEdit: Boolean) {
        when (result) {
            is Result.Success -> {
                _events.emit(
                    if (isEdit) AddTodoUiEvent.UpdateSuccess
                    else AddTodoUiEvent.AddSuccess
                )
            }

            is Result.Error -> {
                _uiState.update {
                    it.copy(isLoading = false, error = result.exception.message)
                }
            }

            is Result.Loading -> {
                _uiState.update { it.copy(isLoading = true) }
            }
        }
    }

    @AssistedFactory
    interface Factory {
        fun create(todoId: String?): AddTodoViewModel
    }
}

data class AddTodoUiState(
    val isLoading: Boolean = false,
    val name: String = "",
    val done: Boolean = false,
    val isEditMode: Boolean = false,
    val isRecording: Boolean = false,
    val error: String? = null
)

sealed interface AddTodoUiEvent {
    object AddSuccess : AddTodoUiEvent
    object UpdateSuccess : AddTodoUiEvent
}