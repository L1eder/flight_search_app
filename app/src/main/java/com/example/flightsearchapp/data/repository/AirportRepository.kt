package com.example.flightsearchapp.data.repository

import com.example.flightsearchapp.data.database.AirportDao

class AirportRepository(private val airportDao: AirportDao) {
    suspend fun searchAirports(query: String) = airportDao.searchAirports(query)
    suspend fun getAirportByCode(code: String) = airportDao.getAirportByCode(code)
    suspend fun getAirportSuggestions(query: String) = airportDao.getAirportSuggestions(query)
    suspend fun getDestinations(departureCode: String) = airportDao.getDestinations(departureCode)
}
