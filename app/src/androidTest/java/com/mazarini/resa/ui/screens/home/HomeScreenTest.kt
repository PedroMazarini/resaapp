package com.mazarini.resa.ui.screens.home

import androidx.compose.runtime.Composable
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.*
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.mazarini.resa.global.fake.FakeFactory
import com.mazarini.resa.ui.navigation.Route.Departures
import com.mazarini.resa.ui.navigation.Route.LocationSearch
import com.mazarini.resa.ui.screens.home.components.*
import com.mazarini.resa.ui.screens.home.components.bars.HomeSearchBar
import com.mazarini.resa.ui.screens.home.state.HomeUiEvent
import com.mazarini.resa.ui.theme.ResaTheme
import kotlinx.coroutines.flow.MutableStateFlow
import com.mazarini.resa.ui.screens.home.state.HomeUiState
import com.mazarini.resa.ui.screens.home.model.SavedJourneyState
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class HomeScreenTest {
    @get:Rule
    val composeTestRule = createComposeRule()

    private val fakeSavedJourneys = listOf(FakeFactory.journeySearch().copy(id = "1"))
    private val fakeRecentJourneys = listOf(FakeFactory.journeySearch().copy(id = "2"))
    private val fakeHomeJourney = FakeFactory.journey().copy(id = "3")

    @Test
    fun testSavedJourneyItemClick_triggersOnEvent() {
        // Given
        var clicked = false
        setResaContent {
            SavedJourneyItem(
                journeySearch = fakeSavedJourneys[0],
                onItemClicked = { clicked = true }
            )
        }
        // When
        onTag("SavedJourneyItem_1").performClick()
        // Then
        assert(clicked)
    }

    @Test
    fun testSavedJourneyItemDelete_triggersOnEvent() {
        // Given
        var deleted = false
        setResaContent {
            SavedJourneyItem(
                journeySearch = fakeSavedJourneys[0],
                showDeleteButton = true,
                onDelete = { deleted = true }
            )
        }
        // When
        onTag("DeleteSavedJourneyItem_1").performClick()
        // Then
        assert(deleted)
    }

    @Test
    fun testSavedJourneyAddClick_triggersOnEvent() {
        // Given
        var added = false
        setResaContent {
            SavedJourneyAdd(onAddClicked = { added = true })
        }
        // When
        onTag("SavedJourneyAdd").performClick()
        // Then
        assert(added)
    }

    @Test
    fun testHomeSearchBarClick_triggersOnEvent() {
        // Given
        var searchClicked = false
        setResaContent {
            HomeSearchBar(onSearchBarClicked = { searchClicked = true })
        }
        // When
        onTag("HomeSearchBar").performClick()
        // Then
        assert(searchClicked)
    }

    @Test
    fun testHomeTimeTableClick_triggersOnEvent() {
        // Given
        var timetableClicked = false
        setResaContent {
            HomeSearchBar(onTimeTableClicked = { timetableClicked = true })
        }
        // When
        onTag("HomeTimeTable").performClick()
        // Then
        assert(timetableClicked)
    }

    @Test
    fun testRecentJourneySearchItemClick_triggersOnEvent() {
        // Given
        var clicked = false
        setResaContent {
            RecentJourneySearchItem(
                journeySearch = fakeRecentJourneys[0],
                onEvent = { if (it is HomeUiEvent.OnSavedJourneyClicked) clicked = true }
            )
        }
        // When
        onTag("RecentJourneySearchItem_2").performClick()
        // Then
        assert(clicked)
    }

    @Test
    fun testRecentJourneySearchItemDelete_triggersOnEvent() {
        // Given
        var deleted = false
        setResaContent {
            RecentJourneySearchItem(
                journeySearch = fakeRecentJourneys[0],
                onEvent = { if (it is HomeUiEvent.DeleteRecentJourney) deleted = true }
            )
        }
        // When
        onTag("DeleteRecentJourneySearchItem_2").performClick()
        // Then
        assert(deleted)
    }

    @Test
    fun testSavedHomeJourneyCardClick_triggersOnEvent() {
        // Given
        var loaded = false
        setResaContent {
            SavedHomeJourneyCard(
                journey = fakeHomeJourney,
                onEvent = { if (it is HomeUiEvent.LoadSavedJourneyToHome) loaded = true },
                navigateTo = {}
            )
        }
        // When
        onTag("SavedHomeJourneyCard_3").performClick()
        // Then
        assert(loaded)
    }

    @Test
    fun testSavedHomeJourneyCardDelete_triggersOnEvent() {
        // Given
        var deleted = false
        setResaContent {
            SavedHomeJourneyCard(
                journey = fakeHomeJourney,
                onEvent = { if (it is HomeUiEvent.DeleteSavedJourneyToHome) deleted = true },
                navigateTo = {}
            )
        }
        // When
        onTag("DeleteSavedHomeJourneyCard_3").performClick()
        // Then
        assert(deleted)
    }

    @Test
    fun testHomeScreen_displaysSavedAndRecentJourneys() {
        // Given
        val state = MutableStateFlow(
            HomeUiState(
                savedJourneyState = SavedJourneyState.Loaded(fakeSavedJourneys),
                recentJourneysState = SavedJourneyState.Loaded(fakeRecentJourneys),
                pinnedHomeJourney = fakeHomeJourney
            )
        )
        setResaContent {
            HomeScreen(
                homeUiState = state,
                onEvent = {},
                navigateTo = {}
            )
        }
        // Then
        onTag("SavedJourneyItem_1").assertExists()
        onTag("RecentJourneySearchItem_2").assertExists()
        onTag("SavedHomeJourneyCard_3").assertExists()
    }

    @Test
    fun testHomeScreen_searchBarClick_navigatesToLocationSearch() {
        // Given
        val state = MutableStateFlow(HomeUiState())
        var navigated = false

        setResaContent {
            HomeScreen(
                homeUiState = state,
                onEvent = {},
                navigateTo = { route ->
                    if (route is LocationSearch) navigated = true
                }
            )
        }
        // When
        onTag("HomeSearchBar").performClick()
        // Then
        assert(navigated)
    }

    @Test
    fun testHomeScreen_timeTableClick_navigatesToDepartures() {
        // Given
        val state = MutableStateFlow(HomeUiState())
        var navigated = false
        setResaContent {
            HomeScreen(
                homeUiState = state,
                onEvent = {},
                navigateTo = { route ->
                    if (route == Departures) navigated = true
                }
            )
        }
        // When
        onTag("HomeTimeTable").performClick()
        // Then
        assert(navigated)
    }

    @Test
    fun testHomeScreen_savedJourneyClick_triggersOnEvent() {
        // Given
        val state = MutableStateFlow(
            HomeUiState(savedJourneyState = SavedJourneyState.Loaded(fakeSavedJourneys))
        )
        var eventTriggered = false
        setResaContent {
            HomeScreen(
                homeUiState = state,
                onEvent = { if (it is HomeUiEvent.OnSavedJourneyClicked) eventTriggered = true },
                navigateTo = {}
            )
        }
        // When
        onTag("SavedJourneyItem_1").performClick()
        // Then
        assert(eventTriggered)
    }

    private fun setResaContent(content: @Composable () -> Unit) {
        composeTestRule.setContent {
            ResaTheme {
                content()
            }
        }
    }

    private fun onTag(tag: String): SemanticsNodeInteraction {
        return composeTestRule.onNodeWithTag(tag)
    }
}
