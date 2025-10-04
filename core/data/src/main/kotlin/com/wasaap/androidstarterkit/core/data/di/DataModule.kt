package com.wasaap.androidstarterkit.core.data.di


import com.wasaap.androidstarterkit.core.data.repository.TodoRepositoryImpl
import com.wasaap.androidstarterkit.core.data.repository.UserDataRepositoryImpl
import com.wasaap.androidstarterkit.core.data.sync.Syncable
import com.wasaap.androidstarterkit.core.domain.repository.TodoRepository
import com.wasaap.androidstarterkit.core.domain.repository.UserDataRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import dagger.multibindings.IntoSet

@Module
@InstallIn(SingletonComponent::class)
abstract class DataModule {

    @Binds
    abstract fun bindTodoRepository(impl: TodoRepositoryImpl): TodoRepository

    @Binds
    internal abstract fun bindsUserDataRepository(
        userDataRepository: UserDataRepositoryImpl,
    ): UserDataRepository


    @Binds
    @IntoSet
    abstract fun bindTodoRepositoryAsSyncable(
        impl: TodoRepositoryImpl
    ): Syncable
}
