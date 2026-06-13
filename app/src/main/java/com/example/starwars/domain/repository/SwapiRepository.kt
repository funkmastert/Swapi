package com.example.starwars.domain.repository

import com.example.starwars.domain.model.SwApiType
import com.example.starwars.domain.model.SwDetail
import com.example.starwars.domain.model.SwItem

interface SwapiRepository {

    /**
     * All items in a topic (e.g. every person, every film).
     * @param forceRefresh when true, bypass the cache and re-fetch from the network.
     */
    suspend fun getTopic(type: SwApiType, forceRefresh: Boolean = false): List<SwItem>

    /** Full detail of a single item within a topic, by its id. Fetched lazily and cached. */
    suspend fun getDetail(type: SwApiType, id: String): SwDetail
}
