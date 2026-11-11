package ua.knu.maksym_pashchenko.movieexplorer.ui.screen.favorites

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import ua.knu.maksym_pashchenko.movieexplorer.domain.model.Movie
import ua.knu.maksym_pashchenko.movieexplorer.ui.common.EmptyContent
import ua.knu.maksym_pashchenko.movieexplorer.ui.common.ErrorContent
import ua.knu.maksym_pashchenko.movieexplorer.ui.common.LoadingContent
import ua.knu.maksym_pashchenko.movieexplorer.ui.screen.home.MovieCard

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
                    Text("Улюблені фільми")
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
                    FavoritesContent(
                        movies = uiState.movies,
                        onMovieClick = onMovieClick,
                        modifier = Modifier.padding(innerPadding)
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

@Composable
private fun FavoritesContent(
    movies: List<Movie>,
    onMovieClick: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
    LazyColumn(
        modifier = modifier.fillMaxSize()
    ) {
        items(
            items = movies,
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