package com.example.flightsearchapp.data.database

import android.content.Context
import androidx.room.Room

object DatabaseProvider {
    fun getDatabase(context: Context): FlightSearchDatabase {
        return Room.databaseBuilder(
            context,
            FlightSearchDatabase::class.java,
            "flight_search.db"
        )
            .createFromAsset("flight_search.db")
            .build()
    }
}