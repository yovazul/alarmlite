package com.yovazul.alarmlite.database

import androidx.lifecycle.LiveData
import com.yovazul.alarmlite.data.Alarm

class AlarmRepository(private val alarmDao: AlarmDao) {
    
    val allAlarms: LiveData<List<Alarm>> = alarmDao.getAllAlarms()
    
    suspend fun insert(alarm: Alarm): Long {
        return alarmDao.insert(alarm)
    }
    
    suspend fun update(alarm: Alarm) {
        alarmDao.update(alarm)
    }
    
    suspend fun delete(alarm: Alarm) {
        alarmDao.delete(alarm)
    }
    
    suspend fun deleteById(id: Int) {
        alarmDao.deleteById(id)
    }
    
    suspend fun getAlarmById(id: Int): Alarm? {
        return alarmDao.getAlarmById(id)
    }
    
    fun getEnabledAlarms(): List<Alarm> {
        return alarmDao.getEnabledAlarms()
    }
}
