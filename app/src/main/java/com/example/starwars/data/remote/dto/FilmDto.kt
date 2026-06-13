package com.example.starwars.data.remote.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/** A film as returned by `GET /films` and `GET /films/{id}`. */
@Serializable
data class FilmDto(
    val title: String,
    @SerialName("episode_id") val episodeId: Int,
    @SerialName("opening_crawl") val openingCrawl: String,
    val director: String,
    val producer: String,
    @SerialName("release_date") val releaseDate: String,
    val characters: List<String>,
    val planets: List<String>,
    val starships: List<String>,
    val vehicles: List<String>,
    val species: List<String>,
    val created: String,
    val edited: String,
    val url: String,
)
