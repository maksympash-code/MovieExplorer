package ua.knu.maksym_pashchenko.movieexplorer.ui.screen.search

import ua.knu.maksym_pashchenko.movieexplorer.domain.model.Movie

sealed interface SearchUiState {

    data object Idle : SearchUiState

    data object Loading : SearchUiState

    data class Success(
        val movies: List<Movie>
    ) : SearchUiState

    data object Empty : SearchUiState

    data class Error(
        val message: String
    ) : SearchUiState
}