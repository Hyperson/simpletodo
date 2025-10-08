package com.wasaap.androidstarterkit.core.testing.util

import com.wasaap.androidstarterkit.core.data.sync.SyncManager
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow

class TestSyncManager : SyncManager {

    private val syncStatusFlow = MutableStateFlow(false)

    override val isSyncing: Flow<Boolean> = syncStatusFlow

    override fun requestSync(): Unit = TODO("Not yet implemented")

    /**
     * A test-only API to set the sync status from tests.
     */
    fun setSyncing(isSyncing: Boolean) {
        syncStatusFlow.value = isSyncing
    }
}
