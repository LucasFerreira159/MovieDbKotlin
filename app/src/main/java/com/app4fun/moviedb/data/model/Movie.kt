package com.app4fun.moviedb.data.model

import java.io.Serializable

data class Movie(
    val id: String,
    val title: String,
    val poster_path: String,
    val release_date: String,
    val vote_average: Double,
    val overview: String
) : Serializable