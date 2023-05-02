package com.example.composemoviesapp.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.moviesapp.models.Actor

@Composable
fun ActorItem(actor: Actor) {
    Column() {
        AsyncImage(
            model = actor.getImageUrl(),
            contentDescription = null,
            modifier = Modifier.size(160.dp).clip(RoundedCornerShape(15.dp)),
            contentScale = ContentScale.FillWidth,
        )
        Spacer(modifier = Modifier.height(10.dp))
        Text(text = actor.name, fontSize = 14.sp, fontWeight = FontWeight.Bold)
    }
}