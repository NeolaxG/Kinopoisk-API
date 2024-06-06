package com.cinema.search.cinemasearch.api

import com.google.gson.annotations.SerializedName

data class MoviesResponse(
    @SerializedName("movies") val movies: List<Movie>
)
