package dev.abrantes.monitor.infrastructure

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface ResponseDao {
    @Query("SELECT * FROM response WHERE uri =:uri")
    fun getAllByUri(uri: String): Flow<List<Response>>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun addResponse(response: Response)

    @Query("DELETE FROM response WHERE uri=:uri")
    suspend fun deleteMany(uri: String)
}
