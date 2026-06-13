package com.example.starwars.feature.todos

import androidx.lifecycle.viewModelScope
import com.example.starwars.core.mvi.MviViewModel
import com.example.starwars.domain.model.Todo
import com.example.starwars.domain.repository.TodoRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject

/**
 * The ViewModel wires everything together. It is the ONLY component that knows about
 * both the domain (the repository) and the state. The View knows nothing about the
 * repository; the repository knows nothing about the View.
 *
 * Flow of control:
 *   1. The View sends an intent via [onIntent].
 *   2. [handleIntent] either changes local UI state (filter, input) or performs a
 *      side effect against the repository (add/toggle/delete).
 *   3. Repository mutations re-emit the list through [TodoRepository.observeTodos],
 *      which we fold back into state as a [TodosChange.TodosLoaded]. The repository is
 *      the source of truth for the data; the state mirrors it for the View.
 */
@HiltViewModel
class TodosViewModel @Inject constructor(
    private val repository: TodoRepository,
) : MviViewModel<TodosIntent, TodosState, TodosEffect>(initialState = TodosState()) {

    // Remembered so the snackbar's "Undo" can put it back. It is transient UI plumbing,
    // not screen state, so it lives here rather than in TodosState.
    private var lastDeleted: Todo? = null

    init {
        // The reactive data source drives state. Any change anywhere (this screen or
        // elsewhere) flows in here and becomes the new rendered list.
        repository.observeTodos()
            .onEach { todos -> setState { TodosReducer.reduce(this, TodosChange.TodosLoaded(todos)) } }
            .launchIn(viewModelScope)
    }

    override suspend fun handleIntent(intent: TodosIntent) {
        when (intent) {
            is TodosIntent.InputChanged ->
                reduce(TodosChange.InputSet(intent.text))

            TodosIntent.AddClicked -> {
                val text = currentState.input.trim()
                if (text.isNotEmpty()) {
                    repository.add(text)
                    reduce(TodosChange.InputSet("")) // clear the field after adding
                }
            }

            is TodosIntent.ToggleClicked ->
                repository.toggle(intent.id)

            is TodosIntent.DeleteClicked -> {
                lastDeleted = currentState.todos.firstOrNull { it.id == intent.id }
                repository.delete(intent.id)
                lastDeleted?.let { sendEffect(TodosEffect.ShowUndoDelete(it.text)) }
            }

            TodosIntent.UndoDeleteClicked -> {
                lastDeleted?.let { repository.restore(it) }
                lastDeleted = null
            }

            is TodosIntent.FilterSelected ->
                reduce(TodosChange.FilterSet(intent.filter))

            TodosIntent.ClearCompletedClicked ->
                repository.clearCompleted()
        }
    }

    /** Small helper so intent handling reads cleanly. State only ever changes here. */
    private fun reduce(change: TodosChange) = setState { TodosReducer.reduce(this, change) }
}
