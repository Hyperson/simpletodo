package com.wasaap.androidstarterkit.feature.addedittodo

import androidx.annotation.StringRes
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.wasaap.androidstarterkit.core.domain.AddTodoUseCase
import com.wasaap.androidstarterkit.core.domain.GetTodoByIdUseCase
import com.wasaap.androidstarterkit.core.domain.common.Result
import com.wasaap.androidstarterkit.core.domain.UpdateTodoCompletedUseCase
import com.wasaap.androidstarterkit.core.model.NewTodo
import com.wasaap.androidstarterkit.core.model.UpdateTodo
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

@HiltViewModel(assistedFactory = AddTodoViewModel.Factory::class)
class AddTodoViewModel @AssistedInject constructor(
    private val getTodoByIdUseCase: GetTodoByIdUseCase,
    private val addTodoUseCase: AddTodoUseCase,
    private val updateTodoCompletedUseCase: UpdateTodoCompletedUseCase,
    @Assisted val todoId: String?,
) : ViewModel() {
    private val _uiState = MutableStateFlow<AddTodoUiState>(AddTodoUiState.Loading)

    val uiState: StateFlow<AddTodoUiState> = _uiState.asStateFlow()

    init {
        if (todoId == null) {
            _uiState.value = AddTodoUiState.Add(
                name = "",
                done = false
            )
        } else {
            viewModelScope.launch {
                getTodoByIdUseCase(todoId).collect { result ->
                    when (result) {
                        is Result.Success -> {
                            val todo = result.data
                            _uiState.value = AddTodoUiState.Edit(
                                name = todo.name,
                                done = todo.done
                            )
                        }

                        is Result.Error -> {
                            _uiState.value = AddTodoUiState.Error(
                                result.exception.message.toString(),
                                R.string.feature_addedittodo_ok
                            )
                        }

                        is Result.Loading -> {
                            _uiState.value = AddTodoUiState.Loading
                        }
                    }
                }
            }
        }
    }

    fun onNameChanged(newName: String) {
        val current = _uiState.value
        when (current) {
            is AddTodoUiState.Edit -> _uiState.value = current.copy(name = newName)
            is AddTodoUiState.Add -> _uiState.value = current.copy(name = newName)
            else -> Unit
        }
    }

    fun onDoneChanged(newDone: Boolean) {
        val current = _uiState.value
        when (current) {
            is AddTodoUiState.Edit -> _uiState.value = current.copy(done = newDone)
            is AddTodoUiState.Add -> _uiState.value = current.copy(done = newDone)
            else -> Unit
        }
    }

    fun onSave() {
        val current = _uiState.value
        viewModelScope.launch {
            _uiState.value = AddTodoUiState.Loading
            when (current) {
                is AddTodoUiState.Add -> {
                    val newTodo = NewTodo(current.name, current.done)

                    addTodoUseCase(newTodo).collect { result ->
                        when (result) {
                            is Result.Success -> {
                                _uiState.value = AddTodoUiState.AddSuccess
                            }

                            is Result.Error -> _uiState.value =
                                AddTodoUiState.Error(
                                    result.exception.message.toString(),
                                    R.string.feature_addedittodo_ok
                                )

                            is Result.Loading -> _uiState.value = AddTodoUiState.Loading
                        }
                    }
                }

                is AddTodoUiState.Edit -> {
                    val updateTodo = UpdateTodo(todoId ?: "", current.name, current.done)

                    updateTodoCompletedUseCase(updateTodo).collect { result ->
                        when (result) {
                            is Result.Success -> {
                                _uiState.value = AddTodoUiState.UpdateSuccess
                            }

                            is Result.Error -> _uiState.value =
                                AddTodoUiState.Error(
                                    result.exception.message.toString(),
                                    R.string.feature_addedittodo_ok
                                )

                            is Result.Loading -> _uiState.value = AddTodoUiState.Loading
                        }
                    }
                }

                else -> Unit
            }
        }
    }


    @AssistedFactory
    interface Factory {
        fun create(todoId: String?): AddTodoViewModel
    }
}

sealed interface AddTodoUiState {
    object Loading : AddTodoUiState

    object AddSuccess : AddTodoUiState

    object UpdateSuccess : AddTodoUiState

    data class Add(
        val name: String,
        val done: Boolean
    ) : AddTodoUiState

    data class Edit(
        val name: String,
        val done: Boolean
    ) : AddTodoUiState

    data class Error(val message: String, @param:StringRes val action: Int) : AddTodoUiState
}