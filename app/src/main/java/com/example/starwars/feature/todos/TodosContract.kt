package com.example.starwars.feature.todos

import com.example.starwars.core.mvi.MviEffect
import com.example.starwars.core.mvi.MviIntent
import com.example.starwars.core.mvi.MviState
import com.example.starwars.domain.model.Todo
import com.example.starwars.domain.model.TodoFilter

/**
 * The complete "contract" for the Todos screen: everything the View can SAY (Intent),
 * everything it can SEE (State), the one-off things that can HAPPEN (Effect), and the
 * internal vocabulary the reducer understands (Change). Keeping these together makes
 * the feature easy to read end-to-end.
 */

/** I — user intentions. The only messages the View sends to the ViewModel. */
sealed interface TodosIntent : MviIntent {
    data class InputChanged(val text: String) : TodosIntent
    data object AddClicked : TodosIntent
    data class ToggleClicked(val id: String) : TodosIntent
    data class DeleteClicked(val id: String) : TodosIntent
    data class FilterSelected(val filter: TodoFilter) : TodosIntent
    data object ClearCompletedClicked : TodosIntent
    data object UndoDeleteClicked : TodosIntent
}

/**
 * M — the single immutable state. Everything the UI shows is derived from these
 * fields. The `visible*`/`*Count` members are computed, not stored: derive view data
 * from the source of truth instead of duplicating it (and risking it going stale).
 */
data class TodosState(
    val input: String = "",
    val todos: List<Todo> = emptyList(),
    val filter: TodoFilter = TodoFilter.All,
    val isLoading: Boolean = true,
) : MviState {
    val visibleTodos: List<Todo>
        get() = when (filter) {
            TodoFilter.All -> todos
            TodoFilter.Active -> todos.filter { !it.completed }
            TodoFilter.Completed -> todos.filter { it.completed }
        }
    val activeCount: Int get() = todos.count { !it.completed }
    val hasCompleted: Boolean get() = todos.any { it.completed }
    val canAdd: Boolean get() = input.isNotBlank()
    val isEmpty: Boolean get() = !isLoading && visibleTodos.isEmpty()
}

/** E — one-off effects the View consumes once (here: a snackbar with an Undo action). */
sealed interface TodosEffect : MviEffect {
    data class ShowUndoDelete(val todoText: String) : TodosEffect
}

/**
 * Internal "changes" — the alphabet the pure [TodosReducer] understands. Intents that
 * cause data mutations go through the repository, whose reactive stream produces a
 * [TodosLoaded] change; intents that only affect local UI state map to the others.
 */
sealed interface TodosChange {
    data class TodosLoaded(val todos: List<Todo>) : TodosChange
    data class InputSet(val text: String) : TodosChange
    data class FilterSet(val filter: TodoFilter) : TodosChange
}
