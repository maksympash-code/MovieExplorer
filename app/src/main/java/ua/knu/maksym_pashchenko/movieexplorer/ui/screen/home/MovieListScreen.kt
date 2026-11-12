package ua.knu.maksym_pashchenko.movieexplorer.ui.screen.home

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import ua.knu.maksym_pashchenko.movieexplorer.ui.common.EmptyContent
import ua.knu.maksym_pashchenko.movieexplorer.ui.common.ErrorContent
import ua.knu.maksym_pashchenko.movieexplorer.ui.common.LoadingContent
import ua.knu.maksym_pashchenko.movieexplorer.ui.screen.common.MovieListContent

@Composable
fun MovieListScreen(
    uiState: MovieListUiState,
    onMovieClick: (Int) -> Unit,
    onRetryClick: () -> Unit
) {
    when (uiState) {
        MovieListUiState.Loading -> {
            LoadingContent()
        }

        is MovieListUiState.Error -> {
            ErrorContent(
                message = uiState.message,
                onRetryClick = onRetryClick
            )
        }

        is MovieListUiState.Success -> {
            if (uiState.movies.isEmpty()) {
                EmptyContent(
                    message = "Наразі немає доступних фільмів"
                )
            } else {
                MovieListContent(
                    movies = uiState.movies,
                    onMovieClick = onMovieClick,
                    modifier = Modifier.fillMaxSize()
                )
            }

        }
    }
}