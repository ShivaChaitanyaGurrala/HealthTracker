package com.example.healthtracker

import android.app.Notification
import android.app.PendingIntent
import android.app.Service
import android.content.Intent
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.os.IBinder
import androidx.core.app.NotificationCompat

class TrackerService:Service(), SensorEventListener{
    var count:String = "0"
    override fun onBind(intent: Intent?): IBinder? {
        return null
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        var idleness = Idleness()
        idleness.checkIdealTime(this)

        var screenTime = ScreenTime()
        screenTime.startScreenTimer(this)

        val userName = intent?.getStringExtra("Username")
        count = intent?.getStringExtra("count").toString()
        val notificationIntent = Intent(this, HealthTracker::class.java)
        notificationIntent.putExtra("count",count)
        val pendingIntent = PendingIntent.getActivity(
            this,
            0, notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT
        )
        val text = "This is a personalized health Tracker application for $userName"
        val notification: Notification = NotificationCompat.Builder(this, getString(R.string.CHANNEL_ID))
            .setSmallIcon(R.drawable.healthicon)
            .setContentTitle("Health Tracker Service")
            .setContentText(text)
            .setContentIntent(pendingIntent)
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