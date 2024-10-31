package com.example.buddy

import android.os.Bundle
import androidx.activity.ComponentActivity
import android.animation.ObjectAnimator
import android.app.ActivityOptions
import android.app.AlertDialog
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.widget.Button
import android.widget.ProgressBar
import android.widget.Toast
import androidx.core.app.ActivityOptionsCompat
import com.google.firebase.auth.FirebaseAuth

class createacc_pageone : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_createacc_pageone)


        // Progress Bar
        val progressBar = findViewById<ProgressBar>(R.id.progressBar)
        progressBar.max = 12
        val current = 2
        ObjectAnimator.ofInt(progressBar, "progress", current)
            .setDuration(500)
            .start()

        val btn = findViewById<Button>(R.id.next)
        btn.setOnClickListener {
            val intent = Intent(this, createacc_pagetwo::class.java)
            val options = ActivityOptionsCompat.makeCustomAnimation(
                this@createacc_pageone,
                R.anim.dissolve_in,   // Animation for the new activity (slide up)
                R.anim.dissolve_out   // Animation for the current activity (slide down)
            )

            // Start the activity with the specified animation options
            startActivity(intent, options.toBundle())
            finish()
        }
    }

    @Suppress("DEPRECATION")
    override fun onBackPressed() {
        // Create a custom function to show a confirmation dialog for exiting the application
        showExitConfirmationDialog()
    }
    private fun showExitConfirmationDialog() {
        val alertDialogBuilder = AlertDialog.Builder(this)
        alertDialogBuilder.setTitle("Create Account")
        alertDialogBuilder.setMessage("You need to complete this section.")
        alertDialogBuilder.setPositiveButton("Okay") { dialog, _ ->
            dialog.dismiss()
        }

        // Create and show the dialog
        val exitConfirmationDialog = alertDialogBuilder.create()
        exitConfirmationDialog.show()
    }

}