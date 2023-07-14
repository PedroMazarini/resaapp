package com.resa.ui.application

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.resa.data.network.services.JourneysService
import com.resa.data.network.services.RetrofitService
import com.resa.domain.model.queryjourneys.QueryJourneysParams
import com.resa.domain.model.TransportMode
import com.resa.domain.usecases.location.QueryLocationByTextUseCase
import com.resa.domain.usecases.RefreshTokenUseCase
import com.resa.ui.navigation.NavigationHost
import com.resa.ui.theme.ResaTheme
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    @Inject lateinit var refreshTokenUseCase: RefreshTokenUseCase
//    @Inject lateinit var queryJourneysUseCase: QueryJourneysUseCase
    @Inject lateinit var queryLocationByTextUseCase: QueryLocationByTextUseCase

    @OptIn(DelicateCoroutinesApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ResaTheme {
                NavigationHost()
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

//            queryLocationByTextUseCase(
//                queryLocationsParams =
//                QueryLocationsParams.ByText(
//                    query = "Smörkärnegatan",
//                )
//            )
//
//            queryLocationByTextUseCase(
//                queryLocationsParams =
//                QueryLocationsParams.ByCoordinates(
//                    latitude = 57.707289,
//                    longitude = 11.970027,
//                )
//            )
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