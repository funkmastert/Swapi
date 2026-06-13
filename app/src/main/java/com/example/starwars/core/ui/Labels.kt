package com.example.starwars.core.ui

import androidx.annotation.StringRes
import com.example.starwars.R
import com.example.starwars.domain.model.SwApiType
import com.example.starwars.domain.model.SwAttrKey

/**
 * Presentation-layer mapping of domain keys to string resources. Lives here (not in the domain
 * model) so the `R` reference stays in the UI layer; callers resolve with `stringResource(...)`.
 */

@StringRes
fun SwApiType.titleRes(): Int = when (this) {
    SwApiType.FILMS -> R.string.topic_films
    SwApiType.PEOPLE -> R.string.topic_people
    SwApiType.PLANETS -> R.string.topic_planets
    SwApiType.VEHICLES -> R.string.topic_vehicles
    SwApiType.SPECIES -> R.string.topic_species
    SwApiType.STARSHIPS -> R.string.topic_starships
}

@StringRes
fun SwAttrKey.labelRes(): Int = when (this) {
    SwAttrKey.HEIGHT -> R.string.attr_height
    SwAttrKey.MASS -> R.string.attr_mass
    SwAttrKey.HAIR_COLOR -> R.string.attr_hair_color
    SwAttrKey.SKIN_COLOR -> R.string.attr_skin_color
    SwAttrKey.EYE_COLOR -> R.string.attr_eye_color
    SwAttrKey.BIRTH_YEAR -> R.string.attr_birth_year
    SwAttrKey.GENDER -> R.string.attr_gender
    SwAttrKey.FILMS -> R.string.attr_films
    SwAttrKey.STARSHIPS -> R.string.attr_starships
    SwAttrKey.VEHICLES -> R.string.attr_vehicles
    SwAttrKey.PLANETS -> R.string.attr_planets
    SwAttrKey.CHARACTERS -> R.string.attr_characters
    SwAttrKey.RESIDENTS -> R.string.attr_residents
    SwAttrKey.PEOPLE -> R.string.attr_people
    SwAttrKey.EPISODE -> R.string.attr_episode
    SwAttrKey.DIRECTOR -> R.string.attr_director
    SwAttrKey.PRODUCER -> R.string.attr_producer
    SwAttrKey.RELEASE_DATE -> R.string.attr_release_date
    SwAttrKey.CLIMATE -> R.string.attr_climate
    SwAttrKey.TERRAIN -> R.string.attr_terrain
    SwAttrKey.POPULATION -> R.string.attr_population
    SwAttrKey.DIAMETER -> R.string.attr_diameter
    SwAttrKey.GRAVITY -> R.string.attr_gravity
    SwAttrKey.ORBITAL_PERIOD -> R.string.attr_orbital_period
    SwAttrKey.ROTATION_PERIOD -> R.string.attr_rotation_period
    SwAttrKey.SURFACE_WATER -> R.string.attr_surface_water
    SwAttrKey.CLASSIFICATION -> R.string.attr_classification
    SwAttrKey.DESIGNATION -> R.string.attr_designation
    SwAttrKey.AVERAGE_HEIGHT -> R.string.attr_average_height
    SwAttrKey.AVERAGE_LIFESPAN -> R.string.attr_average_lifespan
    SwAttrKey.LANGUAGE -> R.string.attr_language
    SwAttrKey.SKIN_COLORS -> R.string.attr_skin_colors
    SwAttrKey.HAIR_COLORS -> R.string.attr_hair_colors
    SwAttrKey.EYE_COLORS -> R.string.attr_eye_colors
    SwAttrKey.MODEL -> R.string.attr_model
    SwAttrKey.MANUFACTURER -> R.string.attr_manufacturer
    SwAttrKey.CLASS -> R.string.attr_class
    SwAttrKey.COST -> R.string.attr_cost
    SwAttrKey.LENGTH -> R.string.attr_length
    SwAttrKey.CREW -> R.string.attr_crew
    SwAttrKey.PASSENGERS -> R.string.attr_passengers
    SwAttrKey.MAX_SPEED -> R.string.attr_max_speed
    SwAttrKey.HYPERDRIVE_RATING -> R.string.attr_hyperdrive_rating
    SwAttrKey.CARGO_CAPACITY -> R.string.attr_cargo_capacity
}
