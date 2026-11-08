package ua.knu.maksym_pashchenko.movieexplorer.ui.screen.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import ua.knu.maksym_pashchenko.movieexplorer.data.mapper.toMovie
import ua.knu.maksym_pashchenko.movieexplorer.data.repository.MovieRepository
import ua.knu.maksym_pashchenko.movieexplorer.ui.screen.details.MovieDetailsUiState

class MovieDetailsViewModel(
    private val repository: MovieRepository,
    private val movieId: Int
) : ViewModel() {

    private val _uiState = MutableStateFlow<MovieDetailsUiState>(MovieDetailsUiState.Loading)

    val uiState: StateFlow<MovieDetailsUiState> = _uiState.asStateFlow()

    private val _isFavorite = MutableStateFlow<Boolean?>(null)

    val isFavorite: StateFlow<Boolean?> = _isFavorite.asStateFlow()

    private val _isFavoriteUpdating = MutableStateFlow<Boolean> (false)

    val isFavoriteUpdating = _isFavoriteUpdating.asStateFlow()

    private val _favoriteError = MutableStateFlow<String?> (null)

    val favoriteError = _favoriteError.asStateFlow()

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

    fun onFavoriteClick() {
        if (_isFavoriteUpdating.value) {
            return
        }

        _isFavoriteUpdating.value = true
        _favoriteError.value = null

        viewModelScope.launch {
            try {
                val state = _isFavorite.value ?: return@launch

                val movieDetails = when (val movieDetailsState = _uiState.value) {
                    is MovieDetailsUiState.Success -> {
                        movieDetailsState.movie
                    }
                    else -> {
                        return@launch
                    }
                }

                when (state) {
                    true -> {
                        repository.removeFavorite(movieId)
                    }
                    false -> {
                        repository.addFavorite(movieDetails.toMovie())
                    }
                }
            } catch (e: CancellationException) {
                throw e
            } catch (e: Exception) {
                _favoriteError.value = e.message ?: "Не вдалося змінити статус улюбленого фільму"
            } finally {
                _isFavoriteUpdating.value = false
            }

        }
    }

    fun clearFavoriteError() {
        _favoriteError.value = null
    }

    private fun observeFavoriteStatus() {
        viewModelScope.launch {
            repository.isFavorite(movieId).collect { state ->
                _isFavorite.value = state
            }
        }
    }


    init {
        loadMovieDetails()
        observeFavoriteStatus()
    }
}