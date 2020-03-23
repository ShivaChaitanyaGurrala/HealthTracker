package com.example.healthtracker

import android.content.Intent
import android.os.Bundle
import android.os.PersistableBundle
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_health.*

class HealthTracker: AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?, persistentState: PersistableBundle?) {
        super.onCreate(savedInstanceState, persistentState)
        val recievedintent = intent.extras
        if(recievedintent!=null){
            if(recievedintent.containsKey("count")){
                textView.text = recievedintent.getString("count").toString()
            }
        }
    }
}