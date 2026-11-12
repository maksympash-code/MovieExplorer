package ua.knu.maksym_pashchenko.movieexplorer.ui.common

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import ua.knu.maksym_pashchenko.movieexplorer.R
import ua.knu.maksym_pashchenko.movieexplorer.ui.theme.AppSpacing

@Composable
fun ErrorContent(
    message: String,
    modifier: Modifier = Modifier,
    onRetryClick: (() -> Unit)? = null
) {
    Box(
        modifier = modifier.fillMaxSize(),
        contentAlignment = Alignment.Center,
    ) {
        Column(
            modifier = Modifier.padding(AppSpacing.Medium),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(AppSpacing.Medium),

            ) {
            Text(
                text = message,
                color = MaterialTheme.colorScheme.error,
                style = MaterialTheme.typography.bodyLarge,
                textAlign = TextAlign.Center
            )

            if (onRetryClick != null) {
                Button(
                    onClick = onRetryClick
                ) {
                    Text(text = stringResource(R.string.retry))
                }
            }

        }

    }
}