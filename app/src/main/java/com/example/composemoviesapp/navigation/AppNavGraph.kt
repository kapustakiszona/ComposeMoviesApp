package com.example.composemoviesapp.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable

@Composable
fun AppNavGraph(
    navHostController: NavHostController,
    detailsScreenContent: @Composable (Int) -> Unit,
    homeScreenContent: @Composable () -> Unit,
) {
    NavHost(
        navController = navHostController,
        startDestination = Screen.Home.route
    ) {

        composable(route = Screen.Home.route) {
            homeScreenContent()
        }
        composable(route = Screen.Details.route) {
            val filmId = it.arguments?.getString("film_id") ?: ""
            detailsScreenContent(filmId.toInt())
        }
    }
}