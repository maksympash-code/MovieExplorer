package ua.knu.maksym_pashchenko.movieexplorer.ui.screen.details

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import ua.knu.maksym_pashchenko.movieexplorer.domain.model.MovieDetails
import ua.knu.maksym_pashchenko.movieexplorer.ui.common.ErrorContent
import ua.knu.maksym_pashchenko.movieexplorer.ui.common.LoadingContent
import ua.knu.maksym_pashchenko.movieexplorer.ui.screen.details.components.MovieBackdrop
import ua.knu.maksym_pashchenko.movieexplorer.ui.screen.details.components.MovieInfo
import ua.knu.maksym_pashchenko.movieexplorer.ui.screen.details.components.MovieOverview
import ua.knu.maksym_pashchenko.movieexplorer.ui.theme.AppSpacing

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

    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
    ) {
        MovieBackdrop(
            backdropUrl = backdropUrl,
            title = movie.title
        )

        MovieInfo(movie = movie)

        MovieOverview(overview = movie.overview)

        Spacer(modifier = Modifier.height(AppSpacing.ExtraLarge))
    }
}