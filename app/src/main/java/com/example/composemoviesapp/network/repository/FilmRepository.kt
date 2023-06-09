package com.example.composemoviesapp.network.repository

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.composemoviesapp.models.Chip
import com.example.composemoviesapp.network.getActorListByIdAsync
import com.example.composemoviesapp.network.getGenreListAsync
import com.example.composemoviesapp.network.getMovieDetailsByIdAsync
import com.example.composemoviesapp.network.getPopularMoviesAsync
import com.example.moviesapp.localdb.database.FilmsDatabase
import com.example.moviesapp.localdb.entities.FilmEntity
import com.example.moviesapp.models.Actor
import com.example.moviesapp.models.Film
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

object FilmRepository {

    val mChipList = MutableLiveData<List<Chip>?>()
    val chipList: LiveData<List<Chip>?> = mChipList

    private val mChipListError = MutableLiveData<String?>()
    val chipListError: LiveData<String?> = mChipListError

    private val mFilmList = MutableLiveData<List<Film>?>()
    val filmList: LiveData<List<Film>?> = mFilmList

    private val mFilmListError = MutableLiveData<String?>()
    val filmListError: LiveData<String?> = mFilmListError

    private val mActorList = MutableLiveData<List<Actor>?>()
    val actorList: LiveData<List<Actor>?> = mActorList

    private val mActorListError = MutableLiveData<String?>()
    val actorListError: LiveData<String?> = mActorListError

    private val mFilmDetails = MutableLiveData<Film?>()
    val filmDetails: LiveData<Film?> = mFilmDetails

    private val mFilmDetailsError = MutableLiveData<String?>()
    val filmDetailsError: LiveData<String?> = mFilmDetailsError

    private val filmDao = FilmsDatabase.getDatabase().getFilmDao()
    private val genreDao = FilmsDatabase.getDatabase().getGenreDao()
    fun clearLiveData() {
        mFilmDetails.value = null
    }

    suspend fun fetchFilmsAfterRefresh() {
        fetchFilms(true)
    }

    private suspend fun insertFilmsToDb(films: List<FilmEntity>) = withContext(Dispatchers.IO) {
        filmDao.insertAllPopularFilms(films)
        filmDao.setupGenreNameInFilms()
    }

    suspend fun fetchFilms(forceFetch: Boolean = false) = withContext(Dispatchers.IO) {
        val filmListFromDb = filmDao.getAllPopularFilms()
        if (filmListFromDb.orEmpty().isEmpty() || forceFetch) {
            val result = getPopularMoviesAsync()
            withContext(Dispatchers.Main) {
                result.error?.let { errorMessage ->
                    mFilmListError.value = errorMessage
                }
                result.results?.let {
                    mFilmList.value = result.toFilmList()
                    insertFilmsToDb(result.toFilmListEntity())
                }
            }
        } else {
            withContext(Dispatchers.Main) {
                mFilmList.value = filmListFromDb.orEmpty().map { it.toFilm() }
            }
        }
    }

    private suspend fun getGenreName(genreId: Int?) =
        withContext(Dispatchers.IO) {
            val genreItem = genreDao.getById(genreId ?: 0)
            return@withContext genreItem.name
        }

    suspend fun fetchDetails(filmId: Int) = withContext(Dispatchers.IO) {
        Log.d("OTAG", "DetailVM on start contains film ${mFilmDetails.value?.name ?: "EMPTY"}")
        val detailsFromDb = filmDao.findFilmDetailsById(filmId)
        if (detailsFromDb != null) {
            withContext(Dispatchers.Main) {
                val genreName = getGenreName(detailsFromDb.genre_ids)
                mFilmDetails.value = detailsFromDb.toFilm().copy(genre_name = genreName)
            }
        } else {
            val result = getMovieDetailsByIdAsync(filmId)
            withContext(Dispatchers.Main) {
                result.error?.let { errorMessage ->
                    mFilmDetailsError.value = errorMessage
                }
                mFilmDetails.value = result.toFilmDetails()
            }
        }
    }


    suspend fun fetchActors(filmId: Int) = withContext(Dispatchers.IO) {
        val result = getActorListByIdAsync(filmId)
        withContext(Dispatchers.Main) {
            result.error?.let { errorMessage ->
                mActorListError.value = errorMessage
            }
            result.cast?.let {
                mActorList.value = result.toActorsList()
            }
        }
    }

    suspend fun fetchChips() = withContext(Dispatchers.IO) {
        val chipListFromDb = genreDao.getAllGenres()
        if (chipListFromDb.isEmpty()) {
            val result = getGenreListAsync()
            withContext(Dispatchers.Main) {
                result.error?.let { errorMessage ->
                    mChipListError.value = errorMessage
                }
                result.genres?.let { genreList ->
                    mChipList.value = result.toChipList()
                    genreDao.insertAllGenres(genreList.map { it.toChip() })
                }
            }
        } else {
            withContext(Dispatchers.Main) {
                mChipList.value = chipListFromDb.map { it.toChip() }
            }
        }
    }
}