package com.example.buddy

import android.animation.ObjectAnimator
import android.app.ActivityOptions
import android.app.AlertDialog
import android.os.Bundle
import androidx.activity.ComponentActivity
import android.content.Intent
import android.text.Editable
import android.text.TextWatcher
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import android.widget.ProgressBar
import androidx.core.app.ActivityOptionsCompat
import com.example.buddy.databinding.ActivityCreateaccPagefiveBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import java.util.regex.Pattern

class createacc_pagefive : ComponentActivity() {

    private lateinit var binding: ActivityCreateaccPagefiveBinding
    private lateinit var database: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCreateaccPagefiveBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Initialize Firebase database reference
        database = FirebaseDatabase.getInstance().getReference("Users")

        // Progress Bar
        val progressBar = findViewById<ProgressBar>(R.id.progressBar)
        progressBar.max = 12
        val current = 10
        ObjectAnimator.ofInt(progressBar, "progress", current)
            .setDuration(500)
            .start()


        val nextButton = findViewById<Button>(R.id.next)
        val dogname = findViewById<EditText>(R.id.dogname)

        // Initially disable the Next button and set its background to gray
        nextButton.isEnabled = false
        nextButton.setBackgroundResource(R.drawable.next_gray_btn)
        // Add a TextWatcher to monitor changes in the dog name EditText field
        dogname.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}

            override fun afterTextChanged(s: Editable?) {
                // Check if the dog name EditText is not empty
                val dogName = s?.toString()?.trim()
                if (dogName.isNullOrEmpty()) {
                    // If EditText is empty, disable the Next button and set its background to gray
                    nextButton.isEnabled = false
                    nextButton.setBackgroundResource(R.drawable.next_gray_btn)
                } else {
                    // If EditText has text, enable the Next button and set its background to the colored drawable
                    nextButton.isEnabled = true
                    nextButton.setBackgroundResource(R.drawable.next_button)
                }
            }
        })


        nextButton.setOnClickListener {
            val dogName = dogname.text.toString().trim()

            if (dogName.isNotEmpty()) {
                // Check if the dog's name contains any digits
                val containsDigits = containsDigits(dogName)

                if (containsDigits) {
                    // Show a toast message if the dog's name contains digits
                    Toast.makeText(this, "Dog's name should only consist of letters.", Toast.LENGTH_SHORT).show()
                } else {
                    // Proceed with updating user text if the dog's name is valid (no digits)
                    updateUserText(dogName)
                }
            } else {
                // Show a message if the EditText is empty
                Toast.makeText(this, "Please name your dog.", Toast.LENGTH_SHORT).show()
            }
        }
    }

    // Function to check if a string contains digits
    private fun containsDigits(input: String): Boolean {
        val pattern = Pattern.compile(".*\\d+.*")
        return pattern.matcher(input).matches()
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


    private fun updateUserText(dogName: String) {
        // Get the current user's ID from Firebase Authentication
        val currentUserID = FirebaseAuth.getInstance().currentUser?.uid
        if (currentUserID != null) {
            // Get a reference to the "Dogs" node directly under the "Users" node
            val userDogsRef = database.child(currentUserID).child("Dogs")
            // Generate a unique ID for the dog
            val dogID = userDogsRef.push().key
            if (dogID != null) {
                // Set the dog name directly under the generated dog ID
                userDogsRef.child(dogID).setValue(mapOf("dogName" to dogName))
                    .addOnSuccessListener {
                        Toast.makeText(this, "Dog Name has been added. Woof!", Toast.LENGTH_SHORT).show()
                        val intent = Intent(this, createacc_pagesix::class.java)
                        ToNextActivity(intent)
                        finish()
                    }
                    .addOnFailureListener { exception ->
                        Toast.makeText(this, "Failed to Add your Dog's Name: ${exception.message}", Toast.LENGTH_SHORT).show()
                    }
            } else {
                Toast.makeText(this, "Failed to generate Dog ID", Toast.LENGTH_SHORT).show()
            }
        } else {
            Toast.makeText(this, "User not authenticated", Toast.LENGTH_SHORT).show()
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

    private fun ToPrevious(intent: Intent) {
        val options = ActivityOptions.makeCustomAnimation(
            this,
            R.anim.slide_in_left, // Slide in from left
            R.anim.slide_out_right // Slide out to right
        )
        startActivity(intent, options.toBundle())
    }


}
