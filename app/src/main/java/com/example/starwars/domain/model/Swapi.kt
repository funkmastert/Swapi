package com.example.starwars.domain.model

import java.util.Locale

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

/** Display label for a topic, e.g. PEOPLE -> "People". */
fun SwApiType.prettyName(): String =
    name.lowercase().replaceFirstChar { it.titlecase(Locale.US) }