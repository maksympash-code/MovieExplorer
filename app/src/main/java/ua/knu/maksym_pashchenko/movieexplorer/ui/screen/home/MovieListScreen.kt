package ua.knu.maksym_pashchenko.movieexplorer.ui.screen.home

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun MovieListScreen(
    uiState: MovieListUiState,
    onMovieClick: (Int) -> Unit
) {
    when (uiState) {
        MovieListUiState.Loading -> {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator()
            }
        }

        is MovieListUiState.Error -> {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "Помилка: ${uiState.message}"
                )
            }
        }

        is MovieListUiState.Success -> {
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