package com.example.healthtracker

import android.content.DialogInterface
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_health_eye.*


class EyeCare: AppCompatActivity() {
    val NO_OF_TASKS = 3

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_health_eye)
        prgrssBar.scaleY = 4f

        txtViewTask1.setOnClickListener{
            val alert = AlertDialog.Builder(this)
            alert.setTitle(R.string.EYE_TASK_1_TITLE)
            alert.setMessage(R.string.EYE_TASK_1)

            alert.setPositiveButton("Ok",
                DialogInterface.OnClickListener { dialog, whichButton ->
                    //Your action here
                })

            alert.show()
        }

        txtViewTask2.setOnClickListener{
            val alert = AlertDialog.Builder(this)
            alert.setTitle(R.string.EYE_TASK_2_TITLE)
            alert.setMessage(R.string.EYE_TASK_2)

            alert.setPositiveButton("Ok",
                DialogInterface.OnClickListener { dialog, whichButton ->
                    //Your action here
                })

            alert.show()
        }

        txtViewTask3.setOnClickListener{
            val alert = AlertDialog.Builder(this)
            alert.setTitle(R.string.EYE_TASK_3_TITLE)
            alert.setMessage(R.string.EYE_TASK_3)

            alert.setPositiveButton("Ok",
                DialogInterface.OnClickListener { dialog, whichButton ->
                    //Your action here
                })

            alert.show()
        }

        chkBox1.setOnClickListener{
            val progress = calculateProgress()
            prgrssBar.progress = progress
            txtViewPrgrssHeading.text = "Completed " + (progress*NO_OF_TASKS)/99 + " of 3 tasks"
        }
        chkBox2.setOnClickListener{
            val progress = calculateProgress()
            prgrssBar.progress = progress
            txtViewPrgrssHeading.text = "Completed " + (progress*NO_OF_TASKS)/99 + " of 3 tasks"
        }
        chkBox3.setOnClickListener{
            val progress = calculateProgress()
            prgrssBar.progress = progress
            txtViewPrgrssHeading.text = "Completed " + (progress*NO_OF_TASKS)/99 + " of 3 tasks"
        }

    }

    private fun calculateProgress() : Int {
        var taskCounter = 0
        if(chkBox1.isChecked) taskCounter++
        if(chkBox2.isChecked) taskCounter++
        if(chkBox3.isChecked) taskCounter++

        return 100*taskCounter/NO_OF_TASKS
    }
}