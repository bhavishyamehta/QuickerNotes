package org.david.notes

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import org.david.notes.data.cache.createDataStore
import org.david.notes.data.cache.dataStoreFileName

fun createDataStorePath(context: Context): DataStore<Preferences> = createDataStore(
    producePath = { context.filesDir.resolve(dataStoreFileName).absolutePath }
)