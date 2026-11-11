package ua.knu.maksym_pashchenko.movieexplorer.ui.screen.home

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import ua.knu.maksym_pashchenko.movieexplorer.ui.common.EmptyContent
import ua.knu.maksym_pashchenko.movieexplorer.ui.common.ErrorContent
import ua.knu.maksym_pashchenko.movieexplorer.ui.common.LoadingContent

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
                LazyColumn(
                    modifier = Modifier.fillMaxSize()
                ) {
                    items(
                        items = uiState.movies,
                        key = { it.id }
                    ) { movie ->
                        MovieCard(
                            movie = movie,
                            modifier = Modifier.padding(
                                horizontal = 12.dp,
                                vertical = 6.dp
                            ),
                            onClick = {
                                onMovieClick(movie.id)
                            }
                        )
                    }
                }
            }

        }
    }
}