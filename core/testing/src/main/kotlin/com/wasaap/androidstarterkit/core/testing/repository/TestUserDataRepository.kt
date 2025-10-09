package com.wasaap.androidstarterkit.core.testing.repository

import com.wasaap.androidstarterkit.core.domain.repository.UserDataRepository
import com.wasaap.androidstarterkit.core.model.DarkThemeConfig
import com.wasaap.androidstarterkit.core.model.UserData
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow

class TestUserDataRepository : UserDataRepository {

    private val _userData = MutableStateFlow(emptyUserData)
    override val userData: Flow<UserData> = _userData

    override suspend fun setDarkThemeConfig(darkThemeConfig: DarkThemeConfig) {
        _userData.value = _userData.value.copy(darkThemeConfig = darkThemeConfig)
    }

    fun setUserData(userData: UserData) {
        _userData.value = userData
    }
}

val emptyUserData = UserData(
    darkThemeConfig = DarkThemeConfig.FOLLOW_SYSTEM,
)