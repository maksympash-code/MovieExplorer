package ua.knu.maksym_pashchenko.movieexplorer.domain.model

data class MovieDetails(
    val id: Int,
    val title: String,
    val overview: String,
    val posterUrl: String?,
    val backdropUrl: String?,
    val rating: Double,
    val releaseYear: Int?,
    val runtimeMinutes: Int?,
    val genres: List<String>,
    val status: String,
    val tagLine: String
)
