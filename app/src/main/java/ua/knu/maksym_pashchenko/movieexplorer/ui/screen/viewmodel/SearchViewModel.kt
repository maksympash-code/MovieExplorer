package ua.knu.maksym_pashchenko.movieexplorer.ui.screen.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import ua.knu.maksym_pashchenko.movieexplorer.data.repository.MovieRepository
import ua.knu.maksym_pashchenko.movieexplorer.ui.common.toUserMessage
import ua.knu.maksym_pashchenko.movieexplorer.ui.screen.search.SearchUiState

@OptIn(FlowPreview::class)
class SearchViewModel(
    private val repository: MovieRepository,
) : ViewModel() {

    private val _query = MutableStateFlow("")
    val query: StateFlow<String> = _query.asStateFlow()

    private val _uiState = MutableStateFlow<SearchUiState>(SearchUiState.Idle)
    val uiState : StateFlow<SearchUiState> = _uiState.asStateFlow()

    fun onQueryChange(newQuery: String) {
        _query.value = newQuery
    }

    init {
        viewModelScope.launch {
            query
                .map { it.trim() }
                .distinctUntilChanged()
                .onEach { normalizedQuery ->
                    _uiState.value =
                        if (normalizedQuery.isEmpty()){
                            SearchUiState.Idle
                        } else {
                            SearchUiState.Loading
                        }
                }
                .debounce { normalizedQuery ->
                    if (normalizedQuery.isEmpty()) 0L else 500L
                }
                .collectLatest { normalizedQuery ->

                    if (normalizedQuery.isEmpty()){
                        return@collectLatest
                    }

                    try {
                        val movies = repository.searchMovies(
                            query = normalizedQuery
                        )

                        if (query.value.trim() != normalizedQuery) {
                            return@collectLatest
                        }

                        _uiState.value = if (movies.isEmpty()){
                            SearchUiState.Empty
                        } else {
                            SearchUiState.Success(movies)
                        }

                    } catch (e: CancellationException) {
                        throw e
                    } catch (e: Exception) {
                        if (query.value.trim() == normalizedQuery) {
                            _uiState.value = SearchUiState.Error(
                                message = e.toUserMessage()
                            )
                        }

                    }
                }
        }
    }
}