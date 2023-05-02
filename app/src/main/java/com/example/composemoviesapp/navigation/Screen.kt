package com.example.composemoviesapp.navigation

import com.example.moviesapp.models.Film

sealed class Screen(
    val route: String
) {
    object Home : Screen(route = ROUTE_HOME)

    object Details : Screen(route = ROUTE_DETAILS) {

        private const val ROUTE_FOR_ARGS = "details"
        fun getRouteWIthArgs(film: Film): String {
            return "$ROUTE_FOR_ARGS/${film.id}"
        }
    }

    private companion object {
        const val ROUTE_HOME = "home"
        const val ROUTE_DETAILS = "details/{film_id}"
    }
}
