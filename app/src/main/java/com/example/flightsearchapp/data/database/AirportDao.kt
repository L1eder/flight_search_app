package com.example.flightsearchapp.data.database

import androidx.room.Dao
import androidx.room.Query
import com.example.flightsearchapp.data.models.Airport

@Dao
interface AirportDao {
    @Query("SELECT * FROM airport WHERE name LIKE :query OR iata_code LIKE :query ORDER BY passengers DESC")
    suspend fun searchAirports(query: String): List<Airport>

    @Query("SELECT * FROM airport WHERE name LIKE :query OR iata_code LIKE :query ORDER BY passengers DESC LIMIT 10")
    suspend fun getAirportSuggestions(query: String): List<Airport>

    @Query("SELECT * FROM airport WHERE iata_code != :departureCode ORDER BY passengers DESC")
    suspend fun getDestinations(departureCode: String): List<Airport>

    @Query("SELECT * FROM airport WHERE iata_code = :code LIMIT 1")
    suspend fun getAirportByCode(code: String): Airport?
}