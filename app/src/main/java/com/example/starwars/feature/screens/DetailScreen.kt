package com.example.starwars.feature.screens

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.starwars.domain.model.SwApiType
import com.example.starwars.domain.model.SwItem
import com.example.starwars.domain.model.prettyName
import com.example.starwars.feature.swapi.SwapiIntent
import com.example.starwars.feature.swapi.SwapiViewModel

@Composable
fun DetailRoute(
    topic: SwApiType,
    itemId: String,
    onBack: () -> Unit,
    viewModel: SwapiViewModel,
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    LaunchedEffect(topic, itemId) {
        viewModel.onIntent(SwapiIntent.LoadDetails(topic, itemId))
    }

    // Only show the selected item once it matches the id we asked for (the shared VM may
    // still hold a previous selection while the new one loads).
    val item = state.selected?.takeIf { it.id == itemId && it.dataType == topic }

    DetailScreen(
        item = item,
        topic = topic,
        isLoading = state.isLoading || item == null,
        onBack = onBack,
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DetailScreen(
    item: SwItem?,
    topic: SwApiType,
    isLoading: Boolean,
    onBack: () -> Unit,
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(item?.text ?: "Detail") },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                },
            )
        },
    ) { padding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding),
        ) {
            when {
                isLoading && item == null ->
                    CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))

                item != null ->
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text("Name: ${item.text}")
                        Text("Topic: ${topic.prettyName()}")
                        Text("Id: ${item.id}")
                    }

                else ->
                    Text("Not found", modifier = Modifier.align(Alignment.Center))
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun DetailScreenPreview() {
    DetailScreen(
        item = SwItem("1", "Luke Skywalker", SwApiType.PEOPLE),
        topic = SwApiType.PEOPLE,
        isLoading = false,
        onBack = {},
    )
}
