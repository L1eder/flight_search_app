package com.example.flightsearchapp.data.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.flightsearchapp.data.models.Airport
import com.example.flightsearchapp.data.models.Favorite

@Database(entities = [Airport::class, Favorite::class], version = 1)
abstract class FlightSearchDatabase : RoomDatabase() {
    abstract fun airportDao(): AirportDao
    abstract fun favoriteDao(): FavoriteDao
}
