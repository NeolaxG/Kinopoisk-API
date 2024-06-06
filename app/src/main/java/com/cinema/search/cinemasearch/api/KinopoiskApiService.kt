package com.cinema.search.cinemasearch.api

import com.google.gson.JsonArray
import com.google.gson.JsonObject
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Headers
import retrofit2.http.Query

interface KinopoiskApiService {
    @Headers("X-API-KEY:2XDHEJ1-R384X90-JKR52C9-YTZ4HHG")
    @GET("v1.4/movie?page=1&limit=250&selectFields=name&selectFields=description&selectFields=year&selectFields=rating&selectFields=poster&lists=top250")
    fun getTopMovies(
        @Query("selectFields") fields: String = "name"
    ): Call<JsonObject>
}