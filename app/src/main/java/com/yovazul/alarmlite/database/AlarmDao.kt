package com.yovazul.alarmlite.database

import androidx.lifecycle.LiveData
import androidx.room.*
import com.yovazul.alarmlite.data.Alarm

@Dao
interface AlarmDao {
    
    @Query("SELECT * FROM alarms ORDER BY hour, minute")
    fun getAllAlarms(): LiveData<List<Alarm>>
    
    @Query("SELECT * FROM alarms WHERE isEnabled = 1 ORDER BY hour, minute")
    fun getEnabledAlarms(): List<Alarm>
    
    @Query("SELECT * FROM alarms WHERE id = :id")
    suspend fun getAlarmById(id: Int): Alarm?
    
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(alarm: Alarm): Long
    
    @Update
    suspend fun update(alarm: Alarm)
    
    @Delete
    suspend fun delete(alarm: Alarm)
    
    @Query("DELETE FROM alarms WHERE id = :id")
    suspend fun deleteById(id: Int)
}
