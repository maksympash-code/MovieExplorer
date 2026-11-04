package ua.knu.maksym_pashchenko.movieexplorer

import androidx.room.Room
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertFalse
import junit.framework.TestCase.assertTrue
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import ua.knu.maksym_pashchenko.movieexplorer.data.local.dao.FavoriteMovieDao
import ua.knu.maksym_pashchenko.movieexplorer.data.local.database.MovieDatabase
import ua.knu.maksym_pashchenko.movieexplorer.data.local.entity.FavoriteMovieEntity
import ua.knu.maksym_pashchenko.movieexplorer.data.repository.MovieRepository
import ua.knu.maksym_pashchenko.movieexplorer.domain.model.Movie


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

        dao.insertFavorite(movie)

        val favorites = dao.getAllFavorites().first()

        assertEquals(1, favorites.size)
        assertEquals(movie, favorites.first())
    }

    @Test
    fun insertWithSameIdReplacesMovie() = runBlocking {
        val movie = testMovie()

        dao.insertFavorite(movie)
        dao.insertFavorite(
            movie.copy(title = "Оновлена назва")
        )

        val favorites = dao.getAllFavorites().first()

        assertEquals(1, favorites.size)
        assertEquals("Оновлена назва", favorites.first().title)
    }

    @Test
    fun deleteFavoriteUpdatesDatabase() = runBlocking {
        val movie = testMovie()

        assertFalse(dao.isFavorite(movie.id).first())

        dao.insertFavorite(movie)

        assertTrue(dao.isFavorite(movie.id).first())

        dao.deleteFavorite(movie)

        assertFalse(dao.isFavorite(movie.id).first())
        assertTrue(dao.getAllFavorites().first().isEmpty())
    }

    private fun testMovie() = FavoriteMovieEntity(
        id = 157336,
        title = "Interstellar",
        posterPath = "/test-poster.jpg",
        rating = 8.5,
        overview = "Тестовий опис"
    )
}