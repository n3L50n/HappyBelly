package com.nodecoyote.happybelly

import com.google.gson.annotations.SerializedName
import org.junit.Before
import org.junit.Test

class MockHappyBellyRepo {

    private lateinit var api: MockYelpApi

    @Before
    fun setupApi(){
        api = MockYelpApi()
    }

    @Test
    fun searchReturnsNull() {
        assert(api.mockSearchResponse(MockResponseType.Null) == null)
    }

    @Test
    fun searchReturnsEmpty(){
        assert(api.mockSearchResponse(MockResponseType.Empty) == MockResponse(emptyList()))
    }

    @Test
    fun searchReturnsFull(){
        assert(api.mockSearchResponse(MockResponseType.Full) == fullResponse())
    }

    @Test
    fun searchReturnsPartial(){
        assert(api.mockSearchResponse(MockResponseType.Partial) == partialResponse())
    }

    // NOTE: An image is null in partial data, so this test should fail
    @Test
    fun imagesAreNotNull(){
        val images = api.mockSearchResponse(MockResponseType.Partial)?.businesses?.map { it.imageUrl != null }!!
        images.let { verified -> verified.forEach { assert(it) } }
    }

    @Test
    fun idsAreNotNull(){
        val ids = api.mockSearchResponse(MockResponseType.Partial)?.businesses?.map { it.id != null }!!
        ids.let { verified -> verified.forEach { assert(it) } }
    }

    @Test
    fun namesAreNotNull(){
        val names = api.mockSearchResponse(MockResponseType.Partial)?.businesses?.map { it.name!= null }!!
        names.let { verified -> verified.forEach { assert(it) } }
    }

    @Test
    fun categoriesAreNotNull(){
        val categories = api.mockSearchResponse(MockResponseType.Partial)?.businesses?.map { it.categories!= null }!!
        categories.let { verified -> verified.forEach { assert(it) } }
    }

    @Test
    fun ratingsAreNotNull(){
        val ratings = api.mockSearchResponse(MockResponseType.Partial)?.businesses?.map { it.rating!= null }!!
        ratings.let { verified -> verified.forEach { assert(it) } }
    }

    @Test
    fun pricesAreNotNull(){
        val prices = api.mockSearchResponse(MockResponseType.Partial)?.businesses?.map { it.price!= null }!!
        prices.let { verified -> verified.forEach { assert(it) } }
    }

}

data class MockSearchResult(

    @SerializedName("id") val id: String?,
    @SerializedName("name") val name: String?,
    @SerializedName("image_url") val imageUrl: String?,
    @SerializedName("rating") val rating: Double?,
    @SerializedName("price") val price: String?,
    @SerializedName("categories") val categories: List<MockCategoryAlias>?
)

data class MockCategoryAlias(
    @SerializedName("alias") val alias: String,
    @SerializedName("title") val title: String
)

enum class MockResponseType { Null, Empty, Full, Partial }

data class MockResponse(
    val businesses: List<MockSearchResult>
)

class MockYelpApi {

    fun mockSearchResponse(response: MockResponseType): MockResponse? {
        return when (response) {
            MockResponseType.Null -> null
            MockResponseType.Full -> fullResponse()
            MockResponseType.Empty -> MockResponse(emptyList())
            MockResponseType.Partial -> partialResponse()
        }
    }

}

fun fullResponse() = MockResponse(
    listOf(
        MockSearchResult(
            "8729h8f9sdhfa",
            "Node Coyote Scraps",
            "image_url",
            3.5,
            "$",
            listOf(
                MockCategoryAlias(
                    "food",
                    "Food"
                ),
                MockCategoryAlias(
                    "beer",
                    "Beer"
                ),
                MockCategoryAlias(
                    "cannabis",
                    "Cannabis"
                ),
                MockCategoryAlias(
                    "art",
                    "Art"
                )
            )
        )
    )
)

fun partialResponse() = MockResponse(
    listOf(
        MockSearchResult(
            "8729h8f9sdhfa",
            "Node Coyote Scraps",
            null,
            3.5,
            "",
            emptyList()
        )
    )
)