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
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.CreationExtras
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import ua.knu.maksym_pashchenko.movieexplorer.data.repository.MovieRepository
import ua.knu.maksym_pashchenko.movieexplorer.ui.screen.details.MovieDetailsScreen
import ua.knu.maksym_pashchenko.movieexplorer.ui.screen.favorites.FavoritesScreen
import ua.knu.maksym_pashchenko.movieexplorer.ui.screen.home.MovieListScreen
import ua.knu.maksym_pashchenko.movieexplorer.ui.screen.home.MovieListUiState
import ua.knu.maksym_pashchenko.movieexplorer.ui.screen.search.SearchScreen
import ua.knu.maksym_pashchenko.movieexplorer.ui.screen.viewmodel.FavoritesViewModel
import ua.knu.maksym_pashchenko.movieexplorer.ui.screen.viewmodel.MovieDetailsViewModel
import ua.knu.maksym_pashchenko.movieexplorer.ui.screen.viewmodel.SearchViewModel

@Composable
fun AppNavGraph(
    uiState: MovieListUiState,
    repository: MovieRepository,
    onRetryClick: () -> Unit
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
                        },
                        onRetryClick = onRetryClick
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

            val factory = object : ViewModelProvider.Factory {
                override fun <T : ViewModel> create(
                    modelClass: Class<T>,
                    extras: CreationExtras
                ): T {
                    require(
                        modelClass.isAssignableFrom(
                            MovieDetailsViewModel::class.java
                        )
                    )

                    @Suppress("UNCHECKED_CAST")
                    return MovieDetailsViewModel(
                        repository = repository,
                        movieId = movieId
                    ) as T
                }
            }

            val detailsViewModel = ViewModelProvider(
                backStackEntry,
                factory
            )[MovieDetailsViewModel::class.java]

            val detailsUiState by detailsViewModel.uiState.collectAsStateWithLifecycle()
            val detailsIsFavorite by detailsViewModel.isFavorite.collectAsStateWithLifecycle()
            val detailsIsFavoriteUpdating by detailsViewModel.isFavoriteUpdating.collectAsStateWithLifecycle()
            val detailsFavoriteError by detailsViewModel.favoriteError.collectAsStateWithLifecycle()

            MovieDetailsScreen(
                uiState = detailsUiState,
                isFavorite = detailsIsFavorite,
                isFavoriteUpdating = detailsIsFavoriteUpdating,
                favoriteError = detailsFavoriteError,
                onBackClick = { navController.popBackStack() },
                onFavoriteClick = { detailsViewModel.onFavoriteClick() },
                onFavoriteClearError = { detailsViewModel.clearFavoriteError() },
                onRetryClick = { detailsViewModel.loadMovieDetails() }
            )
        }

        composable(Routes.SEARCH) { backStackEntry ->

            val factory = object : ViewModelProvider.Factory {
                override fun <T : ViewModel> create(
                    modelClass: Class<T>,
                    extras: CreationExtras
                ): T {
                    require(
                        modelClass.isAssignableFrom(
                            SearchViewModel::class.java
                        )
                    )

                    @Suppress("UNCHECKED_CAST")
                    return SearchViewModel(
                        repository = repository
                    ) as T
                }
            }

            val searchViewModel = ViewModelProvider(
                backStackEntry,
                factory
            )[SearchViewModel::class.java]

            val searchUiState by searchViewModel.uiState.collectAsStateWithLifecycle()
            val query by searchViewModel.query.collectAsStateWithLifecycle()

            SearchScreen(
                query = query,
                uiState = searchUiState,
                onQueryChanged = { newQuery ->
                    searchViewModel.onQueryChange(newQuery)
                },
                onMovieClick = { movieId ->
                    navController.navigate(
                        Routes.movieDetails(movieId)
                    )
                },
                onBackClick = {
                    navController.popBackStack()
                }
            )
        }

        composable(Routes.FAVORITES) { backStackEntry ->

            val factory = object : ViewModelProvider.Factory {
                override fun <T : ViewModel> create(
                    modelClass: Class<T>,
                    extras: CreationExtras
                ): T {
                    require(
                        modelClass.isAssignableFrom(
                            FavoritesViewModel::class.java
                        )
                    )

                    @Suppress("UNCHECKED_CAST")
                    return FavoritesViewModel(
                        repository = repository
                    ) as T
                }
            }

            val favoritesViewModel = ViewModelProvider(
                backStackEntry,
                factory
            )[FavoritesViewModel::class.java]

            val favoritesUiState by favoritesViewModel.uiState.collectAsStateWithLifecycle()

            FavoritesScreen(
                uiState = favoritesUiState,
                onBackClick = { navController.popBackStack() },
                onMovieClick = { movieId ->
                    navController.navigate(
                        Routes.movieDetails(movieId)
                    )
                }
            )
        }
    }
}