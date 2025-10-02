package com.wasaap.androidstarterkit.core.data.repository

import com.wasaap.androidstarterkit.core.datastore.TodoPreferenceDataStore
import com.wasaap.androidstarterkit.core.domain.repository.UserDataRepository
import com.wasaap.androidstarterkit.core.model.DarkThemeConfig
import com.wasaap.androidstarterkit.core.model.UserData
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

internal class UserDataRepositoryImpl @Inject constructor(val todoPreferenceDataStore: TodoPreferenceDataStore) :
    UserDataRepository {
    override val userData: Flow<UserData> = todoPreferenceDataStore.userData

    override suspend fun setDarkThemeConfig(darkThemeConfig: DarkThemeConfig) {
        todoPreferenceDataStore.setDarkThemeConfig(darkThemeConfig)
    }
}