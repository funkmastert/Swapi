package com.example.starwars.domain.model

import kotlinx.serialization.Serializable

data class SwItem(
    val id: String,
    val text: String,
    val dataType: SwApiType,
)

/**
 * Stable identifier for a detail attribute's label. The display text lives in strings.xml and
 * is resolved in the UI (see `SwAttrKey.labelRes`) — never here, so the data layer stays free
 * of presentation strings and the cache stores a stable key, not a localized label.
 */
@Serializable
enum class SwAttrKey {
    HEIGHT, MASS, HAIR_COLOR, SKIN_COLOR, EYE_COLOR, BIRTH_YEAR, GENDER,
    FILMS, STARSHIPS, VEHICLES, PLANETS, CHARACTERS, RESIDENTS, PEOPLE,
    EPISODE, DIRECTOR, PRODUCER, RELEASE_DATE,
    CLIMATE, TERRAIN, POPULATION, DIAMETER, GRAVITY, ORBITAL_PERIOD,
    ROTATION_PERIOD, SURFACE_WATER,
    CLASSIFICATION, DESIGNATION, AVERAGE_HEIGHT, AVERAGE_LIFESPAN, LANGUAGE,
    SKIN_COLORS, HAIR_COLORS, EYE_COLORS,
    MODEL, MANUFACTURER, CLASS, COST, LENGTH, CREW, PASSENGERS, MAX_SPEED,
    HYPERDRIVE_RATING, CARGO_CAPACITY,
}

/** One labelled fact on a detail screen: [key] -> localized label, [value] -> API data. */
@Serializable
data class SwAttribute(
    val key: SwAttrKey,
    val value: String,
)

/** Full detail of a single resource: a title plus an ordered list of attributes. */
data class SwDetail(
    val id: String,
    val type: SwApiType,
    val title: String,
    val attributes: List<SwAttribute>,
)

enum class SwApiType {
    FILMS,
    PEOPLE,
    PLANETS,
    VEHICLES,
    SPECIES,
    STARSHIPS
}

/**
 * Image URL for a resource. SWAPI itself has no images, so these come from the (unofficial)
 * starwars-visualguide.com, keyed by the same ids SWAPI uses. Coverage has gaps — callers
 * should provide a fallback. Note PEOPLE maps to the "characters" path.
 */
fun imageUrlFor(type: SwApiType, id: String): String {
    val category = when (type) {
        SwApiType.PEOPLE -> "characters"
        SwApiType.FILMS -> "films"
        SwApiType.PLANETS -> "planets"
        SwApiType.SPECIES -> "species"
        SwApiType.STARSHIPS -> "starships"
        SwApiType.VEHICLES -> "vehicles"
    }
    return "https://starwars-visualguide.com/assets/img/$category/$id.jpg"
}
