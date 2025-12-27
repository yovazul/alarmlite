package com.yovazul.alarmlite.service

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.Service
import android.content.Context
import android.content.Intent
import android.media.AudioAttributes
import android.media.AudioFocusRequest
import android.media.AudioManager
import android.media.MediaPlayer
import android.net.Uri
import android.os.Build
import android.os.IBinder
import androidx.core.app.NotificationCompat
import com.yovazul.alarmlite.R
import kotlinx.coroutines.*

class AlarmSoundService : Service() {
    
    private var mediaPlayer: MediaPlayer? = null
    private var audioManager: AudioManager? = null
    private var audioFocusRequest: AudioFocusRequest? = null
    private val serviceScope = CoroutineScope(Dispatchers.Main + SupervisorJob())
    private var stopJob: Job? = null
    
    companion object {
        private const val NOTIFICATION_ID = 1
        private const val CHANNEL_ID = "alarm_sound_channel"
        const val ACTION_STOP = "com.yovazul.alarmlite.STOP_ALARM"
    }
    
    override fun onCreate() {
        super.onCreate()
        audioManager = getSystemService(Context.AUDIO_SERVICE) as AudioManager
        createNotificationChannel()
    }
    
    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        if (intent?.action == ACTION_STOP) {
            stopAlarmSound()
            stopSelf()
            return START_NOT_STICKY
        }
        
        val soundUri = intent?.getStringExtra("SOUND_URI") ?: ""
        val duration = intent?.getIntExtra("DURATION", 60) ?: 60
        val label = intent?.getStringExtra("ALARM_LABEL") ?: "Alarma"
        
        startForeground(NOTIFICATION_ID, createNotification(label))
        playAlarmSound(soundUri, duration)
        
        return START_STICKY
    }
    
    private fun createNotificationChannel() {
        val channel = NotificationChannel(
            CHANNEL_ID,
            "Alarm Sound",
            NotificationManager.IMPORTANCE_HIGH
        ).apply {
            description = "Channel for alarm sound playback"
            setSound(null, null)
        }
        
        val notificationManager = getSystemService(NotificationManager::class.java)
        notificationManager?.createNotificationChannel(channel)
    }
    
    private fun createNotification(label: String): Notification {
        return NotificationCompat.Builder(this, CHANNEL_ID)
            .setContentTitle("Alarma sonando")
            .setContentText(label)
            .setSmallIcon(R.drawable.ic_alarm)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setCategory(NotificationCompat.CATEGORY_ALARM)
            .setOngoing(true)
            .build()
    }
    
    private fun playAlarmSound(soundUriString: String, duration: Int) {
        requestAudioFocus()
        
        try {
            mediaPlayer = MediaPlayer().apply {
                if (soundUriString.isNotEmpty()) {
                    val uri = Uri.parse(soundUriString)
                    setDataSource(applicationContext, uri)
                } else {
                    // Use default alarm sound
                    val defaultUri = android.provider.Settings.System.DEFAULT_ALARM_ALERT_URI
                    setDataSource(applicationContext, defaultUri)
                }
                
                // Configure audio attributes to prioritize headphones
                val audioAttributes = AudioAttributes.Builder()
                    .setUsage(AudioAttributes.USAGE_ALARM)
                    .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
                    .build()
                setAudioAttributes(audioAttributes)
                
                isLooping = true
                prepare()
                start()
            }
            
            // Schedule stop after duration
            stopJob = serviceScope.launch {
                delay(duration * 1000L)
                stopAlarmSound()
                stopSelf()
            }
            
        } catch (e: Exception) {
            e.printStackTrace()
            stopSelf()
        }
    }
    
    private fun requestAudioFocus() {
        audioManager?.let { manager ->
            val audioAttributes = AudioAttributes.Builder()
                .setUsage(AudioAttributes.USAGE_ALARM)
                .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
                .build()
            
            audioFocusRequest = AudioFocusRequest.Builder(AudioManager.AUDIOFOCUS_GAIN_TRANSIENT)
                .setAudioAttributes(audioAttributes)
                .setAcceptsDelayedFocusGain(false)
                .setWillPauseWhenDucked(false)
                .build()
            
            audioFocusRequest?.let { request ->
                manager.requestAudioFocus(request)
            }
        }
    }
    
    private fun stopAlarmSound() {
        stopJob?.cancel()
        
        mediaPlayer?.let {
            if (it.isPlaying) {
                it.stop()
            }
            it.release()
        }
        mediaPlayer = null
        
        audioManager?.let { manager ->
            audioFocusRequest?.let { request ->
                manager.abandonAudioFocusRequest(request)
            }
        }
    }
    
    override fun onDestroy() {
        super.onDestroy()
        stopAlarmSound()
        serviceScope.cancel()
    }
    
    override fun onBind(intent: Intent?): IBinder? = null
}
