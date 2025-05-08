package com.mazarini.resa.ui.application

import LocaleHelper.getNewLocaleContext
import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import com.mazarini.resa.domain.usecases.RefreshTokenUseCase
import com.mazarini.resa.domain.usecases.location.QueryLocationByTextUseCase
import com.mazarini.resa.global.model.ThemeSettings
import com.mazarini.resa.global.preferences.PrefsProvider
import com.mazarini.resa.global.preferences.model.UserPreferences
import com.mazarini.resa.ui.application.state.MainViewModel
import com.mazarini.resa.ui.navigation.NavigationHost
import com.mazarini.resa.ui.theme.ResaTheme
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.runBlocking
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    @Inject
    lateinit var refreshTokenUseCase: RefreshTokenUseCase
    @Inject
    lateinit var queryLocationByTextUseCase: QueryLocationByTextUseCase

    private val viewModel: MainViewModel by viewModels()

    @OptIn(DelicateCoroutinesApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val themeSettings = viewModel.themeState

            ResaTheme(
                darkTheme = isDarkTheme(themeSettings.value),
            ) {
                val prefsProvider = PrefsProvider(this)
                val userPreferences = prefsProvider.userPreferencesFlow().collectAsState(
                    initial = UserPreferences()
                )

                NavigationHost()
                updateLanguage(userPreferences.value)
            }
        }
    }

    private fun updateLanguage(userPreference: UserPreferences) {
        val current = this.resources.configuration.locales.get(0).language
        userPreference.language.takeIf { it != current }?.let {
            restartApp()
        }
    }

    @Override
    override fun attachBaseContext(newBase: Context) {
        val language = runBlocking {
            val prefsProvider = PrefsProvider(newBase)
            prefsProvider.getLanguage()
        }
        val context = language?.let {
            getNewLocaleContext(newBase, it)
        } ?: newBase
        super.attachBaseContext(context)
    }

    private fun restartApp() {
        val intent = packageManager.getLaunchIntentForPackage(packageName)
        intent?.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
        startActivity(intent)
        Runtime.getRuntime().exit(0)
    }

    @Composable
    private fun isDarkTheme(themeSettings: ThemeSettings): Boolean {
        return when (themeSettings) {
            ThemeSettings.SYSTEM -> isSystemInDarkTheme()
            ThemeSettings.DARK -> true
            ThemeSettings.LIGHT -> false
        }
    }
}