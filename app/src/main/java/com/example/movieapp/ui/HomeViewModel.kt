package com.example.movieapp.ui

import android.app.Application
import android.content.Context
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.movieapp.models.Movie
import com.example.movieapp.models.Movies
import com.example.movieapp.repositories.MovieRepository
import com.example.movieapp.utilities.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import okhttp3.ResponseBody.Companion.toResponseBody
import retrofit2.Response


class HomeViewModel(application: Application) : AndroidViewModel(application) {


    private val _favoriteMoviesLiveData: MutableLiveData<ArrayList<Movie>> by lazy {
        MutableLiveData()
    }

    val favoriteMovieLiveData: LiveData<ArrayList<Movie>> = _favoriteMoviesLiveData

    private val _nowPlayingMoviesLiveData: MutableLiveData<Response<Movies>> by lazy {
        MutableLiveData()
    }

    val nowPlayingLiveData: LiveData<Response<Movies>> = _nowPlayingMoviesLiveData

    fun addToFavorite(movie: Movie) {
        val sp =
            getApplication<Application>().getSharedPreferences("favorite", Context.MODE_PRIVATE)

        val spList = getMap("fa",sp)
        if (!movie.isLiked) {
            spList.remove(movie.id)
        } else {
            spList[movie.id] = movie
        }
        setMap("fa", spList, sp)
        val list = ArrayList(spList.values)

        _favoriteMoviesLiveData.value = list
        notifyNowPlayingList(movie)
    }

    private fun notifyNowPlayingList(movie: Movie) {
        val nowPlayingList = _nowPlayingMoviesLiveData.value?.body()?.results
        nowPlayingList?.let {
            for (x in it) {
                if (x.id == movie.id) {
                    x.isLiked = movie.isLiked
                    break
                }
            }
            _nowPlayingMoviesLiveData.value = (Response.success(Movies(it)))
        }
    }

    fun getNowPlayingMovies() {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val response = MovieRepository.getNowPlayingMovies()
                _nowPlayingMoviesLiveData.postValue(response)
            } catch (e: Exception) {
                val responseBody = e.message?.toResponseBody() ?: "unknown error".toResponseBody()
                Log.e("getNowPlayingMovies: ", "$responseBody")
                _nowPlayingMoviesLiveData.postValue(Response.error(900, responseBody))
            }
        }
    }
}