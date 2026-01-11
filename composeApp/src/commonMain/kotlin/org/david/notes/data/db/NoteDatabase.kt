package org.david.notes.data.db

import androidx.room.ConstructedBy
import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.RoomDatabaseConstructor
import androidx.sqlite.driver.bundled.BundledSQLiteDriver
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import org.david.notes.models.Note
import org.david.notes.models.SyncMetadata

@Database(entities = [Note::class, SyncMetadata::class], version = 1, exportSchema = false)
@ConstructedBy(NoteDatabaseConstructor::class)
abstract class NoteDatabase : RoomDatabase() {
    abstract fun notesDao(): NoteDao
    abstract fun syncMetaDataDao(): SyncDataDao
}

@Suppress("KotlinNoActualForExpect")
expect object NoteDatabaseConstructor : RoomDatabaseConstructor<NoteDatabase> {
    override fun initialize(): NoteDatabase
}

fun getNoteDatabase(builder: RoomDatabase.Builder<NoteDatabase>): NoteDatabase {
    return builder
        .setDriver(BundledSQLiteDriver())
        .setQueryCoroutineContext(Dispatchers.IO)
        .build()
}