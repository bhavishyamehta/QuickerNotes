package org.david.notes

import androidx.compose.ui.window.ComposeUIViewController
import org.david.notes.db.getNoteDatabase

fun MainViewController() = ComposeUIViewController {
    App(
        database = getNoteDatabase(getDatabaseBuilder())
    )
}