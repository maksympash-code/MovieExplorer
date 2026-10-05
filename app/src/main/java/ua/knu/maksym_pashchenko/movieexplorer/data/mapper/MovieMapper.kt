package ua.knu.maksym_pashchenko.movieexplorer.data.mapper

import ua.knu.maksym_pashchenko.movieexplorer.data.remote.dto.MovieDto
import ua.knu.maksym_pashchenko.movieexplorer.domain.model.Movie

private const val IMAGE_BASE_URL =
    "https://image.tmdb.org/t/p/w500"

fun MovieDto.toMovie(): Movie {
    return Movie(
        id = id,
        title = title,
        overview = overview,
        posterUrl = posterPath?.let {
            "$IMAGE_BASE_URL$it"
        },
        rating = voteAverage
    )
}