package ua.knu.maksym_pashchenko.movieexplorer

import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.advanceTimeBy
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runCurrent
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
import kotlin.time.Duration.Companion.milliseconds

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
    fun initialStateIsIdle() = runTest {
        val viewModel = SearchViewModel(FakeMovieRepository())

        runCurrent()

        assertEquals("", viewModel.query.value)
        assertEquals(SearchUiState.Idle, viewModel.uiState.value)
    }

    @Test
    fun blankQueryReturnsIdle() = runTest {
        val repository =  FakeMovieRepository()
        val viewModel = SearchViewModel(repository)

        runCurrent()
        viewModel.onQueryChange("   ")
        advanceUntilIdle()

        assertEquals(SearchUiState.Idle, viewModel.uiState.value)
        assertEquals(0, repository.searchCalls)
    }

    @Test
    fun searchStartsOnlyAfter500ms() = runTest {
        val movie = testMovie()
        val repository = FakeMovieRepository()
        repository.movies = listOf(movie)

        val viewModel = SearchViewModel(repository)

        runCurrent()
        viewModel.onQueryChange("  Batman  ")
        runCurrent()

        assertEquals(SearchUiState.Loading, viewModel.uiState.value)

        advanceTimeBy(499.milliseconds)
        runCurrent()

        assertEquals(0, repository.searchCalls)

        advanceTimeBy(1.milliseconds)
        runCurrent()

        assertEquals(1, repository.searchCalls)
        assertEquals("Batman", repository.lastQuery)
        assertEquals(
            SearchUiState.Success(listOf(movie)),
            viewModel.uiState.value
        )
    }

    @Test
    fun rapidTypingMakesOnlyOneRequest() = runTest {
        val repository = FakeMovieRepository()
        val viewModel = SearchViewModel(repository)

        runCurrent()

        viewModel.onQueryChange("B")
        runCurrent()
        advanceTimeBy(200.milliseconds)

        viewModel.onQueryChange("Ba")
        runCurrent()
        advanceTimeBy(200.milliseconds)

        viewModel.onQueryChange("Batman")
        runCurrent()

        advanceTimeBy(499.milliseconds)
        runCurrent()

        assertEquals(0, repository.searchCalls)

        advanceTimeBy(1.milliseconds)
        runCurrent()

        assertEquals(1, repository.searchCalls)
        assertEquals("Batman", repository.lastQuery)
    }

    @Test
    fun sameNormalizedQueryDoesNotSearchAgain() = runTest {
        val repository = FakeMovieRepository()
        val viewModel = SearchViewModel(repository)

        runCurrent()

        viewModel.onQueryChange("Batman")
        advanceUntilIdle()

        assertEquals(1, repository.searchCalls)

        viewModel.onQueryChange("  Batman  ")
        advanceUntilIdle()

        assertEquals(1, repository.searchCalls)
    }

    @Test
    fun emptyResultsReturnEmpty() = runTest {
        val viewModel = SearchViewModel(FakeMovieRepository())

        runCurrent()
        viewModel.onQueryChange("Невідомий фільм")
        advanceUntilIdle()

        assertEquals(SearchUiState.Empty, viewModel.uiState.value)
    }

    @Test
    fun exceptionReturnsError() = runTest {
        val repository = FakeMovieRepository()
        repository.failure = IllegalStateException("Помилка API")

        val viewModel = SearchViewModel(repository)

        runCurrent()
        viewModel.onQueryChange("Batman")
        advanceUntilIdle()

        assertEquals(
            SearchUiState.Error("Помилка API"),
            viewModel.uiState.value
        )
    }

    @Test
    fun clearingQueryReturnsIdle() = runTest {
        val repository = FakeMovieRepository()
        val viewModel = SearchViewModel(repository)

        runCurrent()

        viewModel.onQueryChange("Batman")
        advanceUntilIdle()

        assertEquals(1, repository.searchCalls)

        viewModel.onQueryChange("")
        runCurrent()

        assertEquals(SearchUiState.Idle, viewModel.uiState.value)
        assertEquals(1, repository.searchCalls)
    }

    @Test
    fun newSearchCancelsPreviousRequest() = runTest {
        val repository = FakeMovieRepository()
        repository.responseDelayMillis = 2_000

        val viewModel = SearchViewModel(repository)
        runCurrent()

        viewModel.onQueryChange("Batman")
        runCurrent()

        advanceTimeBy(500.milliseconds)
        runCurrent()

        assertEquals(1, repository.searchCalls)

        viewModel.onQueryChange("Superman")
        runCurrent()

        advanceTimeBy(500.milliseconds)
        runCurrent()

        assertEquals(2, repository.searchCalls)
        assertEquals(1, repository.cancelledSearches)

        advanceUntilIdle()

        assertEquals("Superman", repository.lastQuery)
        assertEquals(SearchUiState.Empty, viewModel.uiState.value)
    }

    private fun testMovie() = Movie(
        id = 1,
        title = "Тестовий фільм",
        overview = "Опис",
        posterUrl = null,
        rating = 8.5,
        releaseYear = 2020
    )

}

private class FakeMovieRepository() : MovieRepository {

    var movies: List<Movie> = emptyList()
    var failure: Exception? = null
    var responseDelayMillis: Long = 0
    var searchCalls: Int = 0
    var cancelledSearches = 0
    var lastQuery: String? = null

    override suspend fun searchMovies(query: String): List<Movie> {
        searchCalls++
        lastQuery = query

        try {
            delay(responseDelayMillis.milliseconds)
        } catch (e: CancellationException) {
            cancelledSearches++
            throw e
        }

        failure?.let { throw it }

        return movies
    }

    override suspend fun addFavorite(movie: Movie) {
        error("Не використовується в цих тестах")
    }

    override suspend fun removeFavorite(movieId: Int) {
        error("Не використовується в цих тестах")
    }

    override fun getFavorites(): Flow<List<Movie>> {
        error("Не використовується в цих тестах")
    }

    override fun isFavorite(movieId: Int): Flow<Boolean> {
        error("Не використовується в цих тестах")
    }

    override suspend fun getPopularMovies(page: Int): List<Movie> =
        error("Не використовується в цих тестах")

    override suspend fun getMovieDetails(movieId: Int): MovieDetails =
        error("Не використовується в цих тестах")

}