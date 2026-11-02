package ua.knu.maksym_pashchenko.movieexplorer.ui.screen.details

import ua.knu.maksym_pashchenko.movieexplorer.domain.model.MovieDetails

sealed interface MovieDetailsUiState {

    data object Loading : MovieDetailsUiState

    data class Success(
        val movie: MovieDetails
    ) : MovieDetailsUiState

    data class Error(
        val message: String
    ) : MovieDetailsUiState
}