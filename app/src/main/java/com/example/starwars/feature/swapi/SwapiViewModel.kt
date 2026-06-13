package com.example.starwars.feature.swapi

import androidx.lifecycle.viewModelScope
import com.example.starwars.core.mvi.MviViewModel
import com.example.starwars.domain.repository.SwapiRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject

@HiltViewModel
class SwapiViewModel @Inject constructor(
    private val repository: SwapiRepository,
) : MviViewModel<SwapiIntent, SwapiState, SwapiEffect>(initialState = SwapiState()) {

    init {
        repository.observeSw()
            .onEach {
                todos -> setState {
                    SwapiReducer.reduce(this, SwapiChange.DataLoaded(todos))
                }
            }
            .launchIn(viewModelScope)
    }

    override suspend fun handleIntent(intent: SwapiIntent) {
        when (intent) {
            is SwapiIntent.ShowDetails -> TODO()
        }
    }

    private fun reduce(change: SwapiChange) = setState { SwapiReducer.reduce(this, change) }
}
