package com.example.healthtracker

import android.app.Notification
import android.app.Service
import android.content.Context
import android.content.Intent
import android.os.IBinder
import androidx.core.app.NotificationCompat

class ScreenTimeService:Service(){
    private lateinit var screenTime:ScreenTimeDetection
    private val SHARED_PREFERENCES = "HEALTH_TRACKER_PREFERENCES"

    override fun onBind(intent: Intent?): IBinder? {
        return null
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {

        screenTime = ScreenTimeDetection()
        screenTime.startScreenTimer(this)

        val text = "This is a personalized health Tracker application"
        val notification: Notification = NotificationCompat.Builder(this, getString(R.string.CHANNEL_ID))
            .setSmallIcon(R.drawable.healthicon)
            .setContentTitle("Health Tracker Service")
            .setContentText(text)
            .build()
        notification.flags = 16 or notification.flags
        startForeground(1, notification)
        return START_NOT_STICKY
    }

    override fun onDestroy() {
        var sharedPreferences = getSharedPreferences(SHARED_PREFERENCES, Context.MODE_PRIVATE)
        if(sharedPreferences.contains("ACTIVITY_SCREEN_TIME")) {
            val screenTimeStatus = sharedPreferences.getBoolean("ACTIVITY_SCREEN_TIME", false)
            if(!screenTimeStatus) {
                screenTime.onDestroy(this)
                var editor = sharedPreferences.edit()
                editor.remove("ACTIVITY_SCREEN_TIME")
                editor.commit()
            }
        }
        super.onDestroy()
    }
}