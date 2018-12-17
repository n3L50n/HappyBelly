package com.nodecoyote.happybelly.ui.main

import android.Manifest
import android.annotation.SuppressLint
import android.app.PendingIntent
import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.os.VibrationEffect
import android.os.Vibrator
import android.support.v4.app.ActivityCompat
import android.support.v4.app.Fragment
import android.support.v4.content.ContextCompat
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.SearchView
import android.util.Log
import android.view.LayoutInflater
import android.view.Menu
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.google.android.gms.common.api.GoogleApiClient
import com.google.android.gms.location.*
import com.nodecoyote.happybelly.R
import com.nodecoyote.happybelly.adapter.HappyBellyAdapter
import com.nodecoyote.happybelly.utility.buttonFeedback
import com.nodecoyote.happybelly.viewmodel.HappyBellyListViewModel
import kotlinx.android.synthetic.main.cell_happy_belly_empty_results.*
import kotlinx.android.synthetic.main.cell_happy_belly_location.*
import kotlinx.android.synthetic.main.cell_happy_belly_search_results.*
import kotlinx.android.synthetic.main.main_fragment.*

class MainFragment : Fragment() {

    private lateinit var fusedLocationClient: FusedLocationProviderClient

    companion object {
        private const val REQUEST_CODE = 420
        private const val empty = 0
        private const val results = 1
        private const val location = 2
        fun newInstance() = MainFragment()
    }

    private lateinit var viewModel: HappyBellyListViewModel
    private lateinit var locationCallback: LocationCallback
    private lateinit var googleApiClient: GoogleApiClient

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return inflater.inflate(R.layout.main_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProviders.of(this).get(HappyBellyListViewModel::class.java)
        setupRecycler()
        checkLocation()
        setHasOptionsMenu(true)
    }

    override fun onPrepareOptionsMenu(menu: Menu?) {
        super.onPrepareOptionsMenu(menu)

        val searchItem = menu?.findItem(R.id.happy_belly_search)
        val searchView: SearchView = searchItem?.actionView as SearchView
        searchView.queryHint = resources.getString(R.string.search_hint)

        val queryListener = object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(term: String?): Boolean {
                Log.v("Main", "SEARCH SUBMITTED: $term")
                term?.let { viewModel.search(it) }
                return true
            }

            override fun onQueryTextChange(term: String?): Boolean {
                Log.v("Main", "SEARCH CHANGED: $term")
                term?.let { viewModel.search(it) }
                return false
            }
        }
        searchView.setOnQueryTextListener(queryListener)
    }

    private fun setupRecycler() {
        val adapter = HappyBellyAdapter(viewModel, this@MainFragment)
        happy_belly_recycler?.layoutManager = LinearLayoutManager(context)
        happy_belly_recycler?.adapter = adapter

        viewModel.searchResultCount.observe(this@MainFragment, Observer { count ->
            if (count != null && count > 0) {
                happy_belly_flipper.displayedChild = results
            } else {
                happy_belly_flipper.displayedChild = empty
            }
        })
    }

    private fun requestPermissions() {
        activity?.let { mainActivity ->
            ActivityCompat.requestPermissions(
                mainActivity,
                arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                REQUEST_CODE
            )
        }
    }

    private fun checkLocation() {
        activity?.let { mainActivity ->
            val locationPermission =
                ContextCompat.checkSelfPermission(mainActivity, Manifest.permission.ACCESS_COARSE_LOCATION)
            if (locationPermission != PackageManager.PERMISSION_GRANTED) {
                happy_belly_flipper.displayedChild = location
                happy_belly_location_allow.setOnClickListener {
                    requestPermissions()
                    context?.buttonFeedback()
                }
            } else {
                setupLocation()
                happy_belly_flipper.displayedChild = empty
            }
        }
    }

    @SuppressLint("MissingPermission")
    private fun setupLocation() {
        activity?.let { mainActivity ->
            fusedLocationClient = LocationServices.getFusedLocationProviderClient(mainActivity)
            locationCallback = object : LocationCallback() {
                override fun onLocationResult(locationResult: LocationResult?) {
                    super.onLocationResult(locationResult)
                    viewModel.userLocation.postValue(locationResult?.lastLocation)
                }
            }
            fusedLocationClient.requestLocationUpdates(
                LocationRequest.create().setNumUpdates(10),
                locationCallback,
                null
            )
        }
    }

}