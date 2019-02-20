package com.app4fun.moviedb.data.model

class MovieDetail (

    val backdrop_path: Any,
    val genres: List<MovieGenre>,
    val overview: String,
    val poster_path: String,
    val release_date: String,
    val revenue: Double,
    val runtime: Int,
    val title: String,
    val vote_average: Double,
    val budget: Double
)