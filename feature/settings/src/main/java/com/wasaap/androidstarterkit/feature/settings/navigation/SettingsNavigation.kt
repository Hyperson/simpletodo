package com.wasaap.androidstarterkit.feature.settings.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.wasaap.androidstarterkit.feature.settings.SettingsScreen
import kotlinx.serialization.Serializable

@Serializable
data object SettingsRoute //route to settings

fun NavGraphBuilder.todoSettings(
    onShowSnackbar: suspend (String, String?) -> Boolean,
) {
    composable<SettingsRoute> {
        SettingsScreen(
            onShowSnackbar = onShowSnackbar,
        )
    }
}