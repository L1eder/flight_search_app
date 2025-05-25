import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.flightsearchapp.ui.viewmodel.FlightViewModel

@Composable
fun FlightSearchScreen(viewModel: FlightViewModel) {
    var searchQuery by remember { mutableStateOf("") }
    val isLoading by viewModel.isLoading.collectAsState()
    val suggestions by viewModel.airportSuggestions.collectAsState()
    val favoritesWithNames by viewModel.favoritesWithNames.collectAsState()
    val routes by viewModel.routes.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.searchQuery.collect { savedQuery ->
            searchQuery = savedQuery
            viewModel.searchAirports(savedQuery)
        }
    }

    Column(modifier = Modifier.padding(16.dp)) {
        TextField(
            value = searchQuery,
            onValueChange = {
                searchQuery = it
                viewModel.updateSearchQuery(it)
            },
            label = { Text("Введите аэропорт или код ИАТА") }
        )

        if (isLoading) {
            CircularProgressIndicator()
        }

        if (searchQuery.isBlank()) {
            if (favoritesWithNames.isNotEmpty()) {
                Text("Избранные маршруты:")
                LazyColumn {
                    items(favoritesWithNames) { (favorite, names) ->
                        val (departureName, destinationName) = names
                        Text("${favorite.departureCode} ($departureName) - ${favorite.destinationCode} ($destinationName)")
                    }
                }
            } else {
                Text("Нет избранных маршрутов")
            }
        } else {
            if (suggestions.isNotEmpty()) {
                Text("Подсказки:")
                LazyColumn {
                    items(suggestions) { airport ->
                        Text(
                            text = "${airport.iataCode} - ${airport.name}",
                            modifier = Modifier
                                .clickable {
                                    viewModel.onAirportSelected(airport)
                                    searchQuery = airport.iataCode
                                }
                                .padding(8.dp)
                        )
                    }
                }
            }

            if (routes.isNotEmpty()) {
                Text("Доступные рейсы из $searchQuery:")
                LazyColumn {
                    items(routes) { destinationAirport ->
                        Text(
                            text = "$searchQuery -> ${destinationAirport.iataCode} (${destinationAirport.name})",
                            modifier = Modifier
                                .clickable {
                                    viewModel.addFavoriteRoute(searchQuery, destinationAirport.iataCode)
                                }
                                .padding(8.dp)
                        )
                    }
                }
            }
        }
    }
}

