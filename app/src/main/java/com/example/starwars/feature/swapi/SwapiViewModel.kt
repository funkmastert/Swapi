package com.example.starwars.feature.swapi

import com.example.starwars.core.mvi.MviViewModel
import com.example.starwars.domain.model.SwApiType
import com.example.starwars.domain.repository.SwapiRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class SwapiViewModel @Inject constructor(
    private val repository: SwapiRepository,
) : MviViewModel<SwapiIntent, SwapiState, SwapiEffect>(initialState = SwapiState()) {

    override suspend fun handleIntent(intent: SwapiIntent) {
        when (intent) {
            is SwapiIntent.LoadTopic -> loadTopic(intent.topic, refresh = false)
            is SwapiIntent.RefreshTopic -> loadTopic(intent.topic, refresh = true)
            is SwapiIntent.LoadDetails -> loadDetails(intent.topic, intent.itemId)
        }
    }

    private suspend fun loadTopic(topic: SwApiType, refresh: Boolean) {
        reduce(if (refresh) SwapiChange.Refreshing else SwapiChange.Loading)
        runCatching { repository.getTopic(topic, forceRefresh = refresh) }
            .onSuccess { reduce(SwapiChange.TopicLoaded(topic, it)) }
            .onFailure(::fail)
    }

    private suspend fun loadDetails(topic: SwApiType, itemId: String) {
        reduce(SwapiChange.Loading)
        runCatching { repository.getDetail(topic, itemId) }
            .onSuccess { reduce(SwapiChange.DetailLoaded(it)) }
            .onFailure(::fail)
    }

    private fun fail(t: Throwable) {
        val message = t.message ?: "Something went wrong"
        reduce(SwapiChange.Failed(message))
        sendEffect(SwapiEffect.ShowError(message))
    }

    private fun reduce(change: SwapiChange) = setState {
        SwapiReducer.reduce(this, change)
    }
}
