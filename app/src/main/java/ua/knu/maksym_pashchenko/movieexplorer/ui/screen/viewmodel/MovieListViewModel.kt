package ua.knu.maksym_pashchenko.movieexplorer.ui.screen.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import ua.knu.maksym_pashchenko.movieexplorer.data.repository.MovieRepository
import ua.knu.maksym_pashchenko.movieexplorer.ui.screen.home.MovieListUiState

class MovieListViewModel(
    private val repository: MovieRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow<MovieListUiState>(MovieListUiState.Loading)
    val uiState: StateFlow<MovieListUiState> = _uiState

    fun loadMovies() {

        viewModelScope.launch {
            _uiState.value = MovieListUiState.Loading

            try {
                val movies = repository.getPopularMovies()

                _uiState.value = MovieListUiState.Success(movies)

            } catch (e: Exception) {

                _uiState.value = MovieListUiState.Error(
                    e.message ?: "Unknown error"
                )

            }
        }
    }

    init {
        loadMovies()
    }
}