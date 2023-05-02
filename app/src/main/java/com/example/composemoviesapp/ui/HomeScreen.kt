package com.example.composemoviesapp.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.text.input.TextFieldValue
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.rememberNavController
import com.example.composemoviesapp.MoviesApp
import com.example.composemoviesapp.navigation.AppNavGraph
import com.example.composemoviesapp.navigation.Screen
import com.example.composemoviesapp.ui.films.vm.FilmsViewModel
import com.example.composemoviesapp.ui.utils.SearchView
import com.example.composemoviesapp.ui.utils.ToastWithException

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MoviesListScreen() {
    val viewModel: FilmsViewModel = viewModel()
    val filmListState = viewModel.filteredFilmList.observeAsState()
    val chipState = viewModel.mChipList.observeAsState()
    val textState = remember { mutableStateOf(TextFieldValue("")) }
    val navHostController = rememberNavController()
    val isTopBarVisible = remember {
        mutableStateOf(true)
    }
    Scaffold(
        topBar = {
            if (isTopBarVisible.value) {
                SearchView(state = textState)
                viewModel.setSearchQuery(textState.value.text)
            }
        }
    ) { paddingValues ->
        ToastWithException(viewModel = viewModel, context = MoviesApp.instance)
        Column() {
            AppNavGraph(
                navHostController = navHostController,
                detailsScreenContent = { filmId ->
                    MovieDetailsScreen(filmId)
                    isTopBarVisible.value = false
                },
                homeScreenContent = {
                    PopularMoviesLazyGrid(
                        onRefreshListener = {
                            viewModel.setupFilmsAfterRefresh()
                        },
                        filmList = filmListState.value.orEmpty(),
                        paddingValues = paddingValues,
                        onMovieItemClickListener = { film ->
                            navHostController.navigate(Screen.Details.getRouteWIthArgs(film))
                        },
                        chipList = chipState.value.orEmpty(),
                        onChipClickListener = { chip ->
                            viewModel.toggleChipsState(item = chip)
                        },
                    )
                    isTopBarVisible.value = true
                }
            )
        }
    }
}

