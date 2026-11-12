package ua.knu.maksym_pashchenko.movieexplorer.ui.screen.details.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import coil3.request.ImageRequest
import coil3.request.crossfade
import ua.knu.maksym_pashchenko.movieexplorer.R

@Composable
fun MovieBackdrop(
    backdropUrl: String?,
    title: String,
    modifier: Modifier = Modifier
) {
    val posterPlaceholder = painterResource(
        id = R.drawable.in_movie_placeholder
    )

    AsyncImage(
        model = ImageRequest.Builder(LocalContext.current)
            .data(backdropUrl)
            .crossfade(true)
            .build(),
        contentDescription = "Фон фільму $title",
        placeholder = posterPlaceholder,
        error = posterPlaceholder,
        fallback = posterPlaceholder,
        contentScale = ContentScale.Crop,
        modifier = modifier
            .fillMaxWidth()
            .height(220.dp)
    )
}