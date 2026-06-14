# StarWars — an MVI SWAPI browser

A small, deliberately-complete example of the **MVI (Model–View–Intent)** pattern on
modern Android (Kotlin 2, Jetpack Compose, Coroutines/Flow, Hilt). It browses the
[Star Wars API](https://swapi.info/api) — pick a topic (People, Films, Planets, …), see its
items, tap one for full detail — and is built to be *read*: the core files are commented to
explain the role they play.

## The one idea behind MVI

**State flows in a single direction, and the screen is a pure function of one
immutable state object.**

```
        ┌─────────────────────── unidirectional data flow ───────────────────────┐
        │                                                                         │
        ▼                                                                         │
   ┌─────────┐   Intent    ┌───────────────┐   Change   ┌──────────┐   State     │
   │  View   │ ──────────▶ │   ViewModel   │ ─────────▶ │ Reducer  │ ─────────▶  │
   │(Compose)│             │ handleIntent  │  (pure)    │  (pure)  │   (new)     │
   └─────────┘             └───────┬───────┘            └──────────┘             │
        ▲                          │                                             │
        │  render(state) ──────────┼─────────────────────────────────────────────┘
        │                          │
        │        Effect (one-off)  │   e.g. show an error / navigate
        └──────────────────────────┘
```

- **Intent** — what the user wants (`LoadTopic(topic)`, `RefreshTopic(topic)`,
  `LoadDetails(topic, id)`). The *only* thing the View sends to the ViewModel.
- **Model / State** — one immutable `data class` the View renders. Want to reproduce a
  bug? Reproduce the state.
- **Reducer** — a pure `(State, Change) -> State` function. The single place the next
  state is decided. No I/O, no Android → trivially unit-testable.
- **Effect** — a one-off event (error message, navigation). Kept *out* of state so it fires
  exactly once and never re-triggers after a rotation.

Side effects (DB/network) happen in the ViewModel; their *results* come back in as a
`Change` and go through the reducer. The View never does anything but render state and
emit intents.

## Where each concept lives

| Concept | File |
|---|---|
| Reusable MVI base | `core/mvi/MviViewModel.kt`, `core/mvi/MviContracts.kt` |
| **Intent / State / Effect** | `feature/swapi/SwapiContract.kt` |
| **Reducer** (pure) | `feature/swapi/SwapiReducer.kt` |
| ViewModel (wires it all) | `feature/swapi/SwapiViewModel.kt` |
| **Views** (render state, emit intents) | `feature/screens/{Topics,Items,Detail}Screen.kt` |
| Effect collection | `core/ui/EffectHandler.kt` |
| Type-safe navigation | `core/navigation/Destination.kt`, `core/navigation/SwNavHost.kt` |
| Domain (framework-free) | `domain/model/`, `domain/repository/` |
| Data (network + Room cache) | `data/remote/`, `data/local/`, `data/repository/SwRepository.kt` |
| DI wiring | `di/NetworkModule.kt`, `di/DatabaseModule.kt`, `di/RepositoryModule.kt` |
| Tests | `app/src/test/...` |

## A trip through one interaction

User opens a topic:

1. `SwNavHost` navigates to `Destination.Items(topic)`; on arrival `ItemsRoute`'s
   `LaunchedEffect` fires `onIntent(SwapiIntent.LoadTopic(topic))`.
2. `SwapiViewModel.handleIntent` calls `repository.getTopic(topic)` — **cache-first**: it
   returns the Room-cached rows if present, otherwise fetches from SWAPI and caches them.
3. The result comes back as `SwapiChange.TopicLoaded` → `SwapiReducer` → new `SwapiState`.
4. `state` (a `StateFlow`) emits; Compose recomposes `ItemsScreen` with the list.

Pull-to-refresh sends `RefreshTopic`, which re-fetches past the cache. Tapping an item
navigates to `Detail`, which lazily fetches *that one* record (and caches it). A failed
fetch sends a one-off `ShowError` effect via the `EffectHandler`.

## What's in here beyond MVI

- **Type-safe Navigation Compose** — `@Serializable` destinations (Navigation 2.8+); routes
  are typed objects, not strings.
- **Room as a cache** — SWAPI's data is finite/immutable, so it's cached once and served
  locally thereafter (`data/local/`). Pull-to-refresh forces a re-fetch.
- **Coil images** — SWAPI has no images, so they're pulled from `starwars-visualguide.com`
  by id, with the app artwork as a fallback for gaps.
- **Externalized strings** — all user-facing text is in `strings.xml`; detail attribute
  labels are carried through the data layer as a stable `SwAttrKey` enum and resolved to
  localized strings in the UI.

## Why this is nice to work with

- **Predictable** — one state in, one screen out. No scattered `mutableStateOf`.
- **Testable** — the reducer is pure (zero mocks); the ViewModel is deterministic.
- **Robust to lifecycle** — state survives via `StateFlow`; effects are one-shot, so no
  duplicate messages on rotation; screens reload from their nav args after process death.
- **Layered** — `domain` knows nothing about Android; the data source is swappable behind
  `SwapiRepository` (change one line in `RepositoryModule`).

## Running it

Open the folder in **Android Studio** (Ladybug or newer) and hit Run — it will sync
Gradle and build. From the command line you need the Gradle wrapper jar; generate it
once with a local Gradle install:

```bash
cd ~/StarWars
gradle wrapper                # creates gradlew + the wrapper jar
./gradlew testDebugUnitTest   # runs the unit tests
./gradlew installDebug        # builds & installs on a connected device/emulator
```

## Things to try (to cement the pattern)

1. Add a derived value on `SwapiState` (e.g. item count) — compute it *from* state, never
   store it separately.
2. Enrich detail with a field the list doesn't return — the lazy per-item fetch is already
   the place for it.
3. Add an offline/error banner driven by the `ShowError` effect.
