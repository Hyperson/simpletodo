package com.wasaap.androidstarterkit.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import com.wasaap.androidstarterkit.feature.addedittodo.navigation.AddTodoRoute
import com.wasaap.androidstarterkit.feature.addedittodo.navigation.addTodoScreen
import com.wasaap.androidstarterkit.feature.mytodos.navigation.TodosBaseRoute
import com.wasaap.androidstarterkit.feature.mytodos.navigation.todosSection
import com.wasaap.androidstarterkit.feature.settings.navigation.todoSettings
import com.wasaap.androidstarterkit.ui.TodoAppState

@Composable
fun TodoNavHost(
    appState: TodoAppState,
    onShowSnackbar: suspend (String, String?) -> Boolean,
    modifier: Modifier = Modifier,
) {
    val navController = appState.navController

    NavHost(
        navController = navController,
        startDestination = TodosBaseRoute,
        modifier = modifier,
    ) {
        todosSection(
            onAddTodoClick = {
                navController.navigate(AddTodoRoute())
            },
            onEditTodoClick = { todoId ->
                navController.navigate(AddTodoRoute(todoId))
            },
            onShowSnackbar = onShowSnackbar,
        ) {
            addTodoScreen(
                onSuccess = navController::popBackStack,
                onShowSnackbar = onShowSnackbar
            )
        }

        todoSettings(
            onShowSnackbar = onShowSnackbar
        )
    }
}