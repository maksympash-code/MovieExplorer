package ua.knu.maksym_pashchenko.movieexplorer.data.mapper

import ua.knu.maksym_pashchenko.movieexplorer.data.remote.dto.MovieDetailsDto
import ua.knu.maksym_pashchenko.movieexplorer.data.remote.dto.MovieDto
import ua.knu.maksym_pashchenko.movieexplorer.domain.model.Movie
import ua.knu.maksym_pashchenko.movieexplorer.domain.model.MovieDetails

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
        rating = voteAverage,
        releaseYear = releaseDate.take(4).toIntOrNull()
    )
}

fun MovieDetailsDto.toMovieDetails(): MovieDetails {
    return MovieDetails(
        id = id,
        title = title,
        overview = overview,
        posterUrl = posterPath?.let {
            "$IMAGE_BASE_URL$it"
        },
        backdropUrl = backdropPath?.let {
            "$IMAGE_BASE_URL$it"
        },
        rating = voteAverage,
        releaseDate = releaseDate,
        runtimeMinutes = runtime,
        genres = genres.map { it.name },
        status = status,
        tagLine = tagline
    )
}