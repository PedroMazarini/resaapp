package com.mazarini.resa.ui.screens.home.state

import com.mazarini.resa.domain.usecases.RefreshTokenUseCase
import com.mazarini.resa.domain.usecases.journey.DeleteRecentJourneySearchUseCase
import com.mazarini.resa.domain.usecases.journey.DeleteSavedJourneySearchUseCase
import com.mazarini.resa.domain.usecases.journey.GetRecentJourneySearchesUseCase
import com.mazarini.resa.domain.usecases.journey.GetSavedJourneySearchesUseCase
import com.mazarini.resa.domain.usecases.journey.LoadSavedJourneyToHomeUseCase
import com.mazarini.resa.domain.usecases.journey.SaveCurrentJourneyQueryUseCase
import com.mazarini.resa.global.analytics.isUnitTest
import com.mazarini.resa.global.model.ThemeSettings
import com.mazarini.resa.global.preferences.PrefsProvider
import com.mazarini.resa.ui.screens.mapper.DomainToUiJourneySearchMapper
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.just
import io.mockk.mockk
import io.mockk.runs
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.jupiter.api.Assertions.assertEquals

@OptIn(ExperimentalCoroutinesApi::class)
class HomeViewModelTest {
    private val testDispatcher = UnconfinedTestDispatcher()
    private val prefsProvider: PrefsProvider = mockk(relaxed = true)
    private val refreshTokenUseCase = mockk<RefreshTokenUseCase>(relaxed = true)
    private val getSavedJourneySearchesUseCase =
        mockk<GetSavedJourneySearchesUseCase>(relaxed = true)
    private val saveCurrentJourneyQueryUseCase =
        mockk<SaveCurrentJourneyQueryUseCase>(relaxed = true)
    private val deleteSavedJourneySearchUseCase =
        mockk<DeleteSavedJourneySearchUseCase>(relaxed = true)
    private val deleteRecentJourneySearchUseCase =
        mockk<DeleteRecentJourneySearchUseCase>(relaxed = true)
    private val getRecentJourneySearchesUseCase =
        mockk<GetRecentJourneySearchesUseCase>(relaxed = true)
    private val journeySearchMapper = mockk<DomainToUiJourneySearchMapper>(relaxed = true)
    private val loadSavedJourneyToHomeUseCase = mockk<LoadSavedJourneyToHomeUseCase>(relaxed = true)
    private lateinit var viewModel: HomeViewModel

    @Before
    fun setup() {
        isUnitTest = true
        Dispatchers.setMain(testDispatcher)
        viewModel = HomeViewModel(
            refreshTokenUseCase,
            getSavedJourneySearchesUseCase,
            saveCurrentJourneyQueryUseCase,
            deleteSavedJourneySearchUseCase,
            deleteRecentJourneySearchUseCase,
            getRecentJourneySearchesUseCase,
            journeySearchMapper,
            loadSavedJourneyToHomeUseCase,
            prefsProvider
        )
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `updateThemeSettings updates theme in prefs and state`() = runTest {
        // Given
        val themeSettings = ThemeSettings.DARK
        coEvery { prefsProvider.setThemeSettings(themeSettings) } just runs
        // When
        viewModel.onEvent(HomeUiEvent.OnThemeChanged(themeSettings))
        advanceUntilIdle()
        // Then
        coVerify { prefsProvider.setThemeSettings(themeSettings) }
        assertEquals(themeSettings, viewModel.uiState.value.currentTheme)
    }

    @Test
    fun `setLanguage updates language in prefs`() = runTest {
        // Given
        val language = "en"
        coEvery { prefsProvider.setLanguage(language) } just runs
        // When
        viewModel.onEvent(HomeUiEvent.SetLanguage(language))
        advanceUntilIdle()
        // Then
        coVerify { prefsProvider.setLanguage(language) }
    }

    @Test
    fun `deleteSavedJourneySearch triggers usecase and updates state`() = runTest {
        // Given
        val id = "testId"
        coEvery { deleteSavedJourneySearchUseCase(id) } just runs
        // When
        viewModel.onEvent(HomeUiEvent.DeleteSavedJourney(id))
        advanceUntilIdle()
        // Then
        coVerify { deleteSavedJourneySearchUseCase(id) }
    }

    @Test
    fun `deleteRecentJourneySearch triggers usecase and updates state`() = runTest {
        // Given
        val id = "recentId"
        coEvery { deleteRecentJourneySearchUseCase(id) } just runs
        // When
        viewModel.onEvent(HomeUiEvent.DeleteRecentJourney(id))
        advanceUntilIdle()
        // Then
        coVerify { deleteRecentJourneySearchUseCase(id) }
    }

    @Test
    fun `refreshToken triggers usecase`() = runTest {
        // Given
        coEvery { refreshTokenUseCase() } just runs
        // When
        advanceUntilIdle()
        // Then
        coVerify { refreshTokenUseCase() }
    }

    @Test
    fun `loadSavedJourneys triggers usecase`() = runTest {
        // Given
        coEvery { getSavedJourneySearchesUseCase() } returns mockk(relaxed = true)
        // When
        advanceUntilIdle()
        // Then
        coVerify { getSavedJourneySearchesUseCase() }
    }

    @Test
    fun `loadRecentJourneys triggers usecase`() = runTest {
        // Given
        coEvery { getRecentJourneySearchesUseCase() } returns mockk(relaxed = true)
        // When
        advanceUntilIdle()
        // Then
        coVerify { getRecentJourneySearchesUseCase() }
    }
}
