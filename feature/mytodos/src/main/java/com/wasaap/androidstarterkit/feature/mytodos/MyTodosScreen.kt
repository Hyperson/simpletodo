package com.wasaap.androidstarterkit.feature.mytodos

import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.Card
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.wasaap.core.androidstarterkit.designsystem.component.TodoLoadingWheel
import com.wasaap.core.androidstarterkit.designsystem.icon.TodoIcons

@Composable
internal fun MyTodosScreen(
    onAddTodoClick: () -> Unit,
    onEditTodoClick: (String) -> Unit,
    modifier: Modifier = Modifier,
    onShowSnackbar: suspend (String, String?) -> Boolean,
    viewModel: MyTodosViewModel = hiltViewModel(),
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    val ok = stringResource(R.string.feature_mytodos_ok)

    LaunchedEffect(Unit) {
        viewModel.events.collect { event ->
            when (event) {
                is TodoUiEvent.ShowMessage -> {
                    onShowSnackbar(event.message, ok)
                }
            }
        }
    }

    TodoScreen(
        uiState = uiState,
        onAddTodoClick = onAddTodoClick,
        onEditTodoClick = onEditTodoClick,
        modifier = modifier,
        onDeleteTodoClick = { viewModel.deleteTodo(it) }
    )
}

@Composable
internal fun TodoScreen(
    uiState: TodoUiState,
    onAddTodoClick: () -> Unit,
    onEditTodoClick: (String) -> Unit,
    onDeleteTodoClick: (TodoUiModel) -> Unit,
    modifier: Modifier = Modifier,
) {
    when (uiState) {
        TodoUiState.Loading -> Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            TodoLoadingWheel()
        }

        is TodoUiState.Error -> Box(
            Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Text(stringResource(id = R.string.feature_mytodos_error_list))
        }

        is TodoUiState.Success -> Scaffold(
            floatingActionButton = {
                FloatingActionButton(onClick = onAddTodoClick) {
                    Icon(TodoIcons.Add, contentDescription = null)
                }
            }
        ) { padding ->
            LazyColumn(
                contentPadding = padding,
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(uiState.todos, key = { it.id }) { todo ->
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .combinedClickable(
                                onClick = { onEditTodoClick(todo.id) },
                                onLongClick = { onDeleteTodoClick(todo) }
                            )
                    ) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    text = todo.name,
                                    style = MaterialTheme.typography.titleMedium,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )

                                if (todo.isDone) {
                                    Icon(
                                        imageVector = Icons.Default.Check,
                                        contentDescription = null,
                                        tint = MaterialTheme.colorScheme.primary
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}