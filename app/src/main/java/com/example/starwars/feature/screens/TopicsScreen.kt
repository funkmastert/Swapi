package com.example.starwars.feature.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.starwars.R
import com.example.starwars.core.ui.EffectHandler
import com.example.starwars.core.ui.titleRes
import com.example.starwars.domain.model.SwApiType
import com.example.starwars.feature.swapi.SwapiEffect
import com.example.starwars.feature.swapi.SwapiViewModel

@Composable
fun TopicsRoute(
    onTopicClick: (topicId: SwApiType) -> Unit,
    viewModel: SwapiViewModel,
) {
    EffectHandler(viewModel.effects) { effect ->
        when (effect) {
            is SwapiEffect.ShowError -> {
                // TODO surface the error (snackbar). Message: effect.message
            }
        }
    }

    // Each topic id maps to a SWAPI resource (/people, /films, …).
    TopicsScreen(onTopicClick, SwApiType.entries)
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopicsScreen(
    onTopicClick: (topicId: SwApiType) -> Unit,
    topics: List<SwApiType>
) {
    Scaffold { padding ->
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.fillMaxWidth().padding(padding)
        ) {
            Image(
                painter = painterResource(R.drawable.star_wars),
                contentDescription = null,
                modifier = Modifier.padding(vertical = 40.dp)
            )

            LazyColumn(
                modifier = Modifier.fillMaxWidth(),
            ) {
                items(topics) { topic ->
                    Text(
                        text = stringResource(topic.titleRes()),
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
    TopicsScreen(onTopicClick = {}, SwApiType.entries)
}
