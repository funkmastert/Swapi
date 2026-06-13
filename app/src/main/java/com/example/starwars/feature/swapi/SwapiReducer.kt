package com.example.starwars.feature.swapi

/** Pure function: (old state, change) -> new state. No side effects, easy to unit-test. */
object SwapiReducer {

    fun reduce(state: SwapiState, change: SwapiChange): SwapiState = when (change) {
        SwapiChange.Loading ->
            state.copy(isLoading = true, error = null)

        SwapiChange.Refreshing ->
            state.copy(isRefreshing = true, error = null)

        is SwapiChange.TopicLoaded ->
            state.copy(
                isLoading = false,
                isRefreshing = false,
                topic = change.topic,
                items = change.items,
                error = null,
            )

        is SwapiChange.DetailLoaded ->
            state.copy(isLoading = false, detail = change.detail, error = null)

        is SwapiChange.Failed ->
            state.copy(isLoading = false, isRefreshing = false, error = change.message)
    }
}
