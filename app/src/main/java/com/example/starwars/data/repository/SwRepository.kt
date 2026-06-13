package com.example.starwars.data.repository

import com.example.starwars.data.remote.SwapiService
import com.example.starwars.domain.model.SwApiType
import com.example.starwars.domain.model.SwItem
import com.example.starwars.domain.repository.SwapiRepository
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Maps the SWAPI Retrofit service onto the domain model. Each [SwApiType] picks the matching
 * endpoint; the display [SwItem.text] is the resource's name/title and the id is taken from
 * the trailing path segment of its url (e.g. ".../people/1" -> "1").
 */
@Singleton
class SwRepository @Inject constructor(
    private val service: SwapiService,
) : SwapiRepository {

    override suspend fun getTopic(type: SwApiType): List<SwItem> = when (type) {
        SwApiType.PEOPLE    -> service.getPeople().map { SwItem(it.url.idFromUrl(), it.name, type) }
        SwApiType.FILMS     -> service.getFilms().map { SwItem(it.url.idFromUrl(), it.title, type) }
        SwApiType.PLANETS   -> service.getPlanets().map { SwItem(it.url.idFromUrl(), it.name, type) }
        SwApiType.SPECIES   -> service.getAllSpecies().map { SwItem(it.url.idFromUrl(), it.name, type) }
        SwApiType.STARSHIPS -> service.getStarships().map { SwItem(it.url.idFromUrl(), it.name, type) }
        SwApiType.VEHICLES  -> service.getVehicles().map { SwItem(it.url.idFromUrl(), it.name, type) }
    }

    override suspend fun getItem(type: SwApiType, id: String): SwItem {
        val n = id.toInt()
        return when (type) {
            SwApiType.PEOPLE    -> service.getPerson(n).let { SwItem(id, it.name, type) }
            SwApiType.FILMS     -> service.getFilm(n).let { SwItem(id, it.title, type) }
            SwApiType.PLANETS   -> service.getPlanet(n).let { SwItem(id, it.name, type) }
            SwApiType.SPECIES   -> service.getSpecies(n).let { SwItem(id, it.name, type) }
            SwApiType.STARSHIPS -> service.getStarship(n).let { SwItem(id, it.name, type) }
            SwApiType.VEHICLES  -> service.getVehicle(n).let { SwItem(id, it.name, type) }
        }
    }

    private fun String.idFromUrl(): String = trimEnd('/').substringAfterLast('/')
}
