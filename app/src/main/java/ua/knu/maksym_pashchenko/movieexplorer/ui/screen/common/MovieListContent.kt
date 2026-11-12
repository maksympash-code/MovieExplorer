package ua.knu.maksym_pashchenko.movieexplorer.ui.screen.common

import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import ua.knu.maksym_pashchenko.movieexplorer.domain.model.Movie
import ua.knu.maksym_pashchenko.movieexplorer.ui.theme.AppSpacing

@Composable
fun MovieListContent(
    movies: List<Movie>,
    onMovieClick: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
    LazyColumn(
        modifier = modifier
    ) {
        items(
            items = movies,
            key = { it.id }
        ) { movie ->
            MovieCard(
                movie = movie,
                modifier = Modifier.padding(
                    horizontal = AppSpacing.Medium,
                    vertical = 6.dp
                ),
                onClick = {
                    onMovieClick(movie.id)
                }
            )
        }
    }
}