package com.example.starwars.data.local

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import com.example.starwars.domain.model.SwApiType

@Dao
interface SwDao {

    // --- Topic lists (lightweight rows) --------------------------------------
    @Query("SELECT * FROM sw_items WHERE dataType = :type ORDER BY text")
    suspend fun getByType(type: SwApiType): List<SwItemEntity>

    /** Insert or replace — so re-fetching a topic refreshes the cache rather than duplicating. */
    @Upsert
    suspend fun upsertAll(items: List<SwItemEntity>)

    // --- Item details (cached lazily, on first open) -------------------------
    @Query("SELECT * FROM sw_details WHERE dataType = :type AND id = :id LIMIT 1")
    suspend fun getDetail(type: SwApiType, id: String): SwDetailEntity?

    @Upsert
    suspend fun upsertDetail(detail: SwDetailEntity)
}
