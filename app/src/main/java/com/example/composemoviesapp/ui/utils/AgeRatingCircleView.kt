package com.example.composemoviesapp.ui

import androidx.compose.foundation.layout.Box
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.sp

@Composable
fun AgeRatingCircleView(ageValue: String, modifier: Modifier, fontSize: TextUnit) {
    Box(
        modifier = modifier,
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = ageValue,
            fontSize = fontSize,
            color = Color.Black,
            lineHeight = 8.sp,
            fontWeight = FontWeight.SemiBold
        )
    }

}