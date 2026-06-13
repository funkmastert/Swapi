package com.example.starwars.domain.repository

import com.example.starwars.domain.model.SwItem
import kotlinx.coroutines.flow.Flow

interface SwapiRepository {
    fun observeSw(): Flow<List<SwItem>>
}
