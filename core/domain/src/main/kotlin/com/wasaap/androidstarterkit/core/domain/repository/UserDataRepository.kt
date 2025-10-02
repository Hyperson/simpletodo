package com.wasaap.androidstarterkit.core.domain.repository

import com.wasaap.androidstarterkit.core.model.DarkThemeConfig
import com.wasaap.androidstarterkit.core.model.UserData
import kotlinx.coroutines.flow.Flow

interface UserDataRepository {

    val userData: Flow<UserData>

    suspend fun setDarkThemeConfig(darkThemeConfig: DarkThemeConfig)
}