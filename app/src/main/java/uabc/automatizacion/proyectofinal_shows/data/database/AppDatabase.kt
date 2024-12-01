package uabc.automatizacion.proyectofinal_shows.data.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters


@Database(entities = [ShowDatabase::class], version = 2)
@TypeConverters(
    RatingConverter::class,
    ImageConverter::class,
    GenresConverter::class,
    NetworkConverter::class,
    ScheduleConverter::class
)
abstract class AppDatabase : RoomDatabase() {

    abstract fun showDao(): DaoShows

    companion object {
        @Volatile
        private var instance: AppDatabase? = null

        fun getInstance(context: Context): AppDatabase {
            return instance ?: synchronized(this) {
                Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "app_database"
                )
                    .fallbackToDestructiveMigration()
                    .build()
                    .also { instance = it }
            }
        }
    }

}
