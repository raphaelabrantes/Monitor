package dev.abrantes.monitor.infrastructure

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow


@Dao
interface RegisterUrlDao {

    @Query("SELECT * FROM registerurl")
    fun getAll(): Flow<List<RegisterUrl>>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun addRegisterUrl(registerUrl: RegisterUrl)

    @Delete
    suspend fun deleteRegisterUrl(registerUrl: RegisterUrl)
}