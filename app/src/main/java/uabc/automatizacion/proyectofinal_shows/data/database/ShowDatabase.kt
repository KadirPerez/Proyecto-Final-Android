package uabc.automatizacion.proyectofinal_shows.data.database

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverter
import androidx.room.TypeConverters
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import uabc.automatizacion.proyectofinal_shows.data.model.Image
import uabc.automatizacion.proyectofinal_shows.data.model.Network
import uabc.automatizacion.proyectofinal_shows.data.model.Rating
import uabc.automatizacion.proyectofinal_shows.data.model.Schedule
import uabc.automatizacion.proyectofinal_shows.data.model.Show

@Entity(tableName = "show")
data class ShowDatabase(
    @PrimaryKey
    val id: Int,
    val name: String?,
    @TypeConverters(RatingConverter::class)
    val rating: Rating?,
    @TypeConverters(ImageConverter::class)
    val image: Image?,
    @TypeConverters(GenresConverter::class)
    val genres: List<String>?,
    val summary: String?,
    val premiered: String?,
    val ended: String?,
    val language: String?,
    @TypeConverters(NetworkConverter::class)
    val network: Network?,
    val officialSite: String?,
    @TypeConverters(ScheduleConverter::class)
    val schedule: Schedule?
)

class RatingConverter {
    private val moshi = Moshi.Builder().add(KotlinJsonAdapterFactory()).build()
    private val adapter = moshi.adapter(Rating::class.java)

    @TypeConverter
    fun fromRating(rating: Rating?): String? {
        return rating?.let { adapter.toJson(it) }
    }

    @TypeConverter
    fun toRating(json: String?): Rating? {
        return json?.let { adapter.fromJson(it) }
    }
}

class ImageConverter {
    private val moshi = Moshi.Builder().add(KotlinJsonAdapterFactory()).build()
    private val adapter = moshi.adapter(Image::class.java)

    @TypeConverter
    fun fromImage(image: Image?): String? {
        return image?.let { adapter.toJson(it) }
    }

    @TypeConverter
    fun toImage(json: String?): Image? {
        return json?.let { adapter.fromJson(it) }
    }
}

class GenresConverter {
    @TypeConverter
    fun fromGenres(genres: List<String>?): String? {
        return genres?.joinToString(",")
    }

    @TypeConverter
    fun toGenres(data: String?): List<String>? {
        return data?.split(",") ?: emptyList()
    }
}

class NetworkConverter {
    private val moshi = Moshi.Builder().add(KotlinJsonAdapterFactory()).build()
    private val adapter = moshi.adapter(Network::class.java)

    @TypeConverter
    fun fromNetwork(network: Network?): String? {
        return network?.let { adapter.toJson(it) }
    }

    @TypeConverter
    fun toNetwork(json: String?): Network? {
        return json?.let { adapter.fromJson(it) }
    }
}

class ScheduleConverter {
    private val moshi = Moshi.Builder().add(KotlinJsonAdapterFactory()).build()
    private val adapter = moshi.adapter(Schedule::class.java)

    @TypeConverter
    fun fromSchedule(schedule: Schedule?): String? {
        return schedule?.let { adapter.toJson(it) }
    }

    @TypeConverter
    fun toSchedule(json: String?): Schedule? {
        return json?.let { adapter.fromJson(it) }
    }
}

fun ShowDatabase.toShow(): Show {
    return Show(
        id = this.id,
        name = this.name,
        rating = this.rating,
        image = this.image,
        genres = this.genres,
        summary = this.summary,
        premiered = this.premiered,
        ended = this.ended,
        language = this.language,
        network = this.network,
        officialSite = this.officialSite,
        schedule = this.schedule
    )
}