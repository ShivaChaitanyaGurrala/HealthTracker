package com.example.healthtracker

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        createNotificationChannel()
        btnStartTracking.setOnClickListener(){
            Toast.makeText(this, "Tracking service activated!", Toast.LENGTH_SHORT).show()
            val serviceIntent = Intent(this,TrackerService::class.java)
            ContextCompat.startForegroundService(this,serviceIntent)
        }
        btnStopTracking.setOnClickListener(){
            Toast.makeText(this, "Tracking service deactivated!", Toast.LENGTH_SHORT).show()
            val serviceIntent = Intent(this,TrackerService::class.java)
            stopService(serviceIntent)
        }
    }

    private fun createNotificationChannel(){
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            val notificationChannel = NotificationChannel(getString(R.string.CHANNEL_ID),getString(R.string.CHANNEL_NEWS), NotificationManager.IMPORTANCE_DEFAULT )
            val notificationManager =
                getSystemService(NotificationManager::class.java)
            notificationManager.createNotificationChannel(notificationChannel)
        }
    }

}
