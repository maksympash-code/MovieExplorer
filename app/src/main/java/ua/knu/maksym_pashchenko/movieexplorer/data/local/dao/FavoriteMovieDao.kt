package ua.knu.maksym_pashchenko.movieexplorer.data.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow
import ua.knu.maksym_pashchenko.movieexplorer.data.local.entity.FavoriteMovieEntity

@Dao
interface FavoriteMovieDao {

    @Query("SELECT * FROM favorite_movies ORDER BY title ASC")
    fun getAllFavorites(): Flow<List<FavoriteMovieEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertFavorite(movie: FavoriteMovieEntity)

    @Delete
    suspend fun deleteFavorite(movie: FavoriteMovieEntity)

    @Query("SELECT EXISTS(SELECT 1 FROM favorite_movies WHERE id = :movieId)")
    fun isFavorite(movieId: Int): Flow<Boolean>
}