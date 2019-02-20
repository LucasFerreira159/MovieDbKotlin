package com.app4fun.moviedb.data.network.response

import com.app4fun.moviedb.data.model.Movie
import com.google.gson.annotations.SerializedName

class MovieResponse {

    @SerializedName("results")
    val results: MutableList<Movie> = ArrayList()

}