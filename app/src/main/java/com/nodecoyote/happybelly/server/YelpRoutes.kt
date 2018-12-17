package com.nodecoyote.happybelly.server

import com.google.gson.JsonObject
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Path
import retrofit2.http.Query

private const val API_KEY = "A7tc1SEzsdtrm1zbSU3Vb-4tAVfPYPkYdqmzDvDigNOPFp4VuBq90SJqV5AXX4e8cOE5vCjjpe3CY6lMWskgZMlufamnf1EUZhFbbDU8LHKyTL8DQrSxWORS9C4VXHYx"

interface YelpRoutes {
    @Headers("Authorization: Bearer $API_KEY")
    @GET("businesses/search")
    fun search(
        @Query("term") term: String,
        @Query("latitude")latitude: Double,
        @Query("longitude")longitude: Double,
        @Query("radius")radius: Int): Call<JsonObject>

    @Headers("Authorization: Bearer $API_KEY")
    @GET("businesses/{id}/reviews")
    fun fetchReview(
        @Path("id") id: String): Call<JsonObject>
}