package com.example.starwars.core.mvi

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

/**
 * Reusable MVI base. A feature ViewModel only has to:
 *   1. pass an initial [S],
 *   2. implement [handleIntent] — the single place where intents are turned into
 *      state changes (via [setState]) and effects (via [sendEffect]).
 *
 * Everything the View needs is exposed as exactly two streams:
 *   - [state]   : a hot [StateFlow] that always holds the current screen state.
 *   - [effects] : a cold stream of one-off events, delivered once each.
 *
 * The View only ever pushes data back in through one funnel: [onIntent].
 */
abstract class MviViewModel<I : MviIntent, S : MviState, E : MviEffect>(
    initialState: S,
) : ViewModel() {

    // --- State: the single source of truth -----------------------------------
    private val _state = MutableStateFlow(initialState)
    val state: StateFlow<S> = _state.asStateFlow()

    /** Read-only snapshot for use inside [handleIntent]. */
    protected val currentState: S get() = _state.value

    // --- Effects: one-off events ---------------------------------------------
    // A Channel (not a StateFlow) so each effect is delivered exactly once and is
    // not "replayed" on configuration change.
    private val _effects = Channel<E>(Channel.BUFFERED)
    val effects: Flow<E> = _effects.receiveAsFlow()

    // --- Intents: the only way in --------------------------------------------
    // A Channel (not a SharedFlow) so intents are buffered and every one is delivered
    // to the single collector, even any sent before it starts. Intents must never drop.
    private val intents = Channel<I>(Channel.UNLIMITED)

    init {
        // Process intents one at a time, in order. This makes state transitions
        // deterministic and easy to reason about (and to test).
        intents.receiveAsFlow()
            .onEach { handleIntent(it) }
            .launchIn(viewModelScope)
    }

    /** The View's single entry point. Called from clicks, text changes, etc. */
    fun onIntent(intent: I) {
        intents.trySend(intent)
    }

    /**
     * The ONLY way state may change: apply a pure reducer to the current state.
     * `reduce` receives the old state as `this` and must return a new one.
     */
    protected fun setState(reduce: S.() -> S) {
        _state.update(reduce)
    }

    /** Emit a one-off effect. Fire-and-forget; never part of the rendered state. */
    protected fun sendEffect(effect: E) {
        viewModelScope.launch { _effects.send(effect) }
    }

    /** Turn a user intent into state changes and/or effects. Implemented per feature. */
    protected abstract suspend fun handleIntent(intent: I)
}
