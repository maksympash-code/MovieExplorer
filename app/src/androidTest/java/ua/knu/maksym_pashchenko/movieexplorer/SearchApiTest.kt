package ua.knu.maksym_pashchenko.movieexplorer

import androidx.room.util.query
import androidx.test.ext.junit.runners.AndroidJUnit4
import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertTrue
import kotlinx.coroutines.runBlocking
import org.junit.Test
import org.junit.runner.RunWith
import ua.knu.maksym_pashchenko.movieexplorer.data.remote.RetrofitClient
import ua.knu.maksym_pashchenko.movieexplorer.data.repository.MovieRepositoryImpl

@RunWith(AndroidJUnit4::class)
class SearchApiTest {

    @Test
    fun searchMovies_apiReturnsResults() = runBlocking {
        assertTrue(BuildConfig.TMDB_ACCESS_TOKEN.isNotBlank())

        val response = RetrofitClient.api.searchMovies(
            query = "Interstellar"
        )

        assertEquals(1, response.page)
        assertTrue(response.results.isNotEmpty())
    }

    @Test
    fun searchMovies_repositoryReturnsMovies() = runBlocking {
        val repository = MovieRepositoryImpl(RetrofitClient.api)

        val movies = repository.searchMovies("Interstellar")

        assertTrue(movies.isNotEmpty())
        assertTrue(movies.all { it.id > 0 })
    }

    @Test
    fun searchMovies_blankQueryReturnsEmptyList() = runBlocking {
        val repository = MovieRepositoryImpl(RetrofitClient.api)

        val movies = repository.searchMovies("   ")

        assertTrue(movies.isEmpty())
    }
}