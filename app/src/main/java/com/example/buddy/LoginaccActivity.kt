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
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityOptionsCompat
import androidx.core.content.ContextCompat
import androidx.core.content.res.ResourcesCompat
import com.example.buddy.databinding.ActivityLoginaccBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.FirebaseAuthInvalidUserException
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class LoginaccActivity : AppCompatActivity() {

    private lateinit var binding : ActivityLoginaccBinding
    private lateinit var featherBoldTypeface: Typeface
    private lateinit var firebaseAuth: FirebaseAuth
    private lateinit var database : DatabaseReference
    private val handler = Handler(Looper.getMainLooper())

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginaccBinding.inflate(layoutInflater)
        setContentView(binding.root)
        featherBoldTypeface = ResourcesCompat.getFont(this, R.font.featherbold)!!
        val passwordEditText: EditText = findViewById(R.id.password)
        val eyeButton: Button = findViewById(R.id.eye)
        eyeButton.setOnClickListener {
            togglePasswordVisibility(passwordEditText, eyeButton, featherBoldTypeface)
        }

        firebaseAuth = FirebaseAuth.getInstance()



        binding.account2.setOnClickListener {
            val intent = Intent(this, CreateaccActivity::class.java)
            val options = ActivityOptionsCompat.makeCustomAnimation(
                this,
                R.anim.dissolve_in, // Animation for the new activity
                R.anim.dissolve_out // No animation for the current activity
            )

            // Start the activity with the specified animation options
            startActivity(intent, options.toBundle())
            finish()
        }
        binding.backbtn.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            ToPrevious(intent)
            finish()
        }
        binding.forgotpassword.setOnClickListener {
            val intent = Intent(this, ForgotPasswordActivity::class.java)
            ToNextActivity(intent)
            finish()
        }
        binding.login.setOnClickListener {
            val email = binding.email.text.toString()
            val password = binding.password.text.toString()

            if (email.isNotEmpty() && password.isNotEmpty()) {
                firebaseAuth.signInWithEmailAndPassword(email, password)
                    .addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                            val user = firebaseAuth.currentUser
                            if (user != null && user.isEmailVerified) {
                                // Email is verified, navigate to home page
                                val intent = Intent(this, homepageold::class.java)
                                val options = ActivityOptionsCompat.makeCustomAnimation(
                                    this,
                                    R.anim.dissolve_in,
                                    R.anim.dissolve_out
                                )
                                startActivity(intent, options.toBundle())
                                finish()
                            } else {
                                // Email is not verified, show toast message
                                Toast.makeText(this, "Your email is not verified.", Toast.LENGTH_SHORT).show()
                            }
                        } else {
                            // Authentication failed
                            val exception = task.exception
                            if (exception is FirebaseAuthInvalidCredentialsException) {
                                // Check the error message to determine the specific reason for failure
                                val errorCode =
                                    (exception as FirebaseAuthInvalidCredentialsException).errorCode
                                if (errorCode == "ERROR_WRONG_PASSWORD") {
                                    // Incorrect password
                                    Toast.makeText(
                                        this,
                                        "Invalid password. Please try again.",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                } else {
                                    // Invalid email or other credential-related error
                                    Toast.makeText(
                                        this,
                                        "Invalid email or password. Please try again.",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                }
                            } else {
                                // Generic error message for other authentication failures
                                Toast.makeText(
                                    this,
                                    "Authentication failed. Please try again.",
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                        }
                    }
            } else {
                // Show message if any field is empty
                Toast.makeText(this, "Empty fields are not allowed.", Toast.LENGTH_SHORT).show()

                // Set background to warning for each empty field and add a warning icon
                if (email.isEmpty()) {
                    binding.email.background =
                        ContextCompat.getDrawable(this, R.drawable.roundedwarning)
                    binding.warningcon.visibility = View.VISIBLE
                }
                if (password.isEmpty()) {
                    binding.password.background =
                        ContextCompat.getDrawable(this, R.drawable.roundedwarning)
                    binding.warningcon2.visibility = View.VISIBLE
                }

                // Reset all backgrounds and remove warning icons after a delay
                handler.postDelayed({
                    binding.email.background = ContextCompat.getDrawable(this, R.drawable.rounded)
                    binding.password.background =
                        ContextCompat.getDrawable(this, R.drawable.rounded)
                    binding.warningcon.visibility = View.INVISIBLE
                    binding.warningcon2.visibility = View.INVISIBLE
                }, 2000)
            }
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

        // Set the appropriate eye icon drawable
        val drawableResId = if (isPasswordVisible) R.drawable.eyeicon else R.drawable.eyecancel
        button.setBackgroundResource(drawableResId)
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
            finish()
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