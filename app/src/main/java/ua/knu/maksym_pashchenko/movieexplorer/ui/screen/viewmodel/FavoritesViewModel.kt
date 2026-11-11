package ua.knu.maksym_pashchenko.movieexplorer.ui.screen.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import ua.knu.maksym_pashchenko.movieexplorer.data.repository.MovieRepository
import ua.knu.maksym_pashchenko.movieexplorer.ui.common.toUserMessage
import ua.knu.maksym_pashchenko.movieexplorer.ui.screen.favorites.FavoritesUiState

class FavoritesViewModel(
    private val repository: MovieRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow<FavoritesUiState>(FavoritesUiState.Loading)
    val uiState : StateFlow<FavoritesUiState> = _uiState.asStateFlow()

    private fun observeFavorites() {
        viewModelScope.launch {
            try {
                repository.getFavorites().collect { movies ->
                    _uiState.value = FavoritesUiState.Success(movies)
                }
            } catch (e : CancellationException) {
                throw e
            } catch (e : Exception) {
                _uiState.value = FavoritesUiState.Error(e.toUserMessage())
            }

        }
    }

    init {
        observeFavorites()
    }
}