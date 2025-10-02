package com.wasaap.androidstarterkit.feature.settings

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.selection.selectableGroup
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.wasaap.androidstarterkit.core.model.DarkThemeConfig

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun SettingsScreen(
    onShowSnackbar: suspend (String, String?) -> Boolean,
    viewModel: SettingsViewModel = hiltViewModel(),
) {
    val settingsUiState by viewModel.settingsUiState.collectAsStateWithLifecycle()

    if (settingsUiState is SettingsUiState.Success) {
        val settings = (settingsUiState as SettingsUiState.Success).settings

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
                .selectableGroup()
        ) {
            Text(
                text = stringResource(id = R.string.feature_settings_appearance_title),
                style = MaterialTheme.typography.titleLarge,
                modifier = Modifier.padding(bottom = 16.dp)
            )

            DarkThemeOptionRow(
                text = stringResource(id = R.string.feature_settings_appearance_system_default),
                selected = settings.darkThemeConfig == DarkThemeConfig.FOLLOW_SYSTEM,
                onClick = { viewModel.updateDarkThemeConfig(DarkThemeConfig.FOLLOW_SYSTEM) },
            )
            DarkThemeOptionRow(
                text = stringResource(id = R.string.feature_settings_appearance_system_light),
                selected = settings.darkThemeConfig == DarkThemeConfig.LIGHT,
                onClick = { viewModel.updateDarkThemeConfig(DarkThemeConfig.LIGHT) },
            )
            DarkThemeOptionRow(
                text = stringResource(id = R.string.feature_settings_appearance_system_dark),
                selected = settings.darkThemeConfig == DarkThemeConfig.DARK,
                onClick = { viewModel.updateDarkThemeConfig(DarkThemeConfig.DARK) },
            )
        }
    }
}

@Composable
private fun DarkThemeOptionRow(
    text: String,
    selected: Boolean,
    onClick: () -> Unit,
) {
    Row(
        Modifier
            .fillMaxWidth()
            .selectable(
                selected = selected,
                role = Role.RadioButton,
                onClick = onClick,
            )
            .padding(12.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        RadioButton(selected = selected, onClick = null)
        Text(
            text = text,
            style = MaterialTheme.typography.bodyLarge,
            modifier = Modifier.padding(start = 8.dp),
        )
    }
}


//TODO previews on all screens is needed