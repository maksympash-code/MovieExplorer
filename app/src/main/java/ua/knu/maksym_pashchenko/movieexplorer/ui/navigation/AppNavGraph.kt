package ua.knu.maksym_pashchenko.movieexplorer.ui.navigation

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import ua.knu.maksym_pashchenko.movieexplorer.ui.screen.home.MovieListScreen
import ua.knu.maksym_pashchenko.movieexplorer.ui.screen.home.MovieListUiState

@Composable
fun AppNavGraph(
    uiState: MovieListUiState
) {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = Routes.MOVIES
    ) {
        composable(Routes.MOVIES) {
            Column(
                modifier = Modifier.fillMaxSize()
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Button(
                        onClick = {
                            navController.navigate(Routes.SEARCH)
                        }
                    ) {
                        Text(text = "Пошук")
                    }

                    Button(
                        onClick = {
                            navController.navigate(Routes.FAVORITES)
                        }
                    ) {
                        Text(text = "Улюблені")
                    }
                }

                Box(modifier = Modifier.weight(1f)) {
                    MovieListScreen(
                        uiState = uiState,
                        onMovieClick = { movieId ->
                            navController.navigate(
                                Routes.movieDetails(movieId)
                            )
                        }
                    )
                }
            }
        }

        composable(
            route = Routes.MOVIE_DETAILS,
            arguments = listOf(
                navArgument("movieId") {
                    type = NavType.IntType
                }
            )
        ) { backStackEntry ->
            val movieId = requireNotNull(
                backStackEntry.arguments?.getInt("movieId")
            )

            PlaceholderScreen(
                title = "Деталі фільму ID: $movieId",
                onBack = { navController.popBackStack() }
            )
        }

        composable(Routes.SEARCH) {
            PlaceholderScreen(
                title = "Пошук фільмів",
                onBack = { navController.popBackStack() }
            )
        }

        composable(Routes.FAVORITES) {
            PlaceholderScreen(
                title = "Улюблені фільми",
                onBack = { navController.popBackStack() }
            )
        }
    }
}


@Composable
private fun PlaceholderScreen(
    title: String,
    onBack: () -> Unit
) {
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Text(text = title)

        Button(
            onClick = onBack
        ) {
            Text("Назад")
        }
    }
}