package com.example.starwars.domain.model

/** A single todo item. Immutable — changes produce a new copy. */
data class Todo(
    val id: String,
    val text: String,
    val completed: Boolean = false,
)

/** Which subset of todos the user is currently viewing. */
enum class TodoFilter { All, Active, Completed }
