package com.example.starwars.feature.screens

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
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
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.AsyncImage
import com.example.starwars.R
import com.example.starwars.core.ui.labelRes
import com.example.starwars.core.ui.titleRes
import com.example.starwars.domain.model.SwApiType
import com.example.starwars.domain.model.SwAttrKey
import com.example.starwars.domain.model.SwAttribute
import com.example.starwars.domain.model.SwDetail
import com.example.starwars.domain.model.imageUrlFor
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

    // Only show the loaded detail once it matches what we asked for (the shared VM may still
    // hold a previous detail while the new one loads).
    val detail = state.detail?.takeIf { it.id == itemId && it.type == topic }

    DetailScreen(
        detail = detail,
        topic = topic,
        isLoading = detail == null,
        onBack = onBack,
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DetailScreen(
    detail: SwDetail?,
    topic: SwApiType,
    isLoading: Boolean,
    onBack: () -> Unit,
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(detail?.title ?: stringResource(topic.titleRes())) },
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
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding),
        ) {
            when {
                detail != null ->
                    LazyColumn(modifier = Modifier.fillMaxSize()) {
                        item {
                            AsyncImage(
                                model = imageUrlFor(detail.type, detail.id),
                                contentDescription = detail.title,
                                contentScale = ContentScale.Crop,
                                // visualguide.com has gaps; fall back to the app artwork.
                                error = painterResource(R.drawable.star_wars),
                                placeholder = painterResource(R.drawable.star_wars),
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(280.dp),
                            )
                        }
                        items(detail.attributes) { attribute ->
                            AttributeRow(attribute)
                            HorizontalDivider()
                        }
                    }

                isLoading ->
                    CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))

                else ->
                    Text(
                        stringResource(R.string.label_not_found),
                        modifier = Modifier.align(Alignment.Center),
                    )
            }
        }
    }
}

@Composable
private fun AttributeRow(attribute: SwAttribute) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 12.dp),
    ) {
        Text(
            text = stringResource(attribute.key.labelRes()),
            modifier = Modifier.weight(1f),
            fontWeight = FontWeight.Medium,
        )
        Text(
            text = attribute.value,
            modifier = Modifier.weight(1f),
            textAlign = TextAlign.End,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun DetailScreenPreview() {
    DetailScreen(
        detail = SwDetail(
            id = "1",
            type = SwApiType.PEOPLE,
            title = "Luke Skywalker",
            attributes = listOf(
                SwAttribute(SwAttrKey.HEIGHT, "172"),
                SwAttribute(SwAttrKey.MASS, "77"),
                SwAttribute(SwAttrKey.BIRTH_YEAR, "19BBY"),
            ),
        ),
        topic = SwApiType.PEOPLE,
        isLoading = false,
        onBack = {},
    )
}
