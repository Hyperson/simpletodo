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
import com.wasaap.core.androidstarterkit.core.designsystem.component.TodoLoadingWheel
import com.wasaap.core.androidstarterkit.core.designsystem.icon.TodoIcons

@Composable
internal fun MyTodosScreen(
    onAddTodoClick: () -> Unit,
    onEditTodoClick: (String) -> Unit,
    modifier: Modifier = Modifier,
    onShowSnackbar: suspend (String, String?) -> Boolean,
    viewModel: MyTodosViewModel = hiltViewModel(),
) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    val ok = stringResource(R.string.feature_mytodos_ok)

    LaunchedEffect(Unit) {
        viewModel.effect.collect { effect ->
            when (effect) {
                is TodoEffect.ShowMessage -> {
                    onShowSnackbar(effect.message, ok)
                }
            }
        }
    }

    TodoScreen(
        state = state,
        onAddTodoClick = onAddTodoClick,
        onEditTodoClick = onEditTodoClick,
        modifier = modifier,
        onDeleteTodoClick = { todo ->
            viewModel.sendIntent(TodoIntent.DeleteTodo(todo))
        }
    )
}

@Composable
internal fun TodoScreen(
    state: TodoState,
    onAddTodoClick: () -> Unit,
    onEditTodoClick: (String) -> Unit,
    onDeleteTodoClick: (TodoUiModel) -> Unit,
    modifier: Modifier = Modifier,
) {
    when (state) {
        TodoState.Loading -> Box(
            Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            TodoLoadingWheel()
        }

        is TodoState.Error -> Box(
            Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text(stringResource(R.string.feature_mytodos_error_list))
            }
        }

        is TodoState.Success -> Scaffold(
            floatingActionButton = {
                FloatingActionButton(onClick = onAddTodoClick) {
                    Icon(TodoIcons.Add, contentDescription = "Add Todo")
                }
            }
        ) { padding ->
            LazyColumn(
                contentPadding = padding,
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(state.todos, key = { it.id }) { todo ->
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