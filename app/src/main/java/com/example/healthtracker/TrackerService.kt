package com.example.healthtracker

import android.app.Notification
import android.app.Service
import android.content.Intent
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.os.IBinder
import androidx.core.app.NotificationCompat

class TrackerService:Service(), SensorEventListener{
    override fun onBind(intent: Intent?): IBinder? {
        return null
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        var idleness = Idleness()
        idleness.checkIdealTime(this)

        var screenTime = ScreenTime()
        screenTime.startScreenTimer(this)

        val userName = intent?.getStringExtra("Username")
        val text = "This is a personalized health Tracker application for $userName"
        val notification: Notification = NotificationCompat.Builder(this, getString(R.string.CHANNEL_ID))
            .setSmallIcon(R.drawable.healthicon)
            .setContentTitle("Health Tracker Service")
            .setContentText(text)
            .build()
        notification.flags = 16 or notification.flags
        startForeground(1, notification)
        return START_NOT_STICKY
    }

    override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {
    }

    override fun onSensorChanged(event: SensorEvent?) {
        //To change body of created functions use File | Settings | File Templates.
    }
}