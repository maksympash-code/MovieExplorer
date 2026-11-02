package ua.knu.maksym_pashchenko.movieexplorer.ui.screen.details

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun MovieDetailsScreen(
    movieId: Int,
    uiState: MovieDetailsUiState,
    onBack: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Movie ID: $movieId",
            style = MaterialTheme.typography.titleMedium
        )

        Spacer(modifier = Modifier.height(16.dp))

        when (uiState) {
            MovieDetailsUiState.Loading ->  {
                CircularProgressIndicator()
            }

            is MovieDetailsUiState.Success -> {
                Text(
                    text = uiState.movie.title,
                    style = MaterialTheme.typography.headlineMedium
                )
            }

            is MovieDetailsUiState.Error -> {
                Text(
                    text = "Помилка: ${uiState.message}",
                    color = MaterialTheme.colorScheme.error
                )
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        Button(
            onClick = onBack
        ) {
            Text(
                text = "Назад"
            )
        }
    }
}