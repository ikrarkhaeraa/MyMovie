package com.example.core.retrofit

import com.example.core.retrofit.response.DetailMovieResponse
import com.example.core.retrofit.response.ListMovieResponse
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query

interface ApiService {

    @GET("now_playing")
    suspend fun getMovieList(
        @Header("Authorization") auth: String,
        @Query("page") page: Int
    ): ListMovieResponse

    @GET("{movie_id}")
    suspend fun getMovieDetail(
        @Header("Authorization") auth: String,
        @Path("movie_id") movieId: Int
    ): DetailMovieResponse

}