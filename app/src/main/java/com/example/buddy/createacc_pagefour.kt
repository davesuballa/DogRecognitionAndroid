package com.example.buddy

import android.animation.ObjectAnimator
import android.app.ActivityOptions
import android.app.AlertDialog
import android.os.Bundle
import androidx.activity.ComponentActivity
import android.content.Intent
import android.widget.Toast
import android.widget.Button
import android.widget.ProgressBar
import androidx.core.app.ActivityOptionsCompat
import androidx.core.content.ContextCompat
import com.example.buddy.databinding.ActivityCreateaccPagefourBinding
import com.example.buddy.databinding.ActivityCreateaccPagethreeBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class createacc_pagefour : ComponentActivity() {

    private lateinit var binding: ActivityCreateaccPagefourBinding
    private lateinit var database: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCreateaccPagefourBinding.inflate(layoutInflater)
        setContentView(binding.root)

        database = FirebaseDatabase.getInstance().getReference("Users")


        // Progress Bar
        val progressBar = findViewById<ProgressBar>(R.id.progressBar)
        progressBar.max = 12
        val current = 8
        ObjectAnimator.ofInt(progressBar, "progress", current)
            .setDuration(500)
            .start()

        // Parent corresponding ID to call
        val dadBtn = findViewById<Button>(R.id.dad)
        val momBtn = findViewById<Button>(R.id.mom)
        val nextButton = findViewById<Button>(R.id.next)

        // Initially enable the next button
        updateNextButtonState(false)

        dadBtn.setOnClickListener {
            if (!dadBtn.isSelected) {
                dadBtn.isSelected = true
                dadBtn.setBackgroundResource(R.drawable.dad_dark)
                momBtn.isSelected = false
                momBtn.setBackgroundResource(R.drawable.mom)
                updateNextButtonState(true)
            }
        }

        momBtn.setOnClickListener {
            if (!momBtn.isSelected) {
                momBtn.isSelected = true
                momBtn.setBackgroundResource(R.drawable.mom_dark)
                dadBtn.isSelected = false
                dadBtn.setBackgroundResource(R.drawable.dad)
                updateNextButtonState(true)
            }
        }



        // Next Button Condition
        nextButton.setOnClickListener {
            // Check if either "Mom" or "Dad" button is selected
            if (dadBtn.isSelected || momBtn.isSelected) {
                // Get the parent type
                val parentType = if (dadBtn.isSelected) "Dad" else "Mom"
                // Update the user's parent type in the database
                updateUserParentType(parentType)
            }
        }


    }

    // Function to update the state of the Next button
    private fun updateNextButtonState(isEnabled: Boolean) {
        val nextButton = findViewById<Button>(R.id.next)
        val nextDisabledDrawable = ContextCompat.getDrawable(this, R.drawable.next_gray_btn)
        nextButton.isEnabled = isEnabled
        nextButton.background = if (isEnabled) {
            ContextCompat.getDrawable(this, R.drawable.next_button)
        } else {
            nextDisabledDrawable
        }
    }


    private fun updateUserParentType(parentType: String) {
        // Get the current user's ID from Firebase Authentication
        val currentUserID = FirebaseAuth.getInstance().currentUser?.uid
        if (currentUserID != null) {
            // Get a reference to the "profile" node for the current user
            val userRef = database.child(currentUserID).child("profile")

            // Update the user's profile with the parentType
            userRef.child("parentType").setValue(parentType)
                .addOnSuccessListener {
                    // Proceed to next activity
                    Toast.makeText(this, "Well done! $parentType. Woof!", Toast.LENGTH_SHORT).show()
                    val intent = Intent(this, createacc_pagefive::class.java)
                    ToNextActivity(intent)
                    finish()
                }
                .addOnFailureListener {
                    Toast.makeText(this, "Failed to update parent type", Toast.LENGTH_SHORT).show()
                }
        } else {
            Toast.makeText(this, "User not authenticated", Toast.LENGTH_SHORT).show()
        }
    }


    // Animation to slide the whole activity
    private fun ToNextActivity(intent: Intent) {
        val options = ActivityOptions.makeCustomAnimation(
            this,
            R.anim.slide_in_right, // Slide in from right
            R.anim.slide_out_left // Slide out to left
        )
        startActivity(intent, options.toBundle())
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
