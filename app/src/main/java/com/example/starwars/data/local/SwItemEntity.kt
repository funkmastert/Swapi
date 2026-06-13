package com.example.starwars.data.local

import androidx.room.Entity
import com.example.starwars.domain.model.SwApiType
import com.example.starwars.domain.model.SwItem

/**
 * Cached row for one SWAPI resource. Primary key is (dataType, id) because ids are only
 * unique within a topic — there's both a person #1 and a film #1.
 */
@Entity(tableName = "sw_items", primaryKeys = ["dataType", "id"])
data class SwItemEntity(
    val id: String,
    val dataType: SwApiType,
    val text: String,
)

fun SwItemEntity.toDomain(): SwItem = SwItem(id = id, text = text, dataType = dataType)

fun SwItem.toEntity(): SwItemEntity = SwItemEntity(id = id, dataType = dataType, text = text)
