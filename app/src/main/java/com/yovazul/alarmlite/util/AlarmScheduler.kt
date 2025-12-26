package com.yovazul.alarmlite.util

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import com.yovazul.alarmlite.data.Alarm
import com.yovazul.alarmlite.receiver.AlarmReceiver
import java.util.*

class AlarmScheduler(private val context: Context) {
    
    private val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
    
    fun scheduleAlarm(alarm: Alarm) {
        if (!alarm.isEnabled) return
        
        if (alarm.hasAtLeastOneDay()) {
            // Schedule for specific days of the week
            scheduleDaysOfWeek(alarm)
        } else {
            // Schedule for next occurrence (one-time alarm)
            scheduleNextOccurrence(alarm)
        }
    }
    
    private fun scheduleDaysOfWeek(alarm: Alarm) {
        val daysOfWeek = mapOf(
            Calendar.MONDAY to alarm.monday,
            Calendar.TUESDAY to alarm.tuesday,
            Calendar.WEDNESDAY to alarm.wednesday,
            Calendar.THURSDAY to alarm.thursday,
            Calendar.FRIDAY to alarm.friday,
            Calendar.SATURDAY to alarm.saturday,
            Calendar.SUNDAY to alarm.sunday
        )
        
        daysOfWeek.forEach { (dayOfWeek, isEnabled) ->
            if (isEnabled) {
                val calendar = Calendar.getInstance().apply {
                    set(Calendar.HOUR_OF_DAY, alarm.hour)
                    set(Calendar.MINUTE, alarm.minute)
                    set(Calendar.SECOND, 0)
                    set(Calendar.MILLISECOND, 0)
                    set(Calendar.DAY_OF_WEEK, dayOfWeek)
                    
                    // If the time has passed today, schedule for next week
                    if (timeInMillis <= System.currentTimeMillis()) {
                        add(Calendar.WEEK_OF_YEAR, 1)
                    }
                }
                
                scheduleExactAlarm(alarm.id * 10 + dayOfWeek, alarm, calendar.timeInMillis)
            }
        }
    }
    
    private fun scheduleNextOccurrence(alarm: Alarm) {
        val calendar = Calendar.getInstance().apply {
            set(Calendar.HOUR_OF_DAY, alarm.hour)
            set(Calendar.MINUTE, alarm.minute)
            set(Calendar.SECOND, 0)
            set(Calendar.MILLISECOND, 0)
            
            // If the time has passed today, schedule for tomorrow
            if (timeInMillis <= System.currentTimeMillis()) {
                add(Calendar.DAY_OF_YEAR, 1)
            }
        }
        
        scheduleExactAlarm(alarm.id, alarm, calendar.timeInMillis)
    }
    
    private fun scheduleExactAlarm(requestCode: Int, alarm: Alarm, triggerAtMillis: Long) {
        val intent = Intent(context, AlarmReceiver::class.java).apply {
            putExtra("ALARM_ID", alarm.id)
            putExtra("ALARM_LABEL", alarm.label)
            putExtra("SOUND_URI", alarm.soundUri)
            putExtra("DURATION", alarm.duration)
        }
        
        val pendingIntent = PendingIntent.getBroadcast(
            context,
            requestCode,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )
        
        try {
            alarmManager.setExactAndAllowWhileIdle(
                AlarmManager.RTC_WAKEUP,
                triggerAtMillis,
                pendingIntent
            )
        } catch (e: SecurityException) {
            // Handle permission denial
            e.printStackTrace()
        }
    }
    
    fun cancelAlarm(alarm: Alarm) {
        if (alarm.hasAtLeastOneDay()) {
            // Cancel all day-specific alarms
            for (dayOfWeek in Calendar.MONDAY..Calendar.SUNDAY) {
                cancelAlarmByRequestCode(alarm.id * 10 + dayOfWeek)
            }
        } else {
            // Cancel one-time alarm
            cancelAlarmByRequestCode(alarm.id)
        }
    }
    
    private fun cancelAlarmByRequestCode(requestCode: Int) {
        val intent = Intent(context, AlarmReceiver::class.java)
        val pendingIntent = PendingIntent.getBroadcast(
            context,
            requestCode,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )
        alarmManager.cancel(pendingIntent)
        pendingIntent.cancel()
    }
}
