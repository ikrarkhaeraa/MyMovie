package com.example.core.retrofit.response

import com.google.gson.annotations.SerializedName

data class ListMovieResponse(
    @field:SerializedName("dates")
    val dates: Date,

    @field:SerializedName("page")
    val page: Int,

    @field:SerializedName("results")
    val result: List<Movies>,

    @field:SerializedName("total_pages")
    val totalPages: Int,

    @field:SerializedName("total_result")
    val totalResult: Int,
)

data class Date(
    @field:SerializedName("maximum")
    val maximum: String,

    @field:SerializedName("minimum")
    val minimum: String
)

data class Movies(
    @field:SerializedName("adult")
    val adult: Boolean,

    @field:SerializedName("backdrop_path")
    val backdropPath: String,

    @field:SerializedName("genre_ids")
    val genreIds: List<Int>,

    @field:SerializedName("id")
    val id: Int,

    @field:SerializedName("original_language")
    val originalLanguage: String,

    @field:SerializedName("original_title")
    val originalTitle: String,

    @field:SerializedName("overview")
    val overview: String,

    @field:SerializedName("popularity")
    val popularity: Float,

    @field:SerializedName("poster_path")
    val posterPath: String,

    @field:SerializedName("release_dath")
    val releaseDath: String,

    @field:SerializedName("title")
    val title: String,

    @field:SerializedName("video")
    val video: Boolean,

    @field:SerializedName("vote_average")
    val voteAverage: Float,

    @field:SerializedName("vote_count")
    val voteCount: Int,
)