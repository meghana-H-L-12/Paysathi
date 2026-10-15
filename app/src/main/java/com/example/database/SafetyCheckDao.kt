package com.example.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface SafetyCheckDao {
    @Query("SELECT * FROM safety_checks ORDER BY id DESC")
    fun getAllChecks(): Flow<List<SafetyCheckEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCheck(check: SafetyCheckEntity): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(checks: List<SafetyCheckEntity>)

    @Query("DELETE FROM safety_checks")
    suspend fun clearAll()

    @Query("SELECT COUNT(*) FROM safety_checks")
    suspend fun getCount(): Int
}
