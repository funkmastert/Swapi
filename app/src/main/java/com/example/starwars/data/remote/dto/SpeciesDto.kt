package com.example.starwars.data.remote.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * A species as returned by `GET /species` and `GET /species/{id}`.
 * [homeworld] is nullable — some species (e.g. droids) have none.
 */
@Serializable
data class SpeciesDto(
    val name: String,
    val classification: String,
    val designation: String,
    @SerialName("average_height") val averageHeight: String,
    @SerialName("skin_colors") val skinColors: String,
    @SerialName("hair_colors") val hairColors: String,
    @SerialName("eye_colors") val eyeColors: String,
    @SerialName("average_lifespan") val averageLifespan: String,
    val homeworld: String?,
    val language: String,
    val people: List<String>,
    val films: List<String>,
    val created: String,
    val edited: String,
    val url: String,
)
