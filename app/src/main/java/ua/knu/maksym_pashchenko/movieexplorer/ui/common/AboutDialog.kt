package ua.knu.maksym_pashchenko.movieexplorer.ui.common


import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import ua.knu.maksym_pashchenko.movieexplorer.R
import ua.knu.maksym_pashchenko.movieexplorer.ui.theme.AppSpacing

@Composable
fun AboutDialog(
    onDismiss: () -> Unit
) {
    AlertDialog (
        onDismissRequest = onDismiss,
        title = {
            Text(text = stringResource(R.string.app_name))
        },
        text = {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(AppSpacing.Large)
            ) {
                Text(text = stringResource(R.string.about_app))
                Image(
                    painter = painterResource(R.drawable.tmdb_logo),
                    contentDescription = "TMDB Logo",
                    modifier = Modifier
                        .width(120.dp)
                        .height(60.dp),
                    contentScale = ContentScale.Fit
                )
            }

        },
        confirmButton = {
            TextButton(
                onClick = onDismiss
            ) {
                Text(text = stringResource(R.string.close))
            }
        }
    )
}
