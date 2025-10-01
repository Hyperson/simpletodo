package com.wasaap.androidstarterkit.feature.addedittodo.navigation

import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.toRoute
import com.wasaap.androidstarterkit.feature.addedittodo.AddTodoScreen
import com.wasaap.androidstarterkit.feature.addedittodo.AddTodoViewModel
import kotlinx.serialization.Serializable

@Serializable
data class AddTodoRoute(val id: String? = null)

fun NavGraphBuilder.addTodoScreen(
    onSuccess: () -> Unit,
    onShowSnackbar: suspend (String, String?) -> Boolean,
) {
    composable<AddTodoRoute> { entry ->
        val todo = entry.toRoute<AddTodoRoute>()
        AddTodoScreen(
            onShowSnackbar = onShowSnackbar,
            viewModel = hiltViewModel<AddTodoViewModel, AddTodoViewModel.Factory>(
                key = todo.id,
            ) { factory ->
                factory.create(todoId = todo.id)
            },
            onSuccess = onSuccess
        )
    }
}