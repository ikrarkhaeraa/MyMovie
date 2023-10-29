package com.example.core.retrofit.response

import com.google.gson.annotations.SerializedName

class DetailMovieResponse (
    @field:SerializedName("adult")
    val adult: Boolean,

    @field:SerializedName("backdrop_path")
    val backdropPath: String,

    @field:SerializedName("belongs_to_collection")
    val belongsToCollection: BelongsToCollection,

    @field:SerializedName("budget")
    val budget: Int,

    @field:SerializedName("genres")
    val genres: List<Genres>,

    @field:SerializedName("homepage")
    val homepage: String,

    @field:SerializedName("id")
    val id: Int,

    @field:SerializedName("imdb_id")
    val imdb_id: String,

    @field:SerializedName("original_language")
    val originalLanguage: String,

    @field:SerializedName("originalTitle")
    val originalTitle: String,

    @field:SerializedName("overview")
    val overview: String,

    @field:SerializedName("popularity")
    val popularity:Float,

    @field:SerializedName("poster_path")
    val posterPath: String,

    @field:SerializedName("production_companies")
    val productionCompanies: List<ProductionCompanies>,

    @field:SerializedName("production_countries")
    val productionCountries: List<ProductionCountries>,

    @field:SerializedName("release_date")
    val releaseDate: String,

    @field:SerializedName("revenue")
    val revenue: Int,

    @field:SerializedName("runtime")
    val runtime: Int,

    @field:SerializedName("spoken_language")
    val spokenLanguage: List<SpokenLanguage>,

    @field:SerializedName("status")
    val status: String,

    @field:SerializedName("tagline")
    val tagline: String,

    @field:SerializedName("title")
    val title: String,

    @field:SerializedName("video")
    val video: Boolean,

    @field:SerializedName("vote_average")
    val voteAverage: Float,

    @field:SerializedName("vote_count")
    val voteCount: Int
)

data class SpokenLanguage(
    @field:SerializedName("english_name")
    val englishName: String,

    @field:SerializedName("iso_639_1")
    val iso_639_1: String,

    @field:SerializedName("name")
    val name: String,
)

data class ProductionCountries (
    @field:SerializedName("iso_3166_1")
    val iso3661_1: String,

    @field:SerializedName("name")
    val name: String,
)

data class ProductionCompanies (
    @field:SerializedName("id")
    val id: Int,

    @field:SerializedName("logoPath")
    val logoPath: String,

    @field:SerializedName("name")
    val name: String,

    @field:SerializedName("origin_country")
    val originCountry: String,
)

data class Genres(
    @field:SerializedName("id")
    val id: Int,

    @field:SerializedName("name")
    val name: String,
)

data class BelongsToCollection(
    @field:SerializedName("id")
    val id: Int,

    @field:SerializedName("name")
    val name: String,

    @field:SerializedName("poster_path")
    val posterPath: String,

    @field:SerializedName("backdrop_path")
    val backdropPath: String,
)