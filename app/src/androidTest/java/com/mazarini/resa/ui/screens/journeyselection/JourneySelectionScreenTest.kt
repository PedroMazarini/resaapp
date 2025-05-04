package com.mazarini.resa.ui.screens.journeyselection

import androidx.compose.runtime.Composable
import androidx.compose.ui.test.SemanticsNodeInteraction
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.performClick
import androidx.paging.PagingData
import com.mazarini.resa.global.fake.FakeFactory
import com.mazarini.resa.ui.screens.journeyselection.component.JourneyItem
import com.mazarini.resa.ui.screens.journeyselection.state.JourneySelectionUiEvent
import com.mazarini.resa.ui.screens.journeyselection.state.JourneySelectionUiState
import com.mazarini.resa.ui.theme.ResaTheme
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.flowOf
import org.junit.Rule
import org.junit.Test

class JourneySelectionScreenTest {
    @get:Rule
    val composeTestRule = createComposeRule()

    private val fakeJourney = FakeFactory.journey().copy(id = "1")
    private val fakeJourneys = listOf(fakeJourney)

    private fun setResaContent(content: @Composable () -> Unit) {
        composeTestRule.setContent {
            ResaTheme { content() }
        }
    }

    private fun onTag(tag: String): SemanticsNodeInteraction =
        composeTestRule.onNodeWithTag(tag)

    @Test
    fun journeyItemClick_triggersJourneySelectedEvent() {
        // Given
        var selectedId: String? = null
        setResaContent {
            JourneyItem(
                journey = fakeJourney,
                isSingleJourney = false,
                onJourneyClicked = { selectedId = it }
            )
        }
        // When
        onTag("JourneyItem_1").performClick()
        // Then
        assert(selectedId == "1")
    }

    @Test
    fun journeySelectionScreen_displaysJourneys() {
        // Given
        val state = MutableStateFlow(
            JourneySelectionUiState(upcomingJourneys = flowOf(PagingData.from(fakeJourneys)))
        )
        setResaContent {
            JourneySelectionScreen(
                journeySelectionUiState = state,
                onEvent = {},
                upPress = {},
            )
        }
        // Then
        onTag("JourneyItem_1").assertExists()
    }

    @Test
    fun journeySelectionScreen_selectingJourney_triggersJourneySelectedEvent() {
        // Given
        val state = MutableStateFlow(
            JourneySelectionUiState(upcomingJourneys = flowOf(PagingData.from(fakeJourneys)))
        )
        var eventTriggered = false
        setResaContent {
            JourneySelectionScreen(
                journeySelectionUiState = state,
                onEvent = {
                    if (it is JourneySelectionUiEvent.JourneySelected) eventTriggered = true
                },
                upPress = {},
            )
        }
        // When
        onTag("JourneyItem_1").performClick()
        // Then
        assert(eventTriggered)
    }

    @Test
    fun journeySelectionScreen_switchTabs_displaysCorrectJourneys() {
        // Given
        val upcomingJourney = fakeJourney.copy(id = "1")
        val previousJourney = fakeJourney.copy(id = "2")
        val state = MutableStateFlow(
            JourneySelectionUiState(
                upcomingJourneys = flowOf(PagingData.from(listOf(upcomingJourney))),
                passedJourneys = flowOf(PagingData.from(listOf(previousJourney)))
            )
        )
        setResaContent {
            JourneySelectionScreen(
                journeySelectionUiState = state,
                onEvent = {},
                upPress = {},
            )
        }
        // Then (Upcoming tab)
        onTag("JourneyItem_1").assertExists()
        onTag("JourneyItem_2").assertDoesNotExist()
        // When (switch to Previous tab)
        onTag("JourneysTab_Previous").performClick()
        // Then
        onTag("JourneyItem_2").assertExists()
        onTag("JourneyItem_1").assertDoesNotExist()
        // When (switch back to Upcoming tab)
        onTag("JourneysTab_Upcoming").performClick()
        // Then
        onTag("JourneyItem_1").assertExists()
        onTag("JourneyItem_2").assertDoesNotExist()
    }
}
