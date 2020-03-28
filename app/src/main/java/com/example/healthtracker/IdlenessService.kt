package com.example.healthtracker

import android.app.Notification
import android.app.Service
import android.content.Context
import android.content.Intent
import android.os.IBinder
import android.util.Log
import androidx.core.app.NotificationCompat

class IdlenessService:Service(){
    private lateinit var idleness:IdlenessDetection
    private val SHARED_PREFERENCES = "HEALTH_TRACKER_PREFERENCES"

    override fun onBind(intent: Intent?): IBinder? {
        return null
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {

        idleness = IdlenessDetection()
        idleness.checkIdealTime(this)

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
        Log.i("ON_DESTROY", "onDestroy got called")
        var sharedPreferences = getSharedPreferences(SHARED_PREFERENCES, Context.MODE_PRIVATE)
        if(sharedPreferences.contains("ACTIVITY_IDLENESS")) {
            val idlenessStatus = sharedPreferences.getBoolean("ACTIVITY_IDLENESS", false)
            Log.i("ON_DESTROY", idlenessStatus.toString())
            if(!idlenessStatus) {
                idleness.onDestroy(this)
                var editor = sharedPreferences.edit()
                editor.remove("ACTIVITY_IDLENESS")
                editor.commit()
            }
        }
        super.onDestroy()
    }
}