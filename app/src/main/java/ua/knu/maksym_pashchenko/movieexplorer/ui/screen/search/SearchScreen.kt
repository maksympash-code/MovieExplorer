package ua.knu.maksym_pashchenko.movieexplorer.ui.screen.search

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import ua.knu.maksym_pashchenko.movieexplorer.ui.common.EmptyContent
import ua.knu.maksym_pashchenko.movieexplorer.ui.common.ErrorContent
import ua.knu.maksym_pashchenko.movieexplorer.ui.common.LoadingContent
import ua.knu.maksym_pashchenko.movieexplorer.ui.screen.home.MovieCard

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchScreen(
    query: String,
    uiState: SearchUiState,
    onQueryChanged: (String) -> Unit,
    onMovieClick: (Int) -> Unit,
    modifier: Modifier = Modifier
) {

    Scaffold(
        modifier = modifier,
        topBar = {
            TopAppBar(
                title = {
                    Text("Пошук фільмів")
                }
            )
        }
    ) { innerPadding ->

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(
                        horizontal = 16.dp,
                        vertical = 8.dp
                    )
            ) {
                OutlinedTextField(
                    value = query,
                    onValueChange = onQueryChanged,
                    label = {
                        Text("Назва фільму")
                    },
                    placeholder = {
                        Text("Введіть назву фільму")
                    },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )
            }

            when (uiState) {

                SearchUiState.Idle -> {
                    SearchMessage(
                        message = "Введіть назву фільму для пошуку",
                        modifier = Modifier.weight(1f)
                    )
                }

                SearchUiState.Loading -> {
                    LoadingContent(
                        modifier = Modifier.weight(1f)
                    )
                }

                is SearchUiState.Success -> {
                    LazyColumn(
                        modifier = Modifier
                            .fillMaxWidth()
                            .weight(1f)
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

                SearchUiState.Empty -> {
                    EmptyContent(
                        message = "За вашим запитом нічого не знайдено",
                        modifier = Modifier.weight(1f)
                    )
                }

                is SearchUiState.Error -> {
                    ErrorContent(
                        message = "Помилка: ${uiState.message}",
                        modifier = Modifier.weight(1f)
                    )
                }
            }
        }
    }
}


@Composable
private fun SearchMessage(
    message: String,
    modifier: Modifier = Modifier,
) {
    Box(
        modifier = modifier.fillMaxWidth(),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = message,
            modifier = Modifier.padding(16.dp),
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            style = MaterialTheme.typography.bodyLarge,
            textAlign = TextAlign.Center
        )
    }
}