package org.david.notes

import androidx.compose.ui.window.ComposeUIViewController
import org.david.notes.data.cache.DataStoreManager
import org.david.notes.data.db.getNoteDatabase
import org.david.notes.data.remote.createDataStore

fun MainViewController() = ComposeUIViewController {
    App(
        database = getNoteDatabase(getDatabaseBuilder()), dataStoreManager = DataStoreManager(
            createDataStore()
        )
    )
}