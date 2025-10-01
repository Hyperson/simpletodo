package com.wasaap.androidstarterkit.common.core.network.di

import com.wasaap.androidstarterkit.common.core.network.Dispatcher
import com.wasaap.androidstarterkit.common.core.network.TodoDispatchers.Default
import com.wasaap.androidstarterkit.common.core.network.TodoDispatchers.IO
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers

@Module
@InstallIn(SingletonComponent::class)
object DispatchersModule {
    @Provides
    @Dispatcher(IO)
    fun providesIODispatcher(): CoroutineDispatcher = Dispatchers.IO

    @Provides
    @Dispatcher(Default)
    fun providesDefaultDispatcher(): CoroutineDispatcher = Dispatchers.Default
}
