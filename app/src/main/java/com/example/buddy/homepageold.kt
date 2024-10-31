package com.example.buddy

import android.app.ActivityOptions
import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.activity.ComponentActivity
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityOptionsCompat
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener


class homepageold : AppCompatActivity() {

    private lateinit var userDatabase: DatabaseReference
    private lateinit var auth: FirebaseAuth
    private lateinit var greetingTextView: TextView
    private lateinit var profileRecyclerView: RecyclerView
    private lateinit var profileAdapter: ProfileAdapter
    private lateinit var dogbreed: TextView
    private lateinit var dogage: TextView
    private lateinit var dogname: TextView
    private lateinit var dogweight: TextView
    private lateinit var dogbday: TextView
    private lateinit var doggender: TextView
    private var lastDisplayedDogId: String? = null
    private lateinit var selectedDogViewModel: SelectedDogViewModel


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_homepage)

        selectedDogViewModel = ViewModelProvider(this).get(SelectedDogViewModel::class.java)

        userDatabase = FirebaseDatabase.getInstance().getReference("Users")
        auth = FirebaseAuth.getInstance()

        dogbreed = findViewById(R.id.dog_breedname)
        dogage = findViewById(R.id.dog_agename)
        dogname = findViewById(R.id.nickname)
        dogweight = findViewById(R.id.dog_weightname)
        dogbday = findViewById(R.id.dog_bdayname)
        doggender = findViewById(R.id.dog_sexname)

        profileRecyclerView = findViewById(R.id.profileRecyclerView)
        profileAdapter = ProfileAdapter(userDatabase, auth)
        profileRecyclerView.adapter = profileAdapter
        profileRecyclerView.layoutManager =
            LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)


        greetingTextView = findViewById(R.id.text1)

        fetchParentTypeAndUpdateGreeting()

        val dogProfileButton: TextView = findViewById(R.id.dog_profile_arrow)
        dogProfileButton.setOnClickListener {
            // Retrieve the selected dog's ID from the ViewModel
            val selectedDogId = selectedDogViewModel.selectedDogId
            selectedDogId?.let { dogId ->
                val intent = Intent(this, dog_profilepageold::class.java)
                // Pass the selected dog's ID to the dog_profilepage activity
                intent.putExtra("dogId", dogId)
                ToNextActivity(intent)
                finish()
            }
        }


        //Camera Button for camera page
        val camera_btn : Button = findViewById(R.id.camera)
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



        //health tools button for health tools page
        val healthtools_btn : Button = findViewById(R.id.healthtools)
        healthtools_btn.setOnClickListener {
            val intent = Intent(this, healthtoolspage::class.java)
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

        fetchDogIDsAndUpdateAdapter()

        profileAdapter.setOnItemClickListener { dogId ->
            fetchDogInformationAndUpdateUI(dogId)
            // Store the selected dog's ID in the ViewModel
            selectedDogViewModel.selectedDogId = dogId
        }

        selectedDogViewModel.selectedDogId?.let { dogId ->
            fetchDogInformationAndUpdateUI(dogId)
        }


    }


    private fun fetchDogInformationAndUpdateUI(dogId: String) {
        lastDisplayedDogId = dogId
        val userCurrentID = auth.currentUser?.uid
        if (userCurrentID != null) {
            val userDogsRef = userDatabase.child(userCurrentID).child("Dogs").child(dogId)
            userDogsRef.addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    if (snapshot.exists()) {
                        val breed = snapshot.child("breedName").getValue(String::class.java) ?: "N/A"
                        val age = snapshot.child("age").getValue(String::class.java) ?: "N/A"
                        val name = snapshot.child("dogName").getValue(String::class.java) ?: "N/A"
                        val weight = snapshot.child("weight").getValue(String::class.java) ?: "N/A"
                        val bday = snapshot.child("birthday").getValue(String::class.java) ?: "N/A"
                        val gender = snapshot.child("gender").getValue(String::class.java) ?: "N/A"

                        // Update TextViews with dog's information
                        dogbreed.text = breed
                        dogage.text = age
                        dogname.text = name
                        dogweight.text = weight + " (lbs)"
                        dogbday.text = bday
                        doggender.text = gender
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                    // Handle error
                }
            })
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
            "Dad" -> "Good day, Daddy!"
            "Mom" -> "Good day, Mommy!"
            else -> defaultGreeting
        }
        greetingTextView.text = customizedGreeting
    }

    private fun fetchDogIDsAndUpdateAdapter() {
        val userCurrentID = auth.currentUser?.uid
        if (userCurrentID != null) {
            userDatabase.child(userCurrentID).child("Dogs")
                .addListenerForSingleValueEvent(object : ValueEventListener {
                    override fun onDataChange(snapshot: DataSnapshot) {
                        val dogIDs = snapshot.children.mapNotNull { it.key }
                        runOnUiThread {
                            profileAdapter.updateProfileList(dogIDs)
                            if (dogIDs.isNotEmpty()) {
                                val firstDogId = dogIDs.first()
                                fetchDogInformationAndUpdateUI(firstDogId) // Display the first dog's info
                                val btn : TextView = findViewById(R.id.dog_profile_arrow)
                                btn.setOnClickListener {
                                    val intent = Intent(this@homepageold, dog_profilepageold::class.java)
                                    // Pass the selectedDogId if available, otherwise use the firstDogId
                                    val dogIdToPass = selectedDogViewModel.selectedDogId ?: firstDogId
                                    intent.putExtra("dogId", dogIdToPass)
                                    // Create options for the animation
                                    val options = ActivityOptionsCompat.makeCustomAnimation(
                                        this@homepageold,
                                        R.anim.slide_in_right, // Animation for the new activity
                                        R.anim.slide_out_left // No animation for the current activity
                                    )

                                    // Start the activity with the specified animation options
                                    startActivity(intent, options.toBundle())
                                    finish()
                                }
                            }

                        }
                    }

                    override fun onCancelled(error: DatabaseError) {
                        // Handle onCancelled event
                    }
                })
        }
    }


    override fun onResume() {
        super.onResume()
        // Check if lastDisplayedDogId is not null, then fetch and display dog information
        lastDisplayedDogId?.let { dogId ->
            fetchDogInformationAndUpdateUI(dogId)
        }
    }



    private fun slideUpActivity(intent: Intent) {
        val options = ActivityOptions.makeCustomAnimation(
            this,
            R.anim.slide_up,
            0
        )
        startActivity(intent, options.toBundle())
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
        alertDialogBuilder.setTitle("Exit Application")
        alertDialogBuilder.setMessage("Are you sure you want to exit the application?")
        alertDialogBuilder.setPositiveButton("Confirm") { dialog, _ ->
            // If user confirms, exit the app
            finishAffinity()
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