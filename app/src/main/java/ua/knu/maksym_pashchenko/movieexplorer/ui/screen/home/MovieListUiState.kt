package ua.knu.maksym_pashchenko.movieexplorer.ui.screen.home

import ua.knu.maksym_pashchenko.movieexplorer.domain.model.Movie

sealed interface MovieListUiState {

    data object Loading: MovieListUiState

    data class Success(
        val movies: List<Movie>
    ) : MovieListUiState

    data class Error(
        val error: String
    ) : MovieListUiState
}