package com.nodecoyote.happybelly.viewmodel

import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import android.location.Location
import android.util.Log
import com.nodecoyote.happybelly.repo.HappyBellyRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch


class HappyBellyListViewModel : ViewModel(), HappyBellyRepository {

    val userLocation: MutableLiveData<Location> by lazy { MutableLiveData<Location>() }
    val searchResultCount: MutableLiveData<Int> by lazy { MutableLiveData<Int>() }
    private lateinit var searchResults: List<SearchResult>

    fun search(term: String) {
        GlobalScope.launch(Dispatchers.IO) {
            userLocation.value?.let { currentLocation ->
                searchResults = happyBellyRepository.search(term, currentLocation)
                searchResultCount.postValue(searchResults.count())
            }
        }
    }

    fun getViewModelFor(position: Int): HappyBellyCellViewModel {
        return HappyBellyCellViewModel(searchResults[position])
    }
}