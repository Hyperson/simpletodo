package com.wasaap.androidstarterkit.feature.mytodos

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.wasaap.androidstarterkit.core.domain.GetTodosPageUseCase
import com.wasaap.androidstarterkit.core.domain.common.Result
import com.wasaap.androidstarterkit.core.model.Todo
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class MyTodosViewModel @Inject constructor(
    private val getTodosUseCase: GetTodosPageUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow<TodoUiState>(TodoUiState.Loading)
    val uiState: StateFlow<TodoUiState> = _uiState.asStateFlow()


    init {
        refreshTodos()
    }

    fun refreshTodos() {
        viewModelScope.launch {
            getTodosUseCase().collect { result ->
                when (result) {
                    is Result.Success -> {
                        _uiState.value = TodoUiState.Success(result.data)
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
}

sealed interface TodoUiState {
    object Loading : TodoUiState
    data class Success(val todos: List<Todo>) : TodoUiState
    data class Error(val message: String?) : TodoUiState
}