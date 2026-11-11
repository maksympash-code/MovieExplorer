package ua.knu.maksym_pashchenko.movieexplorer.ui.screen.details

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import coil3.request.ImageRequest
import coil3.request.crossfade
import ua.knu.maksym_pashchenko.movieexplorer.R
import ua.knu.maksym_pashchenko.movieexplorer.domain.model.MovieDetails
import ua.knu.maksym_pashchenko.movieexplorer.ui.common.ErrorContent
import ua.knu.maksym_pashchenko.movieexplorer.ui.common.LoadingContent

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MovieDetailsScreen(
    uiState: MovieDetailsUiState,
    isFavorite: Boolean?,
    isFavoriteUpdating: Boolean,
    favoriteError: String?,
    onBackClick: () -> Unit,
    onFavoriteClick: () -> Unit,
    onFavoriteClearError: () -> Unit,
    onRetryClick: () -> Unit,
    modifier: Modifier = Modifier
) {

    val snackbarHostState = remember { SnackbarHostState() }

    LaunchedEffect(favoriteError) {
        if (favoriteError != null) {
            snackbarHostState.showSnackbar(favoriteError)
            onFavoriteClearError()
        }
    }

    Scaffold(
        modifier = modifier,
        topBar = {
            TopAppBar(
                title = {
                    Text("Деталі фільму")
                },
                navigationIcon = {
                    IconButton(
                        onClick = onBackClick
                    ) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Назад"
                        )
                    }
                },
                actions = {
                    IconButton(
                        onClick = onFavoriteClick,
                        enabled = (isFavorite != null && !isFavoriteUpdating && uiState is MovieDetailsUiState.Success)
                    ) {
                        Icon(
                            imageVector = if (isFavorite == true) Icons.Filled.Favorite else Icons.Filled.FavoriteBorder,
                            contentDescription = when (isFavorite) {
                                null -> {
                                    "Завантаження статусу улюбленого"
                                }

                                true -> {
                                    "Видалити з улюблених"
                                }

                                false -> {
                                    "Додати до улюблених"
                                }
                            }
                        )
                    }
                }
            )
        },
        snackbarHost = { SnackbarHost(hostState = snackbarHostState) }
    ) { innerPadding ->

        when (uiState) {
            MovieDetailsUiState.Loading -> {
                LoadingContent(
                    modifier = Modifier.padding(innerPadding)
                )
            }

            is MovieDetailsUiState.Success -> {
                MovieDetailsContent(
                    movie = uiState.movie,
                    modifier = Modifier.padding(innerPadding)
                )
            }

            is MovieDetailsUiState.Error -> {
                ErrorContent(
                    message = uiState.message,
                    modifier = Modifier.padding(innerPadding),
                    onRetryClick = onRetryClick
                )
            }
        }
    }
}

@Composable
private fun MovieDetailsContent(
    movie: MovieDetails,
    modifier: Modifier = Modifier
) {

    val backdropUrl = movie.backdropUrl
        ?.takeIf { it.isNotBlank() }
        ?: movie.posterUrl?.takeIf { it.isNotBlank() }

    val releaseDate = movie.releaseDate
        ?.takeIf { it.isNotBlank() }
        ?: "Невідома"

    val posterPlaceholder = painterResource(
        id = R.drawable.in_movie_placeholder
    )

    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
    ) {
        // 1. Backdrop
        AsyncImage(
            model = ImageRequest.Builder(LocalContext.current)
                .data(backdropUrl)
                .crossfade(true)
                .build(),
            contentDescription = "Фон фільму ${movie.title}",
            placeholder = posterPlaceholder,
            error = posterPlaceholder,
            fallback = posterPlaceholder,
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .fillMaxWidth()
                .height(220.dp),

            )

        // 2. Poster + movie information
        Row(
            modifier = Modifier
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

        // 3. Overview
        Column(
            modifier = Modifier.padding(horizontal = 16.dp)
        ) {
            Text(
                text = "Опис",
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = movie.overview.ifBlank {
                    "Опис фільму відсутній"
                },
                style = MaterialTheme.typography.bodyLarge
            )
        }

        Spacer(modifier = Modifier.height(24.dp))
    }
}