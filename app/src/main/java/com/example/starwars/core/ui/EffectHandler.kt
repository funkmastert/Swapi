package com.example.starwars.core.ui

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.repeatOnLifecycle
import kotlinx.coroutines.flow.Flow

/**
 * Collects a ViewModel's one-off [effects] in a lifecycle-aware way and runs
 * [onEffect] for each. Collection is paused below STARTED, so an effect emitted
 * while the screen is in the background is delivered when it returns — and it is
 * delivered exactly once (no duplicate snackbars after rotation).
 */
@Composable
fun <E> EffectHandler(
    effects: Flow<E>,
    onEffect: suspend (E) -> Unit,
) {
    val lifecycleOwner = LocalLifecycleOwner.current
    LaunchedEffect(effects, lifecycleOwner) {
        lifecycleOwner.lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
            effects.collect { onEffect(it) }
        }
    }
}
