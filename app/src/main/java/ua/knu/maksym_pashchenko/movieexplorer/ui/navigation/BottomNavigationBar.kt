package ua.knu.maksym_pashchenko.movieexplorer.ui.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import ua.knu.maksym_pashchenko.movieexplorer.R

@Composable
fun BottomNavigationBar(
    currentRoute: String?,
    onNavigate: (String) -> Unit
) {
    NavigationBar {
        NavigationBarItem(
            selected = currentRoute == Routes.MOVIES,
            onClick = {
                onNavigate(Routes.MOVIES)
            },
            icon = {
                Icon(
                    imageVector = Icons.Filled.Home,
                    contentDescription = null
                )
            },
            label = { Text(stringResource(R.string.main)) }
        )

        NavigationBarItem(
            selected = currentRoute == Routes.SEARCH,
            onClick = {
                onNavigate(Routes.SEARCH)
            },
            icon = {
                Icon(
                    imageVector = Icons.Filled.Search,
                    contentDescription = null
                )
            },
            label = { Text(stringResource(R.string.search)) }
        )

        NavigationBarItem(
            selected = currentRoute == Routes.FAVORITES,
            onClick = {
                onNavigate(Routes.FAVORITES)
            },
            icon = {
                Icon(
                    imageVector = if (currentRoute == Routes.FAVORITES) Icons.Filled.Favorite else Icons.Filled.FavoriteBorder,
                    contentDescription = null
                )
            },
            label = { Text(stringResource(R.string.favorites)) }
        )
    }
}