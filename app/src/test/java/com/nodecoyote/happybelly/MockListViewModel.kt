package com.nodecoyote.happybelly

import android.arch.lifecycle.ViewModel
import com.nodecoyote.happybelly.viewmodel.HappyBellyCellViewModel
import org.junit.Before
import org.junit.Test
import java.lang.Error
import kotlin.math.roundToInt

class MockListViewModel : ViewModel() {

    private lateinit var api: MockYelpApi

    @Before
    fun setup() {
        api = MockYelpApi()
    }

    private fun results(): List<MockSearchResult>?{
        return api.mockSearchResponse(MockResponseType.Full)?.businesses
    }

    @Test
    fun mockGetViewModelFor_results(){
        results()?.forEachIndexed { index, _ ->
            getViewModelFor(index)
        }
    }


    // This test illustrates a problem with the pattern I'm using to populate viewmodels in a list
    // Sometimes the test fails because the results are null
    @Test
    fun mockGetViewModelFor_null(){
        val random = randomResults()
        val last = random!!.last()
        val lastPosition = random.indexOf(last)
        getViewModelFor(lastPosition)
    }

    private fun randomResults(): List<MockSearchResult>?{
        return if (Math.random().roundToInt()%2 == 0) results() else null
    }

    private fun getViewModelFor(position: Int): MockCellViewModel{
        return MockCellViewModel(results()?.get(position)?:throw Error("Cannot create ViewModel from null results"))
    }
}
class MockCellViewModel(result: MockSearchResult)