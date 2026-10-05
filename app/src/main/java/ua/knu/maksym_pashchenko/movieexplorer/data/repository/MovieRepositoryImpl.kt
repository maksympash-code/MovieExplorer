package ua.knu.maksym_pashchenko.movieexplorer.data.repository

import ua.knu.maksym_pashchenko.movieexplorer.data.mapper.toMovie
import ua.knu.maksym_pashchenko.movieexplorer.data.remote.MovieApi
import ua.knu.maksym_pashchenko.movieexplorer.data.remote.dto.MovieListResponseDto
import ua.knu.maksym_pashchenko.movieexplorer.domain.model.Movie

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
}