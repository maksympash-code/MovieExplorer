package ua.knu.maksym_pashchenko.movieexplorer.ui.navigation

import androidx.compose.foundation.layout.consumeWindowInsets
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.CreationExtras
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppNavGraph(
    uiState: MovieListUiState,
    repository: MovieRepository,
    onRetryClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val navController = rememberNavController()

    val backStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute: String? = backStackEntry?.destination?.route

    val showBottomBar = currentRoute in setOf(
        Routes.MOVIES,
        Routes.SEARCH,
        Routes.FAVORITES
    )

    fun navigateToTab(route: String) {
        navController.navigate(route) {
            popUpTo(navController.graph.findStartDestination().id) {
                saveState = true
            }
            launchSingleTop = true
            restoreState = true
        }
    }

    Scaffold(
        modifier = modifier,
        topBar = {
            if (currentRoute == Routes.MOVIES) {
                TopAppBar(
                    title = {
                        Text(text = "Довідник фільмів",)
                    }
                )
            }
        },
        bottomBar = {
            if (showBottomBar) {
                NavigationBar {
                    NavigationBarItem(
                        selected = currentRoute == Routes.MOVIES,
                        onClick = {
                            navigateToTab(Routes.MOVIES)
                        },
                        icon = {
                            Icon(
                                imageVector = Icons.Filled.Home,
                                contentDescription = null
                            )
                        },
                        label = { Text("Головна") }
                    )

                    NavigationBarItem(
                        selected = currentRoute == Routes.SEARCH,
                        onClick = {
                            navigateToTab(Routes.SEARCH)
                        },
                        icon = {
                            Icon(
                                imageVector = Icons.Filled.Search,
                                contentDescription = null
                            )
                        },
                        label = { Text("Пошук") }
                    )

                    NavigationBarItem(
                        selected = currentRoute == Routes.FAVORITES,
                        onClick = {
                            navigateToTab(Routes.FAVORITES)
                        },
                        icon = {
                            Icon(
                                imageVector = if (currentRoute == Routes.FAVORITES) Icons.Filled.Favorite else Icons.Filled.FavoriteBorder,
                                contentDescription = null
                            )
                        },
                        label = { Text("Улюблені") }
                    )
                }
            }
        }
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = Routes.MOVIES,
            modifier = Modifier
                .padding(innerPadding)
                .consumeWindowInsets(innerPadding)
        ) {

            composable(Routes.MOVIES) {
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
                    onMovieClick = { movieId ->
                        navController.navigate(
                            Routes.movieDetails(movieId)
                        )
                    }
                )
            }
        }
    }
}

