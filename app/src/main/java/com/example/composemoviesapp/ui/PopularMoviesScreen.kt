package com.example.composemoviesapp.ui

import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.composemoviesapp.models.Chip
import com.example.moviesapp.models.Film
import com.github.fengdai.compose.pulltorefresh.PullToRefresh
import com.github.fengdai.compose.pulltorefresh.rememberPullToRefreshState
import kotlinx.coroutines.delay

@Composable
fun PopularMoviesLazyGrid(
    filmList: List<Film>,
    paddingValues: PaddingValues,
    onMovieItemClickListener: (Film) -> Unit,
    chipList: List<Chip>,
    onChipClickListener: (Chip) -> Unit,
    onRefreshListener: () -> Unit = {}
) {
    Column(
        modifier = Modifier
            .padding(paddingValues)
            .fillMaxSize(),
    ) {
        ChipGenresLazyRow(
            chipList = chipList,
            chipClickListener = {
                onChipClickListener(it)
            },
        )
        Column(
        ) {
            var refreshing by remember { mutableStateOf(false) }
            LaunchedEffect(refreshing) {
                if (refreshing) {
                    delay(1200)
                    refreshing = false
                }
            }
            PullToRefresh(
                state = rememberPullToRefreshState(isRefreshing = refreshing),
                onRefresh = {
                    refreshing = true
                    onRefreshListener()
                },
                indicatorPadding = PaddingValues(20.dp),
            ) {
                LazyVerticalGrid(
                    columns = GridCells.Fixed(2),
                    contentPadding = PaddingValues(horizontal = 20.dp, vertical = 20.dp),
                    verticalArrangement = Arrangement.spacedBy(20.dp),
                    horizontalArrangement = Arrangement.spacedBy(20.dp)
                ) {
                    items(items = filmList, key = { it.id }) { film ->
                        MovieItem(
                            film = film,
                            onFilmItemClickListener = {
                                Log.d("OTAG", "Film ${film.name} was clicked")
                                onMovieItemClickListener(film)
                            }
                        )
                    }
                }
            }
        }
    }
}