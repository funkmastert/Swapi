package com.example.starwars.core.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import com.example.starwars.feature.detail.DetailScreen
import com.example.starwars.feature.items.ItemsScreen
import com.example.starwars.feature.topics.TopicsScreen

/**
 * Single source of truth for the app's navigation graph: Topics → Items → Detail.
 *
 * Each screen receives only the lambdas it needs (navigate forward / go back); it has no
 * reference to the [NavHostController] itself, which keeps screens previewable and testable.
 */
@Composable
fun SwNavHost(
    navController: NavHostController = rememberNavController(),
) {
    NavHost(
        navController = navController,
        startDestination = Destination.Topics,
    ) {
        composable<Destination.Topics> {
            TopicsScreen(
                onTopicClick = { topicId ->
                    navController.navigate(Destination.Items(topicId))
                },
            )
        }

        composable<Destination.Items> { backStackEntry ->
            val route: Destination.Items = backStackEntry.toRoute()
            ItemsScreen(
                topicId = route.topicId,
                onItemClick = { itemId ->
                    navController.navigate(Destination.Detail(route.topicId, itemId))
                },
                onBack = navController::popBackStack,
            )
        }

        composable<Destination.Detail> { backStackEntry ->
            val route: Destination.Detail = backStackEntry.toRoute()
            DetailScreen(
                topicId = route.topicId,
                itemId = route.itemId,
                onBack = navController::popBackStack,
            )
        }
    }
}
