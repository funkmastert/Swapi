package com.example.starwars.domain.repository

import com.example.starwars.domain.model.Todo
import kotlinx.coroutines.flow.Flow

/**
 * The domain's contract for todo storage. The presentation layer depends on this
 * interface, never on a concrete implementation — so the data source (in-memory,
 * Room, network, ...) can be swapped without touching the ViewModel, and tests can
 * supply a fake. [observeTodos] is a reactive stream: any mutation re-emits the
 * full list, which the ViewModel folds into its state.
 */
interface TodoRepository {
    fun observeTodos(): Flow<List<Todo>>
    suspend fun add(text: String)
    suspend fun toggle(id: String)
    suspend fun delete(id: String)
    suspend fun restore(todo: Todo)
    suspend fun clearCompleted()
}
