package ua.knu.maksym_pashchenko.movieexplorer.data.repository

import ua.knu.maksym_pashchenko.movieexplorer.data.remote.dto.MovieListResponseDto

interface MovieRepository {

    suspend fun getPopularMovies(
        page: Int = 1
    ): MovieListResponseDto
}