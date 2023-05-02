package com.example.moviesapp.models

import com.example.composemoviesapp.network.BASE_IMAGE_URL


data class Actor(val name: String, val image: String?) {
    fun getImageUrl(): String {
        return BASE_IMAGE_URL + image
    }
}