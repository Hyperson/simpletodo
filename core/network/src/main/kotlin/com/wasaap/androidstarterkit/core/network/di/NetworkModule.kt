package com.wasaap.androidstarterkit.core.network.di

import com.wasaap.androidstarterkit.core.network.BuildConfig
import com.wasaap.androidstarterkit.core.network.TodoNetworkDataSource
import com.wasaap.androidstarterkit.core.network.retrofit.RetrofitTodoNetwork
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.serialization.json.Json
import okhttp3.Call
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
internal object NetworkModule {

    @Module
    @InstallIn(SingletonComponent::class)
    abstract class NetworkBindModule {
        @Binds
        @Singleton
        abstract fun bindTodoNetworkDataSource(
            impl: RetrofitTodoNetwork
        ): TodoNetworkDataSource
    }

    @Provides
    @Singleton
    fun okHttpCallFactory(): Call.Factory =
        OkHttpClient.Builder()
            .addInterceptor(
                HttpLoggingInterceptor()
                    .apply {
                        if (BuildConfig.DEBUG) {
                            setLevel(HttpLoggingInterceptor.Level.BODY)
                        }
                    },
            )
            .build()

    @Provides
    @Singleton
    fun providesNetworkJson(): Json = Json {
        ignoreUnknownKeys = true
    }
}
