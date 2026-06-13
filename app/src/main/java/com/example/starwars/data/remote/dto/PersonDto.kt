package com.example.starwars.data.remote.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * A person (character) as returned by `GET /people` and `GET /people/{id}`.
 * SWAPI returns every scalar as a string (e.g. height = "172", or "unknown"),
 * so the DTO keeps them as strings; parsing is a domain-layer concern.
 * Related resources are absolute URLs into the same API.
 */
@Serializable
data class PersonDto(
    val name: String,
    val height: String,
    val mass: String,
    @SerialName("hair_color") val hairColor: String,
    @SerialName("skin_color") val skinColor: String,
    @SerialName("eye_color") val eyeColor: String,
    @SerialName("birth_year") val birthYear: String,
    val gender: String,
    val homeworld: String,
    val films: List<String>,
    val species: List<String>,
    val vehicles: List<String>,
    val starships: List<String>,
    val created: String,
    val edited: String,
    val url: String,
)
