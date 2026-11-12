package ua.knu.maksym_pashchenko.movieexplorer.ui.screen.details.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import coil3.request.ImageRequest
import coil3.request.crossfade
import ua.knu.maksym_pashchenko.movieexplorer.R
import ua.knu.maksym_pashchenko.movieexplorer.domain.model.MovieDetails

@Composable
fun MovieInfo(
    movie: MovieDetails,
    modifier: Modifier = Modifier
) {
    val releaseDate = movie.releaseDate
        ?.takeIf { it.isNotBlank() }
        ?: "Невідома"

    val posterPlaceholder = painterResource(
        id = R.drawable.in_movie_placeholder
    )

    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(16.dp),
        horizontalArrangement = Arrangement.spacedBy(16.dp),
        verticalAlignment = Alignment.Top
    ) {
        AsyncImage(
            model = ImageRequest.Builder(LocalContext.current)
                .data(movie.posterUrl?.takeIf { it.isNotBlank() })
                .crossfade(true)
                .build(),
            contentDescription = "Постер фільму ${movie.title}",
            placeholder = posterPlaceholder,
            error = posterPlaceholder,
            fallback = posterPlaceholder,
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .width(110.dp)
                .height(165.dp)
                .clip(RoundedCornerShape(12.dp)),
        )

        Column(
            modifier = Modifier.weight(1f),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            Text(
                text = movie.title,
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Bold
            )

            Text(
                text = "⭐ ${"%.1f".format(movie.rating)} / 10",
                style = MaterialTheme.typography.titleMedium
            )

            Text(
                text = "Дата виходу: $releaseDate",
                style = MaterialTheme.typography.bodyMedium
            )
        }
    }
}