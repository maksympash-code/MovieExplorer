package ua.knu.maksym_pashchenko.movieexplorer.data.repository

import ua.knu.maksym_pashchenko.movieexplorer.data.mapper.toMovie
import ua.knu.maksym_pashchenko.movieexplorer.data.mapper.toMovieDetails
import ua.knu.maksym_pashchenko.movieexplorer.data.remote.MovieApi
import ua.knu.maksym_pashchenko.movieexplorer.domain.model.Movie
import ua.knu.maksym_pashchenko.movieexplorer.domain.model.MovieDetails

class MovieRepositoryImpl(
    private val movieApi: MovieApi
): MovieRepository {
    override suspend fun getPopularMovies(page: Int): List<Movie> {
        return movieApi
            .getPopularMovies(page = page)
            .results
            .map { movieDto ->
                movieDto.toMovie()
            }
    }

    override suspend fun getMovieDetails(movieId: Int): MovieDetails {
        return movieApi
            .getMovieDetails(movieId)
            .toMovieDetails()
    }
}