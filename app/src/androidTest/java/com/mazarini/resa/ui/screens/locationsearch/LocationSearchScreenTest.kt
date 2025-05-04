package com.mazarini.resa.ui.screens.locationsearch

import androidx.compose.runtime.Composable
import androidx.compose.ui.test.SemanticsNodeInteraction
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.onNodeWithTag
import com.mazarini.resa.global.fake.FakeFactory
import com.mazarini.resa.ui.screens.locationsearch.components.LocationItem
import com.mazarini.resa.ui.screens.locationsearch.state.LocationSearchUiEvent
import com.mazarini.resa.ui.screens.locationsearch.state.LocationSearchUiState
import com.mazarini.resa.ui.theme.ResaTheme
import kotlinx.coroutines.flow.MutableStateFlow
import org.junit.Rule
import org.junit.Test

class LocationSearchScreenTest {
    @get:Rule
    val composeTestRule = createComposeRule()

    private val fakeLocation = FakeFactory.location().copy(id = "1", name = "Test Location")
    private val fakeSavedLocations = listOf(fakeLocation)
    private val fakeRecentLocations = listOf(FakeFactory.location().copy(id = "2", name = "Recent Location"))

    private fun setResaContent(content: @Composable () -> Unit) {
        composeTestRule.setContent {
            ResaTheme { content() }
        }
    }

    private fun onTag(tag: String): SemanticsNodeInteraction =
        composeTestRule.onNodeWithTag(tag)

    @Test
    fun locationItemClick_triggersOnLocationSelected() {
        // Given
        var selected = false
        setResaContent {
            LocationItem(
                location = fakeLocation,
                onLocationSelected = { selected = true }
            )
        }
        // When
        onTag("LocationItem_1").performClick()
        // Then
        assert(selected)
    }

    @Test
    fun locationItemSaveClick_triggersSaveLocation() {
        // Given
        var saved = false
        setResaContent {
            LocationItem(
                location = fakeLocation,
                isSaved = false,
                saveLocation = { saved = true }
            )
        }
        // When
        onTag("LocationItemSaveDelete_1").performClick()
        // Then
        assert(saved)
    }

    @Test
    fun locationItemDeleteClick_triggersDeleteLocation() {
        // Given
        var deleted = false
        setResaContent {
            LocationItem(
                location = fakeLocation,
                isSaved = true,
                deleteLocation = { deleted = true }
            )
        }
        // When
        onTag("LocationItemSaveDelete_1").performClick()
        // Then
        assert(deleted)
    }

    @Test
    fun locationSearchScreen_displaysSavedAndRecentLocations() {
        // Given
        val state = MutableStateFlow(
            LocationSearchUiState(
                savedLocations = fakeSavedLocations,
                recentLocations = fakeRecentLocations
            )
        )
        setResaContent {
            LocationSearchScreen(
                locationSearchUiState = state,
                onEvent = {},
            )
        }
        // Then
        onTag("LocationItem_1").assertExists()
        onTag("LocationItem_2").assertExists()
    }

    @Test
    fun locationSearchScreen_selectingLocation_triggersLocationSelectedEvent() {
        // Given
        val state = MutableStateFlow(
            LocationSearchUiState(
                savedLocations = fakeSavedLocations
            )
        )
        var eventTriggered = false
        setResaContent {
            LocationSearchScreen(
                locationSearchUiState = state,
                onEvent = { if (it is LocationSearchUiEvent.LocationSelected) eventTriggered = true },
            )
        }
        // When
        onTag("LocationItem_1").performClick()
        // Then
        assert(eventTriggered)
    }
}
