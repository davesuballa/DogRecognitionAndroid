package com.example.buddy

import android.app.ActivityOptions
import android.app.AlertDialog
import android.content.Intent
import android.graphics.Typeface
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.text.InputType
import android.view.View
import androidx.activity.ComponentActivity
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.core.content.ContextCompat
import com.google.firebase.auth.EmailAuthProvider
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class change_password : ComponentActivity() {

    private lateinit var oldPasswordEditText: EditText
    private lateinit var newPasswordEditText: EditText
    private lateinit var confirmPasswordEditText: EditText
    private lateinit var saveButton: Button
    private lateinit var firebaseAuth: FirebaseAuth
    private lateinit var database: DatabaseReference

    private var isPasswordVisible = false // Track password visibility state


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.change_password)

        firebaseAuth = FirebaseAuth.getInstance()
        database = FirebaseDatabase.getInstance().reference

        oldPasswordEditText = findViewById(R.id.edit_oldpassword)
        newPasswordEditText = findViewById(R.id.edit_newpassword)
        confirmPasswordEditText = findViewById(R.id.edit_confirmpassword)
        val customFont = Typeface.createFromAsset(assets, "fonts/featherbold.ttf")
        val oldPasswordButton: Button = findViewById(R.id.eye)
        val newPasswordButton: Button = findViewById(R.id.eye2)
        val confirmPasswordButton: Button = findViewById(R.id.eye3)

        saveButton = findViewById(R.id.btn_save)

        saveButton.setOnClickListener {
            changePassword()
        }

        oldPasswordButton.setOnClickListener {
            togglePasswordVisibility(oldPasswordEditText, oldPasswordButton, customFont)
        }

        newPasswordButton.setOnClickListener {
            togglePasswordVisibility(newPasswordEditText, newPasswordButton, customFont)
        }

        confirmPasswordButton.setOnClickListener {
            togglePasswordVisibility(confirmPasswordEditText, confirmPasswordButton, customFont)
        }

        val backbtn = findViewById<Button>(R.id.backbtn)
        backbtn.setOnClickListener {
            val intent = Intent(this, profile_settings::class.java)
            ToPrevious(intent)
            finish()
        }
    }
    private fun togglePasswordVisibility(editText: EditText, button: Button, font: Typeface) {
        val isPasswordVisible = editText.inputType != InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD
        editText.inputType = if (isPasswordVisible) {
            InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD
        } else {
            InputType.TYPE_TEXT_VARIATION_PASSWORD or InputType.TYPE_CLASS_TEXT
        }
        editText.typeface = font // Set consistent font
        editText.setSelection(editText.text.length)
        val drawableResId = if (isPasswordVisible) R.drawable.eyeicon else R.drawable.eyecancel
        button.setBackgroundResource(drawableResId)
    }

    private fun changePassword() {
        val oldPassword = oldPasswordEditText.text.toString()
        val newPassword = newPasswordEditText.text.toString()
        val confirmPassword = confirmPasswordEditText.text.toString()
        // Check if any fields are empty
        if (oldPassword.isEmpty() || newPassword.isEmpty() || confirmPassword.isEmpty()) {
            Toast.makeText(this, "Please fill in all the fields", Toast.LENGTH_SHORT).show()
            oldPasswordEditText.setBackgroundResource(R.drawable.roundedwarning)
            newPasswordEditText.setBackgroundResource(R.drawable.roundedwarning)
            confirmPasswordEditText.setBackgroundResource(R.drawable.roundedwarning)
            val handler = Handler(Looper.getMainLooper())
            handler.postDelayed({
                // Reset background of oldPassword view
                oldPasswordEditText.setBackgroundResource(R.drawable.rounded)
                newPasswordEditText.setBackgroundResource(R.drawable.rounded)
                confirmPasswordEditText.setBackgroundResource(R.drawable.rounded)

            }, 1000) // Delay of 2000 milliseconds (2 seconds)
            return
        }

        // Check minimum password length
        if (newPassword.length < 6) {
            Toast.makeText(this, "Password should be at least 6 characters long", Toast.LENGTH_SHORT).show()

            newPasswordEditText.setBackgroundResource(R.drawable.roundedwarning)
            val handler = Handler(Looper.getMainLooper())
            handler.postDelayed({
                // Reset background of oldPassword view
                newPasswordEditText.setBackgroundResource(R.drawable.rounded)
            }, 2000) // Delay of 2000 milliseconds (2 seconds)
            return
        }

        // Check if new password matches confirm password
        if (newPassword != confirmPassword) {
            Toast.makeText(this, "Your new password and confirm password don't match", Toast.LENGTH_SHORT).show()
            newPasswordEditText.setBackgroundResource(R.drawable.roundedwarning)
            confirmPasswordEditText.setBackgroundResource(R.drawable.roundedwarning)

            val handler = Handler(Looper.getMainLooper())
            handler.postDelayed({
                // Reset background of oldPassword view
                newPasswordEditText.setBackgroundResource(R.drawable.rounded)
                confirmPasswordEditText.setBackgroundResource(R.drawable.rounded)
            }, 2000) // Delay of 2000 milliseconds (2 seconds)
            return
        }
        val user: FirebaseUser? = firebaseAuth.currentUser
        if (user != null && user.email != null) {
            val credential = EmailAuthProvider.getCredential(user.email!!, oldPassword)
            user.reauthenticate(credential)
                .addOnCompleteListener { reauthTask ->
                    if (reauthTask.isSuccessful) {
                        user.updatePassword(newPassword)
                            .addOnCompleteListener { updateTask ->
                                if (updateTask.isSuccessful) {
                                    Toast.makeText(this, "Password updated successfully", Toast.LENGTH_SHORT).show()
                                    updateUserPasswordInDatabase(user.uid, newPassword)
                                    val intent = Intent(this, profile_settings::class.java)
                                    ToPrevious(intent)
                                    finish()
                                } else {
                                    // Password update failed
                                    Toast.makeText(this, "Failed to update password", Toast.LENGTH_SHORT).show()
                                }
                            }
                    } else {
                        Toast.makeText(this, "Your old password is incorrect. Woof!", Toast.LENGTH_SHORT).show()
                        val handler = Handler(Looper.getMainLooper())

                        oldPasswordEditText.setBackgroundResource(R.drawable.roundedwarning)
                        handler.postDelayed({
                            // Reset background of oldPassword view
                            oldPasswordEditText.setBackgroundResource(R.drawable.rounded)
                        }, 2000) // Delay of 2000 milliseconds (2 seconds)
                    }
                }
        } else {
            // User is not signed in or email is null
            Toast.makeText(this, "There is no user authenticated. Woof!", Toast.LENGTH_SHORT).show()
        }
    }

    private fun updateUserPasswordInDatabase(userId: String, newPassword: String) {
        // Update user's password in the database
        val userRef = database.child("Users").child(userId).child("profile")
        userRef.child("password").setValue(newPassword)
            .addOnCompleteListener { updateTask ->
                if (updateTask.isSuccessful) {

                    Toast.makeText(this, "Password have updated. Woof!", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(this, "Can't update password.. *sad woof*", Toast.LENGTH_SHORT).show()
                }
            }
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


    private fun ToPrevious(intent: Intent) {
        val options = ActivityOptions.makeCustomAnimation(
            this,
            R.anim.slide_in_left, // Slide in from left
            R.anim.slide_out_right // Slide out to right
        )
        startActivity(intent, options.toBundle())
    }
}