package com.example.starwars.feature.todos

/**
 * The reducer: a PURE function `(State, Change) -> State`. No coroutines, no I/O, no
 * Android. Given the same inputs it always returns the same output, which is exactly
 * why MVI is so testable — see TodosReducerTest. This is the single place where the
 * shape of the next state is decided.
 */
object TodosReducer {

    fun reduce(state: TodosState, change: TodosChange): TodosState = when (change) {
        is TodosChange.TodosLoaded ->
            state.copy(todos = change.todos, isLoading = false)

        is TodosChange.InputSet ->
            state.copy(input = change.text)

        is TodosChange.FilterSet ->
            state.copy(filter = change.filter)
    }
}
