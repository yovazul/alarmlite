package com.yovazul.alarmlite.receiver

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.yovazul.alarmlite.ui.AlarmRingActivity

class AlarmReceiver : BroadcastReceiver() {
    
    override fun onReceive(context: Context, intent: Intent) {
        val alarmId = intent.getIntExtra("ALARM_ID", -1)
        val alarmLabel = intent.getStringExtra("ALARM_LABEL") ?: ""
        val soundUri = intent.getStringExtra("SOUND_URI") ?: ""
        val duration = intent.getIntExtra("DURATION", 60)
        
        // Launch the alarm ring activity
        val ringIntent = Intent(context, AlarmRingActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP
            putExtra("ALARM_ID", alarmId)
            putExtra("ALARM_LABEL", alarmLabel)
            putExtra("SOUND_URI", soundUri)
            putExtra("DURATION", duration)
        }
        
        context.startActivity(ringIntent)
    }
}
