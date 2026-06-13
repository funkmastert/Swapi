package com.example.starwars.data.repository

import com.example.starwars.domain.model.SwItem
import com.example.starwars.domain.repository.SwapiRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SwRepository @Inject constructor() : SwapiRepository {
    override fun observeSw(): Flow<List<SwItem>> {
        TODO("Not yet implemented")
    }

}
