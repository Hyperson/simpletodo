package com.wasaap.androidstarterkit.core.database.di

import com.wasaap.androidstarterkit.core.database.TodoDatabase
import com.wasaap.androidstarterkit.core.database.dao.TodoDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
internal object DaosModule {

    @Provides
    @Singleton
    fun providesTodoDao(
        database: TodoDatabase,
    ): TodoDao = database.todoDao()
}