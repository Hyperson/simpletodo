package com.wasaap.androidstarterkit.feature.settings

import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel

@Composable
internal fun SettingsScreen(
    onShowSnackbar: suspend (String, String?) -> Boolean,
    viewModel: SettingsViewModel = viewModel(),
) {

}