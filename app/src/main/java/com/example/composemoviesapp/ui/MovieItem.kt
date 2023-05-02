package com.example.composemoviesapp.ui

import android.util.Log
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.moviesapp.models.Film
import com.gowtham.ratingbar.RatingBar
import com.gowtham.ratingbar.RatingBarConfig

@Composable
fun MovieItem(
    film: Film,
    onFilmItemClickListener: () -> Unit
) {
    var rating: Float by rememberSaveable { mutableStateOf(value = film.rating) }
    Card(
        shape = RoundedCornerShape(15.dp).copy(
            bottomStart = CornerSize(15.dp),
            bottomEnd = CornerSize(15.dp)
        ),
        modifier = Modifier.clickable(
            onClick = {
                onFilmItemClickListener()
            }
        ),
    ) {
        Column {
            AsyncImage(
                contentScale = ContentScale.FillWidth,
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(
                        shape = RoundedCornerShape(10.dp)
                    ),
                contentDescription = null,
                model = ImageRequest.Builder(LocalContext.current)
                    .data(film.getImageUrl())
                    .build()
            )
            Spacer(modifier = Modifier.height(8.dp))
            Box(modifier = Modifier.height(30.dp)) {
                Text(
                    modifier = Modifier.padding(start = 3.dp, end = 3.dp),
                    fontSize = 14.sp,
                    lineHeight = 14.sp,
                    text = film.name,
                )
            }
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                modifier = Modifier.padding(start = 3.dp, end = 3.dp),
                maxLines = 6,
                fontSize = 12.sp,
                lineHeight = 14.sp,
                overflow = TextOverflow.Ellipsis,
                text = film.description
            )
            Spacer(modifier = Modifier.height(14.dp))

            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                RatingBar(
                    modifier = Modifier.padding(start = 3.dp),
                    value = rating,
                    config = RatingBarConfig()
                        .activeColor(Color.Black)
                        .size(14.dp)
                        .inactiveColor(Color.White)
                        .inactiveBorderColor(Color.Black)
                        .isIndicator(true)
                        .padding(0.5.dp),
                    onValueChange = {
                        rating = it
                    },
                    onRatingChanged = {
                        Log.d("TAG", "onRatingChanged: $it")
                    }
                )
                Spacer(modifier = Modifier.weight(1f))
                AgeRatingCircleView(
                    ageValue = film.adult, modifier = Modifier
                        .padding(end = 3.dp, bottom = 3.dp)
                        .size(30.dp)
                        .clip(CircleShape)
                        .border(width = 1.dp, color = Color.Black, shape = CircleShape),
                    fontSize = 9.sp
                )
            }
        }
    }
}