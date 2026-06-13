package com.example.starwars

import android.app.Application
import dagger.hilt.android.HiltAndroidApp

/** Hilt's entry point. Required for `@AndroidEntryPoint` / `@HiltViewModel` to work. */
@HiltAndroidApp
class StarWarsApp : Application()
