package com.example.starwars.feature.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.starwars.R
import com.example.starwars.core.ui.EffectHandler
import com.example.starwars.core.ui.titleRes
import com.example.starwars.domain.model.SwApiType
import com.example.starwars.domain.model.SwItem
import com.example.starwars.feature.swapi.SwapiEffect
import com.example.starwars.feature.swapi.SwapiIntent
import com.example.starwars.feature.swapi.SwapiViewModel

@Composable
fun ItemsRoute(
    onItemClick: (itemId: String) -> Unit,
    onBack: () -> Unit,
    topic: SwApiType,
    viewModel: SwapiViewModel,
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    // Load this topic on arrival. Keyed on `topic` so it re-fires if the topic changes,
    // and re-runs after process death since `topic` comes from the nav back stack.
    LaunchedEffect(topic) {
        viewModel.onIntent(SwapiIntent.LoadTopic(topic))
    }

    EffectHandler(viewModel.effects) { effect ->
        when (effect) {
            is SwapiEffect.ShowError -> {
                // TODO surface the error (snackbar). Message: effect.message
            }
        }
    }

    ItemsScreen(
        topic = topic,
        items = state.items,
        isLoading = state.isLoading,
        isRefreshing = state.isRefreshing,
        onRefresh = { viewModel.onIntent(SwapiIntent.RefreshTopic(topic)) },
        onItemClick = onItemClick,
        onBack = onBack,
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ItemsScreen(
    topic: SwApiType,
    items: List<SwItem>,
    isLoading: Boolean,
    isRefreshing: Boolean,
    onRefresh: () -> Unit,
    onItemClick: (itemId: String) -> Unit,
    onBack: () -> Unit,
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(stringResource(topic.titleRes())) },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(
                            Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = stringResource(R.string.cd_back),
                        )
                    }
                },
            )
        },
    ) { padding ->
        PullToRefreshBox(
            isRefreshing = isRefreshing,
            onRefresh = onRefresh,
            modifier = Modifier
                .fillMaxSize()
                .padding(padding),
        ) {
            if (isLoading) {
                CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
            } else {
                LazyColumn(modifier = Modifier.fillMaxSize()) {
                    items(items, key = { it.id }) { item ->
                        Text(
                            text = item.text,
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable { onItemClick(item.id) }
                                .padding(16.dp),
                        )
                        HorizontalDivider()
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun ItemsScreenPreview() {
    ItemsScreen(
        topic = SwApiType.PEOPLE,
        items = listOf(
            SwItem("1", "Luke Skywalker", SwApiType.PEOPLE),
            SwItem("2", "C-3PO", SwApiType.PEOPLE),
        ),
        isLoading = false,
        isRefreshing = false,
        onRefresh = {},
        onItemClick = {},
        onBack = {},
    )
}
