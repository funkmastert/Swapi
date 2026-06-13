package com.example.starwars.domain.model

data class SwItem(
    val id: String,
    val text: String,
    val dataType: SwApiType,
)

enum class SwApiType {
    FILMS,
    PEOPLE,
    PLANETS,
    VEHICLES,
    SPECIES,
    STARSHIPS
}