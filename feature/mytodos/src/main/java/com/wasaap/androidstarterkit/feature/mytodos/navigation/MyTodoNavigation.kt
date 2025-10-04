package com.wasaap.androidstarterkit.feature.mytodos.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import com.wasaap.androidstarterkit.feature.mytodos.MyTodosScreen
import kotlinx.serialization.Serializable

@Serializable
data object TodosBaseRoute

@Serializable
data object TodosRoute

fun NavGraphBuilder.todosSection(
    onAddTodoClick: () -> Unit,
    onEditTodoClick: (String) -> Unit,
    onShowSnackbar: suspend (String, String?) -> Boolean,
    addTodoScreen: NavGraphBuilder.() -> Unit,
) {
    navigation<TodosBaseRoute>(startDestination = TodosRoute) {
        composable<TodosRoute> {
            MyTodosScreen(
                onAddTodoClick = onAddTodoClick,
                onEditTodoClick = onEditTodoClick,
                onShowSnackbar = onShowSnackbar,
            )
        }

        addTodoScreen()
    }
}