package com.example.buddy

import android.animation.ObjectAnimator
import android.app.ActivityOptions
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.ProgressBar
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.core.app.ActivityOptionsCompat
import androidx.core.content.ContextCompat
import com.example.buddy.databinding.ChangereferenceBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class ChangeGender : ComponentActivity(){
    private lateinit var binding: ChangereferenceBinding
    private lateinit var database: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ChangereferenceBinding.inflate(layoutInflater)
        setContentView(binding.root)

        database = FirebaseDatabase.getInstance().getReference("Users")


        // Parent corresponding ID to call
        val dadBtn = findViewById<Button>(R.id.dad)
        val momBtn = findViewById<Button>(R.id.mom)
        val nextButton = findViewById<Button>(R.id.btn_save)


        val backbtn = findViewById<Button>(R.id.backbtn)
        backbtn.setOnClickListener {
            val intent = Intent(this, profile_settings::class.java)
            val options = ActivityOptionsCompat.makeCustomAnimation(
                this,
                R.anim.slide_in_left, // Animation for the new activity
                R.anim.slide_out_right // No animation for the current activity
            )

            // Start the activity with the specified animation options
            startActivity(intent, options.toBundle())
            finish()
        }

        dadBtn.setOnClickListener {
            if (!dadBtn.isSelected) {
                dadBtn.isSelected = true
                dadBtn.setBackgroundResource(R.drawable.dad_dark)
                momBtn.isSelected = false
                momBtn.setBackgroundResource(R.drawable.mom)
            }
        }

        momBtn.setOnClickListener {
            if (!momBtn.isSelected) {
                momBtn.isSelected = true
                momBtn.setBackgroundResource(R.drawable.mom_dark)
                dadBtn.isSelected = false
                dadBtn.setBackgroundResource(R.drawable.dad)
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
                    Toast.makeText(this, "Hi $parentType. Woof!", Toast.LENGTH_SHORT).show()
                    val intent = Intent(this, profile_settings::class.java)
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
            R.anim.slide_in_left, // Slide in from right
            R.anim.slide_out_right // Slide out to left
        )
        startActivity(intent, options.toBundle())
    }


    @Suppress("DEPRECATION")
    override fun onBackPressed() {
        // Start a new activity or finish the current activity
        val intent = Intent(this, profile_settings::class.java)
        startActivity(intent)

        // Finish the current activity to trigger the animation
        finish()

        // Define the animation to use after calling finish()
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right)
    }



}