package com.example.starwars.data.repository

import com.example.starwars.data.local.SwDao
import com.example.starwars.data.local.toDomain
import com.example.starwars.data.local.toEntity
import com.example.starwars.data.remote.SwapiService
import com.example.starwars.data.remote.dto.toDetail
import com.example.starwars.domain.model.SwApiType
import com.example.starwars.domain.model.SwDetail
import com.example.starwars.domain.model.SwItem
import com.example.starwars.domain.repository.SwapiRepository
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Cache-first repository backed by Room.
 *
 * - A topic list is fetched once, cached, and served from Room thereafter (or re-fetched on
 *   [getTopic] with `forceRefresh = true`, e.g. pull-to-refresh).
 * - An item's detail is fetched lazily — only when [getDetail] is called — from its own endpoint,
 *   and cached. Opening a topic does NOT preload every item's detail.
 */
@Singleton
class SwRepository @Inject constructor(
    private val service: SwapiService,
    private val dao: SwDao,
) : SwapiRepository {

    override suspend fun getTopic(type: SwApiType, forceRefresh: Boolean): List<SwItem> {
        if (!forceRefresh) {
            dao.getByType(type).takeIf { it.isNotEmpty() }?.let { cached ->
                return cached.map { it.toDomain() }
            }
        }
        val fresh = fetchTopic(type)
        dao.upsertAll(fresh.map { it.toEntity() })
        return fresh
    }

    override suspend fun getDetail(type: SwApiType, id: String): SwDetail {
        dao.getDetail(type, id)?.let { return it.toDomain() }
        // Cache miss — fetch just this one item's full record from its own endpoint, then cache it.
        val detail = fetchDetail(type, id)
        dao.upsertDetail(detail.toEntity())
        return detail
    }

    /** One network round-trip for the whole topic list. */
    private suspend fun fetchTopic(type: SwApiType): List<SwItem> = when (type) {
        SwApiType.PEOPLE    -> service.getPeople().map { SwItem(it.url.idFromUrl(), it.name, type) }
        SwApiType.FILMS     -> service.getFilms().map { SwItem(it.url.idFromUrl(), it.title, type) }
        SwApiType.PLANETS   -> service.getPlanets().map { SwItem(it.url.idFromUrl(), it.name, type) }
        SwApiType.SPECIES   -> service.getAllSpecies().map { SwItem(it.url.idFromUrl(), it.name, type) }
        SwApiType.STARSHIPS -> service.getStarships().map { SwItem(it.url.idFromUrl(), it.name, type) }
        SwApiType.VEHICLES  -> service.getVehicles().map { SwItem(it.url.idFromUrl(), it.name, type) }
    }

    /** One network round-trip for a single item's full record. */
    private suspend fun fetchDetail(type: SwApiType, id: String): SwDetail {
        val n = id.toInt()
        return when (type) {
            SwApiType.PEOPLE    -> service.getPerson(n).toDetail(id)
            SwApiType.FILMS     -> service.getFilm(n).toDetail(id)
            SwApiType.PLANETS   -> service.getPlanet(n).toDetail(id)
            SwApiType.SPECIES   -> service.getSpecies(n).toDetail(id)
            SwApiType.STARSHIPS -> service.getStarship(n).toDetail(id)
            SwApiType.VEHICLES  -> service.getVehicle(n).toDetail(id)
        }
    }

    private fun String.idFromUrl(): String = trimEnd('/').substringAfterLast('/')
}
