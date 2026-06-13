package com.example.starwars.feature.todos

import app.cash.turbine.test
import com.example.starwars.domain.model.Todo
import com.example.starwars.domain.model.TodoFilter
import com.example.starwars.util.MainDispatcherRule
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Rule
import org.junit.Test

/**
 * The ViewModel is deterministic: send an intent, assert the resulting state/effect.
 * Turbine lets us await emissions from the state/effect flows.
 */
@OptIn(ExperimentalCoroutinesApi::class)
class TodosViewModelTest {

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    @Test
    fun `AddClicked adds a todo and clears the input`() = runTest {
        val vm = TodosViewModel(FakeTodoRepository())

        vm.onIntent(TodosIntent.InputChanged("Buy milk"))
        vm.onIntent(TodosIntent.AddClicked)
        advanceUntilIdle()

        val state = vm.state.value
        assertEquals("", state.input)
        assertEquals(listOf("Buy milk"), state.todos.map { it.text })
    }

    @Test
    fun `blank input does not add`() = runTest {
        val vm = TodosViewModel(FakeTodoRepository())

        vm.onIntent(TodosIntent.InputChanged("   "))
        vm.onIntent(TodosIntent.AddClicked)
        advanceUntilIdle()

        assertTrue(vm.state.value.todos.isEmpty())
    }

    @Test
    fun `DeleteClicked removes the todo and emits an undo effect`() = runTest {
        val repo = FakeTodoRepository(listOf(Todo("1", "Walk dog")))
        val vm = TodosViewModel(repo)
        advanceUntilIdle() // let the repository's initial list flow into state

        vm.effects.test {
            vm.onIntent(TodosIntent.DeleteClicked("1"))
            advanceUntilIdle()

            assertEquals(TodosEffect.ShowUndoDelete("Walk dog"), awaitItem())
            assertTrue(vm.state.value.todos.isEmpty())
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `UndoDeleteClicked restores the last deleted todo`() = runTest {
        val repo = FakeTodoRepository(listOf(Todo("1", "Walk dog")))
        val vm = TodosViewModel(repo)
        advanceUntilIdle() // let the repository's initial list flow into state

        vm.onIntent(TodosIntent.DeleteClicked("1"))
        advanceUntilIdle()
        vm.onIntent(TodosIntent.UndoDeleteClicked)
        advanceUntilIdle()

        assertEquals(listOf("Walk dog"), vm.state.value.todos.map { it.text })
    }

    @Test
    fun `FilterSelected changes which todos are visible`() = runTest {
        val repo = FakeTodoRepository(
            listOf(Todo("1", "done", completed = true), Todo("2", "active")),
        )
        val vm = TodosViewModel(repo)
        advanceUntilIdle()

        vm.onIntent(TodosIntent.FilterSelected(TodoFilter.Active))
        advanceUntilIdle()

        assertEquals(TodoFilter.Active, vm.state.value.filter)
        assertEquals(listOf("active"), vm.state.value.visibleTodos.map { it.text })
    }
}
