package com.resa

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.resa.data.network.model.journeys.QueryJourneysParams
import com.resa.data.network.model.journeys.response.TransportMode
import com.resa.data.network.model.location.QueryLocationsParams
import com.resa.data.network.services.JourneysService
import com.resa.data.network.services.RetrofitService
import com.resa.domain.usecases.QueryJourneysUseCase
import com.resa.domain.usecases.QueryLocationsUseCase
import com.resa.domain.usecases.RefreshTokenUseCase
import com.resa.ui.theme.ResaTheme
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    @Inject lateinit var refreshTokenUseCase: RefreshTokenUseCase
    @Inject lateinit var queryJourneysUseCase: QueryJourneysUseCase
    @Inject lateinit var queryLocationsUseCase: QueryLocationsUseCase

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ResaTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    Greeting("Android")
                }
            }
        }

        GlobalScope.launch {
            val service = RetrofitService.getInstance(JourneysService::class.java)

            val param = QueryJourneysParams(
                originName = "Paris",
                transportModes = listOf(TransportMode.bike, TransportMode.bus),
            )
            val map = mapOf<String, String>()
            refreshTokenUseCase()
//            queryJourneysUseCase(
//                queryJourneysParams = QueryJourneysParams(
//                    originLatitude = 57.707289,
//                    originLongitude = 11.970027,
//                    destinationLatitude = 57.721053,
//                    destinationLongitude = 11.953236,
//                )
//            )

            queryLocationsUseCase(
                queryLocationsParams =
                QueryLocationsParams.ByText(
                    query = "Smörkärnegatan",
                )
            )

            queryLocationsUseCase(
                queryLocationsParams =
                QueryLocationsParams.ByCoordinates(
                    latitude = 57.707289,
                    longitude = 11.970027,
                )
            )
        }
    }
}

@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(
        text = "Hello $name!",
        modifier = modifier
    )
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    ResaTheme {
        Greeting("Android")
    }
}