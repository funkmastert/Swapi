package com.example.starwars.feature.swapi

object SwapiReducer {

    fun reduce(state: SwapiState, change: SwapiChange): SwapiState = when (change) {
        is SwapiChange.DataLoaded -> TODO()
    }
}
