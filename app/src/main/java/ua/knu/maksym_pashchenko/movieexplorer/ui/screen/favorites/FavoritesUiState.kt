package ua.knu.maksym_pashchenko.movieexplorer.ui.screen.favorites

import ua.knu.maksym_pashchenko.movieexplorer.domain.model.Movie

sealed interface FavoritesUiState {

    data object Loading : FavoritesUiState

    data class Success(
        val movies: List<Movie>
    ) : FavoritesUiState

    data class Error(
        val message: String
    ) : FavoritesUiState
}