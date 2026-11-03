package ua.knu.maksym_pashchenko.movieexplorer

import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Test
import ua.knu.maksym_pashchenko.movieexplorer.data.repository.MovieRepository
import ua.knu.maksym_pashchenko.movieexplorer.domain.model.Movie
import ua.knu.maksym_pashchenko.movieexplorer.domain.model.MovieDetails
import ua.knu.maksym_pashchenko.movieexplorer.ui.screen.search.SearchUiState
import ua.knu.maksym_pashchenko.movieexplorer.ui.screen.viewmodel.SearchViewModel

@OptIn(ExperimentalCoroutinesApi::class)
class SearchViewModelTest {

    @Before
    fun setUp() {
        Dispatchers.setMain(StandardTestDispatcher())
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun initialStateIsIdle() {
        val viewModel = SearchViewModel(FakeMovieRepository())

        assertEquals(SearchUiState.Idle, viewModel.uiState.value)
    }

    @Test
    fun blankQueryReturnsIdle() = runTest {
        val repository =  FakeMovieRepository()
        val viewModel = SearchViewModel(repository)

        viewModel.searchMovies("   ")
        advanceUntilIdle()

        assertEquals(SearchUiState.Idle, viewModel.uiState.value)
        assertEquals(0, repository.searchCalls)
    }

    @Test
    fun searchReturnsSuccess() = runTest {
        val movie = Movie(
            id = 1,
            title = "Тестовий фільм",
            overview = "Опис",
            posterUrl = null,
            rating = 8.5,
            releaseYear = 2020
        )

        val repository = FakeMovieRepository()
        repository.movies = listOf(movie)

        val viewModel = SearchViewModel(repository)

        viewModel.searchMovies("  Batman  ")

        assertEquals(SearchUiState.Loading, viewModel.uiState.value)

        advanceUntilIdle()

        assertEquals(
            SearchUiState.Success(listOf(movie)),
            viewModel.uiState.value
        )
        assertEquals("Batman", repository.lastQuery)
    }

    @Test
    fun emptyResultsReturnEmpty() = runTest {
        val viewModel = SearchViewModel(FakeMovieRepository())

        viewModel.searchMovies("Невідомий фільм")
        advanceUntilIdle()

        assertEquals(SearchUiState.Empty, viewModel.uiState.value)
    }

    @Test
    fun exceptionReturnsError() = runTest {
        val repository = FakeMovieRepository()
        repository.failure = IllegalStateException("Помилка API")

        val viewModel = SearchViewModel(repository)

        viewModel.searchMovies("Batman")
        advanceUntilIdle()

        assertEquals(
            SearchUiState.Error("Помилка API"),
            viewModel.uiState.value
        )
    }
}

private class FakeMovieRepository() : MovieRepository {

    var movies: List<Movie> = emptyList()
    var failure: Exception? = null
    var searchCalls: Int = 0
    var lastQuery: String? = null

    override suspend fun searchMovies(query: String): List<Movie> {
        searchCalls++
        lastQuery = query

        failure?.let { throw it }

        return movies
    }

    override suspend fun getPopularMovies(page: Int): List<Movie> =
        error("Не використовується в цих тестах")

    override suspend fun getMovieDetails(movieId: Int): MovieDetails =
        error("Не використовується в цих тестах")

}