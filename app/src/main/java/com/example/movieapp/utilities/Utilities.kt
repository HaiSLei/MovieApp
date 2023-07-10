package com.example.movieapp.utilities

import android.content.SharedPreferences
import android.content.res.Resources
import android.widget.ImageView
import androidx.annotation.DrawableRes
import com.example.movieapp.R
import com.example.movieapp.models.Movie
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken


fun setMap(key: String, map: HashMap<Int,Movie>, sp : SharedPreferences) {
    val gson = Gson()
    val json = gson.toJson(map)
    set(key, json, sp.edit())
}

fun set(key: String, value: String, editor: SharedPreferences.Editor) {
    editor.putString(key, value)
    editor.commit()
}

fun getMap( key : String, sp : SharedPreferences): HashMap<Int,Movie> {
    var items: HashMap<Int,Movie>
    val serializedObject: String? = sp.getString(key, null)
    if (serializedObject != null) {
        val gson = Gson()
        val type = object : TypeToken<HashMap<Int,Movie>?>() {}.type
        items = gson.fromJson(serializedObject, type)
    } else {
        items = hashMapOf()
    }
    return items
}

fun checkFavorite(favoriteMap: HashMap<Int, Movie>, list: List<Movie>): List<Movie> {
    for (x in list) {
        if (favoriteMap.containsKey(x.id)) {
            x.isLiked = true
        }
    }
    return list
}

