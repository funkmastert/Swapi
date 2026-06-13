package com.example.starwars

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.example.starwars.core.navigation.SwNavHost
import com.example.starwars.ui.theme.StarWarsTheme
import dagger.hilt.android.AndroidEntryPoint

/** Single-activity host. `@AndroidEntryPoint` lets Hilt inject into composables below. */
@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            StarWarsTheme {
                SwNavHost()
            }
        }
    }
}
