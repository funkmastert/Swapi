package com.example.starwars.data.repository

import com.example.starwars.domain.model.Todo
import com.example.starwars.domain.repository.TodoRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import java.util.UUID
import javax.inject.Inject
import javax.inject.Singleton

/**
 * A simple in-memory implementation backed by a [MutableStateFlow], so every
 * mutation automatically re-emits to observers. In a real app you'd swap this for
 * Room or a network source — nothing else in the app would change, because callers
 * depend only on [TodoRepository].
 */
@Singleton
class InMemoryTodoRepository @Inject constructor() : TodoRepository {

    private val todos = MutableStateFlow(
        listOf(
            Todo(id = UUID.randomUUID().toString(), text = "Learn the MVI data flow", completed = true),
            Todo(id = UUID.randomUUID().toString(), text = "Keep state immutable"),
            Todo(id = UUID.randomUUID().toString(), text = "Send navigation as an effect"),
        ),
    )

    override fun observeTodos(): Flow<List<Todo>> = todos.asStateFlow()

    override suspend fun add(text: String) {
        todos.update { it + Todo(id = UUID.randomUUID().toString(), text = text) }
    }

    override suspend fun toggle(id: String) {
        todos.update { list ->
            list.map { if (it.id == id) it.copy(completed = !it.completed) else it }
        }
    }

    override suspend fun delete(id: String) {
        todos.update { list -> list.filterNot { it.id == id } }
    }

    override suspend fun restore(todo: Todo) {
        todos.update { it + todo }
    }

    override suspend fun clearCompleted() {
        todos.update { list -> list.filterNot { it.completed } }
    }
}
