package ua.knu.maksym_pashchenko.movieexplorer.data.remote

import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.kotlinx.serialization.asConverterFactory
import ua.knu.maksym_pashchenko.movieexplorer.BuildConfig

object RetrofitClient {

    private const val BASE_URL = "https://api.themoviedb.org/3/"

    private val json = Json {
        ignoreUnknownKeys = true
    }

    private val okHttpClient = OkHttpClient.Builder()
        .addInterceptor { chain ->

            val request = chain.request()
                .newBuilder()
                .header(
                    "Authorization",
                    "Bearer ${BuildConfig.TMDB_ACCESS_TOKEN}",
                )
                .header(
                    "Accept",
                    "application/json"
                ).build()

            chain.proceed(request)
        }
        .build()

    val api: MovieApi by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(
                json.asConverterFactory(
                    "application/json".toMediaType()
                )
            )
            .build()
            .create(MovieApi::class.java)
    }
}