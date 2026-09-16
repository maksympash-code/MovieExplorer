package ua.knu.maksym_pashchenko.movieexplorer.data.remote.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class MovieDetailsDto(
    val id: Int,

    val title: String,

    val overview: String,

    @SerialName("poster_path")
    val posterPath: String?,

    @SerialName("backdrop_path")
    val backdropPath: String?,

    @SerialName("release_date")
    val releaseDate: String,

    @SerialName("vote_average")
    val voteAverage: Double,

    @SerialName("vote_count")
    val voteCount: Int,

    val runtime: Int?,

    val genres: List<GenreDto>,

    val status: String,

    val tagline: String
)

@Serializable
data class GenreDto(
    val id: Int,
    val name: String,
)