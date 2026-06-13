package com.example.starwars.feature.topics

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.starwars.R
import com.example.starwars.core.ui.EffectHandler
import com.example.starwars.feature.swapi.SwapiEffect
import com.example.starwars.feature.swapi.SwapiViewModel

@Composable
fun TopicsRoute(
    onTopicClick: (topicId: String) -> Unit,
    viewModel: SwapiViewModel = hiltViewModel(),
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    EffectHandler(viewModel.effects) { effect ->
        when (effect) {
            is SwapiEffect.ShowUndoDelete -> {

            }
        }
    }

    TopicsScreen(onTopicClick)
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopicsScreen(
    onTopicClick: (topicId: String) -> Unit,
) {
    // Each topic id maps to a SWAPI resource (/people, /films, …).
    val topics = listOf("people", "films", "planets", "species", "starships", "vehicles")

    Scaffold { padding ->
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.fillMaxWidth()
        ) {
            Image(
                painter = painterResource(R.drawable.star_wars),
                contentDescription = null,
                modifier = Modifier.padding(vertical = 40.dp)
            )

            LazyColumn(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(padding),
            ) {
                items(topics) { topic ->
                    Text(
                        text = topic.replaceFirstChar { it.uppercase() },
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { onTopicClick(topic) }
                            .padding(16.dp),
                    )
                    HorizontalDivider()
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun TopicsScreenPreview() {
    TopicsScreen(onTopicClick = {})
}
