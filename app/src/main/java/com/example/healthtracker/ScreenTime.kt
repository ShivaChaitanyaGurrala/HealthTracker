package com.example.healthtracker

import android.content.Context
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.util.Log

class ScreenTime: SensorEventListener {
    private var sensorManager: SensorManager? = null
    private var lightSensor: Sensor? = null
    private var timer = 0L
    private val SCREEN_THRESHOLD = 0.5*60*1000L
    private val LIGHT_PERCENTAGE = 50
    private var AMBIENT_LIGHT_AROUND = 0F

    fun startScreenTimer(context: Context) {
        // initialize the sensor manager
        sensorManager = context.getSystemService(Context.SENSOR_SERVICE) as SensorManager?
        lightSensor = sensorManager!!.getDefaultSensor(Sensor.TYPE_LIGHT)
        sensorManager!!.registerListener(this, sensorManager!!.getDefaultSensor(Sensor.TYPE_LIGHT), SensorManager.SENSOR_DELAY_NORMAL)

    }

    override fun onSensorChanged(event: SensorEvent?) {
        if(event!!.sensor.type == Sensor.TYPE_LIGHT){
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
                        val msg = "You have been looking at the screen for more than " + SCREEN_THRESHOLD/60000 + "minutes!"
                        Log.i("NOTIFICATION", msg)
                        timer = 0L
                    }
                }
            }
        }
    }

    override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {
    }

    // check if the light has goes down by certain amount
    fun isEligible(ambientLightAround : Float, presentLight : Float) : Boolean {
        val lightPercentage = (presentLight/ambientLightAround)*100
        Log.i("LIGHT PERCENTAGE", lightPercentage.toString())
        return lightPercentage < LIGHT_PERCENTAGE
    }
}