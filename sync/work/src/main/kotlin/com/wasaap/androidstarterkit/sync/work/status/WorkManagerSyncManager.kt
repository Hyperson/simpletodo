package com.wasaap.androidstarterkit.sync.work.status

import android.content.Context
import androidx.work.ExistingWorkPolicy
import androidx.work.WorkInfo
import androidx.work.WorkManager
import com.wasaap.androidstarterkit.core.data.sync.SyncManager
import com.wasaap.androidstarterkit.sync.work.SyncWorker
import com.wasaap.androidstarterkit.sync.work.initializers.SYNC_WORK_NAME
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.conflate
import kotlinx.coroutines.flow.map
import androidx.work.WorkInfo.State

import javax.inject.Inject

internal class WorkManagerSyncManager @Inject constructor(
    @param:ApplicationContext private val context: Context,
) : SyncManager {
    override val isSyncing: Flow<Boolean> =
        WorkManager.getInstance(context).getWorkInfosForUniqueWorkFlow(SYNC_WORK_NAME)
            .map(List<WorkInfo>::anyRunning)
            .conflate()

    override fun requestSync() {
        val workManager = WorkManager.getInstance(context)
        // Run sync on app startup and ensure only one sync worker runs at any time
        workManager.enqueueUniqueWork(
            SYNC_WORK_NAME,
            ExistingWorkPolicy.KEEP,
            SyncWorker.startUpSyncWork(),
        )
    }
}


private fun List<WorkInfo>.anyRunning() = any { it.state == State.RUNNING }
