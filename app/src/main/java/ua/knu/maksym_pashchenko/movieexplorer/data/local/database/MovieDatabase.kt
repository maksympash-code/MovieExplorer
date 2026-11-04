package ua.knu.maksym_pashchenko.movieexplorer.data.local.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import ua.knu.maksym_pashchenko.movieexplorer.data.local.dao.FavoriteMovieDao
import ua.knu.maksym_pashchenko.movieexplorer.data.local.entity.FavoriteMovieEntity

@Database(
    entities = [FavoriteMovieEntity::class],
    version = 1,
    exportSchema = false
)
abstract class MovieDatabase : RoomDatabase() {

    abstract fun favoriteMovieDao() : FavoriteMovieDao

    companion object {

        @Volatile
        private var INSTANCE: MovieDatabase? = null

        fun getInstance(context: Context): MovieDatabase {
            return INSTANCE ?: synchronized(this) {
                INSTANCE ?: Room.databaseBuilder(
                    context.applicationContext,
                    MovieDatabase::class.java,
                    "movie_explorer.db"
                ).build().also { database ->
                    INSTANCE = database
                }
            }
        }
    }
}