package com.example.composemoviesapp.ui.films.vm

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.composemoviesapp.models.Chip
import com.example.composemoviesapp.network.repository.FilmRepository
import com.example.moviesapp.models.Film
import kotlinx.coroutines.launch

class FilmsViewModel : ViewModel() {
    init {
        setupChipList()
        setupFilmList()
    }

    val mChipList = FilmRepository.mChipList
    val chipError = FilmRepository.chipListError

    private val _searchQueryString = MutableLiveData<String>(null)
    private val searchQueryString: LiveData<String> = _searchQueryString

    val filmError = FilmRepository.filmListError

    fun setSearchQuery(query: String) {
        _searchQueryString.value = query
    }

    private val _filmList = FilmRepository.filmList

    private val mFilteredFilmList = MediatorLiveData<List<Film>>().apply {
        addSource(mChipList) {
            value = mergeFilteredFilmList(
                chipsList = it,
                filmsList = _filmList.value,
                searchQuery = searchQueryString.value
            )
        }
        addSource(_filmList) {
            value = mergeFilteredFilmList(
                chipsList = mChipList.value,
                filmsList = it,
                searchQuery = searchQueryString.value
            )
        }
        addSource(searchQueryString) {
            value = mergeFilteredFilmList(
                chipsList = mChipList.value,
                filmsList = _filmList.value,
                searchQuery = it
            )
        }
    }

    val filteredFilmList: LiveData<List<Film>> =
        mFilteredFilmList

    private fun mergeFilteredFilmList(
        chipsList: List<Chip>?, filmsList: List<Film>?, searchQuery: String?
    ): MutableList<Film> {
        val listOfSelectedChips = chipsList.orEmpty().filter { it.state }.map { it.id }
        return filmsList.orEmpty()
            .filter {
                it.genre_id == null || (listOfSelectedChips.isEmpty() || listOfSelectedChips.contains(
                    it.genre_id
                ))
            }
            .filter { it.name.contains(searchQuery.orEmpty(), true) }
            .toMutableList()
    }

    fun setupFilmsAfterRefresh() {
        viewModelScope.launch {
            FilmRepository.fetchFilmsAfterRefresh()
        }
    }

    private fun setupChipList() {
        viewModelScope.launch {
            FilmRepository.fetchChips()
        }
    }

    private fun setupFilmList() {
        viewModelScope.launch {
            FilmRepository.fetchFilms()
        }
    }

    fun toggleChipsState(item: Chip) {
        val modifiedList = mChipList.value?.toMutableList() ?: mutableListOf()
        modifiedList.replaceAll {
            if (it == item) {
                it.copy(state = !it.state)
            } else {
                it
            }
        }
        mChipList.value = modifiedList
    }
}