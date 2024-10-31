package com.example.buddy

import android.app.ActivityOptions
import android.app.AlertDialog
import android.os.Bundle
import androidx.activity.ComponentActivity
import android.content.Intent
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityOptionsCompat
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class healthtoolspage : AppCompatActivity() {

    private lateinit var userDatabase: DatabaseReference
    private lateinit var auth: FirebaseAuth
    private lateinit var greetingTextView: TextView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_healthtoolspage)

        userDatabase = FirebaseDatabase.getInstance().getReference("Users")
        auth = FirebaseAuth.getInstance()

        greetingTextView = findViewById(R.id.text1)

        fetchParentTypeAndUpdateGreeting()
        //Symptoms Checker Button for symptoms chat page
        val symptom_checker_btn: Button = findViewById(R.id.symptom_checker)
        symptom_checker_btn.setOnClickListener {
            startNextActivityWithFadeIn()

        }

        //Weight checker Button for weight checker chat page
        val weight_checker_btn : Button = findViewById(R.id.weight_checker)
        weight_checker_btn.setOnClickListener {
            val intent = Intent(this, weight_checker_chat::class.java)

            // Create options for the animation
            val options = ActivityOptionsCompat.makeCustomAnimation(
                this,
                R.anim.dissolve_in, // Animation for the new activity
                R.anim.dissolve_out // No animation for the current activity
            )

            // Start the activity with the specified animation options
            startActivity(intent, options.toBundle())
        }

//Camera Button for camera page
        val camera_btn : Button = findViewById(R.id.camera)
        camera_btn.setOnClickListener {
            val intent = Intent(this, camera::class.java)

            // Create options for the animation
            val options = ActivityOptionsCompat.makeCustomAnimation(
                this,
                R.anim.dissolve_in, // Animation for the new activity
                R.anim.dissolve_out // No animation for the current activity
            )

            // Start the activity with the specified animation options
            startActivity(intent, options.toBundle())
            finish()
        }

        val clndar : Button = findViewById(R.id.calendar)
        clndar.setOnClickListener {
            val intent = Intent(this, calendar::class.java)
            val options = ActivityOptionsCompat.makeCustomAnimation(
                this,
                R.anim.dissolve_in, // Animation for the new activity
                R.anim.dissolve_out // No animation for the current activity
            )

            // Start the activity with the specified animation options
            startActivity(intent, options.toBundle())
            finish()
        }


        //Profile button to view the user's profile
        val user_profile_btn : Button = findViewById(R.id.profile)
        user_profile_btn.setOnClickListener {
            val intent = Intent(this, profile_overview::class.java)
            val options = ActivityOptionsCompat.makeCustomAnimation(
                this,
                R.anim.dissolve_in, // Animation for the new activity
                R.anim.dissolve_out // No animation for the current activity
            )

            // Start the activity with the specified animation options
            startActivity(intent, options.toBundle())
            finish()
        }

        val home : Button = findViewById(R.id.homepage)
        home.setOnClickListener {
            val intent = Intent(this, homepageold::class.java)
            val options = ActivityOptionsCompat.makeCustomAnimation(
                this,
                R.anim.dissolve_in, // Animation for the new activity
                R.anim.dissolve_out // No animation for the current activity
            )

            // Start the activity with the specified animation options
            startActivity(intent, options.toBundle())
            finish()
        }

    }

    private fun fetchParentTypeAndUpdateGreeting() {
        val userCurrentID = auth.currentUser?.uid
        if (userCurrentID != null) {
            val userRef = userDatabase.child(userCurrentID).child("profile")
            userRef.addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    val parentType = snapshot.child("parentType").getValue(String::class.java)
                    updateGreetingText(parentType)
                }

                override fun onCancelled(error: DatabaseError) {
                    // Handle error
                }
            })
        }
    }
    private fun updateGreetingText(parentType: String?) {
        val defaultGreeting = "Good day, Parent!"
        val customizedGreeting = when (parentType) {
            "Dad" -> "Hi Daddy, \nI can help you learn \nmore about dog's health"
            "Mom" -> "Hi Mommy, \nI can help you learn \nmore about dog's health"
            else -> defaultGreeting
        }
        greetingTextView.text = customizedGreeting
    }
    private fun startNextActivityWithFadeIn() {
        val intent = Intent(this, symptom_checker_chat::class.java)

        // Create options for the animation
        val options = ActivityOptionsCompat.makeCustomAnimation(
            this,
            R.anim.dissolve_in, // Animation for the new activity
            R.anim.dissolve_out // No animation for the current activity
        )

        // Start the activity with the specified animation options
        startActivity(intent, options.toBundle())
    }

    @Suppress("DEPRECATION")
    override fun onBackPressed() {
        // Start a new activity or finish the current activity
        val intent = Intent(this, homepageold::class.java)
        startActivity(intent)

        // Finish the current activity to trigger the animation
        finish()

        // Define the animation to use after calling finish()
        overridePendingTransition(R.anim.dissolve_in, R.anim.dissolve_out)
    }
}