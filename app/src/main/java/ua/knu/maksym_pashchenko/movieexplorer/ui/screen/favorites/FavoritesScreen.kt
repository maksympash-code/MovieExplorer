package ua.knu.maksym_pashchenko.movieexplorer.ui.screen.favorites

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import ua.knu.maksym_pashchenko.movieexplorer.R
import ua.knu.maksym_pashchenko.movieexplorer.ui.common.EmptyContent
import ua.knu.maksym_pashchenko.movieexplorer.ui.common.ErrorContent
import ua.knu.maksym_pashchenko.movieexplorer.ui.common.LoadingContent
import ua.knu.maksym_pashchenko.movieexplorer.ui.screen.common.MovieListContent

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun  FavoritesScreen(
    uiState: FavoritesUiState,
    onMovieClick: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
    Scaffold(
        modifier = modifier,
        topBar = {
            TopAppBar(
                title = {
                    Text(text = stringResource(R.string.favorites_title))
                }
            )
        }
    ) { innerPadding ->

        when (uiState) {
            FavoritesUiState.Loading -> {
                LoadingContent(
                    modifier = Modifier.padding(innerPadding)
                )
            }

            is FavoritesUiState.Success -> {
                if (uiState.movies.isEmpty()) {
                    EmptyContent(
                        message = "Поки немає улюблених фільмів",
                        modifier = Modifier.padding(innerPadding)
                    )
                } else {
                    MovieListContent(
                        movies = uiState.movies,
                        onMovieClick = onMovieClick,
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(innerPadding)
                    )
                }
            }

            is FavoritesUiState.Error -> {
                ErrorContent(
                    message = uiState.message,
                    modifier = Modifier.padding(innerPadding)
                )
            }
        }
    }
}