package com.yovazul.alarmlite.ui

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.WindowManager
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.yovazul.alarmlite.R
import com.yovazul.alarmlite.service.AlarmSoundService
import java.text.SimpleDateFormat
import java.util.*

class AlarmRingActivity : AppCompatActivity() {
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        
        // Show activity over lock screen
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O_MR1) {
            setShowWhenLocked(true)
            setTurnScreenOn(true)
        } else {
            window.addFlags(
                WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED or
                WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON
            )
        }
        
        setContentView(R.layout.activity_alarm_ring)
        
        val alarmId = intent.getIntExtra("ALARM_ID", -1)
        val alarmLabel = intent.getStringExtra("ALARM_LABEL") ?: "Alarma"
        val soundUri = intent.getStringExtra("SOUND_URI") ?: ""
        val duration = intent.getIntExtra("DURATION", 60)
        
        // Display alarm info
        findViewById<TextView>(R.id.textViewAlarmLabel).text = alarmLabel
        
        val currentTime = SimpleDateFormat("HH:mm", Locale.getDefault()).format(Date())
        findViewById<TextView>(R.id.textViewTime).text = currentTime
        
        // Start alarm sound service
        val serviceIntent = Intent(this, AlarmSoundService::class.java).apply {
            putExtra("ALARM_ID", alarmId)
            putExtra("ALARM_LABEL", alarmLabel)
            putExtra("SOUND_URI", soundUri)
            putExtra("DURATION", duration)
        }
        
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            startForegroundService(serviceIntent)
        } else {
            startService(serviceIntent)
        }
        
        // Setup dismiss button
        findViewById<Button>(R.id.buttonDismiss).setOnClickListener {
            dismissAlarm()
        }
    }
    
    private fun dismissAlarm() {
        // Stop the alarm sound service
        val stopIntent = Intent(this, AlarmSoundService::class.java).apply {
            action = AlarmSoundService.ACTION_STOP
        }
        startService(stopIntent)
        
        finish()
    }
    
    override fun onBackPressed() {
        // Prevent back button from dismissing alarm
        // User must explicitly press dismiss button
    }
}
