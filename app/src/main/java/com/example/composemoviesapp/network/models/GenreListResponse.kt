package com.example.composemoviesapp.network.models

import com.example.composemoviesapp.models.Chip
import com.example.moviesapp.network.models.NetworkError

data class GenreListResponse(
    val genres: List<Genre>?,
    override val error: String?,
) : NetworkError {
    data class Genre(
        val id: Int,
        val name: String
    ) {
        fun toChip(): Chip {
            return Chip(
                id = id,
                name = name,
            )
        }
    }


    fun toChipList(): List<Chip> {
        return genres.orEmpty().map {
            it.toChip()
        }
    }
}

