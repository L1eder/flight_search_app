package com.example.flightsearchapp.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.flightsearchapp.data.models.Airport
import com.example.flightsearchapp.data.models.Favorite
import com.example.flightsearchapp.data.repository.AirportRepository
import com.example.flightsearchapp.data.repository.FavoriteRepository
import com.example.flightsearchapp.preferences.PreferencesManager
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class FlightViewModel(
    private val airportRepository: AirportRepository,
    private val favoriteRepository: FavoriteRepository,
    private val preferencesManager: PreferencesManager
) : ViewModel() {

    private val _airports = MutableStateFlow<List<Airport>>(emptyList())

    private val _airportSuggestions = MutableStateFlow<List<Airport>>(emptyList())
    val airportSuggestions: StateFlow<List<Airport>> get() = _airportSuggestions

    private val _favorites = MutableStateFlow<List<Favorite>>(emptyList())
    val favorites: StateFlow<List<Favorite>> get() = _favorites

    private val _routes = MutableStateFlow<List<Airport>>(emptyList())
    val routes: StateFlow<List<Airport>> get() = _routes

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> get() = _isLoading

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
            val results = airportRepository.searchAirports(query)
            _airports.value = results
            if (results.isEmpty()) {
                _routes.value = emptyList()
            }
            loadSuggestions(query)
            _isLoading.value = false
        }
    }

    private suspend fun loadSuggestions(query: String) {
        _airportSuggestions.value = airportRepository.getAirportSuggestions(query)
    }

    fun onAirportSelected(airport: Airport) {
        viewModelScope.launch {
            val destinations = airportRepository.getDestinations(airport.iataCode)
            _routes.value = if (destinations.isNotEmpty()) destinations else emptyList()
        }
    }

    private fun loadFavorites() {
        viewModelScope.launch {
            _favorites.value = favoriteRepository.getFavorites()
            loadFavoritesWithNames()
        }
    }

    private suspend fun refreshFavorites() {
        _favorites.value = favoriteRepository.getFavorites()
        loadFavoritesWithNames()
    }

    fun addFavoriteRoute(departureCode: String, destinationCode: String) {
        viewModelScope.launch {
            if (departureCode.isNotEmpty() && destinationCode.isNotEmpty()) {
                val favorite = Favorite(departureCode = departureCode, destinationCode = destinationCode)

                val existingFavorites = favoriteRepository.getFavorites()

                if (!existingFavorites.contains(favorite)) {
                    favoriteRepository.insertFavorite(favorite)
                    refreshFavorites()
                }
            }
        }
    }

    fun removeFavorite(favorite: Favorite) {
        viewModelScope.launch {
            favoriteRepository.deleteFavorite(favorite)

            val updatedFavorites = _favorites.value.toMutableList()
            updatedFavorites.remove(favorite)
            refreshFavorites()
        }
    }

    private fun loadFavoritesWithNames() {
        viewModelScope.launch {
            val favs = favoriteRepository.getFavorites()
            val favsWithNames = favs.map { fav ->
                val depAirport = airportRepository.getAirportByCode(fav.departureCode)
                val destAirport = airportRepository.getAirportByCode(fav.destinationCode)
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