package com.example.starwars.di

import com.example.starwars.data.repository.SwRepository
import com.example.starwars.domain.repository.SwapiRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

/**
 * Hilt binding: whenever something asks for a [SwapiRepository], give it the [SwRepository].
 * Swap this one line to change the data source app-wide.
 */
@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Binds
    @Singleton
    abstract fun bindSwapiRepository(impl: SwRepository): SwapiRepository
}
