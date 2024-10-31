package com.example.buddy

import android.app.ActivityOptions
import android.app.AlarmManager
import android.app.AlertDialog
import android.app.Dialog
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.os.SystemClock
import android.text.Editable
import android.text.InputFilter
import android.text.TextWatcher
import android.text.method.PasswordTransformationMethod
import android.view.Window
import android.widget.Button
import android.widget.EditText
import android.widget.Switch
import android.widget.TextView
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SwitchCompat
import androidx.core.app.ActivityOptionsCompat
import androidx.core.content.ContextCompat
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class profile_settings : AppCompatActivity() {

    private lateinit var firebaseAuth: FirebaseAuth
    private lateinit var database: DatabaseReference
    private lateinit var profile : TextView
    private lateinit var usernameEditText: EditText
    private lateinit var emailEditText: EditText
    private lateinit var nicknameEditText: EditText
    private lateinit var contactNumberEditText: EditText
    private lateinit var notifSwitch: Switch


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile_settings)

        notifSwitch = findViewById(R.id.notif_switch)


        firebaseAuth = FirebaseAuth.getInstance()
        database = FirebaseDatabase.getInstance().reference

        val passbtn = findViewById<Button>(R.id.changepass)
        passbtn.setOnClickListener {
            // Open new activity when the EditText is clicked
            val intent = Intent(this, change_password::class.java)
            val options = ActivityOptions.makeCustomAnimation(
                this,
                R.anim.slide_in_right, // Slide in from left
                R.anim.slide_out_left // Slide out to right
            )
            startActivity(intent, options.toBundle())
            finish() // Finish the current activity after starting the login activity
        }


        profile = findViewById(R.id.profileicon)
        usernameEditText = findViewById(R.id.edit_username)
        emailEditText = findViewById(R.id.edit_email)
        nicknameEditText = findViewById(R.id.name)
        contactNumberEditText = findViewById(R.id.edit_contact)

        val initialContactNumber = "+63" // You can replace this with the actual contact number prefix
        contactNumberEditText.setText(initialContactNumber)

        // Disable editing for the "+63" prefix
        disablePrefixEditing(contactNumberEditText, initialContactNumber)

        // Add a TextWatcher to monitor changes in the EditText
        contactNumberEditText.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                // No action needed before text changes
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                // Check if the current text is shorter than the "+63" prefix
                if (s != null && s.length < initialContactNumber.length) {
                    // Restore the "+63" prefix if the user attempts to delete it
                    contactNumberEditText.setText(initialContactNumber)
                    contactNumberEditText.setSelection(initialContactNumber.length)
                }
            }

            override fun afterTextChanged(s: Editable?) {
                // No action needed after text changes
            }
        })




        retrieveUserInformation()


        //Sign out btn
        val signout_btn: Button = findViewById(R.id.signout)
        signout_btn.setOnClickListener {
            val alertDialogBuilder = AlertDialog.Builder(this@profile_settings)

            alertDialogBuilder.setTitle("Sign out")
            alertDialogBuilder.setMessage("Are you sure you want to sign out?")
            alertDialogBuilder.setCancelable(false)

            alertDialogBuilder.setPositiveButton("Confirm") { dialog, _ ->
                // Positive button click handler - sign out
                firebaseAuth.signOut() // Sign out the user

                //for event clearing the data
                EventDataUtils.clearEventData(this)
                Toast.makeText(this@profile_settings, "Signed out successfully", Toast.LENGTH_LONG).show()
                val intent = Intent(this@profile_settings, LoginaccActivity::class.java)
                val options = ActivityOptions.makeCustomAnimation(
                    this,
                    R.anim.slide_in_right, // Slide in from left
                    R.anim.slide_out_left // Slide out to right
                )
                startActivity(intent, options.toBundle())
                finish() // Finish the current activity after starting the login activity
                dialog.dismiss() // Dismiss the dialog upon positive action
            }

            alertDialogBuilder.setNegativeButton("Cancel") { dialog, _ ->
                // Negative button click handler - cancel sign out
                Toast.makeText(this@profile_settings, "Sign out cancelled", Toast.LENGTH_LONG).show()
                dialog.dismiss() // Dismiss the dialog upon negative action
            }

            // Create and show the AlertDialog
            val alertDialog = alertDialogBuilder.create()
            alertDialog.show()
        }


        val btnterms : Button = findViewById(R.id.tnc)
        btnterms.setOnClickListener {
            val message : String? = "Terms and Policy"
            showCustomDialogBox(message)
        }



        val save_btn: Button = findViewById(R.id.btn_save)
        save_btn.setOnClickListener {

            val nickname = nicknameEditText.text.toString().trim()
            val username = usernameEditText.text.toString().trim()
            val contact = contactNumberEditText.text.toString().trim()

            // Validate nickname (should only consist of letters)
            if (nickname.any { !it.isLetter() }) {
                Toast.makeText(this@profile_settings, "Nickname should only consist of letters", Toast.LENGTH_SHORT).show()
                nicknameEditText.setBackgroundResource(R.drawable.roundedwarning)
                val handler = Handler(Looper.getMainLooper())
                handler.postDelayed({
                    nicknameEditText.setBackgroundResource(R.drawable.rounded)
                }, 1000) // Delay of 2000 milliseconds (2 seconds)
            }
            // Validate username (should only consist of letters)
            else if (username.any { !it.isLetter() }) {
                Toast.makeText(this@profile_settings, "Username should only consist of letters", Toast.LENGTH_SHORT).show()
                usernameEditText.setBackgroundResource(R.drawable.roundedwarning)
                val handler = Handler(Looper.getMainLooper())
                handler.postDelayed({
                    usernameEditText.setBackgroundResource(R.drawable.rounded)
                }, 1000) // Delay of 2000 milliseconds (2 seconds)
            }
            else if (contact.startsWith('+') && contact.substring(1).any { !it.isDigit() }) {
                Toast.makeText(this@profile_settings, "Contact Number should only consist of numbers after '+63'", Toast.LENGTH_SHORT).show()
                contactNumberEditText.setBackgroundResource(R.drawable.roundedwarning)
                val handler = Handler(Looper.getMainLooper())
                handler.postDelayed({
                    contactNumberEditText.setBackgroundResource(R.drawable.rounded)
                }, 1000) // Delay of 2000 milliseconds (2 seconds)
            }
            else {
                // All fields are valid, proceed to update user information
                updateUserInformation()

                // Navigate back to profile overview activity
                val intent = Intent(this@profile_settings, profile_overview::class.java)
                ToPrevious(intent)
                finish() // Finish the current activity to prevent going back to it from the profile overview
            }
        }


        val backbtn: Button = findViewById(R.id.backbtn)
        backbtn.setOnClickListener {
            val intent = Intent(this, profile_overview::class.java)
            ToPrevious(intent)
            finish()
        }

        val changeprofile = findViewById<TextView>(R.id.change_picture)
        changeprofile.setOnClickListener {
            val intent = Intent(this, ChangeGender::class.java)
            val options = ActivityOptions.makeCustomAnimation(
                this,
                R.anim.slide_in_right, // Slide in from left
                R.anim.slide_out_left // Slide out to right
            )
            startActivity(intent, options.toBundle())
            finish()
        }


    }

    private fun disablePrefixEditing(editText: EditText, prefix: String) {
        // Create an InputFilter to prevent editing of the prefix
        val filter = InputFilter { source, start, end, dest, dstart, dend ->
            if (dstart < prefix.length) {
                // Check if the user is modifying the prefix
                val builder = StringBuilder(dest)
                builder.replace(dstart, dend, source.subSequence(start, end).toString())
                if (builder.toString() == prefix) {
                    // If the user is trying to modify the prefix, block the change
                    prefix
                } else {
                    // If the modification is not the prefix, allow it
                    null
                }
            } else {
                // Allow changes to the rest of the text
                null
            }
        }

        // Apply the InputFilter to the EditText
        val filters = editText.filters.toMutableList()
        filters.add(filter)
        editText.filters = filters.toTypedArray()
    }



    private fun retrieveUserInformation() {
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
                    val contactNumber = snapshot.child("contact_number").getValue(String::class.java)?:""

                    // Display user information in EditText fields


                    usernameEditText.setText(username)
                    emailEditText.setText(email)

                    // Check if nickname and contact number are not null
                    nickname?.let { nicknameEditText.setText(it) }
                    contactNumber?.let { contactNumberEditText.setText(it) }
                    // Set background resource based on parentType
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
                }
            })
        }
    }

    private fun updateUserInformation() {
        val currentUser = firebaseAuth.currentUser
        currentUser?.let { user ->
            val userId = user.uid
            val userRef = database.child("Users").child(userId).child("profile")

            val newUsername = usernameEditText.text.toString().trim()
            val newNickname = nicknameEditText.text.toString().trim()
            val newContactNumber = contactNumberEditText.text.toString().trim()

            // Update user information in the database
            userRef.child("name").setValue(newUsername)
            userRef.child("nickname").setValue(newNickname)
            userRef.child("contact_number").setValue(newContactNumber)
            // Apply text color based on whether newContactNumber is not null

            // Display a message or perform any other actions after updating the information
            Toast.makeText(this, "User information updated successfully", Toast.LENGTH_SHORT).show()
        }
    }





    private fun showCustomDialogBox(message:String?){
        val dialog = Dialog(this)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setCancelable(false)
        dialog.setContentView(R.layout.custom_dialog)
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        val terms : TextView = dialog.findViewById(R.id.termsandconditiontext)
        val btnreturn : Button = dialog.findViewById(R.id.returnbtn)
        terms.text = message

        btnreturn.setOnClickListener {
            dialog.dismiss()
        }

        dialog.show()
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
        alertDialogBuilder.setTitle("Return to Homepage?")
        alertDialogBuilder.setMessage("Are you sure you want to return to homepage? Your current data will still be updated.")
        alertDialogBuilder.setPositiveButton("Confirm") { dialog, _ ->
            val nickname = nicknameEditText.text.toString().trim()
            val username = usernameEditText.text.toString().trim()
            val contact = contactNumberEditText.text.toString().trim()

            // Validate nickname (should only consist of letters)
            if (nickname.any { !it.isLetter() }) {
                Toast.makeText(this@profile_settings, "Nickname should only consist of letters", Toast.LENGTH_SHORT).show()
            }
            // Validate username (should only consist of letters)
            else if (username.any { !it.isLetter() }) {
                Toast.makeText(this@profile_settings, "Username should only consist of letters", Toast.LENGTH_SHORT).show()
            }
            else if (contact.startsWith('+') && contact.substring(1).any { !it.isDigit() }) {
                Toast.makeText(this@profile_settings, "Contact Number should only consist of numbers after '+63'", Toast.LENGTH_SHORT).show()
            }
            else {
                // All fields are valid, proceed to update user information
                updateUserInformation()
                val intent = Intent(this, homepageold::class.java)
                val options = ActivityOptions.makeCustomAnimation(
                    this,
                    R.anim.dissolve_in, // Slide in from left
                    R.anim.dissolve_out // Slide out to right
                )
                startActivity(intent, options.toBundle())
                finish()
            }
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
