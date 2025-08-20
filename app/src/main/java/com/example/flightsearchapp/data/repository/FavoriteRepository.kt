package com.example.flightsearchapp.data.repository

import com.example.flightsearchapp.data.database.FavoriteDao
import com.example.flightsearchapp.data.models.Favorite

class FavoriteRepository(private val favoriteDao: FavoriteDao) {
    suspend fun insertFavorite(favorite: Favorite) = favoriteDao.insertFavorite(favorite)
    suspend fun getFavorites() = favoriteDao.getFavorites()
    suspend fun deleteFavorite(favorite: Favorite) = favoriteDao.deleteFavorite(favorite)
}
