package ua.knu.maksym_pashchenko.movieexplorer.data.remote

import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query
import ua.knu.maksym_pashchenko.movieexplorer.data.remote.dto.MovieDetailsDto
import ua.knu.maksym_pashchenko.movieexplorer.data.remote.dto.MovieListResponseDto

private const val DEFAULT_LANGUAGE = "uk-UA"
interface MovieApi {

    @GET("movie/popular")
    suspend fun getPopularMovies(
        @Query("language") language: String = DEFAULT_LANGUAGE,
        @Query("page") page: Int = 1
    ): MovieListResponseDto

    @GET("movie/{movieId}")
    suspend fun getMovieDetails(
        @Path("movieId") movieId: Int,
        @Query("language") language: String = DEFAULT_LANGUAGE
    ): MovieDetailsDto

    @GET("search/movie")
    suspend fun searchMovies(
        @Query("query") query: String,
        @Query("language") language: String = DEFAULT_LANGUAGE,
        @Query("page") page: Int = 1
    ): MovieListResponseDto
}