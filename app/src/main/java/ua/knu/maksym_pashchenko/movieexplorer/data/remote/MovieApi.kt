package ua.knu.maksym_pashchenko.movieexplorer.data.remote

import retrofit2.http.GET
import retrofit2.http.Query
import ua.knu.maksym_pashchenko.movieexplorer.data.remote.dto.MovieListResponseDto

interface MovieApi {

    @GET("movie/popular")
    suspend fun getPopularMovies(
        @Query("language") language: String = "en-US",
        @Query("page") page: Int = 1
    ): MovieListResponseDto
}