package ua.knu.maksym_pashchenko.movieexplorer.ui.screen.common

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import coil3.request.ImageRequest
import coil3.request.crossfade
import ua.knu.maksym_pashchenko.movieexplorer.R
import ua.knu.maksym_pashchenko.movieexplorer.domain.model.Movie
import ua.knu.maksym_pashchenko.movieexplorer.ui.theme.AppSpacing

@Composable
fun MovieCard(
    movie: Movie,
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {

    val posterPlaceholder = painterResource(
        id = R.drawable.in_movie_placeholder
    )

    Card(
        onClick = onClick,
        modifier = modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier.padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            AsyncImage(
                model = ImageRequest.Builder(LocalContext.current)
                    .data(movie.posterUrl?.takeIf { it.isNotBlank() })
                    .crossfade(true)
                    .build()
                ,
                contentDescription = "Постер фільму ${movie.title}",
                placeholder = posterPlaceholder,
                error = posterPlaceholder,
                fallback = posterPlaceholder,
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .width(100.dp)
                    .height(150.dp)
                    .clip(RoundedCornerShape(12.dp))
            )

            Spacer(modifier = Modifier.width(12.dp))

            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(
                    AppSpacing.Small
                )
            ) {
                Text(
                    text = movie.title,
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.onSurface,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )

                Text(
                    text = "⭐ ${"%.1f".format(movie.rating)}/10",
                    style = MaterialTheme.typography.bodyMedium
                )

                Text(
                    text = movie.releaseYear?.toString() ?: "Рік невідомий",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
    }

}