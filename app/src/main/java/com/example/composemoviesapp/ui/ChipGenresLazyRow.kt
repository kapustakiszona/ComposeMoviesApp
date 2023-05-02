package com.example.composemoviesapp.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.composemoviesapp.models.Chip

@Composable
fun ChipGenresLazyRow(
    chipList: List<Chip>,
    chipClickListener: (Chip) -> Unit
) {
    Column() {
        Text(
            text = "Popular now",
            fontSize = 22.sp,
            modifier = Modifier.padding(start = 20.dp)
        )
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 20.dp)
        ) {
            LazyRow(modifier = Modifier.fillMaxWidth()) {
                items(
                    items = chipList,
                    key = { it.id }
                ) { chip ->
                    ChipItem(
                        chip = chip,
                        onChipClickListener = {
                            chipClickListener(chip)
                        }
                    )
                }
            }
        }
    }

}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun ChipItem(
    chip: Chip,
    onChipClickListener: (Chip) -> Unit
) {
    var selected by remember { mutableStateOf(chip.state) }
    FilterChip(
        modifier = Modifier.height(28.dp).padding(end=4.dp),
        selected = selected,
        onClick = {
            selected = !selected
            onChipClickListener(chip)
        },
        label = { Text(text = chip.name, color = Color.Black) },
        shape = RoundedCornerShape(20.dp),
    )
}

