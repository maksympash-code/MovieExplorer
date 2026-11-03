package ua.knu.maksym_pashchenko.movieexplorer.data.repository

import ua.knu.maksym_pashchenko.movieexplorer.domain.model.Movie
import ua.knu.maksym_pashchenko.movieexplorer.domain.model.MovieDetails

interface MovieRepository {

    suspend fun getPopularMovies(
        page: Int = 1
    ): List<Movie>

    suspend fun getMovieDetails(
        movieId: Int
    ): MovieDetails

    suspend fun searchMovies(
        query: String
    ): List<Movie>
}