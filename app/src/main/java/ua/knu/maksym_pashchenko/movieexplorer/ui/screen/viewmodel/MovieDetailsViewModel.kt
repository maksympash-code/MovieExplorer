package ua.knu.maksym_pashchenko.movieexplorer.ui.screen.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import ua.knu.maksym_pashchenko.movieexplorer.data.repository.MovieRepository
import ua.knu.maksym_pashchenko.movieexplorer.ui.screen.details.MovieDetailsUiState

class MovieDetailsViewModel(
    private val repository: MovieRepository,
    private val movieId: Int
) : ViewModel() {

    private val _uiState = MutableStateFlow<MovieDetailsUiState>(MovieDetailsUiState.Loading)

    val uiState: StateFlow<MovieDetailsUiState> = _uiState.asStateFlow()

    fun loadMovieDetails() {
        viewModelScope.launch {
            _uiState.value = MovieDetailsUiState.Loading

            try {
                val movie = repository.getMovieDetails(movieId)

                _uiState.value = MovieDetailsUiState.Success(movie = movie)
            } catch (e: CancellationException) {
                throw e
            } catch (e: Exception) {
                _uiState.value = MovieDetailsUiState.Error(
                    message = e.message ?: "Не вдалося завантажити деталі фільму"
                )
            }
        }
    }

    init {
        loadMovieDetails()
    }
}