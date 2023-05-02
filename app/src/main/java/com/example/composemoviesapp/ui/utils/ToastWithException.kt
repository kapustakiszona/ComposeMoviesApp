package com.example.composemoviesapp.ui.utils

import android.content.Context
import android.widget.Toast
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import com.example.composemoviesapp.ui.films.vm.FilmsViewModel

@Composable
fun ToastWithException(
    viewModel: FilmsViewModel,
    context: Context
) {
    val errorChipState by viewModel.chipError.observeAsState()
    val errorFilmState by viewModel.filmError.observeAsState()

    if (
        errorChipState.orEmpty().isNotEmpty() || errorFilmState.orEmpty().isNotEmpty()
    ) {
        Toast.makeText(context, "Connection error", Toast.LENGTH_LONG).show()
    }
}