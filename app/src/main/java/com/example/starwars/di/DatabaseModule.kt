package com.example.starwars.di

import android.content.Context
import androidx.room.Room
import com.example.starwars.data.local.SwDao
import com.example.starwars.data.local.SwDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

/** Provides the Room database and its DAO as app-wide singletons. */
@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext context: Context): SwDatabase =
        Room.databaseBuilder(context, SwDatabase::class.java, "swapi.db")
            // It's only a cache of finite, immutable data — fine to rebuild on a schema bump.
            .fallbackToDestructiveMigration()
            .build()

    @Provides
    fun provideSwDao(database: SwDatabase): SwDao = database.swDao()
}
