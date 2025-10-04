package com.wasaap.androidstarterkit.sync.work

import android.content.Context
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.ForegroundInfo
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.OutOfQuotaPolicy
import androidx.work.WorkerParameters
import com.wasaap.androidstarterkit.core.common.network.Dispatcher
import com.wasaap.androidstarterkit.core.common.network.TodoDispatchers
import com.wasaap.androidstarterkit.core.data.sync.Syncable
import com.wasaap.androidstarterkit.core.data.sync.Synchronizer
import com.wasaap.androidstarterkit.sync.work.initializers.SyncConstraints
import com.wasaap.androidstarterkit.sync.work.initializers.syncForegroundInfo
import com.wasaap.androidstarterkit.sync.work.workers.DelegatingWorker
import com.wasaap.androidstarterkit.sync.work.workers.delegatedData
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext

@HiltWorker
internal class SyncWorker @AssistedInject constructor(
    @Assisted private val appContext: Context,
    @Assisted workerParams: WorkerParameters,
    private val repositories: Set<@JvmSuppressWildcards Syncable>,
    @param:Dispatcher(TodoDispatchers.IO) private val ioDispatcher: CoroutineDispatcher,
) : CoroutineWorker(appContext, workerParams), Synchronizer {

    override suspend fun getForegroundInfo(): ForegroundInfo =
        appContext.syncForegroundInfo()

    override suspend fun doWork(): Result = withContext(ioDispatcher) {

        val results = repositories.map { repo ->
            repo.syncWith(this@SyncWorker) }
        if (results.all { it }) Result.success() else Result.retry()
    }

    companion object {
        /**
         * Expedited one time work to sync data on app startup
         */
        fun startUpSyncWork() = OneTimeWorkRequestBuilder<DelegatingWorker>()
            .setExpedited(OutOfQuotaPolicy.RUN_AS_NON_EXPEDITED_WORK_REQUEST)
            .setConstraints(SyncConstraints)
            .setInputData(SyncWorker::class.delegatedData())
            .build()
    }
}
