package com.example.moviesapp.localdb.entities

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import com.example.moviesapp.models.Film

@Entity(
    tableName = "films",
    indices = [
        Index("id", unique = true)
    ]
)
data class FilmEntity(
    @PrimaryKey(autoGenerate = false) val id: Int,
    val name: String,
    val date_publication: String,
    val adult: String,
    val rating: Float,
    val description: String,
    val photo: String,
    val genre_ids: Int?,
    var genre_name: String
) {
    fun toFilm(): Film = Film(
        id = id,
        name = name,
        date_publication = date_publication,
        adult = adult,
        rating = rating,
        description = description,
        photo = photo,
        genre_id = genre_ids ?: 0,
        genre_name = genre_name
    )
}
