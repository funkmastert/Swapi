package com.example.starwars.feature.items

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

/**
 * The items inside one topic. Receives only the [topicId]; a real implementation's ViewModel
 * would use it to fetch the matching list from the repository.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ItemsScreen(
    topicId: String,
    onItemClick: (itemId: String) -> Unit,
    onBack: () -> Unit,
) {
    // Placeholder: pretend each topic has a handful of items keyed by id.
    val items = (1..10).map { it.toString() }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(topicId.replaceFirstChar { it.uppercase() }) },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                },
            )
        },
    ) { padding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding),
        ) {
            items(items) { itemId ->
                Text(
                    text = "$topicId #$itemId",
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { onItemClick(itemId) }
                        .padding(16.dp),
                )
                HorizontalDivider()
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun ItemsScreenPreview() {
    ItemsScreen(topicId = "people", onItemClick = {}, onBack = {})
}
