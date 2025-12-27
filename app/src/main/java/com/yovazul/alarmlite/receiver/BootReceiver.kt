package com.yovazul.alarmlite.receiver

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.yovazul.alarmlite.database.AlarmDatabase
import com.yovazul.alarmlite.database.AlarmRepository
import com.yovazul.alarmlite.util.AlarmScheduler
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class BootReceiver : BroadcastReceiver() {
    
    override fun onReceive(context: Context, intent: Intent) {
        if (intent.action == Intent.ACTION_BOOT_COMPLETED) {
            // Reschedule all enabled alarms after device reboot
            val database = AlarmDatabase.getDatabase(context)
            val repository = AlarmRepository(database.alarmDao())
            val scheduler = AlarmScheduler(context)
            
            CoroutineScope(Dispatchers.IO).launch {
                val alarms = repository.getEnabledAlarms()
                alarms.forEach { alarm ->
                    scheduler.scheduleAlarm(alarm)
                }
            }
        }
    }
}
