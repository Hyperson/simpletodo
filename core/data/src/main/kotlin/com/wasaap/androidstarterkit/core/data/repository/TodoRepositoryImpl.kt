package com.wasaap.androidstarterkit.core.data.repository

import com.wasaap.androidstarterkit.core.common.network.Dispatcher
import com.wasaap.androidstarterkit.core.common.network.TodoDispatchers
import com.wasaap.androidstarterkit.core.data.sync.SyncManager
import com.wasaap.androidstarterkit.core.data.sync.Syncable
import com.wasaap.androidstarterkit.core.data.sync.Synchronizer
import com.wasaap.androidstarterkit.core.database.dao.TodoDao
import com.wasaap.androidstarterkit.core.database.model.TodoEntity
import com.wasaap.androidstarterkit.core.database.model.asExternalModel
import com.wasaap.androidstarterkit.core.domain.common.Result
import com.wasaap.androidstarterkit.core.domain.common.asResult
import com.wasaap.androidstarterkit.core.domain.repository.TodoRepository
import com.wasaap.androidstarterkit.core.model.NewTodo
import com.wasaap.androidstarterkit.core.model.Todo
import com.wasaap.androidstarterkit.core.model.UpdateTodo
import com.wasaap.androidstarterkit.core.network.TodoNetworkDataSource
import com.wasaap.androidstarterkit.core.network.model.NetworkTodoWrite
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import java.util.UUID
import javax.inject.Inject

class TodoRepositoryImpl @Inject constructor(
    private val remote: TodoNetworkDataSource,
    private val todoDao: TodoDao,
    private val syncManager: SyncManager,
    @param:Dispatcher(TodoDispatchers.IO) private val ioDispatcher: CoroutineDispatcher
) : TodoRepository, Syncable {

    override fun getTodos(): Flow<Result<List<Todo>>> =
        todoDao.getTodosStream()
            .map { entities -> entities.map { it.asExternalModel() } }
            .asResult()
            .flowOn(ioDispatcher)

    override fun getTodoById(id: String): Flow<Result<Todo>> =
        todoDao.getTodoByIdStream(id)
            .filterNotNull()
            .map { entity -> entity.asExternalModel() }
            .asResult()
            .flowOn(ioDispatcher)

    override fun addTodo(newTodo: NewTodo): Flow<Result<Todo>> = flow {
        val localEntity = TodoEntity(
            localId = UUID.randomUUID().toString(),
            remoteId = null,
            name = newTodo.name,
            done = newTodo.done,
            isSynced = false,
            isDeleted = false
        )
        todoDao.insertOrReplace(listOf(localEntity))
        emit(localEntity.asExternalModel())
        syncManager.requestSync()
    }
        .asResult()
        .flowOn(ioDispatcher)

    override fun updateTodo(updateTodo: UpdateTodo): Flow<Result<Unit>> = flow {
        val entity = todoDao.getTodoById(updateTodo.id)?.copy(
            name = updateTodo.name,
            done = updateTodo.done,
            isSynced = false
        )
        if (entity != null) {
            todoDao.updateTodos(listOf(entity))
        }
        emit(Unit)
        syncManager.requestSync()
    }
        .asResult()
        .flowOn(ioDispatcher)

    override fun deleteTodo(id: String): Flow<Result<Unit>> = flow {
        val entity = todoDao.getTodoById(id)?.copy(
            isDeleted = true,
            isSynced = false
        )
        if (entity != null) {
            todoDao.updateTodos(listOf(entity))
        }
        emit(Unit)
        syncManager.requestSync()
    }
        .asResult()
        .flowOn(ioDispatcher)

    override suspend fun syncWith(synchronizer: Synchronizer): Boolean = try {
        pushPendingTodos()
        pullRemoteTodos()
        true
    } catch (e: Exception) {
        false
    }

    private suspend fun pushPendingTodos() {
        val pending = todoDao.getPendingTodos()
        for (pendingTodo in pending) {
            when {
                !pendingTodo.isDeleted && pendingTodo.remoteId == null -> pushNewTodo(pendingTodo)
                !pendingTodo.isDeleted -> pushUpdatedTodo(pendingTodo)
                pendingTodo.remoteId != null -> pushDeletedTodo(pendingTodo)
                pendingTodo.isDeleted && pendingTodo.remoteId == null -> {
                    todoDao.deleteByLocalId(pendingTodo.localId)
                }
            }
        }
    }

    private suspend fun pushNewTodo(pendingTodo: TodoEntity) {
        val remoteTodo = remote.addTodo(NetworkTodoWrite(pendingTodo.name, pendingTodo.done))
        todoDao.insertOrReplace(
            listOf(pendingTodo.copy(remoteId = remoteTodo.id, isSynced = true))
        )
    }

    private suspend fun pushUpdatedTodo(pendingTodo: TodoEntity) {
        pendingTodo.remoteId?.let { remoteId ->
            remote.updateTodo(remoteId, NetworkTodoWrite(pendingTodo.name, pendingTodo.done))
            todoDao.insertOrReplace(listOf(pendingTodo.copy(isSynced = true)))
        }
    }

    private suspend fun pushDeletedTodo(pendingTodo: TodoEntity) {
        pendingTodo.remoteId?.let { remoteId ->
            remote.deleteTodo(remoteId)
            todoDao.deleteByLocalId(pendingTodo.localId)
        }
    }

    private suspend fun pullRemoteTodos() {
        val remoteTodos = remote.getTodos()
        val localByRemoteId = todoDao.getTodos()
            .mapNotNull { it.remoteId?.let { id -> id to it } }
            .toMap()

        val entities = remoteTodos.map { remoteTodo ->
            val existing = localByRemoteId[remoteTodo.id]
            val localId = existing?.localId ?: UUID.randomUUID().toString()

            TodoEntity(
                localId = localId,
                remoteId = remoteTodo.id,
                name = remoteTodo.name,
                done = remoteTodo.done,
                isSynced = true,
                isDeleted = false
            )
        }

        todoDao.insertOrReplace(entities)
    }
}