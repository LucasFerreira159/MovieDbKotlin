package com.app4fun.moviedb.data.model

import java.io.Serializable

class Movie: Serializable {

    var id: String = ""
    var title: String = ""
    var poster_path: String = ""
    var release_date: String = ""
    var vote_average: Double = 0.0
    var overview: String = ""


}