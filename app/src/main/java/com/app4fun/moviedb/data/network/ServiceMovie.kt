package com.app4fun.moviedb.data.network

import com.app4fun.moviedb.data.network.response.MovieResponse
import retrofit2.Call
import retrofit2.http.GET

interface ServiceMovie {

    @GET("3/movie/upcoming?api_key=1f54bd990f1cdfb230adb312546d765d&language=pt-BR&region=BR")
    fun getResults(): Call<MovieResponse>

}