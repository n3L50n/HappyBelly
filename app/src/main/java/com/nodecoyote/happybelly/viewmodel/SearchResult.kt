package com.nodecoyote.happybelly.viewmodel

import com.google.gson.annotations.SerializedName


data class SearchResult(
    @SerializedName("id") val id: String,
    @SerializedName("name") val name: String,
    @SerializedName("image_url") val imageUrl: String,
    //@SerializedName("review") val review: String,
    @SerializedName("rating") val rating: Double,
    @SerializedName("price") val price: String,
    @SerializedName("categories") val categories: List<CategoryAlias>

)

data class SearchResultReview(
    val id: String,
    val text: String,
    val url: String
)

data class CategoryAlias(
    @SerializedName("alias") val alias: String,
    @SerializedName("title") val title: String
)