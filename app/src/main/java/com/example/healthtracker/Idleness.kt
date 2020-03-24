package com.example.healthtracker

import android.content.Context
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.util.Log
import kotlin.math.abs

class Idleness: SensorEventListener {
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
    fun checkIdealTime(context: Context){
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
                    val msg = "You are Idle for more than " + idle/60000 + "minutes!"
                    Log.i("NOTIFICATION", msg)
                    timer = 0L
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

}