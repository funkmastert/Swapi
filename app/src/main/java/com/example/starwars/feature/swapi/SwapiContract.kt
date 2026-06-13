package com.example.starwars.feature.swapi

import com.example.starwars.core.mvi.MviEffect
import com.example.starwars.core.mvi.MviIntent
import com.example.starwars.core.mvi.MviState
import com.example.starwars.domain.model.SwItem

/** I — user intentions. The only messages the View sends to the ViewModel. */
sealed interface SwapiIntent : MviIntent {
    data class ShowDetails(val id: String) : SwapiIntent
}

data class SwapiState(
    val text: String = "",
    val items: List<SwItem> = emptyList(),
    val isLoading: Boolean = true,
) : MviState {
    val isEmpty: Boolean get() = !isLoading && items.isEmpty()
}

sealed interface SwapiEffect : MviEffect {
    data class ShowUndoDelete(val todoText: String) : SwapiEffect
}

sealed interface SwapiChange {
    data class DataLoaded(val todos: List<SwItem>) : SwapiChange
}
