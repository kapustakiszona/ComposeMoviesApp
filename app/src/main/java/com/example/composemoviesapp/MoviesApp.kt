package com.example.composemoviesapp

import android.app.Application
import com.example.composemoviesapp.network.NetworkClient

class MoviesApp : Application() {
    companion object {
        lateinit var instance: MoviesApp
    }

    override fun onCreate() {
        super.onCreate()
        NetworkClient.initNetwork()
        instance = this
    }
}