package com.example.composemoviesapp.ui

import android.util.Log
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.BottomSheetScaffold
import androidx.compose.material.BottomSheetState
import androidx.compose.material.BottomSheetValue
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.rememberBottomSheetScaffoldState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import com.example.composemoviesapp.ui.details.vm.DetailsViewModel
import com.example.composemoviesapp.ui.details.vm.DetailsViewModelFactory
import com.example.moviesapp.models.Film
import com.gowtham.ratingbar.RatingBar
import com.gowtham.ratingbar.RatingBarConfig


@Composable
fun MovieDetailsScreen(
    filmId: Int,
) {
    val viewModel: DetailsViewModel = viewModel(factory = DetailsViewModelFactory(filmId = filmId))
    val detailState by viewModel.filmLiveData.observeAsState()

    Box {
        DetailsScreenItem(
            film = detailState,
            viewModel = viewModel
        )
    }
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
private fun DetailsScreenItem(
    film: Film?,
    viewModel: DetailsViewModel
) {
    val detailsError = viewModel.filmError.observeAsState()
    val actorsError = viewModel.actorError.observeAsState()
    Column(modifier = Modifier) {
        val scaffoldState = rememberBottomSheetScaffoldState(
            bottomSheetState = BottomSheetState(BottomSheetValue.Collapsed)
        )
        BottomSheetScaffold(
            modifier = Modifier,
            scaffoldState = scaffoldState,
            sheetShape = RoundedCornerShape(topStart = 20.dp, topEnd = 20.dp),
            sheetPeekHeight = 120.dp,
            sheetElevation = 20.dp,
            sheetContent = {
                if (film != null ) {
                    BottomSheetContent(film = film, viewModel = viewModel)
                }else{
                    Text(text = detailsError.value.orEmpty())
                    Text(text = actorsError.value.orEmpty())
                }
            }
        ) {
            AsyncImage(
                model = film?.getImageUrl(),
                contentDescription = null,
                modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.FillBounds
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun BottomSheetContent(film: Film, viewModel: DetailsViewModel) {
    var rating: Float by rememberSaveable { mutableStateOf(value = film.rating) }
    val actors = viewModel.actorLiveData.observeAsState(listOf())
    Column(
        modifier = Modifier
            .padding(start = 20.dp, end = 20.dp, top = 8.dp),
        verticalArrangement = Arrangement.Bottom
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            FilterChip(
                label = {
                    Text(text = film.genre_name)
                },
                modifier = Modifier.height(25.dp),
                selected = false,
                onClick = {}
            )
            Spacer(modifier = Modifier.width(14.dp))
            Text(text = film.date_publication)
            Spacer(modifier = Modifier.weight(1f))
            AgeRatingCircleView(
                ageValue = film.adult,
                modifier = Modifier
                    .size(50.dp)
                    .clip(CircleShape)
                    .border(width = 1.dp, color = Color.Black, shape = CircleShape),
                fontSize = 14.sp
            )
        }
        Row() {
            Column() {
                Text(text = film.name, fontSize = 20.sp, lineHeight = 22.sp)
                Spacer(modifier = Modifier.height(8.dp))
                RatingBar(
                    modifier = Modifier.padding(start = 3.dp),
                    value = rating,
                    config = RatingBarConfig()
                        .activeColor(Color.Black)
                        .size(24.dp)
                        .inactiveColor(Color.Gray)
                        .inactiveBorderColor(Color.Black)
                        .isIndicator(true),
                    onValueChange = {
                        rating = it
                    },
                    onRatingChanged = {
                        Log.d("TAG", "onRatingChanged: $it")
                    }
                )
                Spacer(modifier = Modifier.height(8.dp))
            }
        }
        Column() {
            Text(text = film.description, fontSize = 16.sp)
            Text(
                text = "Cast",
                fontWeight = FontWeight.Bold,
                fontSize = 18.sp
            )
            LazyRow(
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                items(
                    items = actors.value.orEmpty(),
                    key = { it.name }
                ) { actor ->
                    ActorItem(actor = actor)
                }
            }
        }
    }
}