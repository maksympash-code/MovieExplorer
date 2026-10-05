package ua.knu.maksym_pashchenko.movieexplorer.data.repository

import ua.knu.maksym_pashchenko.movieexplorer.domain.model.Movie

interface MovieRepository {

    suspend fun getPopularMovies(
        page: Int = 1
    ): List<Movie>
}