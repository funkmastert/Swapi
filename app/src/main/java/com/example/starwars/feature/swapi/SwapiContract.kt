package com.example.starwars.feature.swapi

import com.example.starwars.core.mvi.MviEffect
import com.example.starwars.core.mvi.MviIntent
import com.example.starwars.core.mvi.MviState
import com.example.starwars.domain.model.SwApiType
import com.example.starwars.domain.model.SwDetail
import com.example.starwars.domain.model.SwItem

/** I — user intentions. The only messages the View sends to the ViewModel. */
sealed interface SwapiIntent : MviIntent {
    data class LoadTopic(val topic: SwApiType) : SwapiIntent
    data class RefreshTopic(val topic: SwApiType) : SwapiIntent
    data class LoadDetails(val topic: SwApiType, val itemId: String) : SwapiIntent
}

data class SwapiState(
    val topic: SwApiType? = null,
    val items: List<SwItem> = emptyList(),
    val detail: SwDetail? = null,
    val isLoading: Boolean = false,
    val isRefreshing: Boolean = false,
    val error: String? = null,
) : MviState {
    val isEmpty: Boolean get() = !isLoading && items.isEmpty()
}

sealed interface SwapiEffect : MviEffect {
    data class ShowError(val message: String) : SwapiEffect
}

/** Internal results of handling an intent — the only things the reducer turns into state. */
sealed interface SwapiChange {
    data object Loading : SwapiChange
    data object Refreshing : SwapiChange
    data class TopicLoaded(val topic: SwApiType, val items: List<SwItem>) : SwapiChange
    data class DetailLoaded(val detail: SwDetail) : SwapiChange
    data class Failed(val message: String) : SwapiChange
}
