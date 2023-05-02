package com.example.composemoviesapp.ui.details.vm

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.composemoviesapp.network.repository.FilmRepository
import kotlinx.coroutines.launch


class DetailsViewModel(
    filmId: Int
) : ViewModel() {

    val actorLiveData = FilmRepository.actorList
    val actorError = FilmRepository.actorListError

    val filmLiveData = FilmRepository.filmDetails
    val filmError = FilmRepository.filmDetailsError

    init {
        onInitialized(filmId)
    }

    private fun onInitialized(filmId: Int) {
        viewModelScope.launch {
            FilmRepository.fetchActors(filmId)
        }
        viewModelScope.launch {
            FilmRepository.fetchDetails(filmId)
        }
    }

}