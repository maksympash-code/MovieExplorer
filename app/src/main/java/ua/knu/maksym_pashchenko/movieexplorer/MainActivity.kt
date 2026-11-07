package ua.knu.maksym_pashchenko.movieexplorer

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.getValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.CreationExtras
import androidx.room.Room
import androidx.sqlite.driver.AndroidSQLiteDriver
import ua.knu.maksym_pashchenko.movieexplorer.data.local.database.MovieDatabase
import ua.knu.maksym_pashchenko.movieexplorer.data.remote.RetrofitClient
import ua.knu.maksym_pashchenko.movieexplorer.data.repository.MovieRepositoryImpl
import ua.knu.maksym_pashchenko.movieexplorer.ui.navigation.AppNavGraph
import ua.knu.maksym_pashchenko.movieexplorer.ui.screen.viewmodel.MovieListViewModel
import ua.knu.maksym_pashchenko.movieexplorer.ui.theme.MovieExplorerTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val database =
            Room.databaseBuilder<MovieDatabase>(
                context = applicationContext,
                "MovieDatabase"
            )
                .setDriver(AndroidSQLiteDriver())
                .build()

        val favoriteMovieDao = database.favoriteMovieDao()

        val repository = MovieRepositoryImpl(
            RetrofitClient.api,
            favoriteMovieDao
        )

        val factory = object : ViewModelProvider.Factory {
            override fun <T : ViewModel> create(
                modelClass: Class<T>,
                extras: CreationExtras
            ): T {
                require(
                    modelClass.isAssignableFrom(
                        MovieListViewModel::class.java
                    )
                )

                @Suppress("UNCHECKED_CAST")
                return MovieListViewModel(repository) as T
            }
        }

        val viewModel = ViewModelProvider(
            this,
            factory
        )[MovieListViewModel::class.java]

        enableEdgeToEdge()
        setContent {
            MovieExplorerTheme {
                val uiState by viewModel.uiState.collectAsStateWithLifecycle()

                AppNavGraph(
                    uiState = uiState,
                    repository = repository
                )
            }
        }
    }
}