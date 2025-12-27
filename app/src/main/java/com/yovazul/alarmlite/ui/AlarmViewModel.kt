package com.yovazul.alarmlite.ui

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import com.yovazul.alarmlite.data.Alarm
import com.yovazul.alarmlite.database.AlarmDatabase
import com.yovazul.alarmlite.database.AlarmRepository
import com.yovazul.alarmlite.util.AlarmScheduler
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class AlarmViewModel(application: Application) : AndroidViewModel(application) {
    
    private val repository: AlarmRepository
    private val scheduler: AlarmScheduler
    val allAlarms: LiveData<List<Alarm>>
    
    init {
        val alarmDao = AlarmDatabase.getDatabase(application).alarmDao()
        repository = AlarmRepository(alarmDao)
        allAlarms = repository.allAlarms
        scheduler = AlarmScheduler(application)
    }
    
    fun insert(alarm: Alarm) = viewModelScope.launch(Dispatchers.IO) {
        val id = repository.insert(alarm)
        val insertedAlarm = alarm.copy(id = id.toInt())
        if (insertedAlarm.isEnabled) {
            scheduler.scheduleAlarm(insertedAlarm)
        }
    }
    
    fun update(alarm: Alarm) = viewModelScope.launch(Dispatchers.IO) {
        repository.update(alarm)
        scheduler.cancelAlarm(alarm)
        if (alarm.isEnabled) {
            scheduler.scheduleAlarm(alarm)
        }
    }
    
    fun delete(alarm: Alarm) = viewModelScope.launch(Dispatchers.IO) {
        scheduler.cancelAlarm(alarm)
        repository.delete(alarm)
    }
    
    fun toggleAlarmEnabled(alarm: Alarm) = viewModelScope.launch(Dispatchers.IO) {
        val updatedAlarm = alarm.copy(isEnabled = !alarm.isEnabled)
        repository.update(updatedAlarm)
        scheduler.cancelAlarm(alarm)
        if (updatedAlarm.isEnabled) {
            scheduler.scheduleAlarm(updatedAlarm)
        }
    }
}
