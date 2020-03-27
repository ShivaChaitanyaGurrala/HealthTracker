package com.example.healthtracker

import android.app.Notification
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import kotlin.math.abs

class IdlenessDetection: SensorEventListener {
    private var sensorManager: SensorManager? = null
    private var accelerometerSensor: Sensor? = null
    private var threshold = 7.0f
    private var idle = 0.5*60*1000L
    private var timer = 0L
    var lastX = 0F
    var lastY = 0F
    var lastZ = 0F
    var force = 0F
    var FORCE = 0F
    private var contextOfService : Context? = null

    fun checkIdealTime(context: Context){
        contextOfService = context
        sensorManager = context.getSystemService(Context.SENSOR_SERVICE) as SensorManager?
        accelerometerSensor = sensorManager!!.getDefaultSensor(Sensor.TYPE_ACCELEROMETER)
        sensorManager!!.registerListener(this, sensorManager!!.getDefaultSensor(Sensor.TYPE_ACCELEROMETER), SensorManager.SENSOR_DELAY_NORMAL)

    }


    override fun onSensorChanged(event: SensorEvent?) {
        if(event!!.sensor.type == Sensor.TYPE_ACCELEROMETER){
            force = abs(event.values[0]+event.values[1]+event.values[2] -lastX-lastY-lastZ)
            Log.i("force", force.toString())
            if(force>FORCE){
                FORCE = force
                timer = 0L
            }
            else if(!isEligible(FORCE,force)){
                if(timer == 0L) {
                        timer = System.currentTimeMillis()
                }
                if(System.currentTimeMillis() - timer >= idle) {
                    val msg = "Idle for more than " + idle/60000 + " minutes?"
                    Log.i("NOTIFICATION", msg)
                    timer = 0L
                    generateNotification(msg)
                }
            }

        }
    }
    override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {
    }
    private fun isEligible(previousForce:Float,presentForce:Float): Boolean {
        var differenceCheck = abs(previousForce-presentForce)
        return differenceCheck > threshold
    }

    private fun generateNotification(msg : String) {
        // generate a notification
        val notificationIntent = Intent(contextOfService, PhysicalCare::class.java)
        val pendingIntent = PendingIntent.getActivity(
            contextOfService,
            0, notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT
        )
        val notification: Notification = NotificationCompat.Builder(contextOfService!!.applicationContext, "Health Tracker")
            .setSmallIcon(R.drawable.healthicon)
            .setContentTitle("Health Tracker Service")
            .setContentText(msg)
            .setContentIntent(pendingIntent)
            .build()
        notification.flags = 16 or notification.flags
        with(NotificationManagerCompat.from(contextOfService!!.applicationContext)) {
            // notificationId is a unique int for each notification that you must define
            notify(3, notification)
        }
    }
}