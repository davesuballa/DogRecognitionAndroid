package com.example.buddy

import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.text.method.HideReturnsTransformationMethod
import android.text.method.PasswordTransformationMethod
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.UserProfileChangeRequest
import com.google.firebase.database.FirebaseDatabase

class DeleteAccount : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth
    private lateinit var editEmail: EditText
    private lateinit var editPassword: EditText
    private lateinit var eyeIcon: TextView
    private var isPasswordVisible = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.deleteaccount)

        auth = FirebaseAuth.getInstance()

        editEmail = findViewById(R.id.edit_email)
        editPassword = findViewById(R.id.edit_password)
        eyeIcon = findViewById(R.id.eye2)

        // Inside your activity or fragment
        eyeIcon.setOnClickListener {
            isPasswordVisible = !isPasswordVisible
            togglePasswordVisibility()
        }
        val confirmBtn: Button = findViewById(R.id.btn_confirm)
        confirmBtn.setOnClickListener {
            val email = editEmail.text.toString().trim()
            val password = editPassword.text.toString().trim()

            if (email.isEmpty() || password.isEmpty()) {
                Toast.makeText(
                    this@DeleteAccount,
                    "Please enter both email and password.",
                    Toast.LENGTH_LONG
                ).show()
            } else {
                // Authenticate user with entered email and password
                signInAndDeleteAccount(email, password)
            }
        }
    }


    private fun togglePasswordVisibility() {
        if (isPasswordVisible) {
            // Show password
            editPassword.transformationMethod = HideReturnsTransformationMethod.getInstance()
        } else {
            // Hide password
            editPassword.transformationMethod = PasswordTransformationMethod.getInstance()
        }
        // Move cursor to the end of the text after changing transformation method
        editPassword.setSelection(editPassword.text.length)
    }


    private fun signInAndDeleteAccount(email: String, password: String) {
        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { signInTask ->
                if (signInTask.isSuccessful) {
                    // Proceed to show delete confirmation dialog
                    showDeleteConfirmationDialog()
                } else {
                    // Authentication failed
                    Toast.makeText(
                        this@DeleteAccount,
                        "Invalid email or password. Please try again.",
                        Toast.LENGTH_LONG
                    ).show()
                }
            }
    }

    private fun showDeleteConfirmationDialog() {
        val alertDialogBuilder = AlertDialog.Builder(this@DeleteAccount)
        alertDialogBuilder.setTitle("Are you sure you want to disable your account?")
        alertDialogBuilder.setMessage("Disabling your account will prevent access and remove data.")
        alertDialogBuilder.setCancelable(false)

        alertDialogBuilder.setPositiveButton("Confirm") { dialog, _ ->
            // User confirmed deletion, now disable the account
            disableAccount()

            // Start MainActivity (or any other activity) after disabling
            val intent = Intent(this@DeleteAccount, MainActivity::class.java)
            startActivity(intent)
            finish()

            dialog.dismiss()
        }

        alertDialogBuilder.setNegativeButton("Cancel") { dialog, _ ->
            // Negative button click handler - cancel account disabling
            Toast.makeText(this@DeleteAccount, "Account disabling cancelled", Toast.LENGTH_LONG).show()
            dialog.dismiss()
        }

        val alertDialog = alertDialogBuilder.create()
        alertDialog.show()
    }


    private fun disableAccount() {
        val user = auth.currentUser

        // Disable the user's account
        user?.let { currentUser ->
            currentUser.updateProfile(
                UserProfileChangeRequest.Builder()
                    .setDisplayName("DISABLED") // Optional: Set a flag to indicate disabled status
                    .build()
            ).addOnCompleteListener { updateProfileTask ->
                if (updateProfileTask.isSuccessful) {
                    Toast.makeText(
                        this@DeleteAccount,
                        "Account disabled successfully",
                        Toast.LENGTH_LONG
                    ).show()
                } else {
                    Toast.makeText(
                        this@DeleteAccount,
                        "Failed to disable account. Please try again.",
                        Toast.LENGTH_LONG
                    ).show()
                }
            }
        } ?: run {
            Toast.makeText(
                this@DeleteAccount,
                "User not found. Account disabling failed.",
                Toast.LENGTH_LONG
            ).show()
        }
    }

}