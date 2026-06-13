package com.example.starwars.data.remote.dto

import com.example.starwars.domain.model.SwApiType
import com.example.starwars.domain.model.SwAttrKey
import com.example.starwars.domain.model.SwAttribute
import com.example.starwars.domain.model.SwDetail

/**
 * Maps each SWAPI DTO to the generic [SwDetail] shown on the detail screen. Attribute labels are
 * referenced by [SwAttrKey] (resolved to localized strings in the UI), so no display text lives
 * here. Related resources are summarised as counts since the API returns them as lists of urls.
 */

fun PersonDto.toDetail(id: String) = SwDetail(
    id = id,
    type = SwApiType.PEOPLE,
    title = name,
    attributes = listOf(
        SwAttribute(SwAttrKey.HEIGHT, height),
        SwAttribute(SwAttrKey.MASS, mass),
        SwAttribute(SwAttrKey.HAIR_COLOR, hairColor),
        SwAttribute(SwAttrKey.SKIN_COLOR, skinColor),
        SwAttribute(SwAttrKey.EYE_COLOR, eyeColor),
        SwAttribute(SwAttrKey.BIRTH_YEAR, birthYear),
        SwAttribute(SwAttrKey.GENDER, gender),
        SwAttribute(SwAttrKey.FILMS, films.size.toString()),
        SwAttribute(SwAttrKey.STARSHIPS, starships.size.toString()),
        SwAttribute(SwAttrKey.VEHICLES, vehicles.size.toString()),
    ),
)

fun FilmDto.toDetail(id: String) = SwDetail(
    id = id,
    type = SwApiType.FILMS,
    title = title,
    attributes = listOf(
        SwAttribute(SwAttrKey.EPISODE, episodeId.toString()),
        SwAttribute(SwAttrKey.DIRECTOR, director),
        SwAttribute(SwAttrKey.PRODUCER, producer),
        SwAttribute(SwAttrKey.RELEASE_DATE, releaseDate),
        SwAttribute(SwAttrKey.CHARACTERS, characters.size.toString()),
        SwAttribute(SwAttrKey.PLANETS, planets.size.toString()),
        SwAttribute(SwAttrKey.STARSHIPS, starships.size.toString()),
    ),
)

fun PlanetDto.toDetail(id: String) = SwDetail(
    id = id,
    type = SwApiType.PLANETS,
    title = name,
    attributes = listOf(
        SwAttribute(SwAttrKey.CLIMATE, climate),
        SwAttribute(SwAttrKey.TERRAIN, terrain),
        SwAttribute(SwAttrKey.POPULATION, population),
        SwAttribute(SwAttrKey.DIAMETER, diameter),
        SwAttribute(SwAttrKey.GRAVITY, gravity),
        SwAttribute(SwAttrKey.ORBITAL_PERIOD, orbitalPeriod),
        SwAttribute(SwAttrKey.ROTATION_PERIOD, rotationPeriod),
        SwAttribute(SwAttrKey.SURFACE_WATER, surfaceWater),
        SwAttribute(SwAttrKey.RESIDENTS, residents.size.toString()),
        SwAttribute(SwAttrKey.FILMS, films.size.toString()),
    ),
)

fun SpeciesDto.toDetail(id: String) = SwDetail(
    id = id,
    type = SwApiType.SPECIES,
    title = name,
    attributes = listOf(
        SwAttribute(SwAttrKey.CLASSIFICATION, classification),
        SwAttribute(SwAttrKey.DESIGNATION, designation),
        SwAttribute(SwAttrKey.AVERAGE_HEIGHT, averageHeight),
        SwAttribute(SwAttrKey.AVERAGE_LIFESPAN, averageLifespan),
        SwAttribute(SwAttrKey.LANGUAGE, language),
        SwAttribute(SwAttrKey.SKIN_COLORS, skinColors),
        SwAttribute(SwAttrKey.HAIR_COLORS, hairColors),
        SwAttribute(SwAttrKey.EYE_COLORS, eyeColors),
        SwAttribute(SwAttrKey.PEOPLE, people.size.toString()),
        SwAttribute(SwAttrKey.FILMS, films.size.toString()),
    ),
)

fun StarshipDto.toDetail(id: String) = SwDetail(
    id = id,
    type = SwApiType.STARSHIPS,
    title = name,
    attributes = listOf(
        SwAttribute(SwAttrKey.MODEL, model),
        SwAttribute(SwAttrKey.MANUFACTURER, manufacturer),
        SwAttribute(SwAttrKey.CLASS, starshipClass),
        SwAttribute(SwAttrKey.COST, costInCredits),
        SwAttribute(SwAttrKey.LENGTH, length),
        SwAttribute(SwAttrKey.CREW, crew),
        SwAttribute(SwAttrKey.PASSENGERS, passengers),
        SwAttribute(SwAttrKey.MAX_SPEED, maxAtmospheringSpeed),
        SwAttribute(SwAttrKey.HYPERDRIVE_RATING, hyperdriveRating),
        SwAttribute(SwAttrKey.CARGO_CAPACITY, cargoCapacity),
        SwAttribute(SwAttrKey.FILMS, films.size.toString()),
    ),
)

fun VehicleDto.toDetail(id: String) = SwDetail(
    id = id,
    type = SwApiType.VEHICLES,
    title = name,
    attributes = listOf(
        SwAttribute(SwAttrKey.MODEL, model),
        SwAttribute(SwAttrKey.MANUFACTURER, manufacturer),
        SwAttribute(SwAttrKey.CLASS, vehicleClass),
        SwAttribute(SwAttrKey.COST, costInCredits),
        SwAttribute(SwAttrKey.LENGTH, length),
        SwAttribute(SwAttrKey.CREW, crew),
        SwAttribute(SwAttrKey.PASSENGERS, passengers),
        SwAttribute(SwAttrKey.MAX_SPEED, maxAtmospheringSpeed),
        SwAttribute(SwAttrKey.CARGO_CAPACITY, cargoCapacity),
        SwAttribute(SwAttrKey.FILMS, films.size.toString()),
    ),
)
