package com.wasaap.androidstarterkit.core.data.sync

import kotlinx.coroutines.flow.Flow

interface SyncManager {
    val isSyncing: Flow<Boolean>
    fun requestSync()
}