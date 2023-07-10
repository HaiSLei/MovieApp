package com.example.movieapp.repositories

import ApiService
import com.example.movieapp.models.Movies
import retrofit2.Response

object MovieRepository {


    private val api by lazy {
        ApiService.sharedInstance
    }

    suspend fun getNowPlayingMovies() : Response<Movies> {
        return api.getNowPlayingMovies()
    }
}