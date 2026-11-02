package ua.knu.maksym_pashchenko.movieexplorer.data.remote

import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query
import ua.knu.maksym_pashchenko.movieexplorer.data.remote.dto.MovieDetailsDto
import ua.knu.maksym_pashchenko.movieexplorer.data.remote.dto.MovieListResponseDto

interface MovieApi {

    @GET("movie/popular")
    suspend fun getPopularMovies(
        @Query("language") language: String = "uk-UA",
        @Query("page") page: Int = 1
    ): MovieListResponseDto

    @GET("movie/{movieId}")
    suspend fun getMovieDetails(
        @Path("movieId") movieId: Int,
        @Query("language") language: String = "uk-UA"
    ) : MovieDetailsDto
}