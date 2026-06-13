package com.example.starwars.feature.swapi

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.SnackbarResult
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.starwars.R
import com.example.starwars.core.ui.EffectHandler

@Composable
fun SwapiRoute(
    viewModel: SwapiViewModel = hiltViewModel(),
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    EffectHandler(viewModel.effects) { effect ->
        when (effect) {
            is SwapiEffect.ShowUndoDelete -> {

            }
        }
    }

    SwapiScreen(
        state = state,
        onIntent = viewModel::onIntent
    )
}

@OptIn(ExperimentalMaterial3Api::class) // Material3 TopAppBar is still experimental
@Composable
fun SwapiScreen(
    state: SwapiState,
    onIntent: (SwapiIntent) -> Unit,
) {
    Scaffold(
        topBar = {
            TopAppBar(title = { Text("MVI Todos") })
        },
    ) { padding ->
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
        ) {
            Image(
                painter = painterResource(R.drawable.star_wars),
                contentDescription = null,
                modifier = Modifier.padding(top = 40.dp)
            )
        }
    }
}


@Preview(showBackground = true)
@Composable
private fun SwapiScreenPreview() {
    SwapiScreen(
        state = SwapiState(
            isLoading = false,
        ),
        onIntent = {}
    )
}
