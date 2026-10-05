package ua.knu.maksym_pashchenko.movieexplorer.domain.model

data class Movie(
    val id: Int,
    val title: String,
    val overview: String,
    val posterUrl: String?,
    val rating: Double
)
