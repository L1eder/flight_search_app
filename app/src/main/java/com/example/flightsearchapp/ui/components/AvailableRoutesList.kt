package com.example.flightsearchapp.ui.components

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Card
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.flightsearchapp.data.models.Airport
import com.example.flightsearchapp.data.models.Favorite

@Composable
fun AvailableRoutesList(
    routes: List<Airport>,
    searchQuery: String,
    favorites: List<Favorite>,
    onAddFavorite: (String, String) -> Unit,
    onRemoveFavorite: (Favorite) -> Unit
) {
    LazyColumn {
        items(routes) { destinationAirport ->
            val favoriteToRemove = favorites.find {
                it.departureCode == searchQuery && it.destinationCode == destinationAirport.iataCode
            }
            val isFavorite = favoriteToRemove != null

            Card(
                modifier = Modifier.padding(8.dp),
                elevation = 4.dp
            ) {
                Row(
                    modifier = Modifier
                        .padding(16.dp)
                        .fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = "$searchQuery -> ${destinationAirport.iataCode} (${destinationAirport.name})",
                        modifier = Modifier.weight(1f),
                    )
                    IconButton(onClick = {
                        if (isFavorite && favoriteToRemove != null) {
                            onRemoveFavorite(favoriteToRemove)
                        } else {
                            onAddFavorite(searchQuery, destinationAirport.iataCode)
                        }
                    }) {
                        Icon(
                            imageVector = Icons.Default.Star,
                            contentDescription = "Toggle favorite",
                            tint = if (isFavorite) Color.Yellow else Color.Gray
                        )
                    }
                }
            }
        }
    }
}
