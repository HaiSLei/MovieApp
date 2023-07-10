package com.example.movieapp.models

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class Movie(val id : Int,
                 val title: String,
                 @SerializedName("poster_path") val posterPath: String,
                 var isLiked : Boolean = false) : Parcelable




