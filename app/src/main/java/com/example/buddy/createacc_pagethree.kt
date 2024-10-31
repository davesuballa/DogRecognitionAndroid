package com.example.buddy

import android.animation.ObjectAnimator
import android.app.ActivityOptions
import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.ProgressBar
import android.widget.Toast
import androidx.activity.ComponentActivity
import com.example.buddy.databinding.ActivityCreateaccPagethreeBinding
import com.example.buddy.databinding.ActivityCreateaccPagetwoBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class createacc_pagethree : ComponentActivity() {

    private lateinit var binding: ActivityCreateaccPagethreeBinding
    private lateinit var database: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCreateaccPagethreeBinding.inflate(layoutInflater)
        setContentView(binding.root)


        // Initialize Firebase database reference
        database = FirebaseDatabase.getInstance().getReference("Survey")

        // Progress Bar
        val progressBar = findViewById<ProgressBar>(R.id.progressBar)
        progressBar.max = 12
        val current = 6
        ObjectAnimator.ofInt(progressBar, "progress", current)
            .setDuration(500)
            .start()


        findViewById<Button>(R.id.s6).setOnClickListener {
            toggleSelection(1)
            it.setBackgroundResource(if (it.isSelected) R.drawable.managedogsdark else R.drawable.managedogs)
            updateNextButtonState()
        }


        findViewById<Button>(R.id.s7).setOnClickListener {
            toggleSelection(2)
            it.setBackgroundResource(if (it.isSelected) R.drawable.doghealthremindersdark else R.drawable.dhm)
            updateNextButtonState()
        }

        findViewById<Button>(R.id.s8).setOnClickListener {
            toggleSelection(3)
            it.setBackgroundResource(if (it.isSelected) R.drawable.cwdd else R.drawable.cwd)
            updateNextButtonState()
        }

        findViewById<Button>(R.id.s9).setOnClickListener {
            toggleSelection(4)
            it.setBackgroundResource(if(it.isSelected) R.drawable.othersdark else R.drawable.others)
            updateNextButtonState()
        }

        // Initialize the next button to proceed
        findViewById<Button>(R.id.next).setOnClickListener {
            submitSelection()
            val intent = Intent(this, createacc_pagefour::class.java)
            ToNextActivity(intent)
            finish()
        }

        // Initially disable next button
        updateNextButtonState()
    }

    private fun updateNextButtonState() {
        val nextButton = findViewById<Button>(R.id.next)
        // Check if any button in the survey is selected
        val isAnyButtonSelected = (1..4).any { findViewById<Button>(getResourceIdForOption(it)).isSelected }
        nextButton.isEnabled = isAnyButtonSelected

        // Condition for the disable next button
        if (nextButton.isEnabled) {
            nextButton.setBackgroundResource(R.drawable.next_button) // When any of the survey button is clicked
        } else {
            nextButton.setBackgroundResource(R.drawable.next_gray_btn) // Default button color
        }
    }

    // Toggle the selection state of the button
    private fun toggleSelection(option: Int) {
        val button = findViewById<Button>(getResourceIdForOption(option))
        button.isSelected = !button.isSelected
    }

    private fun submitSelection() {
        val selectedItems = mutableListOf<String>()

        // First set of survey buttons (s6, s7, s8, s9)
        val firstSetButtonIds = listOf(R.id.s6, R.id.s7, R.id.s8, R.id.s9)
        firstSetButtonIds.forEachIndexed { index, buttonId ->
            val button = findViewById<Button>(buttonId)
            if (button.isSelected) {
                val item = when (index) {
                    0 -> "To manage my dogs"
                    1 -> "To remind me"
                    2 -> "Connect with dogs"
                    3 -> "Others"
                    else -> ""
                }
                selectedItems.add(item)
            }
        }
        // Store selected items in Firebase under user's profile
        val userId = FirebaseAuth.getInstance().currentUser?.uid
        userId?.let { uid ->
            val surveyRef = database.child(uid).child("Survey")
            selectedItems.forEach { item ->
                val key = generateUniqueKey()
                surveyRef.child(key).setValue(item)
                    .addOnSuccessListener {
                        Toast.makeText(this, "Survey Added: $item", Toast.LENGTH_SHORT).show()
                    }
                    .addOnFailureListener {
                        Toast.makeText(this, "Failed to add survey: $item", Toast.LENGTH_SHORT).show()
                    }
            }
        }
    }

    private fun generateUniqueKey(): String {
        // Generate unique key based on current timestamp to duplicate the survey selection
        return System.currentTimeMillis().toString()
    }

    private fun getResourceIdForOption(option: Int): Int {
        return when (option) {
            1 -> R.id.s6
            2 -> R.id.s7
            3 -> R.id.s8
            4 -> R.id.s9
            else -> throw IllegalArgumentException("Invalid option: $option")
        }
    }
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
        alertDialogBuilder.setPositiveButton("Confirm") { dialog, _ ->
            dialog.dismiss()
        }
        alertDialogBuilder.setNegativeButton("Cancel") { dialog, _ ->
            // If user cancels, dismiss the dialog (do nothing)
            dialog.dismiss()
        }

        // Create and show the dialog
        val exitConfirmationDialog = alertDialogBuilder.create()
        exitConfirmationDialog.show()
    }


}