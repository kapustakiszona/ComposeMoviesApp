package com.example.composemoviesapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.example.composemoviesapp.ui.MoviesListScreen
import com.example.composemoviesapp.ui.theme.ComposeMoviesAppTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ComposeMoviesAppTheme {
                MoviesListScreen()
            }
        }
    }
}
