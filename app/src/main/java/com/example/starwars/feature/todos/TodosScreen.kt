package com.example.starwars.feature.todos

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Checkbox
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.SnackbarResult
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.starwars.core.ui.EffectHandler
import com.example.starwars.domain.model.Todo
import com.example.starwars.domain.model.TodoFilter

/**
 * The "Route" is the only stateful piece of the View. It connects the ViewModel to the
 * stateless [TodosScreen]:
 *   - collects [state] in a lifecycle-aware way,
 *   - handles one-off [effects] (the Undo snackbar),
 *   - forwards user actions straight back in via `viewModel::onIntent`.
 * Keeping the rendering in a stateless composable makes it trivially previewable/testable.
 */
@Composable
fun TodosRoute(
    viewModel: TodosViewModel = hiltViewModel(),
) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    val snackbarHostState = remember { SnackbarHostState() }

    EffectHandler(viewModel.effects) { effect ->
        when (effect) {
            is TodosEffect.ShowUndoDelete -> {
                val result = snackbarHostState.showSnackbar(
                    message = "Deleted “${effect.todoText}”",
                    actionLabel = "Undo",
                )
                if (result == SnackbarResult.ActionPerformed) {
                    viewModel.onIntent(TodosIntent.UndoDeleteClicked)
                }
            }
        }
    }

    TodosScreen(
        state = state,
        onIntent = viewModel::onIntent,
        snackbarHostState = snackbarHostState,
    )
}

/**
 * Pure render of [TodosState]. It receives state in and pushes intents out — it never
 * touches the ViewModel, repository, or coroutines. `state -> UI` is a plain function.
 */
@OptIn(ExperimentalMaterial3Api::class) // Material3 TopAppBar is still experimental
@Composable
fun TodosScreen(
    state: TodosState,
    onIntent: (TodosIntent) -> Unit,
    snackbarHostState: SnackbarHostState,
) {
    Scaffold(
        topBar = { TopAppBar(title = { Text("MVI Todos") }) },
        snackbarHost = { SnackbarHost(snackbarHostState) },
    ) { padding ->
        Column(Modifier.fillMaxSize().padding(padding)) {

            AddTodoRow(
                input = state.input,
                canAdd = state.canAdd,
                onInputChanged = { onIntent(TodosIntent.InputChanged(it)) },
                onAdd = { onIntent(TodosIntent.AddClicked) },
            )

            FilterRow(
                selected = state.filter,
                onSelect = { onIntent(TodosIntent.FilterSelected(it)) },
            )

            HorizontalDivider()

            when {
                state.isLoading -> CenteredText("Loading…")
                state.isEmpty -> CenteredText(emptyMessage(state.filter))
                else -> LazyColumn(Modifier.weight(1f).fillMaxWidth()) {
                    items(state.visibleTodos, key = { it.id }) { todo ->
                        TodoRow(
                            todo = todo,
                            onToggle = { onIntent(TodosIntent.ToggleClicked(todo.id)) },
                            onDelete = { onIntent(TodosIntent.DeleteClicked(todo.id)) },
                        )
                    }
                }
            }

            HorizontalDivider()
            FooterRow(
                activeCount = state.activeCount,
                hasCompleted = state.hasCompleted,
                onClearCompleted = { onIntent(TodosIntent.ClearCompletedClicked) },
            )
        }
    }
}

@Composable
private fun AddTodoRow(
    input: String,
    canAdd: Boolean,
    onInputChanged: (String) -> Unit,
    onAdd: () -> Unit,
) {
    Row(
        modifier = Modifier.fillMaxWidth().padding(12.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(8.dp),
    ) {
        TextField(
            value = input,
            onValueChange = onInputChanged,
            placeholder = { Text("What needs doing?") },
            singleLine = true,
            modifier = Modifier.weight(1f),
        )
        IconButton(onClick = onAdd, enabled = canAdd) {
            Icon(Icons.Filled.Add, contentDescription = "Add todo")
        }
    }
}

@Composable
private fun FilterRow(
    selected: TodoFilter,
    onSelect: (TodoFilter) -> Unit,
) {
    Row(
        modifier = Modifier.fillMaxWidth().padding(horizontal = 12.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
    ) {
        TodoFilter.entries.forEach { filter ->
            FilterChip(
                selected = filter == selected,
                onClick = { onSelect(filter) },
                label = { Text(filter.name) },
            )
        }
    }
}

@Composable
private fun TodoRow(
    todo: Todo,
    onToggle: () -> Unit,
    onDelete: () -> Unit,
) {
    Row(
        modifier = Modifier.fillMaxWidth().padding(horizontal = 12.dp, vertical = 4.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Checkbox(checked = todo.completed, onCheckedChange = { onToggle() })
        Text(
            text = todo.text,
            modifier = Modifier.weight(1f).padding(start = 4.dp),
            style = MaterialTheme.typography.bodyLarge,
            textDecoration = if (todo.completed) TextDecoration.LineThrough else TextDecoration.None,
        )
        IconButton(onClick = onDelete) {
            Icon(Icons.Filled.Delete, contentDescription = "Delete todo")
        }
    }
}

@Composable
private fun FooterRow(
    activeCount: Int,
    hasCompleted: Boolean,
    onClearCompleted: () -> Unit,
) {
    Row(
        modifier = Modifier.fillMaxWidth().padding(horizontal = 12.dp, vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween,
    ) {
        Text("$activeCount active", style = MaterialTheme.typography.labelLarge)
        if (hasCompleted) {
            TextButton(onClick = onClearCompleted) { Text("Clear completed") }
        }
    }
}

@Composable
private fun CenteredText(text: String) {
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Text(text, style = MaterialTheme.typography.bodyLarge)
    }
}

private fun emptyMessage(filter: TodoFilter): String = when (filter) {
    TodoFilter.All -> "No todos yet. Add one above!"
    TodoFilter.Active -> "Nothing active — nice work."
    TodoFilter.Completed -> "Nothing completed yet."
}

@Preview(showBackground = true)
@Composable
private fun TodosScreenPreview() {
    TodosScreen(
        state = TodosState(
            isLoading = false,
            todos = listOf(
                Todo("1", "Learn MVI", completed = true),
                Todo("2", "Keep state immutable"),
                Todo("3", "Effects are one-off"),
            ),
        ),
        onIntent = {},
        snackbarHostState = remember { SnackbarHostState() },
    )
}
