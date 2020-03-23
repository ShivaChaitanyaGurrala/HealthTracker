package com.example.healthtracker

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Intent
import android.os.Build
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        createNotificationChannel()
        var count:String = "0"
        buttonStartService.setOnClickListener(){
            val input = editText.text.toString()
            val serviceintent = Intent(this,TrackerService::class.java)
            serviceintent.putExtra("Username",input)
            serviceintent.putExtra("count",count)
            ContextCompat.startForegroundService(this,serviceintent)
        }
        buttonStopService.setOnClickListener(){
            val serviceintent = Intent(this,TrackerService::class.java)
            stopService(serviceintent)
        }
        buttonTracker.setOnClickListener(){
            val intent = Intent(this,HealthTracker::class.java)
            intent.putExtra("count",count)
            startActivityForResult(intent,12)
        }

    }
    private fun createNotificationChannel(){
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            val notificationChannel = NotificationChannel(getString(R.string.CHANNEL_ID),getString(R.string.CHANNEL_NEWS), NotificationManager.IMPORTANCE_DEFAULT );
            val notificationManager =
                getSystemService(NotificationManager::class.java)
            notificationManager.createNotificationChannel(notificationChannel)
        }
    }

}
