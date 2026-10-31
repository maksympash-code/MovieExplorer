package ua.knu.maksym_pashchenko.movieexplorer.ui.navigation

object Routes {
    const val MOVIES = "movies"
    const val MOVIE_DETAILS = "movie/{movieId}"
    const val SEARCH = "search"
    const val FAVORITES = "favorites"

    fun movieDetails(movieId: Int): String {
        return "movie/$movieId"
    }
}