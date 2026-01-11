package org.david.notes.data.remote

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import kotlinx.cinterop.ExperimentalForeignApi
import org.david.notes.data.cache.createDataStore
import org.david.notes.data.cache.dataStoreFileName
import platform.Foundation.NSDocumentDirectory
import platform.Foundation.NSFileManager
import platform.Foundation.NSUserDomainMask

fun createDataStore(): DataStore<Preferences> {
    return createDataStore(
        producePath ={
            getDocumentPath() + "/${dataStoreFileName}"
        }
    )
}

@OptIn(ExperimentalForeignApi::class)
fun getDocumentPath(): String {
    val documentDirectory = NSFileManager.defaultManager.URLForDirectory(
        directory = NSDocumentDirectory,
        inDomain = NSUserDomainMask,
        appropriateForURL = null,
        create = false,
        error = null
    )
    return requireNotNull(documentDirectory?.path)
}