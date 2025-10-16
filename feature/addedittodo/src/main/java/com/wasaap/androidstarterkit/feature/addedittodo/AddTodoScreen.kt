package com.wasaap.androidstarterkit.feature.addedittodo

import android.Manifest
import android.content.pm.PackageManager
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Checkbox
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.wasaap.core.androidstarterkit.core.designsystem.component.TodoLoadingWheel
import com.wasaap.core.androidstarterkit.core.designsystem.component.TodoTextButton
import com.wasaap.core.androidstarterkit.core.designsystem.icon.TodoIcons

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
        onRecord = viewModel::startVoiceInput,
        modifier = modifier,
    )
}

@Composable
fun AddTodoContent(
    uiState: AddTodoUiState,
    onNameChanged: (String) -> Unit,
    onDoneChanged: (Boolean) -> Unit,
    onSave: () -> Unit,
    onRecord: () -> Unit,
    modifier: Modifier = Modifier,
) {
    when {
        uiState.isLoading -> LoadingState(modifier)
        uiState.error != null -> ErrorState(uiState.error, modifier)
        else -> AddTodoForm(
            title = if (uiState.isEditMode) {
                stringResource(R.string.feature_addedittodo_edit_todo)
            } else {
                stringResource(R.string.feature_addedittodo_add_todo)
            },
            name = uiState.name,
            done = uiState.done,
            recording = uiState.isRecording,
            onNameChanged = onNameChanged,
            onDoneChanged = onDoneChanged,
            onSave = onSave,
            onRecord = onRecord,
            modifier = modifier,
        )
    }
}

@Composable
private fun LoadingState(modifier: Modifier) {
    Box(modifier = modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        TodoLoadingWheel(modifier = modifier)
    }
}

@Composable
private fun ErrorState(message: String, modifier: Modifier) {
    Box(modifier = modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Text(message)
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun AddTodoForm(
    title: String,
    name: String,
    done: Boolean,
    recording: Boolean,
    onNameChanged: (String) -> Unit,
    onDoneChanged: (Boolean) -> Unit,
    onSave: () -> Unit,
    onRecord: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val focusRequester = remember { FocusRequester() }
    val context = LocalContext.current

    val (requestPermission, showPermissionDialog, dismissDialog) = rememberPermissionHandler(
        onGranted = onRecord
    )

    if (showPermissionDialog) {
        PermissionDeniedDialog(onDismiss = dismissDialog)
    }

    LaunchedEffect(Unit) {
        focusRequester.requestFocus()
    }

    Column(
        modifier = modifier
            .padding(16.dp)
            .fillMaxSize(),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text(text = title, style = MaterialTheme.typography.headlineSmall)

        OutlinedTextField(
            value = name,
            onValueChange = onNameChanged,
            label = { Text(stringResource(R.string.feature_addedittodo_name)) },
            modifier = Modifier
                .fillMaxWidth()
                .focusRequester(focusRequester)
        )

        TodoOptionsRow(
            done = done,
            recording = recording,
            onDoneChanged = onDoneChanged,
            onSave = onSave,
            onRecordClick = {
                val permissionCheck = ContextCompat.checkSelfPermission(
                    context, Manifest.permission.RECORD_AUDIO
                )
                if (permissionCheck == PackageManager.PERMISSION_GRANTED) onRecord()
                else requestPermission()
            }
        )
    }
}

@Composable
private fun TodoOptionsRow(
    done: Boolean,
    recording: Boolean,
    onDoneChanged: (Boolean) -> Unit,
    onSave: () -> Unit,
    onRecordClick: () -> Unit,
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Checkbox(
            checked = done,
            onCheckedChange = onDoneChanged,
            modifier = Modifier.testTag("todo_completed_checkbox")
        )

        Text(stringResource(R.string.feature_addedittodo_completed))

        TodoTextButton(
            onClick = onSave,
            leadingIcon = { Icon(imageVector = TodoIcons.Check, contentDescription = null) },
            text = { Text(stringResource(R.string.feature_addedittodo_save)) }
        )

        TodoTextButton(
            onClick = onRecordClick,
            enabled = !recording,
            leadingIcon = { Icon(imageVector = TodoIcons.ArrowBack, contentDescription = null) },
            text = {
                Text(
                    if (recording) stringResource(R.string.feature_addedittodo_recording) else stringResource(
                        R.string.feature_addedittodo_start_recording
                    )
                )
            }
        )
    }
}

@Composable
private fun rememberPermissionHandler(
    onGranted: () -> Unit
): Triple<() -> Unit, Boolean, () -> Unit> {
    var showDialog by remember { mutableStateOf(false) }

    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        if (isGranted) onGranted() else showDialog = true
    }

    val requestPermission = {
        launcher.launch(Manifest.permission.RECORD_AUDIO)
    }

    return Triple(requestPermission, showDialog) { showDialog = false }
}

@Composable
private fun PermissionDeniedDialog(onDismiss: () -> Unit) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(stringResource(R.string.feature_addedittodo_mic_permission_required)) },
        text = { Text(stringResource(R.string.feature_addedittodo_mic_permission_required_description)) },
        confirmButton = {
            TextButton(onClick = onDismiss) { Text(stringResource(R.string.feature_addedittodo_ok)) }
        }
    )
}
