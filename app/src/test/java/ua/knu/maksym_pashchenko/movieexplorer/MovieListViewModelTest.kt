package ua.knu.maksym_pashchenko.movieexplorer

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import ua.knu.maksym_pashchenko.movieexplorer.domain.model.Movie
import ua.knu.maksym_pashchenko.movieexplorer.domain.model.MovieDetails
import ua.knu.maksym_pashchenko.movieexplorer.domain.repository.MovieRepository
import ua.knu.maksym_pashchenko.movieexplorer.ui.screen.home.MovieListUiState
import ua.knu.maksym_pashchenko.movieexplorer.ui.screen.viewmodel.MovieListViewModel

@OptIn(ExperimentalCoroutinesApi::class)
class MovieListViewModelTest {

    @Before
    fun setUp() {
        Dispatchers.setMain(StandardTestDispatcher())
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun initialStateIsLoading() = runTest {
        val repository = FakeHomeRepository()

        val viewModel = MovieListViewModel(repository)

        assertEquals(
            MovieListUiState.Loading,
            viewModel.uiState.value
        )
    }

    @Test
    fun loadMoviesSuccess() = runTest {
        val movie = testMovie()

        val repository = FakeHomeRepository()

        repository.movies = listOf(movie)

        val viewModel = MovieListViewModel(repository)

        advanceUntilIdle()

        assertEquals(
            MovieListUiState.Success(listOf(movie)),
            viewModel.uiState.value
        )
    }

    @Test
    fun retryAfterErrorReturnsSuccess() = runTest {
        val movie = testMovie()

        val repository = FakeHomeRepository()

        repository.failure = IllegalStateException("Помилка API")

        val viewModel = MovieListViewModel(repository)

        advanceUntilIdle()

        assertEquals(
            MovieListUiState.Error(message = "Сталася непередбачена помилка"),
            viewModel.uiState.value
        )

        repository.failure = null
        repository.movies = listOf(movie)

        viewModel.loadMovies()

        advanceUntilIdle()

        assertEquals(
            MovieListUiState.Success(listOf(movie)),
            viewModel.uiState.value
        )
    }

    private fun testMovie() = Movie(
        id = 1,
        title = "Interstellar",
        overview = "Test overview",
        posterUrl = null,
        rating = 8.5,
        releaseYear = 2014
    )
}

private class FakeHomeRepository() : MovieRepository {
    var movies = emptyList<Movie>()

    var failure: Exception? = null

    override suspend fun getPopularMovies(page: Int): List<Movie> {

        failure?.let { throw it }

        return movies
    }

    override suspend fun getMovieDetails(movieId: Int): MovieDetails {
        error("getMovieDetails is not used in MovieListViewModelTest")
    }

    override suspend fun searchMovies(query: String): List<Movie> {
        error("searchMovies is not used in MovieListViewModelTest")
    }

    override suspend fun addFavorite(movie: Movie) {
        error("addFavorite is not used in MovieListViewModelTest")
    }

    override suspend fun removeFavorite(movieId: Int) {
        error("removeFavorite is not used in MovieListViewModelTest")
    }

    override fun getFavorites(): Flow<List<Movie>> {
        error("getFavorites is not used in MovieListViewModelTest")
    }

    override fun isFavorite(movieId: Int): Flow<Boolean> {
        error("isFavorite is not used in MovieListViewModelTest")
    }
}