package com.wasaap.androidstarterkit.core.datastore

import androidx.datastore.core.DataStore
import com.wasaap.androidstarterkit.core.model.DarkThemeConfig
import com.wasaap.androidstarterkit.core.model.UserData
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class TodoPreferenceDataStore @Inject constructor(
    private val userPreferences: DataStore<UserPreferences>,
) {

    val userData = userPreferences.data
        .map {
            UserData(
                darkThemeConfig = when (it.darkThemeConfig) {
                    null,
                    DarkThemeConfigProto.DARK_THEME_CONFIG_UNSPECIFIED,
                    DarkThemeConfigProto.UNRECOGNIZED,
                    DarkThemeConfigProto.DARK_THEME_CONFIG_FOLLOW_SYSTEM ->
                        DarkThemeConfig.FOLLOW_SYSTEM

                    DarkThemeConfigProto.DARK_THEME_CONFIG_LIGHT ->
                        DarkThemeConfig.LIGHT

                    DarkThemeConfigProto.DARK_THEME_CONFIG_DARK ->
                        DarkThemeConfig.DARK
                }
            )
        }

    suspend fun setDarkThemeConfig(darkThemeConfig: DarkThemeConfig) {
        userPreferences.updateData { prefs ->
            prefs.copy {
                this.darkThemeConfig = when (darkThemeConfig) {
                    DarkThemeConfig.FOLLOW_SYSTEM -> DarkThemeConfigProto.DARK_THEME_CONFIG_FOLLOW_SYSTEM
                    DarkThemeConfig.LIGHT -> DarkThemeConfigProto.DARK_THEME_CONFIG_LIGHT
                    DarkThemeConfig.DARK -> DarkThemeConfigProto.DARK_THEME_CONFIG_DARK
                }
            }
        }
    }
}