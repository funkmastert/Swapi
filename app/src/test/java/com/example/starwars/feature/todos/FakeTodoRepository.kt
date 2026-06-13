package com.example.starwars.feature.todos

import com.example.starwars.domain.model.Todo
import com.example.starwars.domain.repository.TodoRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update

/** A tiny fake so the ViewModel can be tested without the real data layer. */
class FakeTodoRepository(initial: List<Todo> = emptyList()) : TodoRepository {
    private val todos = MutableStateFlow(initial)
    private var nextId = 0

    override fun observeTodos(): Flow<List<Todo>> = todos
    override suspend fun add(text: String) = todos.update { it + Todo("gen-${nextId++}", text) }
    override suspend fun toggle(id: String) =
        todos.update { list -> list.map { if (it.id == id) it.copy(completed = !it.completed) else it } }
    override suspend fun delete(id: String) = todos.update { list -> list.filterNot { it.id == id } }
    override suspend fun restore(todo: Todo) = todos.update { it + todo }
    override suspend fun clearCompleted() = todos.update { list -> list.filterNot { it.completed } }
}
