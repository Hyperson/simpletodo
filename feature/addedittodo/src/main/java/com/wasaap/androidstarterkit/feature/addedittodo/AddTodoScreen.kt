package com.wasaap.androidstarterkit.feature.addedittodo

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Checkbox
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.wasaap.core.androidstarterkit.designsystem.component.TodoLoadingWheel
import com.wasaap.core.androidstarterkit.designsystem.component.TodoTextButton
import com.wasaap.core.androidstarterkit.designsystem.icon.TodoIcons

@Composable
internal fun AddTodoScreen(
    onShowSnackbar: suspend (String, String?) -> Boolean,
    modifier: Modifier = Modifier,
    onSuccess: () -> Unit,
    viewModel: AddTodoViewModel,
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    val added = stringResource(R.string.feature_addedittodo_add_success)
    val updated = stringResource(R.string.feature_addedittodo_update_success)

    val ok = stringResource(R.string.feature_addedittodo_ok)

    LaunchedEffect(Unit) {
        viewModel.events.collect { event ->
            when (event) {
                is AddTodoUiEvent.AddSuccess -> {
                    onSuccess()
                    onShowSnackbar(added, ok)
                }
                is AddTodoUiEvent.UpdateSuccess -> {
                    onSuccess()
                    onShowSnackbar(updated, ok)
                }
            }
        }
    }

    AddTodoContent(
        uiState = uiState,
        onNameChanged = viewModel::onNameChanged,
        onDoneChanged = viewModel::onDoneChanged,
        onSave = viewModel::onSave,
        modifier = modifier,
    )
}

@Composable
private fun AddTodoContent(
    uiState: AddTodoUiState,
    onNameChanged: (String) -> Unit,
    onDoneChanged: (Boolean) -> Unit,
    onSave: () -> Unit,
    modifier: Modifier = Modifier,
) {
    when {
        uiState.isLoading -> {
            Box(
                modifier = modifier.fillMaxSize(),
                contentAlignment = Alignment.Center,
            ) {
                TodoLoadingWheel(modifier = modifier)
            }
        }

        uiState.error != null -> {
            Box(
                modifier = modifier.fillMaxSize(),
                contentAlignment = Alignment.Center,
            ) {
                Text(uiState.error)
            }
        }

        else -> {
            AddTodoScreen(
                title = if (uiState.isEditMode) {
                    stringResource(R.string.feature_addedittodo_edit_todo)
                } else {
                    stringResource(R.string.feature_addedittodo_add_todo)
                },
                name = uiState.name,
                done = uiState.done,
                onNameChanged = onNameChanged,
                onDoneChanged = onDoneChanged,
                onSave = onSave,
                modifier = modifier,
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun AddTodoScreen(
    title: String,
    name: String,
    done: Boolean,
    onNameChanged: (String) -> Unit,
    onDoneChanged: (Boolean) -> Unit,
    onSave: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier
            .padding(16.dp)
            .fillMaxSize(),
        verticalArrangement = Arrangement.spacedBy(16.dp),
    ) {
        Text(
            text = title,
            style = MaterialTheme.typography.headlineSmall
        )

        OutlinedTextField(
            value = name,
            onValueChange = onNameChanged,
            label = { Text(stringResource(R.string.feature_addedittodo_name)) },
            modifier = Modifier.fillMaxWidth(),
        )

        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Checkbox(checked = done, onCheckedChange = onDoneChanged)
            Text(stringResource(R.string.feature_addedittodo_completed))
            TodoTextButton(
                onClick = onSave,
                leadingIcon = {
                    Icon(
                        imageVector = TodoIcons.Check,
                        contentDescription = null
                    )
                },
                text = { Text(text = stringResource(R.string.feature_addedittodo_save)) }
            )
        }
    }
}