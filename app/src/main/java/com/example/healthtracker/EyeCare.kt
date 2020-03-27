package com.example.healthtracker

import android.content.DialogInterface
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_health_eye.*


class EyeCare: AppCompatActivity() {
    val NO_OF_TASKS = 3
    val MAX_PROGRESS = 100
    val PRORESS_BAR_SCALING = 4f

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_health_eye)
        prgrssBar.scaleY = PRORESS_BAR_SCALING
        txtViewPrgrssHeading.text = "Completed 0 of " + NO_OF_TASKS + " tasks"

        txtViewTask1.setOnClickListener{
            generateAlertDialog(getString(R.string.EYE_TASK_1_TITLE), getString(R.string.EYE_TASK_1))
        }

        txtViewTask2.setOnClickListener{
            generateAlertDialog(getString(R.string.EYE_TASK_2_TITLE), getString(R.string.EYE_TASK_2))
        }

        txtViewTask3.setOnClickListener{
            generateAlertDialog(getString(R.string.EYE_TASK_3_TITLE), getString(R.string.EYE_TASK_3))
        }

        chkBox1.setOnClickListener{
            updateProgressbar()
        }

        chkBox2.setOnClickListener{
            updateProgressbar()
        }

        chkBox3.setOnClickListener{
            updateProgressbar()
        }

    }

    private fun updateProgressbar() {
        val progress = calculateProgress()
        prgrssBar.progress = progress
        txtViewPrgrssHeading.text = "Completed " + (progress*NO_OF_TASKS)/99 + " of " + NO_OF_TASKS + " tasks"
        if(progress == MAX_PROGRESS) {
            generateAlertDialog(getString(R.string.CONGRATS_MSG), "")
        }
    }

    private fun calculateProgress() : Int {
        var taskCounter = 0
        if(chkBox1.isChecked) taskCounter++
        if(chkBox2.isChecked) taskCounter++
        if(chkBox3.isChecked) taskCounter++
        return 100*taskCounter/NO_OF_TASKS
    }

    private fun generateAlertDialog(title : String, description : String) {
        val alert = AlertDialog.Builder(this)
        alert.setTitle(title)
        if(description.isNotEmpty()) alert.setMessage(description)

        alert.setPositiveButton("Ok",
            DialogInterface.OnClickListener { dialog, whichButton ->
                //Your action here
            })

        alert.show()
    }
}