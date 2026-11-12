package ua.knu.maksym_pashchenko.movieexplorer.domain.repository

import kotlinx.coroutines.flow.Flow
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

    suspend fun addFavorite(movie: Movie)

    suspend fun removeFavorite(movieId: Int)

    fun getFavorites() : Flow<List<Movie>>

    fun isFavorite(movieId: Int) : Flow<Boolean>
}