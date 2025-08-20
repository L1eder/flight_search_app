package com.example.flightsearchapp.ui.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shadow
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.flightsearchapp.R
import com.example.flightsearchapp.ui.components.AvailableRoutesList
import com.example.flightsearchapp.ui.components.BackgroundImage
import com.example.flightsearchapp.ui.components.FavoritesList
import com.example.flightsearchapp.ui.components.Header
import com.example.flightsearchapp.ui.components.SearchBar
import com.example.flightsearchapp.ui.components.SuggestionsList
import com.example.flightsearchapp.ui.viewmodel.FlightViewModel

@Composable
fun FlightSearchScreen(viewModel: FlightViewModel) {
    var searchQuery by remember { mutableStateOf("") }
    val isLoading by viewModel.isLoading.collectAsState()
    val suggestions by viewModel.airportSuggestions.collectAsState()
    val favoritesWithNames by viewModel.favoritesWithNames.collectAsState()
    val routes by viewModel.routes.collectAsState()
    val favorites by viewModel.favorites.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.searchQuery.collect { savedQuery ->
            searchQuery = savedQuery
            viewModel.searchAirports(savedQuery)
        }
    }

    BackgroundImage()

    Column(modifier = Modifier.padding(16.dp)) {
        Header()
        SearchBar(
            query = searchQuery,
            onQueryChange = { query ->
                searchQuery = query
                viewModel.updateSearchQuery(query)
            }
        )

        if (isLoading) {
            CircularProgressIndicator()
        }

        if (searchQuery.isBlank()) {
            if (favoritesWithNames.isNotEmpty()) {
                FavoritesList(
                    favoritesWithNames = favoritesWithNames,
                    onRemoveFavorite = { favorite ->
                        viewModel.removeFavorite(favorite)
                    }
                )
            } else {
                Text(
                    text = stringResource(R.string.warning),
                    modifier = Modifier.padding(bottom = 16.dp).padding(top = 16.dp),
                    fontSize = 26.sp,
                    style = TextStyle(
                        color = Color.White,
                        shadow = Shadow(
                            color = Color.Black.copy(alpha = 1f),
                            blurRadius = 8f
                        )
                    )
                )
            }
        }

        if (searchQuery.isNotBlank() && suggestions.isNotEmpty()) {
            SuggestionsList(
                suggestions = suggestions,
                onSuggestionClick = { airport ->
                    viewModel.onAirportSelected(airport)
                    searchQuery = airport.iataCode
                }
            )
        }

        if (searchQuery.isNotBlank() && routes.isNotEmpty()) {
            AvailableRoutesList(
                routes = routes,
                searchQuery = searchQuery,
                favorites = favorites,
                onAddFavorite = { departureCode, destinationCode ->
                    viewModel.addFavoriteRoute(departureCode, destinationCode)
                },
                onRemoveFavorite = { favorite ->
                    viewModel.removeFavorite(favorite)
                }
            )
        }
    }
}
