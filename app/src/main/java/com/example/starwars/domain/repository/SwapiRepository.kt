package com.example.starwars.domain.repository

import com.example.starwars.domain.model.SwApiType
import com.example.starwars.domain.model.SwItem

interface SwapiRepository {

    /** All items in a topic (e.g. every person, every film). */
    suspend fun getTopic(type: SwApiType): List<SwItem>

    /** A single item within a topic, by its id. */
    suspend fun getItem(type: SwApiType, id: String): SwItem
}
