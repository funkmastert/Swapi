package com.example.starwars.core.navigation

import com.example.starwars.domain.model.SwApiType
import kotlinx.serialization.Serializable

/**
 * Type-safe navigation destinations. Each route is a `@Serializable` type, so arguments
 * are passed as real fields (compiler-checked) instead of stringly-typed paths.
 *
 * Rule of thumb: pass IDs between screens, never whole objects — the destination's
 * ViewModel fetches its own data from the repository.
 */
sealed interface Destination {

    /** Top-level list of topics (People, Films, Planets, …). */
    @Serializable
    data object Topics : Destination

    /** The items inside one topic. */
    @Serializable
    data class Items(val topicId: SwApiType) : Destination

    /** Full details of a single item within a topic. */
    @Serializable
    data class Detail(val topicId: SwApiType, val itemId: String) : Destination
}
