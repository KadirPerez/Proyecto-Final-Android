package uabc.automatizacion.proyectofinal_shows.data.database

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface DaoShows {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(shows: ShowDatabase)

    @Delete
    suspend fun delete(show: ShowDatabase)

    @Query("SELECT * FROM show")
    fun getAll(): Flow<List<ShowDatabase>>
}