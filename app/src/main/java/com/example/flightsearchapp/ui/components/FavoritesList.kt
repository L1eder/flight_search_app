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
import com.example.flightsearchapp.data.models.Favorite

@Composable
fun FavoritesList(favoritesWithNames: List<Pair<Favorite, Pair<String, String>>>, onRemoveFavorite: (Favorite) -> Unit) {
    LazyColumn {
        items(favoritesWithNames) { (favorite, names) ->
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
                        text = "${favorite.departureCode} (${names.first}) - ${favorite.destinationCode} (${names.second})",
                        modifier = Modifier.weight(1f),
                    )
                    IconButton(onClick = { onRemoveFavorite(favorite) }) {
                        Icon(
                            imageVector = Icons.Default.Star,
                            contentDescription = "Remove from favorites",
                            tint = Color.Blue
                        )
                    }
                }
            }
        }
    }
}
