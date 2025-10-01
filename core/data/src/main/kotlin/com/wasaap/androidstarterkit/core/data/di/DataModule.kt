package com.wasaap.androidstarterkit.core.data.di


import com.wasaap.androidstarterkit.core.data.repository.TodoRepositoryImpl
import com.wasaap.androidstarterkit.core.domain.repository.TodoRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
abstract class DataModule {

    @Binds
    abstract fun bindTodoRepository(impl: TodoRepositoryImpl): TodoRepository
}