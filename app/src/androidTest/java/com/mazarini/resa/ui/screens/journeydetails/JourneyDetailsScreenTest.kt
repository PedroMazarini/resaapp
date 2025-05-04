package com.mazarini.resa.ui.screens.journeydetails

import androidx.compose.runtime.Composable
import androidx.compose.ui.test.SemanticsNodeInteraction
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.performClick
import com.mazarini.resa.global.fake.FakeFactory
import com.mazarini.resa.ui.screens.journeydetails.state.JourneyDetailsUiEvent
import com.mazarini.resa.ui.screens.journeydetails.state.JourneyDetailsUiState
import com.mazarini.resa.ui.theme.ResaTheme
import kotlinx.coroutines.flow.MutableStateFlow
import org.junit.Rule
import org.junit.Test

class JourneyDetailsScreenTest {
    @get:Rule
    val composeTestRule = createComposeRule()

    private val fakeJourney = FakeFactory.journey().copy(id = "1")

    private fun setResaContent(content: @Composable () -> Unit) {
        composeTestRule.setContent {
            ResaTheme { content() }
        }
    }

    private fun onTag(tag: String): SemanticsNodeInteraction =
        composeTestRule.onNodeWithTag(tag)

    @Test
    fun journeyDetailsScreen_showsDetails_whenJourneyPresent() {
        // Given
        val state = MutableStateFlow(
            JourneyDetailsUiState(selectedJourney = fakeJourney)
        )
        setResaContent {
            JourneyDetailsScreen(
                journeyDetailsUiState = state,
                onEvent = {},
            )
        }
        // Then
        onTag("JourneySelected_${fakeJourney.id}").assertExists()
    }

    @Test
    fun journeyDetailsScreen_showsLoading_whenJourneyNull() {
        // Given
        val state = MutableStateFlow(
            JourneyDetailsUiState(selectedJourney = null)
        )
        setResaContent {
            JourneyDetailsScreen(
                journeyDetailsUiState = state,
                onEvent = {},
            )
        }
        onTag("DetailsTopBar_Back").assertDoesNotExist()
    }

    @Test
    fun journeyDetailsScreen_backButtonClick_triggersOnBackPressed() {
        // Given
        val state = MutableStateFlow(
            JourneyDetailsUiState(selectedJourney = fakeJourney)
        )
        var backPressed = false
        setResaContent {
            JourneyDetailsScreen(
                journeyDetailsUiState = state,
                onEvent = { if (it is JourneyDetailsUiEvent.OnBackPressed) backPressed = true },
            )
        }
        // When
        onTag("DetailsTopBar_Back").performClick()
        // Then
        assert(backPressed)
    }

    @Test
    fun journeyDetailsScreen_showsLegsSummaryBar_whenJourneyPresent() {
        // Given
        val state = MutableStateFlow(
            JourneyDetailsUiState(selectedJourney = fakeJourney)
        )
        setResaContent {
            JourneyDetailsScreen(
                journeyDetailsUiState = state,
                onEvent = {},
            )
        }
        // Then
        onTag("LegsSummaryBar").assertExists()
    }

    @Test
    fun journeyDetailsScreen_showsLegItems_whenJourneyPresent() {
        // Given
        val state = MutableStateFlow(
            JourneyDetailsUiState(selectedJourney = fakeJourney)
        )
        setResaContent {
            JourneyDetailsScreen(
                journeyDetailsUiState = state,
                onEvent = {},
            )
        }
        // Then
        fakeJourney.legs.forEach { leg ->
            onTag("LegSummaryItem_${leg.name}").assertExists()
        }
    }
}
