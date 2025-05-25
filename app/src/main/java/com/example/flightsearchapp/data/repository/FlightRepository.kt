package com.example.flightsearchapp.data.repository

import com.example.flightsearchapp.data.database.FavoriteDao
import com.example.flightsearchapp.data.database.AirportDao
import com.example.flightsearchapp.data.models.Favorite

class FlightRepository(private val airportDao: AirportDao, private val favoriteDao: FavoriteDao) {
    suspend fun searchAirports(query: String) = airportDao.searchAirports(query)
    suspend fun insertFavorite(favorite: Favorite) = favoriteDao.insertFavorite(favorite)
    suspend fun getFavorites() = favoriteDao.getFavorites()
    suspend fun getAirportByCode(code: String) = airportDao.getAirportByCode(code)
    suspend fun getAirportSuggestions(query: String) = airportDao.getAirportSuggestions(query)
    suspend fun getDestinations(departureCode: String) = airportDao.getDestinations(departureCode)
    suspend fun deleteFavorite(favorite: Favorite) {favoriteDao.deleteFavorite(favorite)}
}
