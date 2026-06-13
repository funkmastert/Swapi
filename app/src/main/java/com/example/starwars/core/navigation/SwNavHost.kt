package com.example.starwars.core.navigation

import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import com.example.starwars.feature.screens.DetailRoute
import com.example.starwars.feature.screens.ItemsRoute
import com.example.starwars.feature.screens.TopicsRoute
import com.example.starwars.feature.swapi.SwapiViewModel

/**
 * Single source of truth for the app's navigation graph: Topics → Items → Detail.
 *
 * Navigation is pure routing here — each screen's Route loads its own data on arrival via a
 * LaunchedEffect keyed on its nav arguments, so the load survives config change / process death.
 */
@Composable
fun SwNavHost(
    navController: NavHostController = rememberNavController(),
    viewModel: SwapiViewModel = hiltViewModel(),
) {
    NavHost(
        navController = navController,
        startDestination = Destination.Topics,
    ) {
        composable<Destination.Topics> {
            TopicsRoute(
                onTopicClick = { topic ->
                    navController.navigate(Destination.Items(topic))
                },
                viewModel = viewModel,
            )
        }

        composable<Destination.Items> { backStackEntry ->
            val route: Destination.Items = backStackEntry.toRoute()
            ItemsRoute(
                topic = route.topicId,
                onItemClick = { itemId ->
                    navController.navigate(Destination.Detail(route.topicId, itemId))
                },
                onBack = navController::popBackStack,
                viewModel = viewModel,
            )
        }

        composable<Destination.Detail> { backStackEntry ->
            val route: Destination.Detail = backStackEntry.toRoute()
            DetailRoute(
                topic = route.topicId,
                itemId = route.itemId,
                onBack = navController::popBackStack,
                viewModel = viewModel,
            )
        }
    }
}
