package com.nodecoyote.happybelly.repo

import android.location.Location
import android.util.Log
import com.google.gson.Gson
import com.google.gson.JsonObject
import com.nodecoyote.happybelly.server.Router
import com.nodecoyote.happybelly.viewmodel.SearchResult
import com.nodecoyote.happybelly.viewmodel.SearchResultReview
import retrofit2.Call

/**
 * Create a static instance of the repository and
 * an interface from which to pull functionality.
 */
interface HappyBellyRepository{ val happyBellyRepository get() = HappyBellyRepositoryMap.implemented }
class HappyBellyRepositoryMap{ companion object { val implemented = HappyBellyRepositoryImplemented() } }

/**
 * The implementation of the repository
 */
class HappyBellyRepositoryImplemented: Router {

    private val tenThousandMeters = 10000
    private val tag = "Repo"
    private val factory = router.build()

    fun search(term: String, userLocation: Location): List<SearchResult> {
        val lat = userLocation.latitude
        val long = userLocation.longitude
        val data = factory.search(
            term = term,
            latitude = lat,
            longitude = long,
            radius = tenThousandMeters
        ).execute().body()?.asJsonObject

        val rawBusinesses = data?.getAsJsonArray("businesses")
        val businesses = rawBusinesses?.map { business ->
            val gson = Gson()
            gson.fromJson(business.asJsonObject, SearchResult::class.java)
        }

        return businesses?: emptyList()
    }

    fun getReview(id: String): SearchResultReview? {
        val data = factory.fetchReview(id).execute().body()?.asJsonObject
        val rawReviews = data?.getAsJsonArray("reviews")
        val reviews = rawReviews?.map { review ->
            val gson = Gson()
            gson.fromJson(review.asJsonObject, SearchResultReview::class.java)
        }
        return reviews?.get(0)
    }
}