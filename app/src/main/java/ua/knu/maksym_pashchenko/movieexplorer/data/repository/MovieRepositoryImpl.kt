package ua.knu.maksym_pashchenko.movieexplorer.data.repository

import ua.knu.maksym_pashchenko.movieexplorer.data.remote.MovieApi
import ua.knu.maksym_pashchenko.movieexplorer.data.remote.dto.MovieListResponseDto

class MovieRepositoryImpl(
    private val movieApi: MovieApi
): MovieRepository {
    override suspend fun getPopularMovies(page: Int): MovieListResponseDto {
        return movieApi.getPopularMovies(
            page = page
        )
    }
}