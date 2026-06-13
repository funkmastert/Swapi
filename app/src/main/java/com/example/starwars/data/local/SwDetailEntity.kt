package com.example.starwars.data.local

import androidx.room.Entity
import com.example.starwars.domain.model.SwApiType
import com.example.starwars.domain.model.SwAttribute
import com.example.starwars.domain.model.SwDetail

/**
 * Cached full detail for one resource. [attributes] is persisted as JSON via a TypeConverter.
 * Primary key is (dataType, id) — same reasoning as [SwItemEntity].
 */
@Entity(tableName = "sw_details", primaryKeys = ["dataType", "id"])
data class SwDetailEntity(
    val id: String,
    val dataType: SwApiType,
    val title: String,
    val attributes: List<SwAttribute>,
)

fun SwDetailEntity.toDomain(): SwDetail =
    SwDetail(id = id, type = dataType, title = title, attributes = attributes)

fun SwDetail.toEntity(): SwDetailEntity =
    SwDetailEntity(id = id, dataType = type, title = title, attributes = attributes)
