package com.mazarini.resa.ui.screens.journeyselection.state

import com.mazarini.resa.domain.model.journey.Journey
import com.mazarini.resa.domain.model.queryjourneys.QueryJourneysParams
import com.mazarini.resa.domain.usecases.journey.GetCurrentJourneyQueryUseCase
import com.mazarini.resa.domain.usecases.journey.QueryJourneysUseCase
import com.mazarini.resa.domain.usecases.journey.QueryPassedJourneysUseCase
import com.mazarini.resa.domain.usecases.journey.SaveCurrentJourneyQueryUseCase
import com.mazarini.resa.domain.usecases.journey.SaveJourneySearchUseCase
import com.mazarini.resa.domain.usecases.journey.SetSelectedJourneyUseCase
import com.mazarini.resa.global.analytics.isUnitTest
import com.mazarini.resa.global.preferences.PrefsProvider
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
import java.time.LocalDateTime
import java.time.ZoneId
import java.util.Date

@OptIn(ExperimentalCoroutinesApi::class)
class JourneySelectionViewModelTest {
    private val testDispatcher = StandardTestDispatcher()
    private val queryJourneysUseCase: QueryJourneysUseCase = mockk(relaxed = true)
    private val queryPassedJourneysUseCase: QueryPassedJourneysUseCase = mockk(relaxed = true)
    private val getCurrentJourneyQueryUseCase: GetCurrentJourneyQueryUseCase = mockk(relaxed = true)
    private val saveJourneySearchUseCase: SaveJourneySearchUseCase = mockk(relaxed = true)
    private val saveCurrentJourneyQueryUseCase: SaveCurrentJourneyQueryUseCase =
        mockk(relaxed = true)
    private val setSelectedJourneyUseCase: SetSelectedJourneyUseCase = mockk(relaxed = true)
    private val prefsProvider: PrefsProvider = mockk(relaxed = true)
    private lateinit var viewModel: JourneySelectionViewModel

    @Before
    fun setup() {
        isUnitTest = true
        Dispatchers.setMain(testDispatcher)
        coEvery { getCurrentJourneyQueryUseCase() } returns Result.success(QueryJourneysParams())
        every { prefsProvider.collectPreferredTransportModes() } returns flowOf(emptyList())
        coEvery { prefsProvider.getUserPrefs() } returns mockk(relaxed = true)
        coEvery { queryJourneysUseCase() } returns flowOf()
        coEvery { queryPassedJourneysUseCase() } returns flowOf()
        viewModel = JourneySelectionViewModel(
            queryJourneysUseCase,
            queryPassedJourneysUseCase,
            getCurrentJourneyQueryUseCase,
            saveJourneySearchUseCase,
            saveCurrentJourneyQueryUseCase,
            setSelectedJourneyUseCase,
            prefsProvider
        )
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `onEvent IsDepartFilterChanged updates isDeparture`() = runTest {
        // Given
        // When
        viewModel.onEvent(JourneySelectionUiEvent.IsDepartFilterChanged(true))
        // Then
        assert(viewModel.uiState.value.filterUiState.filters.isDepartureFilters)
    }

    @Test
    fun `onEvent DateFilterChanged updates date`() = runTest {
        // Given
        val date = Date()
        // When
        viewModel.onEvent(JourneySelectionUiEvent.DateFilterChanged(date))
        // Then
        assert(viewModel.uiState.value.filterUiState.filters.date == date)
    }

    @Test
    fun `onEvent TimeFilterChanged updates time`() = runTest {
        // Given
        val localDateTime = LocalDateTime.of(2023, 1, 1, 10, 30)
        val date = Date.from(localDateTime.atZone(ZoneId.systemDefault()).toInstant())
        // When
        viewModel.onEvent(JourneySelectionUiEvent.TimeFilterChanged(date))
        // Then
        assert(viewModel.uiState.value.filterUiState.filters.date.hours == 10)
        assert(viewModel.uiState.value.filterUiState.filters.date.minutes == 30)
    }

    @Test
    fun `onEvent SaveCurrentJourneySearch triggers usecase`() = runTest {
        // Given
        coEvery { saveJourneySearchUseCase(any()) } just Runs
        // When
        viewModel.onEvent(JourneySelectionUiEvent.SaveCurrentJourneySearch)
        // Then
        coVerify { saveJourneySearchUseCase(any()) }
    }

    @Test
    fun `onEvent JourneySelected triggers setSelectedJourneyUseCase`() = runTest {
        // Given
        val journey = mockk<Journey>(relaxed = true)
        coEvery { setSelectedJourneyUseCase(any()) } just Runs
        // When
        viewModel.onEvent(JourneySelectionUiEvent.JourneySelected(journey))
        // Then
        coVerify { setSelectedJourneyUseCase(any()) }
    }

    @Test
    fun `onEvent UpdateJourneySearch updates filters and queries`() = runTest {
        // Given
        coEvery { saveCurrentJourneyQueryUseCase(any()) } just Runs
        coEvery { queryJourneysUseCase() } returns flowOf()
        coEvery { queryPassedJourneysUseCase() } returns flowOf()
        viewModel.onEvent(JourneySelectionUiEvent.DateFilterChanged(Date()))
        // When
        viewModel.onEvent(JourneySelectionUiEvent.UpdateJourneySearch)
        // Then
        coVerify { saveCurrentJourneyQueryUseCase(any()) }
        coVerify { queryJourneysUseCase() }
        coVerify { queryPassedJourneysUseCase() }
        assert(!viewModel.uiState.value.filtersChanged)
    }
}

