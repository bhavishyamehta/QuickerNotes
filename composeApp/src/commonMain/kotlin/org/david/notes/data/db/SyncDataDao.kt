package org.david.notes.data.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import org.david.notes.models.SyncMetadata

@Dao
interface SyncDataDao {

    @Query("SELECT * FROM syncmetadata WHERE id = 1")
    suspend fun getSyncMetadata(): SyncMetadata?

    @Query("UPDATE syncmetadata SET lastSyncTimestamp = :timestamp WHERE id = 1")
    suspend fun updateLastSyncTimestamp(timestamp: String)

    @Insert(onConflict = androidx.room.OnConflictStrategy.REPLACE)
    suspend fun insertSyncMetadata(metadata: SyncMetadata)

    @Query("UPDATE syncmetadata SET isSyncing = :isSyncing WHERE id = 1")
    suspend fun updateSyncingStatus(isSyncing: Boolean)
}