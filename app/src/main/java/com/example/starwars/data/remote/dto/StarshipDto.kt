package com.example.starwars.data.remote.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/** A starship as returned by `GET /starships` and `GET /starships/{id}`. */
@Serializable
data class StarshipDto(
    val name: String,
    val model: String,
    val manufacturer: String,
    @SerialName("cost_in_credits") val costInCredits: String,
    val length: String,
    @SerialName("max_atmosphering_speed") val maxAtmospheringSpeed: String,
    val crew: String,
    val passengers: String,
    @SerialName("cargo_capacity") val cargoCapacity: String,
    val consumables: String,
    @SerialName("hyperdrive_rating") val hyperdriveRating: String,
    @SerialName("MGLT") val mglt: String,
    @SerialName("starship_class") val starshipClass: String,
    val pilots: List<String>,
    val films: List<String>,
    val created: String,
    val edited: String,
    val url: String,
)
