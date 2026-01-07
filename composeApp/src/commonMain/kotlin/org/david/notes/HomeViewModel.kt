package org.david.notes

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import org.david.notes.db.NoteDatabase
import org.david.notes.models.Note

class HomeViewModel(noteDatabase: NoteDatabase) : ViewModel() {

    private val dao = noteDatabase.notesDao()
    private val _notes = dao.getAllNotes()
    val notes = _notes

    fun addNote(note: Note) {
        viewModelScope.launch {
            dao.insertNote(note)
        }
    }
}