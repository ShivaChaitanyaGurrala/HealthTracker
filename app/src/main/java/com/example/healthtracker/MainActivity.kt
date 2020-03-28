package com.example.healthtracker

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    val SHARED_PREFERENCES = "HEALTH_TRACKER_PREFERENCES"
    var idlenessStatus = false
    var screenTimeStatus = false


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setAppropriateStatusIcon()
        createNotificationChannel()

        imgBtnIdleness.setOnClickListener {

            // get & update the shared preferences
            var sharedPreferences = getSharedPreferences(SHARED_PREFERENCES, Context.MODE_PRIVATE)
            if(sharedPreferences.contains("ACTIVITY_IDLENESS")) {
                idlenessStatus = sharedPreferences.getBoolean("ACTIVITY_IDLENESS", false)
                Log.i("ACTIVITY_IDLENESS", idlenessStatus.toString())
            }
            val editor = sharedPreferences.edit()
            idlenessStatus = !idlenessStatus
            editor.putBoolean("ACTIVITY_IDLENESS", idlenessStatus)
            editor.commit()

            // start the background service
            if(idlenessStatus) {
                imgBtnIdleness.setImageResource(R.drawable.pause)
                txtViewIdleness.text = getString(R.string.STOP_IDLE_BUTTON)
                Toast.makeText(this, "Idleness tracking activated!", Toast.LENGTH_SHORT).show()
                val serviceIntent = Intent(this,IdlenessService::class.java)
                serviceIntent.putExtra("TRACKING_ACTIVITY", "Idleness")
                serviceIntent.putExtra("TRACKING_ACTIVITY_STATUS", idlenessStatus)
                ContextCompat.startForegroundService(this,serviceIntent)
            }
            else {
                imgBtnIdleness.setImageResource(R.drawable.play)
                txtViewIdleness.text = getString(R.string.START_IDLE_BUTTON)
                Toast.makeText(this, "Idleness tracking deactivated!", Toast.LENGTH_SHORT).show()
                val serviceIntent = Intent(this,IdlenessService::class.java)
                stopService(serviceIntent)
            }
        }

        imgBtnScreenTime.setOnClickListener {

            // get & update the shared preferences
            var sharedPreferences = getSharedPreferences(SHARED_PREFERENCES, Context.MODE_PRIVATE)
            if(sharedPreferences.contains("ACTIVITY_SCREEN_TIME")) {
                screenTimeStatus = sharedPreferences.getBoolean("ACTIVITY_SCREEN_TIME", false)
                Log.i("ACTIVITY_SCREEN_TIME", screenTimeStatus.toString())
            }
            val editor = sharedPreferences.edit()
            screenTimeStatus = !screenTimeStatus
            editor.putBoolean("ACTIVITY_SCREEN_TIME", screenTimeStatus)
            editor.commit()

            // start the background service
            if(screenTimeStatus) {
                imgBtnScreenTime.setImageResource(R.drawable.pause)
                txtViewScreenTime.text = getString(R.string.STOP_SCREEN_BUTTON)
                Toast.makeText(this, "Screen time tracking activated!", Toast.LENGTH_SHORT).show()
                val serviceIntent = Intent(this,ScreenTimeService::class.java)
                serviceIntent.putExtra("TRACKING_ACTIVITY", "ScreenTime")
                serviceIntent.putExtra("TRACKING_ACTIVITY_STATUS", screenTimeStatus)
                ContextCompat.startForegroundService(this,serviceIntent)
            }
            else {
                imgBtnScreenTime.setImageResource(R.drawable.play)
                txtViewScreenTime.text = getString(R.string.START_SCREEN_BUTTON)
                Toast.makeText(this, "Screen time tracking deactivated!", Toast.LENGTH_SHORT).show()
                val serviceIntent = Intent(this,ScreenTimeService::class.java)
                stopService(serviceIntent)
            }
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

    private fun setAppropriateStatusIcon() {
        var sharedPreferences = getSharedPreferences(SHARED_PREFERENCES, Context.MODE_PRIVATE)
        if(sharedPreferences.contains("ACTIVITY_IDLENESS")) {
            val status = sharedPreferences.getBoolean("ACTIVITY_IDLENESS", false)
            if(status) {
                imgBtnIdleness.setImageResource(R.drawable.pause)
                txtViewIdleness.text = getString(R.string.STOP_IDLE_BUTTON)
            }
            else {
                imgBtnIdleness.setImageResource(R.drawable.play)
                txtViewIdleness.text = getString(R.string.START_IDLE_BUTTON)
            }
        }
        if(sharedPreferences.contains("ACTIVITY_SCREEN_TIME")) {
            val status = sharedPreferences.getBoolean("ACTIVITY_SCREEN_TIME", false)
            if(status) {
                imgBtnScreenTime.setImageResource(R.drawable.pause)
                txtViewScreenTime.text = getString(R.string.STOP_SCREEN_BUTTON)
            }
            else {
                imgBtnScreenTime.setImageResource(R.drawable.play)
                txtViewScreenTime.text = getString(R.string.START_SCREEN_BUTTON)
            }
        }
    }
}
