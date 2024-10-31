package com.example.buddy

import android.app.ActivityOptions
import android.app.AlertDialog
import android.os.Bundle
import androidx.activity.ComponentActivity
import android.content.Intent
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val myButton = findViewById<Button>(R.id.already_have_an_account_button)
        myButton.setOnClickListener {
            val intent = Intent(this, LoginaccActivity::class.java)
            startActivityWithSlideAnimation(intent)
            finish()
        }

        val getStartedBtn = findViewById<Button>(R.id.get_started_button)
        getStartedBtn.setOnClickListener {
            val intent = Intent(this, CreateaccActivity::class.java)
            startActivityWithSlideAnimation(intent)
            finish()
        }
    }

    private fun startActivityWithSlideAnimation(intent: Intent) {
        val options = ActivityOptions.makeCustomAnimation(
            this,
            R.anim.slide_in_right,
            R.anim.slide_out_left
        )
        startActivity(intent, options.toBundle())
    }

    @Suppress("DEPRECATION")
    override fun onBackPressed() {
        // Create an alert dialog to confirm exiting the application
        AlertDialog.Builder(this)
            .setMessage("Are you sure you want to exit the application?")
            .setPositiveButton("Confirm") { dialog, _ ->
                // If user confirms, exit the app
                finish()
            }
            .setNegativeButton("Cancel") { dialog, _ ->
                // If user cancels, dismiss the dialog (do nothing)
                dialog.dismiss()
            }
            .show()
    }
}