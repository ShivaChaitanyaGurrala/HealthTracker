package com.example.healthtracker

import android.app.Notification
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.os.Build
import android.os.PowerManager
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat


class ScreenTimeDetection: SensorEventListener {
    private var sensorManager: SensorManager? = null
    private var lightSensor: Sensor? = null
    private var powermanager:PowerManager? = null
    private var timer = 0L
    private val SCREEN_THRESHOLD = 5*60*1000L
    private val LIGHT_PERCENTAGE = 35
    private var AMBIENT_LIGHT_AROUND = 0F
    private var contextOfService : Context? = null

    fun startScreenTimer(context: Context) {
        // initialize the sensor manager
        contextOfService = context
        sensorManager = context.getSystemService(Context.SENSOR_SERVICE) as SensorManager?
        lightSensor = sensorManager!!.getDefaultSensor(Sensor.TYPE_LIGHT)
        sensorManager!!.registerListener(this, sensorManager!!.getDefaultSensor(Sensor.TYPE_LIGHT), SensorManager.SENSOR_DELAY_NORMAL)
        powermanager = context.getSystemService(Context.POWER_SERVICE) as PowerManager?
    }

    @RequiresApi(Build.VERSION_CODES.KITKAT_WATCH)
    override fun onSensorChanged(event: SensorEvent?) {
        if((event!!.sensor.type == Sensor.TYPE_LIGHT) and (powermanager?.isInteractive!!)){
            Log.i("LIGHT AMT: ", event.values[0].toString())

            // check if the light goes down or not
            if(event.values[0] > AMBIENT_LIGHT_AROUND) {
                AMBIENT_LIGHT_AROUND = event.values[0]
                timer = 0L
            }
            else {
                if(isEligible(AMBIENT_LIGHT_AROUND, event.values[0])) {
                    Log.i("TIMER VALUE", timer.toString())

                    if(timer == 0L) {
                        timer = System.currentTimeMillis()
                    }
                    if(System.currentTimeMillis() - timer >= SCREEN_THRESHOLD) {
                        val msg = "Looking at the screen for more than " + SCREEN_THRESHOLD/60000 + " minutes?"
                        Log.i("NOTIFICATION", msg)
                        timer = 0L
                        generateNotification(msg)
                    }
                }
            }
        }
    }

    override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {
    }

    // check if the light has goes down by certain amount
    private fun isEligible(ambientLightAround : Float, presentLight : Float) : Boolean {
        val lightPercentage = (presentLight/ambientLightAround)*100
        Log.i("LIGHT PERCENTAGE", lightPercentage.toString())
        return lightPercentage < LIGHT_PERCENTAGE
    }

    private fun generateNotification(msg : String) {
        // generate a notification
        val notificationIntent = Intent(contextOfService, EyeCare::class.java)
        val pendingIntent = PendingIntent.getActivity(
            contextOfService,
            0, notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT
        )
        val notification: Notification = NotificationCompat.Builder(contextOfService!!.applicationContext,
            "Health Tracker"
        )
            .setSmallIcon(R.drawable.healthicon)
            .setContentTitle("Health Tracker Service")
            .setContentText(msg)
            .setContentIntent(pendingIntent)
            .build()
        notification.flags = 16 or notification.flags
        with(NotificationManagerCompat.from(contextOfService!!.applicationContext)) {
            // notificationId is a unique int for each notification that you must define
            notify(2, notification)
        }
    }
}
