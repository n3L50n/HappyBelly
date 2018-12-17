package com.nodecoyote.happybelly.viewmodel

import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import com.nodecoyote.happybelly.repo.HappyBellyRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class HappyBellyCellViewModel(searchResult: SearchResult): ViewModel(), HappyBellyRepository {
    val name: MutableLiveData<String> by lazy { MutableLiveData<String>() }
    val image: MutableLiveData<String> by lazy { MutableLiveData<String>() }
    val rating: MutableLiveData<Double> by lazy { MutableLiveData<Double>() }
    val price: MutableLiveData<String> by lazy { MutableLiveData<String>() }
    val review: MutableLiveData<String> by lazy { MutableLiveData<String>() }
    val url: MutableLiveData<String> by lazy { MutableLiveData<String>() }

    init {
        name.postValue(searchResult.name)
        image.postValue(searchResult.imageUrl)
        rating.postValue(searchResult.rating)
        price.postValue(searchResult.price)
        getReview(searchResult.id)
    }

    private fun getReview(id: String) {
        GlobalScope.launch(Dispatchers.IO) {
            val firstReview =  happyBellyRepository.getReview(id)
            review.postValue(firstReview?.text)
            url.postValue(firstReview?.url)
        }
    }
}