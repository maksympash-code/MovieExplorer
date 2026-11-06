package ua.knu.maksym_pashchenko.movieexplorer

import androidx.room.Room
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertFalse
import junit.framework.TestCase.assertTrue
import kotlinx.coroutines.cancelAndJoin
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withTimeout
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import ua.knu.maksym_pashchenko.movieexplorer.data.local.dao.FavoriteMovieDao
import ua.knu.maksym_pashchenko.movieexplorer.data.local.database.MovieDatabase
import ua.knu.maksym_pashchenko.movieexplorer.data.local.entity.FavoriteMovieEntity
import kotlin.time.Duration.Companion.milliseconds


@RunWith(AndroidJUnit4::class)
class FavoriteMovieDaoTest {

    private lateinit var database: MovieDatabase
    private lateinit var dao: FavoriteMovieDao

    @Before
    fun createDatabase() {
        val context = InstrumentationRegistry
            .getInstrumentation()
            .targetContext

        database = Room.inMemoryDatabaseBuilder(
            context,
            MovieDatabase::class.java
        ).build()

        dao = database.favoriteMovieDao()
    }

    @After
    fun closeDatabase() {
        database.close()
    }

    @Test
    fun insertAndReadFavorite() = runBlocking {
        val movie = testMovie()

        dao.insertMovie(movie)

        val favorites = dao.getFavoriteMovies().first()

        assertEquals(1, favorites.size)
        assertEquals(movie, favorites.first())
    }

    @Test
    fun insertWithSameIdReplacesMovie() = runBlocking {
        val movie = testMovie()

        dao.insertMovie(movie)
        dao.insertMovie(
            movie.copy(title = "Оновлена назва")
        )

        val favorites = dao.getFavoriteMovies().first()

        assertEquals(1, favorites.size)
        assertEquals("Оновлена назва", favorites.first().title)
    }

    @Test
    fun deleteFavoriteUpdatesDatabase() = runBlocking {
        val movie = testMovie()

        assertFalse(dao.isFavorite(movie.id).first())

        dao.insertMovie(movie)

        assertTrue(dao.isFavorite(movie.id).first())

        dao.deleteMovie(movie)

        assertFalse(dao.isFavorite(movie.id).first())
        assertTrue(dao.getFavoriteMovies().first().isEmpty())
    }

    @Test
    fun updateFavoriteMovies() = runBlocking {
        val movie = testMovie()

        val channel = Channel<List<FavoriteMovieEntity>> (
            Channel.UNLIMITED
        )

        val job = launch {
            dao.getFavoriteMovies()
                .collect { movies ->
                    channel.send(movies)
                }
        }

        try {
            val firstFavorites = withTimeout(5_000.milliseconds) {
                channel.receive()
            }

            assertEquals(0, firstFavorites.size)

            dao.insertMovie(movie)

            val secondFavorites = withTimeout(5_000.milliseconds) {
                channel.receive()
            }

            assertEquals(1, secondFavorites.size)
            assertEquals("Interstellar", secondFavorites[0].title)

            dao.insertMovie(
                FavoriteMovieEntity(
                    id = 157331,
                    title = "Superman",
                    posterPath = "/test-poster_1.jpg",
                    rating = 8.0,
                    overview = "Тестовий опис"
                )
            )

            val thirdFavorites = withTimeout(5_000.milliseconds) {
                channel.receive()
            }

            assertEquals(2, thirdFavorites.size)
            assertEquals("Interstellar", thirdFavorites[0].title)
            assertEquals("Superman", thirdFavorites[1].title)
        } finally {
            job.cancelAndJoin()
            channel.close()
        }
    }

    @Test
    fun updateFavoriteIsFavorite() = runBlocking {
        val movie = testMovie()

        val channel  = Channel<Boolean> (
            Channel.UNLIMITED
        )

        val job = launch {
            dao.isFavorite(movie.id)
                .collect { isFavorite ->
                    channel.send(isFavorite)
                }
        }

        try {
            val firstCheck = withTimeout(5_000.milliseconds) {
                channel.receive()
            }

            assertEquals(false, firstCheck)

            dao.insertMovie(movie)

            val secondCheck = withTimeout(5_000.milliseconds) {
                channel.receive()
            }

            assertEquals(true, secondCheck)

            dao.deleteMovie(movie)

            val thirdCheck = withTimeout(5_000.milliseconds) {
                channel.receive()
            }

            assertEquals(false, thirdCheck)

        } finally {
            job.cancelAndJoin()
            channel.close()
        }
    }

    private fun testMovie() = FavoriteMovieEntity(
        id = 157336,
        title = "Interstellar",
        posterPath = "/test-poster.jpg",
        rating = 8.5,
        overview = "Тестовий опис"
    )
}