package com.mazarini.resa.ui.screens.journeydetails.state

import com.mazarini.resa.domain.model.VehiclePosition
import com.mazarini.resa.domain.usecases.journey.CheckHasVehiclePositionUseCase
import com.mazarini.resa.domain.usecases.journey.FetchSelectedJourneyDetailsUseCase
import com.mazarini.resa.domain.usecases.journey.GetSelectedJourneyUseCase
import com.mazarini.resa.domain.usecases.journey.ListenVehiclePositionUseCase
import com.mazarini.resa.domain.usecases.journey.SaveCurrentJourneyToHomeUseCase
import com.mazarini.resa.domain.usecases.journey.StartVehicleTrackingUseCase
import com.mazarini.resa.global.analytics.isUnitTest
import io.mockk.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNull

@OptIn(ExperimentalCoroutinesApi::class)
class JourneyDetailsViewModelTest {
    private val testDispatcher = StandardTestDispatcher()
    private val getSelectedJourneyUseCase: GetSelectedJourneyUseCase = mockk(relaxed = true)
    private val fetchSelectedJourneyDetailsUseCase: FetchSelectedJourneyDetailsUseCase = mockk(relaxed = true)
    private val listenVehiclePositionUseCase: ListenVehiclePositionUseCase = mockk(relaxed = true)
    private val startVehicleTrackingUseCase: StartVehicleTrackingUseCase = mockk(relaxed = true)
    private val checkHasVehiclePositionUseCase: CheckHasVehiclePositionUseCase = mockk(relaxed = true)
    private val saveCurrentJourneyToHomeUseCase: SaveCurrentJourneyToHomeUseCase = mockk(relaxed = true)
    private lateinit var viewModel: JourneyDetailsViewModel

    @Before
    fun setup() {
        isUnitTest = true
        Dispatchers.setMain(testDispatcher)
        coEvery { fetchSelectedJourneyDetailsUseCase() } just Runs
        coEvery { getSelectedJourneyUseCase() } returns MutableStateFlow(null)
        coEvery { checkHasVehiclePositionUseCase() } returns Result.success(false)
        viewModel = JourneyDetailsViewModel(
            getSelectedJourneyUseCase,
            fetchSelectedJourneyDetailsUseCase,
            listenVehiclePositionUseCase,
            startVehicleTrackingUseCase,
            checkHasVehiclePositionUseCase,
            saveCurrentJourneyToHomeUseCase
        )
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `init triggers getSelectedJourney and checkHasVehiclePosition`() = runTest {
        // Given When
        // Then
        coVerify { fetchSelectedJourneyDetailsUseCase() }
        coVerify { getSelectedJourneyUseCase() }
        coVerify { checkHasVehiclePositionUseCase() }
    }

    @Test
    fun `onEvent LocationAccessResult updates hasLocationAccess`() = runTest {
        // Given When
        viewModel.onEvent(JourneyDetailsUiEvent.LocationAccessResult(true))
        // Then
        assertEquals(true, viewModel.uiState.value.hasLocationAccess)
    }

    @Test
    fun `onEvent SetShouldShowMap updates shouldShowMap`() = runTest {
        // Given When
        viewModel.onEvent(JourneyDetailsUiEvent.SetShouldShowMap(true))
        // Then
        assertEquals(true, viewModel.uiState.value.shouldShowMap)
    }

    private fun fakeVehiclePosition() = VehiclePosition(
        name = "id",
        directionName = "dir",
        colors = mockk(relaxed = true),
        position = mockk(relaxed = true),
        transportMode = mockk(relaxed = true)
    )

    @Test
    fun `onEvent StopFollowingVehicle clears followingVehicle`() = runTest {
        // Given
        val vehicle = fakeVehiclePosition()
        viewModel.onEvent(JourneyDetailsUiEvent.OnFollowClicked(vehicle))
        // When
        viewModel.onEvent(JourneyDetailsUiEvent.StopFollowingVehicle)
        // Then
        assertNull(viewModel.uiState.value.followingVehicle)
    }

    @Test
    fun `onEvent OnFollowClicked toggles followingVehicle`() = runTest {
        // Given
        val vehicle = fakeVehiclePosition()
        // When
        viewModel.onEvent(JourneyDetailsUiEvent.OnFollowClicked(vehicle))
        // Then
        assertEquals(vehicle, viewModel.uiState.value.followingVehicle)
        // When (toggle off)
        viewModel.onEvent(JourneyDetailsUiEvent.OnFollowClicked(vehicle))
        // Then
        assertNull(viewModel.uiState.value.followingVehicle)
    }

    @Test
    fun `onEvent SaveJourneyToHome triggers usecase`() = runTest {
        // Given
        coEvery { saveCurrentJourneyToHomeUseCase() } just Runs
        // When
        viewModel.onEvent(JourneyDetailsUiEvent.SaveJourneyToHome)
        advanceUntilIdle()
        // Then
        coVerify { saveCurrentJourneyToHomeUseCase() }
    }

    @Test
    fun `startTrackingVehicles triggers usecases and updates trackedVehicles`() = runTest {
        // Given
        val vehicles = listOf(fakeVehiclePosition())
        coEvery { startVehicleTrackingUseCase() } just Runs
        coEvery { listenVehiclePositionUseCase() } returns flowOf(vehicles)
        // When
        viewModel.onEvent(JourneyDetailsUiEvent.StartTrackingVehicles)
        advanceUntilIdle()
        // Then
        assertEquals(vehicles, viewModel.uiState.value.trackedVehicles)
        coVerify { startVehicleTrackingUseCase() }
        coVerify { listenVehiclePositionUseCase() }
    }
}
