package com.example.buddy

import android.app.ActivityOptions
import android.app.AlertDialog
import android.content.ContentValues.TAG
import android.os.Bundle
import androidx.activity.ComponentActivity
import android.content.Intent
import android.util.Log
import android.widget.Button
import android.widget.Switch
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityOptionsCompat
import androidx.core.content.ContextCompat
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class profile_overview : AppCompatActivity() {

    private lateinit var firebaseAuth: FirebaseAuth
    private lateinit var database: DatabaseReference
    private lateinit var profile : TextView


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile_overview)

        firebaseAuth = FirebaseAuth.getInstance()
        database = FirebaseDatabase.getInstance().reference
        profile = findViewById(R.id.profileicon2)


        //Profile Settings to edit the user's profile
        val profile_settings_btn: Button = findViewById(R.id.profile_settings_button)
        profile_settings_btn.setOnClickListener {
            val intent = Intent(this, profile_settings::class.java)
            ToNext(intent)
            finish()
        }


        //Camera Button for camera page
        val camera_btn: Button = findViewById(R.id.camera)
        camera_btn.setOnClickListener {
            val intent = Intent(this, camera::class.java)
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

        //Home Button for Home Page
        val home_btn: Button = findViewById(R.id.homepage)
        home_btn.setOnClickListener {
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


        //health tools button for health tools page
        val healthtools_btn : Button = findViewById(R.id.healthtools)
        healthtools_btn.setOnClickListener {
            val intent = Intent(this, healthtoolspage::class.java)
            val options = ActivityOptionsCompat.makeCustomAnimation(
                this,
                R.anim.dissolve_in, // Animation for the new activity
                R.anim.dissolve_out // No animation for the current activity
            )

            // Start the activity with the specified animation options
            startActivity(intent, options.toBundle())
            finish()
        }



        fetchAndDisplayUserInfo()

        val defaultNickname = "Nickname"
        val defaultPhoneNumber = "+63"
        val nicknameTextView: TextView = findViewById(R.id.nckname)
        val phoneNumberTextView: TextView = findViewById(R.id.nmbr)
        if (nicknameTextView.text.isNullOrBlank()) {
            Log.d(TAG, "Nickname is empty")
            nicknameTextView.text = defaultNickname
        }
        if (phoneNumberTextView.text.isNullOrBlank()) {
            Log.d(TAG, "Phone number is empty")
            phoneNumberTextView.text = defaultPhoneNumber
        }

    }


    private fun ToNext(intent: Intent) {
        val options = ActivityOptions.makeCustomAnimation(
            this,
            R.anim.slide_in_right, // Slide in from left
            R.anim.slide_out_left // Slide out to right
        )
        startActivity(intent, options.toBundle())
    }

    private fun fetchAndDisplayUserInfo() {
        val currentUser = firebaseAuth.currentUser
        currentUser?.let { user ->
            val userId = user.uid
            val userRef = database.child("Users").child(userId).child("profile")

            userRef.addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    val parent = snapshot.child("parentType").getValue(String::class.java)
                    val username = snapshot.child("name").getValue(String::class.java)
                    val email = snapshot.child("email").getValue(String::class.java)
                    val nickname = snapshot.child("nickname").getValue(String::class.java)
                    val phonenumber = snapshot.child("contact_number").getValue(String::class.java)
                    val shortUserId = userId.take(12)
                    findViewById<TextView>(R.id.uid).text = "UID: $shortUserId"
                    findViewById<TextView>(R.id.usrnm).text = "Username: $username"
                    findViewById<TextView>(R.id.eml).text = "$email"


                    val nicknameTextView: TextView = findViewById(R.id.nckname)
                    val phoneNumberTextView: TextView = findViewById(R.id.nmbr)


                    if (nickname.isNullOrBlank()) {
                        nicknameTextView.text = "Nickname"
                    } else {
                        nicknameTextView.text = nickname
                    }

                    if (phonenumber.isNullOrBlank()) {
                        phoneNumberTextView.text = "+63"
                        phoneNumberTextView.setTextColor(ContextCompat.getColor(this@profile_overview, R.color.pinkspinner))
                    } else {
                        phoneNumberTextView.text = phonenumber
                        phoneNumberTextView.setTextColor(ContextCompat.getColor(this@profile_overview, R.color.darkbrown))
                    }


                    if (parent != null) {
                        if (parent.equals("Dad", ignoreCase = true)) {
                            profile.setBackgroundResource(R.drawable.male_profile)
                        } else if (parent.equals("Mom", ignoreCase = true)) {
                            profile.setBackgroundResource(R.drawable.female_profile)
                        }
                    }
                }
                override fun onCancelled(error: DatabaseError) {
                    // Handle error
                    // For simplicity, just printing the error message
                    println("Failed to read value: ${error.message}")
                }
            })
        }
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
        // Start a new activity or finish the current activity
        val intent = Intent(this, homepageold::class.java)
        startActivity(intent)

        // Finish the current activity to trigger the animation
        finish()

        // Define the animation to use after calling finish()
        overridePendingTransition(R.anim.dissolve_in, R.anim.dissolve_out)
    }


}