package com.wasaap.androidstarterkit.feature.settings

import com.wasaap.androidstarterkit.core.model.DarkThemeConfig
import com.wasaap.androidstarterkit.core.testing.repository.TestUserDataRepository
import com.wasaap.androidstarterkit.core.testing.repository.emptyUserData
import com.wasaap.androidstarterkit.core.testing.util.MainDispatcherRule
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test


@OptIn(ExperimentalCoroutinesApi::class)
class SettingsViewModelTest {

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    private val userDataRepository = TestUserDataRepository()
    private lateinit var viewModel: SettingsViewModel

    @Before
    fun setup() {
        viewModel = SettingsViewModel(
            userDataRepository = userDataRepository,
        )
    }

    @Test
    fun stateIsInitiallySuccess_withDefaultUserData() = runTest {
        val expected = SettingsUiState.Success(
            settings = UserEditableSettings(DarkThemeConfig.FOLLOW_SYSTEM)
        )

        assertEquals(expected, viewModel.settingsUiState.value)
    }

    @Test
    fun stateEmitsSuccessAfterUserDataLoaded() = runTest {
        val collectJob = backgroundScope.launch(UnconfinedTestDispatcher(testScheduler)) {
            viewModel.settingsUiState.collect {}
        }

        userDataRepository.setUserData(
            emptyUserData.copy(darkThemeConfig = DarkThemeConfig.DARK)
        )
        advanceUntilIdle()

        val expected = SettingsUiState.Success(
            settings = UserEditableSettings(darkThemeConfig = DarkThemeConfig.DARK)
        )

        assertEquals(expected, viewModel.settingsUiState.value)

        collectJob.cancel()
    }

    @Test
    fun updateDarkThemeConfig_updatesRepositoryAndUiState() = runTest {
        val collectJob = backgroundScope.launch(UnconfinedTestDispatcher(testScheduler)) {
            viewModel.settingsUiState.collect {}
        }

        userDataRepository.setUserData(
            emptyUserData.copy(darkThemeConfig = DarkThemeConfig.FOLLOW_SYSTEM)
        )
        advanceUntilIdle()

        viewModel.updateDarkThemeConfig(DarkThemeConfig.DARK)
        advanceUntilIdle()

        val updatedUserData = userDataRepository.userData.first()
        assertEquals(DarkThemeConfig.DARK, updatedUserData.darkThemeConfig)

        val expectedUiState = SettingsUiState.Success(
            settings = UserEditableSettings(darkThemeConfig = DarkThemeConfig.DARK)
        )

        assertEquals(expectedUiState, viewModel.settingsUiState.value)

        collectJob.cancel()
    }
}