package com.example.starwars.data.remote

import com.example.starwars.data.remote.dto.FilmDto
import com.example.starwars.data.remote.dto.PersonDto
import com.example.starwars.data.remote.dto.PlanetDto
import com.example.starwars.data.remote.dto.SpeciesDto
import com.example.starwars.data.remote.dto.StarshipDto
import com.example.starwars.data.remote.dto.VehicleDto
import retrofit2.http.GET
import retrofit2.http.Path

/**
 * Retrofit definition of the SWAPI (https://swapi.info/api) surface — read-only,
 * no auth. Unlike the older swapi.dev, list endpoints return a plain JSON array
 * (no count/next/previous envelope), so each list call maps straight to a List.
 */
interface SwapiService {

    @GET("people")
    suspend fun getPeople(): List<PersonDto>

    @GET("people/{id}")
    suspend fun getPerson(@Path("id") id: Int): PersonDto

    @GET("films")
    suspend fun getFilms(): List<FilmDto>

    @GET("films/{id}")
    suspend fun getFilm(@Path("id") id: Int): FilmDto

    @GET("planets")
    suspend fun getPlanets(): List<PlanetDto>

    @GET("planets/{id}")
    suspend fun getPlanet(@Path("id") id: Int): PlanetDto

    @GET("species")
    suspend fun getAllSpecies(): List<SpeciesDto>

    @GET("species/{id}")
    suspend fun getSpecies(@Path("id") id: Int): SpeciesDto

    @GET("starships")
    suspend fun getStarships(): List<StarshipDto>

    @GET("starships/{id}")
    suspend fun getStarship(@Path("id") id: Int): StarshipDto

    @GET("vehicles")
    suspend fun getVehicles(): List<VehicleDto>

    @GET("vehicles/{id}")
    suspend fun getVehicle(@Path("id") id: Int): VehicleDto
}
