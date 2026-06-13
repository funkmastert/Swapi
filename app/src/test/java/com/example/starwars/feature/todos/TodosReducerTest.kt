package com.example.starwars.feature.todos

import com.example.starwars.domain.model.Todo
import com.example.starwars.domain.model.TodoFilter
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

/**
 * The reducer is a pure function, so these tests need no mocks, no coroutines, no
 * Android. They just assert `reduce(state, change) == expectedState`. This is the
 * clearest demonstration of why MVI is easy to test.
 */
class TodosReducerTest {

    private val initial = TodosState()

    @Test
    fun `TodosLoaded replaces list and clears loading`() {
        val todos = listOf(Todo("1", "a"), Todo("2", "b"))

        val result = TodosReducer.reduce(initial, TodosChange.TodosLoaded(todos))

        assertEquals(todos, result.todos)
        assertFalse(result.isLoading)
    }

    @Test
    fun `InputSet only changes the input field`() {
        val result = TodosReducer.reduce(initial, TodosChange.InputSet("Buy milk"))

        assertEquals("Buy milk", result.input)
        assertEquals(initial.todos, result.todos)
        assertEquals(initial.filter, result.filter)
    }

    @Test
    fun `FilterSet only changes the filter`() {
        val result = TodosReducer.reduce(initial, TodosChange.FilterSet(TodoFilter.Completed))

        assertEquals(TodoFilter.Completed, result.filter)
    }

    @Test
    fun `visibleTodos is derived from filter`() {
        val loaded = TodosReducer.reduce(
            initial,
            TodosChange.TodosLoaded(
                listOf(
                    Todo("1", "done", completed = true),
                    Todo("2", "todo", completed = false),
                ),
            ),
        )

        assertEquals(2, loaded.copy(filter = TodoFilter.All).visibleTodos.size)
        assertEquals(1, loaded.copy(filter = TodoFilter.Active).visibleTodos.size)
        assertEquals(1, loaded.copy(filter = TodoFilter.Completed).visibleTodos.size)
        assertEquals(1, loaded.activeCount)
        assertTrue(loaded.hasCompleted)
    }
}
