package com.example.buddy

import android.animation.ObjectAnimator
import android.app.ActivityOptions
import android.app.AlertDialog
import android.os.Bundle
import androidx.activity.ComponentActivity
import android.content.Intent
import android.view.View
import android.widget.AdapterView
import android.widget.Button
import android.widget.ProgressBar
import android.widget.Spinner
import android.widget.Toast
import androidx.core.app.ActivityOptionsCompat
import com.example.buddy.databinding.ActivityCreateaccPagesixBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class createacc_pagesix : ComponentActivity() {

    private lateinit var database: DatabaseReference
    private lateinit var binding: ActivityCreateaccPagesixBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCreateaccPagesixBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Initialize Firebase database reference
        database = FirebaseDatabase.getInstance().getReference("Users")

        // Progress Bar
        val progressBar = findViewById<ProgressBar>(R.id.progressBar)
        progressBar.max = 12
        val current = 12
        ObjectAnimator.ofInt(progressBar, "progress", current)
            .setDuration(500)
            .start()

        val spinner: Spinner = findViewById(R.id.breed_spinner)
        val defaultText = "Select Breed"
        val items = listOf(defaultText, "Golden Retriever", "Shi Tzu", "Chihuahua", "Poodle", "Pomeranian")
        val adapter = CustomSpinnerAdapter(this, R.layout.spinner_item, items)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinner.adapter = adapter

        // Next button
        val nextButton: Button = findViewById(R.id.next)
        nextButton.isEnabled = false // Disable initially

        spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                // Check if the selected item is not the default text
                val selectedBreed = parent?.getItemAtPosition(position).toString()
                if (selectedBreed != defaultText) {
                    nextButton.isEnabled = true
                    nextButton.setBackgroundResource(R.drawable.next_button)
                } else {
                    nextButton.isEnabled = false
                    nextButton.setBackgroundResource(R.drawable.next_gray_btn)
                }
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                // Handle nothing selected, if needed
            }
        }

        // Handle next button click
        nextButton.setOnClickListener {
            // Get the selected breed from the spinner
            val selectedBreed = spinner.selectedItem.toString()
            // Assuming you have the currentDogID variable available, pass it to the storeSelectedBreed function
            // Replace "currentDogID" with the actual variable that holds the current dog's ID
            storeSelectedBreed(selectedBreed)
            val intent = Intent(this, dog_avatar_creation::class.java)
            ToNextActivity(intent)
            finish()
        }

    }

    private fun storeSelectedBreed(dogBreed: String) {
        // Get the current user's ID from Firebase Authentication
        val currentUserID = FirebaseAuth.getInstance().currentUser?.uid
        if (currentUserID != null) {
            // Get a reference to the user's "Dogs" node in the database
            val userDogsRef = database.child(currentUserID).child("Dogs")

            // Retrieve the current dog ID from the database
            userDogsRef.addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    for (dogSnapshot in snapshot.children) {
                        val currentDogID = dogSnapshot.key // This will give you the ID of each dog
                        // Now you can use the currentDogID to store the selected breed
                        currentDogID?.let { storeSelectedBreed(it, dogBreed) }
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                    // Handle database error
                    Toast.makeText(this@createacc_pagesix, "Database error occurred", Toast.LENGTH_SHORT).show()
                }
            })
        } else {
            // User not authenticated
            Toast.makeText(this, "User not authenticated", Toast.LENGTH_SHORT).show()
        }
    }

    private fun storeSelectedBreed(currentDogID: String, dogBreed: String) {
        // Get the current user's ID from Firebase Authentication
        val currentUserID = FirebaseAuth.getInstance().currentUser?.uid
        if (currentUserID != null) {
            // Get a reference to the user's "Dogs" node in the database
            val userDogsRef = database.child(currentUserID).child("Dogs")
            // Set the breed name under the specified dog ID
            userDogsRef.child(currentDogID).child("breedName").setValue(dogBreed)
                .addOnSuccessListener {
                    // Breed successfully stored in the database
                    Toast.makeText(this, "Selected breed stored: $dogBreed", Toast.LENGTH_SHORT).show()
                }
                .addOnFailureListener {
                    // Failed to store breed in the database
                    Toast.makeText(this, "Failed to store selected breed", Toast.LENGTH_SHORT).show()
                }
        } else {
            // User not authenticated
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