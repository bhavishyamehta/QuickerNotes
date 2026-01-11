package org.david.notes.data.remote

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.withContext
import org.david.notes.data.db.NoteDao
import org.david.notes.data.db.SyncDataDao
import org.david.notes.models.Note
import org.david.notes.models.NoteChange
import org.david.notes.models.SyncRequest
import org.david.notes.models.SyncResponse

class SyncRepository(
    private val userId: String,
    private val noteDao: NoteDao,
    private val syncDataDao: SyncDataDao,
    private val apiService: ApiService
) {

    private val _syncState = MutableStateFlow<SyncState>(SyncState.Idle)
    val syncState = _syncState.asStateFlow()

    suspend fun performSync() = withContext(Dispatchers.IO) {
        try {
            // gather sync data
            val metaData = syncDataDao.getSyncMetadata()
            if (metaData?.isSyncing == true) {
                return@withContext
            }

            _syncState.value = SyncState.Syncing
            syncDataDao.updateSyncingStatus(true)


            //fetch dirty notes from local database
            val dirtyNotes = noteDao.getDirtyNotes()

            // create sync request object
            val syncRequest = SyncRequest(
                since = metaData?.lastSyncTimestamp,
                changes = dirtyNotes.map { note ->
                    NoteChange(
                        id = note.id,
                        title = note.title,
                        body = note.description,
                        isDeleted = note.isDeleted,
                        updatedAt = note.updatedAt
                    )
                }
            )
            val response = apiService.sync(syncRequest)


            // process response from server
            response.getOrNull()?.let {
                processSyncResponse(it)
            }

            syncDataDao.updateLastSyncTimestamp(response.getOrNull()?.nextSince ?: "")
            syncDataDao.updateSyncingStatus(false)

            _syncState.value = SyncState.Success(
                response.getOrNull()!!
            )
        } catch (e: Exception) {
            _syncState.value = SyncState.Error(e.message ?: "Unknown error")
            //syncDataDao.updateSyncingStatus(false)
        }
    }

    suspend fun processSyncResponse(response: SyncResponse) = withContext(Dispatchers.IO) {
        //applied
        if (response.applied.isNotEmpty()) {
            noteDao.markAsSynced(response.applied)
        }
        // conflicts

        if (response.conflicts.isNotEmpty()) {
            val conflictNotes = response.conflicts.map { noteChange ->
                Note(
                    id = noteChange.id,
                    title = noteChange.title,
                    description = noteChange.body,
                    isDeleted = noteChange.isDeleted,
                    updatedAt = noteChange.updatedAt,
                    isDirty = false,
                    userId = userId
                )
            }
            noteDao.insertNotes(conflictNotes)
        }

        // changes
        if (response.changes.isNotEmpty()) {
            val serverNotes = response.changes.map { noteChange ->
                Note(
                    id = noteChange.id,
                    title = noteChange.title,
                    description = noteChange.body,
                    isDeleted = noteChange.isDeleted,
                    updatedAt = noteChange.updatedAt,
                    isDirty = false,
                    userId = userId
                )
            }
            noteDao.insertNotes(serverNotes)
        }
    }


}

sealed class SyncState {
    object Idle : SyncState()
    object Syncing : SyncState()
    data class Success(val data: SyncResponse) : SyncState()
    data class Error(val errorMSg: String) : SyncState()
}