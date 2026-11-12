package ua.knu.maksym_pashchenko.movieexplorer.data.repositoryImpl

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import ua.knu.maksym_pashchenko.movieexplorer.data.local.dao.FavoriteMovieDao
import ua.knu.maksym_pashchenko.movieexplorer.data.mapper.toDomainMovie
import ua.knu.maksym_pashchenko.movieexplorer.data.mapper.toFavoriteMovieEntity
import ua.knu.maksym_pashchenko.movieexplorer.data.mapper.toMovie
import ua.knu.maksym_pashchenko.movieexplorer.data.mapper.toMovieDetails
import ua.knu.maksym_pashchenko.movieexplorer.data.remote.MovieApi
import ua.knu.maksym_pashchenko.movieexplorer.domain.model.Movie
import ua.knu.maksym_pashchenko.movieexplorer.domain.model.MovieDetails
import ua.knu.maksym_pashchenko.movieexplorer.domain.repository.MovieRepository

class MovieRepositoryImpl(
    private val movieApi: MovieApi,
    private val favoriteMovieDao: FavoriteMovieDao
) : MovieRepository {
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

    override suspend fun searchMovies(query: String): List<Movie> {

        val normalizedQuery = query.trim()

        if (normalizedQuery.isEmpty()) {
            return emptyList()
        }

        return movieApi
            .searchMovies(query = normalizedQuery)
            .results
            .map { movieDto ->
                movieDto.toMovie()
            }
    }

    override suspend fun addFavorite(movie: Movie) {
        favoriteMovieDao.insertMovie(movie.toFavoriteMovieEntity())
    }

    override suspend fun removeFavorite(movieId: Int) {
        favoriteMovieDao.deleteMovie(movieId)
    }

    override fun getFavorites(): Flow<List<Movie>> {
        return favoriteMovieDao
            .getFavoriteMovies()
            .map { movieList ->
                movieList.map { it.toDomainMovie() }
            }
    }

    override fun isFavorite(movieId: Int): Flow<Boolean> {
        return favoriteMovieDao.isFavorite(movieId)
    }
}