package com.nodecoyote.happybelly.server

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

interface Router{ val router get() = RouterMap.router }
class RouterMap{ companion object { val router = RouterImplemented() } }

class RouterImplemented {

    private val baseRoute = "https://api.yelp.com/v3/"

    fun build(): YelpRoutes {
        val route = Retrofit.Builder()
            .baseUrl(baseRoute)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        return route.create(YelpRoutes::class.java)
    }
}