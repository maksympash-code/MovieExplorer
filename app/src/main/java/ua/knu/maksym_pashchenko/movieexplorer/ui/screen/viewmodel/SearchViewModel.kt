package ua.knu.maksym_pashchenko.movieexplorer.ui.screen.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import ua.knu.maksym_pashchenko.movieexplorer.data.repository.MovieRepository
import ua.knu.maksym_pashchenko.movieexplorer.ui.screen.search.SearchUiState

class SearchViewModel(
    private val repository: MovieRepository,
) : ViewModel() {

    private val _uiState = MutableStateFlow<SearchUiState>(SearchUiState.Idle)

    val uiState : StateFlow<SearchUiState> = _uiState.asStateFlow()

    private var searchJob: Job? = null

    fun searchMovies(query: String) {
        searchJob?.cancel()

        val normalizedQuery = query.trim()

        if (normalizedQuery.isEmpty()) {
            searchJob = null
            _uiState.value = SearchUiState.Idle
            return
        }

        _uiState.value = SearchUiState.Loading

        searchJob = viewModelScope.launch {
            try {
                val movies = repository.searchMovies(
                    query = normalizedQuery
                )

                _uiState.value = if (movies.isEmpty()){
                    SearchUiState.Empty
                } else {
                    SearchUiState.Success(movies)
                }

            } catch (e: CancellationException) {
                throw e
            } catch (e: Exception) {
                _uiState.value = SearchUiState.Error(
                    message = e.message ?: "Не вдалося виконати пошук фільмів"
                )
            }
        }
    }
}