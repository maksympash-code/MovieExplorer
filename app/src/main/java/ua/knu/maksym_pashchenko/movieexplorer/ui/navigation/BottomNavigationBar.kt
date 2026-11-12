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
            label = { Text("Головна") }
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
            label = { Text("Пошук") }
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
            label = { Text("Улюблені") }
        )
    }
}