package org.david.notes

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import org.david.notes.data.cache.DataStoreManager
import org.david.notes.data.db.NoteDatabase
import org.david.notes.data.remote.ApiService
import org.david.notes.data.remote.HttpClientFactory
import org.david.notes.data.remote.SyncRepository
import org.david.notes.data.remote.SyncState
import org.david.notes.models.Note

class HomeViewModel(
    val noteDatabase: NoteDatabase,
    val dataStoreManager: DataStoreManager,
) : ViewModel() {

    private val dao = noteDatabase.notesDao()
    private val _notes = dao.getAllNotes()
    val notes = _notes

    val userEmail = MutableStateFlow<String>("")

    init {
        viewModelScope.launch {
            val email = dataStoreManager.getEmail()
            userEmail.value = email ?: ""
            performSync()
        }
    }

    fun performSync() {
        viewModelScope.launch {
            val apiService = ApiService(HttpClientFactory.getHttpClient(), dataStoreManager)
            val userId = dataStoreManager.getUserId() ?: return@launch
            val syncRepository = SyncRepository(
                userId = userId,
                noteDao = noteDatabase.notesDao(),
                syncDataDao = noteDatabase.syncMetaDataDao(),
                apiService = apiService
            )

            syncRepository.performSync()

            syncRepository.syncState.collectLatest {
                when (it) {
                    is SyncState.Error -> {

                    }
                    is SyncState.Idle -> {

                    }
                    is SyncState.Success -> {

                    }
                    is SyncState.Syncing -> {

                    }
                }
            }
        }
    }

    fun addNote(note: Note) {
        viewModelScope.launch {
            dao.insertNote(note)
            performSync()
        }
    }
}