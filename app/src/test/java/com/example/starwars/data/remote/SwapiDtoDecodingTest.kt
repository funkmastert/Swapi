package com.example.starwars.data.remote

import com.example.starwars.data.remote.dto.FilmDto
import com.example.starwars.data.remote.dto.PersonDto
import com.example.starwars.data.remote.dto.PlanetDto
import com.example.starwars.data.remote.dto.SpeciesDto
import com.example.starwars.data.remote.dto.StarshipDto
import com.example.starwars.data.remote.dto.VehicleDto
import kotlinx.serialization.json.Json
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Test

/**
 * Decodes real SWAPI payloads (snapshotted from https://swapi.info/api on
 * 2026-06-12 into test/resources/swapi) through every DTO. Catches any drift
 * between the DTO field names/nullability and what the API actually returns —
 * the kind of bug that otherwise only shows up as a runtime crash.
 */
class SwapiDtoDecodingTest {

    // Mirror NetworkModule's parser configuration.
    private val json = Json { ignoreUnknownKeys = true }

    private fun fixture(name: String): String =
        checkNotNull(javaClass.getResourceAsStream("/swapi/$name.json")) {
            "missing fixture $name.json"
        }.bufferedReader().use { it.readText() }

    @Test
    fun `decodes all people`() {
        val people = json.decodeFromString<List<PersonDto>>(fixture("people"))
        assertTrue(people.size >= 80)
        val luke = people.first { it.name == "Luke Skywalker" }
        assertEquals("19BBY", luke.birthYear)
        assertEquals("https://swapi.info/api/planets/1", luke.homeworld)
    }

    @Test
    fun `decodes all films`() {
        val films = json.decodeFromString<List<FilmDto>>(fixture("films"))
        assertEquals(6, films.size)
        val anh = films.first { it.episodeId == 4 }
        assertEquals("A New Hope", anh.title)
        assertEquals("George Lucas", anh.director)
    }

    @Test
    fun `decodes all planets`() {
        val planets = json.decodeFromString<List<PlanetDto>>(fixture("planets"))
        assertTrue(planets.size >= 60)
        val tatooine = planets.first { it.name == "Tatooine" }
        assertEquals("23", tatooine.rotationPeriod)
        assertEquals("1", tatooine.surfaceWater)
    }

    @Test
    fun `decodes all species including null homeworld`() {
        val species = json.decodeFromString<List<SpeciesDto>>(fixture("species"))
        assertTrue(species.size >= 30)
        assertNull(species.first { it.name == "Droid" }.homeworld)
    }

    @Test
    fun `decodes all starships`() {
        val starships = json.decodeFromString<List<StarshipDto>>(fixture("starships"))
        assertTrue(starships.size >= 30)
        val deathStar = starships.first { it.name == "Death Star" }
        assertEquals("10", deathStar.mglt)
        assertEquals("Deep Space Mobile Battlestation", deathStar.starshipClass)
    }

    @Test
    fun `decodes all vehicles`() {
        val vehicles = json.decodeFromString<List<VehicleDto>>(fixture("vehicles"))
        assertTrue(vehicles.size >= 30)
        val sandcrawler = vehicles.first { it.name == "Sand Crawler" }
        assertEquals("wheeled", sandcrawler.vehicleClass)
    }
}
