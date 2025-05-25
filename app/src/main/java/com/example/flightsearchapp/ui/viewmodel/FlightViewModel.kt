package com.example.flightsearchapp.ui.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.flightsearchapp.data.models.Airport
import com.example.flightsearchapp.data.models.Favorite
import com.example.flightsearchapp.data.repository.FlightRepository
import com.example.flightsearchapp.preferences.PreferencesManager
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class FlightViewModel(private val repository: FlightRepository, private val preferencesManager: PreferencesManager) : ViewModel() {
    private val _airports = MutableStateFlow<List<Airport>>(emptyList())
    val airports: StateFlow<List<Airport>> get() = _airports

    private val _airportSuggestions = MutableStateFlow<List<Airport>>(emptyList())
    val airportSuggestions: StateFlow<List<Airport>> get() = _airportSuggestions

    private val _favorites = MutableStateFlow<List<Favorite>>(emptyList())
    val favorites: StateFlow<List<Favorite>> get() = _favorites

    private val _routes = MutableStateFlow<List<Airport>>(emptyList())
    val routes: StateFlow<List<Airport>> get() = _routes

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> get() = _isLoading

    private val _allAirports = MutableStateFlow<List<Airport>>(emptyList())
    val allAirports: StateFlow<List<Airport>> get() = _allAirports

    private val _favoritesWithNames = MutableStateFlow<List<Pair<Favorite, Pair<String, String>>>>(emptyList())
    val favoritesWithNames: StateFlow<List<Pair<Favorite, Pair<String, String>>>> get() = _favoritesWithNames

    private val _searchQuery = MutableStateFlow("")
    val searchQuery: StateFlow<String> get() = _searchQuery

    init {
        viewModelScope.launch {
            preferencesManager.searchQueryFlow.collect { query ->
                _searchQuery.value = query
                searchAirports(query)
                loadFavorites()
            }
        }
    }

    fun updateSearchQuery(query: String) {
        _searchQuery.value = query
        saveSearchQuery(query)
        _routes.value = emptyList()
        searchAirports(query)
    }

    fun searchAirports(query: String) {
        viewModelScope.launch {
            _isLoading.value = true
            val results = repository.searchAirports("%$query%")
            _airports.value = results
            if (results.isEmpty()) {
                _routes.value = emptyList()
            }
            loadSuggestions(query)
            _isLoading.value = false
        }
    }


    private suspend fun loadSuggestions(query: String) {
        _airportSuggestions.value = repository.getAirportSuggestions("%$query%")
    }

    fun onAirportSelected(airport: Airport) {
        viewModelScope.launch {
            val destinations = repository.getDestinations(airport.iataCode)
            _routes.value = if (destinations.isNotEmpty()) destinations else emptyList() // Обновляем маршруты
        }
    }

    private fun loadFavorites() {
        viewModelScope.launch {
            _favorites.value = repository.getFavorites()
            loadFavoritesWithNames()
        }
    }

    fun addFavoriteRoute(departureCode: String, destinationCode: String) {
        viewModelScope.launch {
            if (departureCode.isNotEmpty() && destinationCode.isNotEmpty()) {
                val existingFavorite = favorites.value.find {
                    it.departureCode == departureCode && it.destinationCode == destinationCode
                }
                if (existingFavorite == null) {
                    val favorite = Favorite(departureCode = departureCode, destinationCode = destinationCode)
                    repository.insertFavorite(favorite)
                    loadFavorites()
                }
            }
        }
    }

    fun removeFavorite(favorite: Favorite) {
        viewModelScope.launch {
            repository.deleteFavorite(favorite)
            loadFavorites()
        }
    }

    private fun loadFavoritesWithNames() {
        viewModelScope.launch {
            val favs = repository.getFavorites()
            val favsWithNames = favs.map { fav ->
                val depAirport = repository.getAirportByCode(fav.departureCode)
                val destAirport = repository.getAirportByCode(fav.destinationCode)
                fav to (Pair(depAirport?.name ?: fav.departureCode, destAirport?.name ?: fav.destinationCode))
            }
            _favoritesWithNames.value = favsWithNames
        }
    }

    private fun saveSearchQuery(query: String) {
        viewModelScope.launch {
            preferencesManager.saveSearchQuery(query)
        }
    }
}
