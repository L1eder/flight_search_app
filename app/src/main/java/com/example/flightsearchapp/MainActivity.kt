package com.example.flightsearchapp

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.example.flightsearchapp.data.repository.FlightRepository
import com.example.flightsearchapp.preferences.PreferencesManager
import com.example.flightsearchapp.ui.viewmodel.FlightViewModel
import com.example.flightsearchapp.ui.viewmodel.FlightViewModelFactory
import androidx.datastore.preferences.preferencesDataStore
import com.example.flightsearchapp.data.database.DatabaseProvider
import com.example.flightsearchapp.ui.screens.FlightSearchScreen

val ComponentActivity.dataStore by preferencesDataStore(name = "settings")

class MainActivity : ComponentActivity() {
    private lateinit var viewModel: FlightViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val db = DatabaseProvider.getDatabase(this)

        val repository = FlightRepository(db.airportDao(), db.favoriteDao())

        val preferencesManager = PreferencesManager(dataStore)

        val viewModelFactory = FlightViewModelFactory(repository, preferencesManager)

        Log.d("ViewModel", "Initializing ViewModel...")
        viewModel = viewModelFactory.create(FlightViewModel::class.java)

        setContent {
            FlightSearchScreen(viewModel)
        }
    }
}