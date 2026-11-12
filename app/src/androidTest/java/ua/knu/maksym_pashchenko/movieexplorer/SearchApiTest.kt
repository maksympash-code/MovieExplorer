package ua.knu.maksym_pashchenko.movieexplorer

import androidx.room.Room
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertTrue
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import ua.knu.maksym_pashchenko.movieexplorer.data.local.dao.FavoriteMovieDao
import ua.knu.maksym_pashchenko.movieexplorer.data.local.database.MovieDatabase
import ua.knu.maksym_pashchenko.movieexplorer.data.remote.RetrofitClient
import ua.knu.maksym_pashchenko.movieexplorer.data.repositoryImpl.MovieRepositoryImpl

@RunWith(AndroidJUnit4::class)
class SearchApiTest {

    private lateinit var database: MovieDatabase
    private lateinit var dao: FavoriteMovieDao

    @Before
    fun createDatabase() {
        val context = InstrumentationRegistry
            .getInstrumentation()
            .targetContext

        database = Room.inMemoryDatabaseBuilder(
            context = context,
            MovieDatabase::class.java
        ).build()

        dao = database.favoriteMovieDao()
    }

    @After
    fun closeDatabase() {
        database.close()
    }

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
        val repository = MovieRepositoryImpl(
            RetrofitClient.api,
            dao
            )

        val movies = repository.searchMovies("Interstellar")

        assertTrue(movies.isNotEmpty())
        assertTrue(movies.all { it.id > 0 })
    }

    @Test
    fun searchMovies_blankQueryReturnsEmptyList() = runBlocking {
        val repository = MovieRepositoryImpl(
            RetrofitClient.api,
            dao
        )

        val movies = repository.searchMovies("   ")

        assertTrue(movies.isEmpty())
    }
}