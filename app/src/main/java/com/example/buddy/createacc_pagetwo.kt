package com.example.buddy

import android.os.Bundle
import androidx.activity.ComponentActivity
import android.animation.ObjectAnimator
import android.app.ActivityOptions
import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.view.animation.Animation
import android.view.animation.TranslateAnimation
import android.widget.Button
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import com.example.buddy.databinding.ActivityCreateaccPagetwoBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class createacc_pagetwo : ComponentActivity() {

    private lateinit var binding: ActivityCreateaccPagetwoBinding
    private lateinit var database: DatabaseReference
    private lateinit var auth: FirebaseAuth


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCreateaccPagetwoBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val puppy = findViewById<TextView>(R.id.puppy)

        // Convert dp to pixels
        val density = resources.displayMetrics.density
        val originalMarginStartInDp = 150
        val originalMarginTopInDp = 400
        val finalMarginStartInDp = 50
        val finalMarginTopInDp = 150

        val originalMarginStartInPixels = (originalMarginStartInDp * density).toInt()
        val originalMarginTopInPixels = (originalMarginTopInDp * density).toInt()
        val finalMarginStartInPixels = (finalMarginStartInDp * density).toInt()
        val finalMarginTopInPixels = (finalMarginTopInDp * density).toInt()

        // Calculate the difference in margin start and margin top
        val diffX = finalMarginStartInPixels - originalMarginStartInPixels
        val diffY = finalMarginTopInPixels - originalMarginTopInPixels

        val animation = TranslateAnimation(
            Animation.RELATIVE_TO_SELF, 0.0f,
            Animation.ABSOLUTE, diffX.toFloat(),
            Animation.RELATIVE_TO_SELF, 0.0f,
            Animation.ABSOLUTE, diffY.toFloat()
        )

        animation.duration = 750 // in milliseconds
        animation.fillAfter = true // to keep the final position after animation ends

        puppy.startAnimation(animation)


        // Initialize Firebase database reference
        database = FirebaseDatabase.getInstance().getReference("Survey")
        auth  = FirebaseAuth.getInstance()

        // Progress Bar
        val progressBar = findViewById<ProgressBar>(R.id.progressBar)
        progressBar.max = 12
        val current = 4
        ObjectAnimator.ofInt(progressBar, "progress", current)
            .setDuration(500)
            .start()

        // Set up survey buttons
        findViewById<Button>(R.id.s1).setOnClickListener {
            toggleSelection(1)
            updateNextButtonState()
            it.setBackgroundResource(if (it.isSelected) R.drawable.articledark else R.drawable.article)
        }


        findViewById<Button>(R.id.s2).setOnClickListener {
            toggleSelection(2)
            it.setBackgroundResource(if (it.isSelected) R.drawable.friendsdark else R.drawable.friends)
            updateNextButtonState()
        }

        findViewById<Button>(R.id.s3).setOnClickListener {
            toggleSelection(3)
            it.setBackgroundResource(if (it.isSelected) R.drawable.socialdark else R.drawable.social)
            updateNextButtonState()
        }


        findViewById<Button>(R.id.s5).setOnClickListener {
            toggleSelection(4)
            it.setBackgroundResource(if (it.isSelected) R.drawable.othersdark else R.drawable.others)
            updateNextButtonState()
        }

        // Initialize the next button to proceed
        findViewById<Button>(R.id.next).setOnClickListener {
            submitSelection()
            val intent = Intent(this, createacc_pagethree::class.java)
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
        val surveyButtonIds = listOf(R.id.s1, R.id.s2, R.id.s3, R.id.s5)
        // Collect selected survey items
        surveyButtonIds.forEachIndexed { index, buttonId ->
            val button = findViewById<Button>(buttonId)
            if (button.isSelected) {
                val item = when (index) {
                    0 -> "Article/News"
                    1 -> "Friends/Family"
                    2 -> "Social"
                    3 -> "Others"
                    else -> ""
                }
                selectedItems.add(item)
            }
        }
        // Store selected items in Firebase
        val userId = auth.currentUser?.uid
        userId?.let { uid ->
            val surveyRef = database.child(uid).child("Survey")
            selectedItems.forEach { item ->
                val key = generateUniqueKey()
                surveyRef.child(key).setValue(item)
                    .addOnSuccessListener {
                        Toast.makeText(this, "Survey Added", Toast.LENGTH_SHORT).show()
                    }
                    .addOnFailureListener {
                        Toast.makeText(this, "Survey failed to add", Toast.LENGTH_SHORT).show()
                    }
            }
        }
    }


    private fun generateUniqueKey(): String {
        return database.push().key ?: ""
    }


    private fun getResourceIdForOption(option: Int): Int {
        return when (option) {
            1 -> R.id.s1
            2 -> R.id.s2
            3 -> R.id.s3
            4 -> R.id.s5
            else -> throw IllegalArgumentException("Invalid option: $option")
        }
    }

    private fun ToNextActivity(intent: Intent) {
        val options = ActivityOptions.makeCustomAnimation(
            this,
            R.anim.slide_in_right, // Slide in from left
            R.anim.slide_out_left // Slide out to right
        )
        startActivity(intent, options.toBundle())
    }

    private fun ToPrevious(intent: Intent) {
        val options = ActivityOptions.makeCustomAnimation(
            this,
            R.anim.slide_in_left, // Slide in from left
            R.anim.slide_out_right // Slide out to right
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