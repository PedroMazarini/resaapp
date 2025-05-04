package com.mazarini.resa.ui.screens.locationsearch.state

import com.mazarini.resa.domain.usecases.RefreshTokenUseCase
import com.mazarini.resa.domain.usecases.journey.GetCurrentJourneyQueryUseCase
import com.mazarini.resa.domain.usecases.journey.SaveCurrentJourneyQueryUseCase
import com.mazarini.resa.domain.usecases.journey.SaveRecentJourneySearchUseCase
import com.mazarini.resa.domain.usecases.location.DeleteSavedLocationUseCase
import com.mazarini.resa.domain.usecases.location.GetRecentLocationsUseCase
import com.mazarini.resa.domain.usecases.location.GetSavedLocationsUseCase
import com.mazarini.resa.domain.usecases.location.QueryLocationByTextUseCase
import com.mazarini.resa.domain.usecases.location.SaveLocationUseCase
import com.mazarini.resa.domain.usecases.location.SaveRecentLocationUseCase
import com.mazarini.resa.global.analytics.isUnitTest
import com.mazarini.resa.global.fake.FakeFactory
import com.mazarini.resa.global.preferences.PrefsProvider
import com.mazarini.resa.ui.commoncomponents.journeySearchFilters.state.JourneyFilterUiEvent
import com.mazarini.resa.ui.screens.locationsearch.state.LocationSearchUiEvent.ClearDest
import com.mazarini.resa.ui.screens.locationsearch.state.LocationSearchUiEvent.ClearOrigin
import com.mazarini.resa.ui.screens.locationsearch.state.LocationSearchUiEvent.DeleteLocation
import com.mazarini.resa.ui.screens.locationsearch.state.LocationSearchUiEvent.DestFocusChanged
import com.mazarini.resa.ui.screens.locationsearch.state.LocationSearchUiEvent.DestSearchChanged
import com.mazarini.resa.ui.screens.locationsearch.state.LocationSearchUiEvent.FilterEvent
import com.mazarini.resa.ui.screens.locationsearch.state.LocationSearchUiEvent.LocationSelected
import com.mazarini.resa.ui.screens.locationsearch.state.LocationSearchUiEvent.OriginFocusChanged
import com.mazarini.resa.ui.screens.locationsearch.state.LocationSearchUiEvent.OriginSearchChanged
import com.mazarini.resa.ui.screens.locationsearch.state.LocationSearchUiEvent.SaveLocation
import com.mazarini.resa.ui.screens.locationsearch.state.LocationSearchUiEvent.SwapLocations
import com.mazarini.resa.ui.screens.mapper.DomainToUiLocationMapper
import io.mockk.Runs
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.just
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNull

@OptIn(ExperimentalCoroutinesApi::class)
class LocationSearchViewModelTest {
    private val testDispatcher = StandardTestDispatcher()
    private lateinit var viewModel: LocationSearchViewModel
    private val refreshTokenUseCase: RefreshTokenUseCase = mockk(relaxed = true)
    private val queryLocationByTextUseCase: QueryLocationByTextUseCase = mockk(relaxed = true)
    private val saveCurrentJourneyQueryUseCase: SaveCurrentJourneyQueryUseCase =
        mockk(relaxed = true)
    private val saveLocationUseCase: SaveLocationUseCase = mockk(relaxed = true)
    private val saveRecentJourneySearchesUseCase: SaveRecentJourneySearchUseCase =
        mockk(relaxed = true)
    private val deleteSavedLocationUseCase: DeleteSavedLocationUseCase = mockk(relaxed = true)
    private val getSavedLocationsUseCase: GetSavedLocationsUseCase = mockk(relaxed = true)
    private val saveRecentLocationUseCase: SaveRecentLocationUseCase = mockk(relaxed = true)
    private val getRecentLocationsUseCase: GetRecentLocationsUseCase = mockk(relaxed = true)
    private val getCurrentJourneyQueryUseCase: GetCurrentJourneyQueryUseCase = mockk(relaxed = true)
    private val locationMapper: DomainToUiLocationMapper = mockk(relaxed = true)
    private val prefsProvider: PrefsProvider = mockk(relaxed = true)

    @Before
    fun setup() {
        isUnitTest = true
        Dispatchers.setMain(testDispatcher)
        coEvery { getSavedLocationsUseCase() } returns flowOf(emptyList())
        coEvery { getRecentLocationsUseCase() } returns flowOf(emptyList())
        every { prefsProvider.collectPreferredTransportModes() } returns flowOf(emptyList())
        viewModel = LocationSearchViewModel(
            refreshTokenUseCase,
            queryLocationByTextUseCase,
            saveCurrentJourneyQueryUseCase,
            saveLocationUseCase,
            saveRecentJourneySearchesUseCase,
            deleteSavedLocationUseCase,
            getSavedLocationsUseCase,
            saveRecentLocationUseCase,
            getRecentLocationsUseCase,
            getCurrentJourneyQueryUseCase,
            locationMapper,
            prefsProvider
        )
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `ClearDest event clears destination`() = runTest {
        // Given
        // When
        viewModel.onEvent(ClearDest)
        // Then
        assertNull(viewModel.uiState.value.destSelected)
        assertEquals("", viewModel.uiState.value.destSearch)
    }

    @Test
    fun `ClearOrigin event clears origin`() = runTest {
        // Given
        // When
        viewModel.onEvent(ClearOrigin)
        // Then
        assertNull(viewModel.uiState.value.originSelected)
        assertEquals("", viewModel.uiState.value.originSearch)
    }

    @Test
    fun `SwapLocations swaps origin and destination`() = runTest {
        // Given
        val origin = FakeFactory.location().copy(id = "1")
        val dest = FakeFactory.location().copy(id = "2")
        viewModel.onEvent(OriginFocusChanged(true))
        viewModel.onEvent(LocationSelected(origin))
        viewModel.onEvent(DestFocusChanged(true))
        viewModel.onEvent(DestSearchChanged("dest"))
        viewModel.onEvent(LocationSelected(dest))
        // When
        viewModel.onEvent(SwapLocations)
        // Then
        assertEquals(dest, viewModel.uiState.value.originSelected)
        assertEquals(origin, viewModel.uiState.value.destSelected)
    }

    @Test
    fun `OriginSearchChanged updates origin search`() = runTest {
        // Given
        // When
        viewModel.onEvent(OriginSearchChanged("test"))
        // Then
        assertEquals("test", viewModel.uiState.value.originSearch)
        assertNull(viewModel.uiState.value.originSelected)
    }

    @Test
    fun `DestSearchChanged updates dest search`() = runTest {
        // Given
        // When
        viewModel.onEvent(DestSearchChanged("test"))
        // Then
        assertEquals("test", viewModel.uiState.value.destSearch)
        assertNull(viewModel.uiState.value.destSelected)
    }

    @Test
    fun `SaveLocation triggers usecase`() = runTest {
        // Given
        val location = FakeFactory.location()
        val domainLocation = FakeFactory.domainLocation()
        every { locationMapper.reverse(location) } returns domainLocation
        coEvery { saveLocationUseCase(domainLocation) } just Runs
        // When
        viewModel.onEvent(SaveLocation(location))
        // Then
        coVerify { saveLocationUseCase(domainLocation) }
    }

    @Test
    fun `DeleteLocation triggers usecase`() = runTest {
        // Given
        val id = "testId"
        coEvery { deleteSavedLocationUseCase(id) } just Runs
        // When
        viewModel.onEvent(DeleteLocation(id))
        // Then
        coVerify { deleteSavedLocationUseCase(id) }
    }

    @Test
    fun `OriginFocusChanged updates focus and search type`() = runTest {
        // Given
        // When
        viewModel.onEvent(OriginFocusChanged(true))
        // Then
        assertEquals(false, viewModel.uiState.value.requestOriginFocus)
    }

    @Test
    fun `DestFocusChanged updates focus and search type`() = runTest {
        // Given
        // When
        viewModel.onEvent(DestFocusChanged(true))
        // Then
        assertEquals(false, viewModel.uiState.value.requestDestFocus)
    }

    @Test
    fun `FilterEvent OnReferenceChanged updates isDeparture`() = runTest {
        // Given
        // When
        viewModel.onEvent(FilterEvent(JourneyFilterUiEvent.OnReferenceChanged(true)))
        // Then
        assertEquals(true, viewModel.uiState.value.filtersUiState.filters.isDepartureFilters)
    }
}
