package com.wasaap.androidstarterkit.feature.settings

import androidx.activity.ComponentActivity
import androidx.compose.ui.test.assertIsSelected
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithText
import com.wasaap.androidstarterkit.core.model.DarkThemeConfig
import org.junit.Rule
import org.junit.Test

class SettingsScreenTest {

    @get:Rule
    val composeTestRule = createAndroidComposeRule<ComponentActivity>()

    private fun getString(id: Int) = composeTestRule.activity.getString(id)

    @Test
    fun whenStateIsSuccess_allDarkThemeOptionsAreDisplayed() {
        composeTestRule.setContent {
            SettingsContent(
                uiState = SettingsUiState.Success(
                    settings = UserEditableSettings(
                        darkThemeConfig = DarkThemeConfig.DARK,
                    )
                ),
                onSelectTheme = {},
                onShowSnackbar = { _, _ -> false }
            )
        }

        composeTestRule.onNodeWithText(getString(R.string.feature_settings_appearance_title))
            .assertExists()
        composeTestRule.onNodeWithText(getString(R.string.feature_settings_appearance_system_default))
            .assertExists()
        composeTestRule.onNodeWithText(getString(R.string.feature_settings_appearance_system_light))
            .assertExists()
        composeTestRule.onNodeWithText(getString(R.string.feature_settings_appearance_system_dark))
            .assertExists()
    }

    @Test
    fun whenDarkThemeIsSelected_onlyDarkOptionIsSelected() {
        composeTestRule.setContent {
            SettingsContent(
                uiState = SettingsUiState.Success(
                    settings = UserEditableSettings(
                        darkThemeConfig = DarkThemeConfig.DARK,
                    )
                ),
                onSelectTheme = {},
                onShowSnackbar = { _, _ -> false }
            )
        }

        composeTestRule.onNodeWithText(getString(R.string.feature_settings_appearance_system_default))
            .assertExists()
        composeTestRule.onNodeWithText(getString(R.string.feature_settings_appearance_system_light))
            .assertExists()
        composeTestRule.onNodeWithText(getString(R.string.feature_settings_appearance_system_dark))
            .assertIsSelected()
    }

    @Test
    fun whenLightThemeIsSelected_onlyLightOptionIsSelected() {
        composeTestRule.setContent {
            SettingsContent(
                uiState = SettingsUiState.Success(
                    settings = UserEditableSettings(
                        darkThemeConfig = DarkThemeConfig.LIGHT,
                    )
                ),
                onSelectTheme = {},
                onShowSnackbar = { _, _ -> false }
            )
        }

        composeTestRule.onNodeWithText(getString(R.string.feature_settings_appearance_system_light))
            .assertIsSelected()
        composeTestRule.onNodeWithText(getString(R.string.feature_settings_appearance_system_dark))
            .assertExists()
        composeTestRule.onNodeWithText(getString(R.string.feature_settings_appearance_system_default))
            .assertExists()
    }

    @Test
    fun whenFollowSystemIsSelected_onlySystemDefaultOptionIsSelected() {
        composeTestRule.setContent {
            SettingsContent(
                uiState = SettingsUiState.Success(
                    settings = UserEditableSettings(
                        darkThemeConfig = DarkThemeConfig.FOLLOW_SYSTEM,
                    )
                ),
                onSelectTheme = {},
                onShowSnackbar = { _, _ -> false }
            )
        }

        composeTestRule.onNodeWithText(getString(R.string.feature_settings_appearance_system_default))
            .assertIsSelected()
    }

    @Test
    fun whenUiStateIsLoading_noOptionsAreDisplayed() {
        composeTestRule.setContent {
            SettingsContent(
                uiState = SettingsUiState.Loading,
                onSelectTheme = {},
                onShowSnackbar = { _, _ -> false }
            )
        }

        composeTestRule.onNodeWithText(getString(R.string.feature_settings_appearance_system_default))
            .assertDoesNotExist()
        composeTestRule.onNodeWithText(getString(R.string.feature_settings_appearance_title))
            .assertDoesNotExist()
    }
}