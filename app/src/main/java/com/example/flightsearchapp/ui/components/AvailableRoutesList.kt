package com.example.flightsearchapp.ui.components

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.CardDefaults
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
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 4.dp),
                shape = MaterialTheme.shapes.medium,
                colors = CardDefaults.cardColors(
                    containerColor = Color.White,
                    contentColor = MaterialTheme.colorScheme.onSurface
                )
            ) {
                Row(
                    modifier = Modifier
                        .padding(12.dp)
                        .fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = "$searchQuery -> ${destinationAirport.iataCode} (${destinationAirport.name})",
                        modifier = Modifier.weight(1f),
                        style = MaterialTheme.typography.bodyMedium
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
                            contentDescription = null,
                            tint = if (isFavorite) MaterialTheme.colorScheme.surfaceTint else Color.Gray
                        )
                    }
                }
            }
        }
    }
}
