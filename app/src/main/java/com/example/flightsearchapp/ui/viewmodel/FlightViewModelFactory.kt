package com.example.flightsearchapp.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.flightsearchapp.data.repository.AirportRepository
import com.example.flightsearchapp.data.repository.FavoriteRepository
import com.example.flightsearchapp.preferences.PreferencesManager

class FlightViewModelFactory(
    private val airportRepository: AirportRepository,
    private val favoriteRepository: FavoriteRepository,
    private val preferencesManager: PreferencesManager
) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return FlightViewModel(airportRepository, favoriteRepository, preferencesManager) as T
    }
}
