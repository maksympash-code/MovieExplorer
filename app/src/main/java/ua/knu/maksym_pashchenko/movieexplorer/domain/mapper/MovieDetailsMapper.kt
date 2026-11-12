package ua.knu.maksym_pashchenko.movieexplorer.domain.mapper

import ua.knu.maksym_pashchenko.movieexplorer.domain.model.Movie
import ua.knu.maksym_pashchenko.movieexplorer.domain.model.MovieDetails

fun MovieDetails.toMovie(): Movie {
    return Movie(
        id = id,
        title = title,
        overview = overview,
        posterUrl = posterUrl,
        rating = rating,
        releaseYear = releaseDate?.take(4)?.toIntOrNull()
    )
}