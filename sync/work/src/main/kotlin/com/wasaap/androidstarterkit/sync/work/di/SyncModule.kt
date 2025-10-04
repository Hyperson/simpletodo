package com.wasaap.androidstarterkit.sync.work.di

import com.wasaap.androidstarterkit.core.data.sync.SyncManager
import com.wasaap.androidstarterkit.sync.work.status.WorkManagerSyncManager
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
abstract class SyncModule {
    @Binds
    internal abstract fun bindsSyncStatusMonitor(
        syncStatusMonitor: WorkManagerSyncManager,
    ): SyncManager
}
