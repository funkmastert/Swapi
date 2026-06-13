# StarWars — an MVI Android Todo app

A small, deliberately-complete example of the **MVI (Model–View–Intent)** pattern on
modern Android (Kotlin 2, Jetpack Compose, Coroutines/Flow, Hilt). It's built to be
*read* — every core file is commented to explain the role it plays.

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
        │        Effect (one-off)  │   e.g. show snackbar / navigate
        └──────────────────────────┘
```

- **Intent** — what the user wants (`AddClicked`, `ToggleClicked(id)`, …). The *only*
  thing the View sends to the ViewModel.
- **Model / State** — one immutable `data class` the View renders. Want to reproduce a
  bug? Reproduce the state.
- **Reducer** — a pure `(State, Change) -> State` function. The single place the next
  state is decided. No I/O, no Android → trivially unit-testable.
- **Effect** — a one-off event (snackbar, navigation). Kept *out* of state so it fires
  exactly once and never re-triggers after a rotation.

Side effects (DB/network) happen in the ViewModel; their *results* come back in as a
`Change` and go through the reducer. The View never does anything but render state and
emit intents.

## Where each concept lives

| MVI concept        | File |
|--------------------|------|
| Reusable MVI base  | `core/mvi/MviViewModel.kt`, `core/mvi/MviContracts.kt` |
| **Intent / State / Effect** | `feature/todos/TodosContract.kt` |
| **Reducer** (pure) | `feature/todos/TodosReducer.kt` |
| ViewModel (wires it all) | `feature/todos/TodosViewModel.kt` |
| **View** (renders state, emits intents) | `feature/todos/TodosScreen.kt` |
| Effect collection  | `core/ui/EffectHandler.kt` |
| Domain (framework-free) | `domain/model/`, `domain/repository/` |
| Data (swappable source) | `data/repository/InMemoryTodoRepository.kt` |
| DI wiring          | `di/RepositoryModule.kt` |
| Tests (reducer + VM) | `app/src/test/.../feature/todos/` |

## A trip through one interaction

User types and taps **＋**:

1. `TextField` → `onIntent(TodosIntent.InputChanged("Buy milk"))`, then the button →
   `onIntent(TodosIntent.AddClicked)`.
2. `TodosViewModel.handleIntent` calls `repository.add("Buy milk")` and clears the input.
3. The repository's `observeTodos()` Flow re-emits the new list →
   `TodosChange.TodosLoaded` → `TodosReducer` → new `TodosState`.
4. `state` (a `StateFlow`) emits; Compose recomposes `TodosScreen` with the new list.

Delete shows the same loop **plus** a one-off effect: `handleIntent` calls
`sendEffect(ShowUndoDelete(...))`, the `EffectHandler` shows a snackbar, and tapping
*Undo* sends `UndoDeleteClicked` back in — closing the loop the MVI way.

## Why this is nice to work with

- **Predictable** — one state in, one screen out. No scattered `mutableStateOf`.
- **Testable** — the reducer is pure (`TodosReducerTest` has zero mocks); the ViewModel
  is deterministic (`TodosViewModelTest`).
- **Robust to lifecycle** — state survives via `StateFlow`; effects are one-shot, so no
  duplicate snackbars on rotation.
- **Layered** — `domain` knows nothing about Android; the data source is swappable
  behind `TodoRepository` (change one line in `RepositoryModule`).

## Running it

Open the folder in **Android Studio** (Ladybug or newer) and hit Run — it will sync
Gradle and build. From the command line you need the Gradle wrapper jar; generate it
once with a local Gradle install:

```bash
cd ~/StarWars
gradle wrapper            # creates gradlew + the wrapper jar
./gradlew testDebugUnitTest   # runs the reducer + view-model tests
./gradlew installDebug        # builds & installs on a connected device/emulator
```

## Things to try (to cement the pattern)

1. Add an `EditTodo(id, text)` intent + change and a corresponding reducer case.
2. Add a `Stats` derived value (e.g. "% complete") — note you compute it *from* state,
   never store it.
3. Swap `InMemoryTodoRepository` for a Room-backed one — only `RepositoryModule` and the
   `data` layer change; the ViewModel/View don't move.
