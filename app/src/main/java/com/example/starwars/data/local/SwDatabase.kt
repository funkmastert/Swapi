package com.example.starwars.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverter
import androidx.room.TypeConverters
import com.example.starwars.domain.model.SwApiType
import com.example.starwars.domain.model.SwAttribute
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

@Database(
    entities = [SwItemEntity::class, SwDetailEntity::class],
    version = 2,
    exportSchema = false,
)
@TypeConverters(SwConverters::class)
abstract class SwDatabase : RoomDatabase() {
    abstract fun swDao(): SwDao
}

/** Stores the [SwApiType] enum as its name, and attribute lists as JSON. */
class SwConverters {

    @TypeConverter
    fun fromType(type: SwApiType): String = type.name

    @TypeConverter
    fun toType(value: String): SwApiType = SwApiType.valueOf(value)

    @TypeConverter
    fun fromAttributes(attributes: List<SwAttribute>): String = json.encodeToString(attributes)

    @TypeConverter
    fun toAttributes(value: String): List<SwAttribute> = json.decodeFromString(value)

    private companion object {
        val json = Json { ignoreUnknownKeys = true }
    }
}
