package com.example.movieapp.models

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Movies( val results : List<Movie>) : Parcelable
