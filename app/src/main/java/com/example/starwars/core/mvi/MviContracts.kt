package com.example.starwars.core.mvi

/**
 * The three pillars of MVI (Model–View–Intent). Every feature defines its own
 * sealed implementations of these. They are marker interfaces so the base
 * [MviViewModel] can be generic and type-safe.
 *
 *  - [MviIntent]  what the user wants to do          (View -> ViewModel)
 *  - [MviState]   the single immutable source of truth that the View renders
 *  - [MviEffect]  one-off side effects that are NOT state (navigation, snackbars)
 *
 * The golden rule: data flows in ONE direction.
 *
 *      View --Intent--> ViewModel --reduce--> State --render--> View
 *                              \--Effect--> View (consumed once)
 */
interface MviIntent

/** Immutable. The View is a pure function of this. Reproduce a bug by reproducing the state. */
interface MviState

/**
 * A one-shot event. Unlike state, an effect must be handled exactly once and is gone
 * (e.g. "show a snackbar", "navigate"). Modelling these as state causes classic bugs
 * like a snackbar re-appearing after a rotation, so they live in a separate stream.
 */
interface MviEffect
