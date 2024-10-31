package com.example.buddy

import android.app.ActivityOptions
import android.app.AlertDialog
import android.app.Dialog
import android.content.ContentValues.TAG
import android.content.Context
import android.os.Bundle

import android.content.Intent
import android.graphics.Color
import android.graphics.Typeface
import android.graphics.drawable.ColorDrawable
import android.os.Handler
import android.os.Looper
import android.provider.ContactsContract
import android.text.Editable
import android.text.InputType
import android.text.TextWatcher
import android.text.method.HideReturnsTransformationMethod
import android.text.method.PasswordTransformationMethod
import android.util.Log
import android.util.Patterns
import android.view.View
import android.view.Window
import android.widget.Button
import android.widget.EditText
import android.widget.ScrollView
import android.widget.TextView
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityOptionsCompat
import androidx.core.content.ContextCompat
import androidx.core.content.res.ResourcesCompat
import com.example.buddy.databinding.ActivityCreateaccBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class CreateaccActivity : AppCompatActivity() {

    private lateinit var binding: ActivityCreateaccBinding
    private lateinit var firebaseAuth: FirebaseAuth
    private lateinit var database : DatabaseReference
    private lateinit var featherBoldTypeface: Typeface
    private var isEmailVerificationDialogVisible = false
    private var alertDialog: AlertDialog? = null
    private val handler = Handler(Looper.getMainLooper())
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (savedInstanceState != null) {
            isEmailVerificationDialogVisible = savedInstanceState.getBoolean("emailVerificationDialogVisible", false)
        }


        binding = ActivityCreateaccBinding.inflate(layoutInflater)
        setContentView(binding.root)

        firebaseAuth = FirebaseAuth.getInstance()
        database = FirebaseDatabase.getInstance().reference


        featherBoldTypeface = ResourcesCompat.getFont(this, R.font.featherbold)!!

        val passwordEditText: EditText = findViewById(R.id.password)
        val confirmEditText: EditText = findViewById(R.id.confirmpassword)

        val eyeButton: Button = findViewById(R.id.eye)
        val eyeButton2: Button = findViewById(R.id.eye2)

        eyeButton.setOnClickListener {
            togglePasswordVisibility(passwordEditText, eyeButton, featherBoldTypeface)
        }

        eyeButton2.setOnClickListener {
            togglePasswordVisibility(confirmEditText, eyeButton2, featherBoldTypeface)
        }

        val account2TextView: TextView = findViewById(R.id.account2)

        val backbtn = findViewById<Button>(R.id.backbtn)
        backbtn.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            backToMainActivity(intent)
            finish()
        }
        // Set a click listener on account2 TextView
        account2TextView.setOnClickListener {
            // Create an intent to navigate to the next activity
            val intent = Intent(this, LoginaccActivity::class.java)
            val options = ActivityOptionsCompat.makeCustomAnimation(
                this,
                R.anim.dissolve_in, // Animation for the new activity
                R.anim.dissolve_out // No animation for the current activity
            )

            // Start the activity with the specified animation options
            startActivity(intent, options.toBundle())
            finish()
        }

        binding.createaccount.setOnClickListener {

            val name = binding.name.text.toString()
            val email = binding.email.text.toString()
            val password = binding.password.text.toString()
            val confirmpass = binding.confirmpassword.text.toString()

            // Check if all fields are filled
            if (name.isNotEmpty() && email.isNotEmpty() && password.isNotEmpty() && confirmpass.isNotEmpty()) {
                // Check if password meets minimum length requirement
                if (password.length >= 6) {
                    // Check if password matches confirmation
                    if (password == confirmpass) {
                        // Check if the email meets the character requirements
                        if (isEmailValid(email)) {
                            if (name.matches(Regex("^[a-zA-Z ]+\$"))) {
                                // Proceed with creating the account
                                // Check if the email already exists
                                checkIfEmailExists(email) { emailExists ->
                                    if (!emailExists) {
                                        // Email doesn't exist, proceed with account creation
                                        firebaseAuth.createUserWithEmailAndPassword(email, password)
                                            .addOnCompleteListener { authTask ->
                                                if (authTask.isSuccessful) {
                                                    // Send email verification
                                                    firebaseAuth.currentUser?.sendEmailVerification()
                                                        ?.addOnCompleteListener { verificationTask ->
                                                            if (verificationTask.isSuccessful) {

                                                                showEmailVerificationDialog()
                                                                binding.createaccount.isEnabled = false
                                                                binding.createaccount.setBackgroundResource(R.drawable.create_acc_btn_clicked)
                                                            } else {
                                                                // Failed to send email verification
                                                                Toast.makeText(
                                                                    this, "Failed to send verification email",
                                                                    Toast.LENGTH_SHORT
                                                                ).show()
                                                                binding.createaccount.isEnabled = true
                                                                binding.createaccount.setBackgroundResource(R.drawable.create_account_btn)
                                                            }
                                                        }

                                                    val currentUserID = FirebaseAuth.getInstance().currentUser?.uid
                                                    if (currentUserID != null) {
                                                        // Initialize Realtime Database reference
                                                        val database = FirebaseDatabase.getInstance().reference

                                                        // Get a reference to the user's profile
                                                        val userProfileRef = database.child("Users").child(currentUserID).child("profile")

                                                        // Fetch the current maxUserNumber directly from the Users path
                                                        val maxUserNumberRef = database.child("Users").child("maxUserNumber")
                                                        maxUserNumberRef.addListenerForSingleValueEvent(object : ValueEventListener {
                                                            override fun onDataChange(maxUserNumberSnapshot: DataSnapshot) {
                                                                val maxUserNumberStr = maxUserNumberSnapshot.getValue(String::class.java)
                                                                val maxUserNumber = maxUserNumberStr?.toIntOrNull() ?: 0 // Default to 1 if null or conversion fails

                                                                // Fetch the userNumber from the user's profile
                                                                userProfileRef.child("userNumber").addListenerForSingleValueEvent(object : ValueEventListener {
                                                                    override fun onDataChange(userNumberSnapshot: DataSnapshot) {

                                                                        // Increment userNumber based on the current maxUserNumber
                                                                        val newUserNumber = maxUserNumber + 1
                                                                        Log.d("Debug", "New User Number: $newUserNumber")

                                                                        // Update the user's profile information including the new userNumber
                                                                        val userValues = mapOf(
                                                                            "name" to name,
                                                                            "email" to email,
                                                                            "password" to password,
                                                                            "userNumber" to newUserNumber.toString()
                                                                        )

                                                                        // Update the profile of the current user
                                                                        userProfileRef.setValue(userValues)
                                                                            .addOnSuccessListener {
                                                                                // Clear input fields
                                                                                binding.name.text.clear()
                                                                                binding.email.text.clear()
                                                                                binding.password.text.clear()
                                                                                binding.confirmpassword.text.clear()

                                                                                // Update the maxUserNumber
                                                                                maxUserNumberRef.setValue((newUserNumber).toString())
                                                                            }
                                                                            .addOnFailureListener {
                                                                                binding.createaccount.isEnabled = true
                                                                                binding.createaccount.setBackgroundResource(R.drawable.create_account_btn)
                                                                                Toast.makeText(
                                                                                    this@CreateaccActivity,
                                                                                    "Failed to create account",
                                                                                    Toast.LENGTH_SHORT
                                                                                ).show()
                                                                            }
                                                                    }

                                                                    override fun onCancelled(error: DatabaseError) {
                                                                        // Handle error
                                                                    }
                                                                })
                                                            }

                                                            override fun onCancelled(error: DatabaseError) {
                                                                // Handle error
                                                            }
                                                        })
                                                    }


                                                } else {
                                                    // Show error message if user creation fails
                                                    Toast.makeText(
                                                        this,
                                                        authTask.exception.toString(),
                                                        Toast.LENGTH_SHORT
                                                    ).show()
                                                }
                                            }

                                    } else {
                                        binding.createaccount.isEnabled = true
                                        binding.createaccount.setBackgroundResource(R.drawable.create_account_btn)
                                        Toast.makeText(
                                            this,
                                            "Email already exists. Woof!",
                                            Toast.LENGTH_SHORT
                                        ).show()
                                        binding.email.background = ContextCompat.getDrawable(
                                            this,
                                            R.drawable.roundedwarning
                                        )
                                        binding.warningcon2.visibility = View.VISIBLE
                                        handler.postDelayed({
                                            binding.email.background =
                                                ContextCompat.getDrawable(this, R.drawable.rounded)
                                            binding.warningcon2.visibility = View.INVISIBLE
                                        }, 2000)
                                    }
                                }
                            } else{
                                binding.createaccount.isEnabled = true
                                binding.createaccount.setBackgroundResource(R.drawable.create_account_btn)
                                Toast.makeText(this, "Name should contain letters only!", Toast.LENGTH_SHORT).show()
                                binding.name.background = ContextCompat.getDrawable(this, R.drawable.roundedwarning)
                                binding.warningicon.visibility = View.VISIBLE
                                handler.postDelayed({
                                    binding.name.background = ContextCompat.getDrawable(this, R.drawable.rounded)
                                    binding.warningicon.visibility = View.INVISIBLE
                                }, 2000)
                            }
                        } else {
                            binding.createaccount.isEnabled = true
                            binding.createaccount.setBackgroundResource(R.drawable.create_account_btn)
                            Toast.makeText(this, "Buddy only accepts valid emails.", Toast.LENGTH_SHORT).show()
                            binding.email.background = ContextCompat.getDrawable(this, R.drawable.roundedwarning)
                            binding.warningcon2.visibility = View.VISIBLE
                            handler.postDelayed({
                                binding.email.background = ContextCompat.getDrawable(this, R.drawable.rounded)
                                binding.warningcon2.visibility = View.INVISIBLE
                            }, 2000)
                        }
                    } else {
                        binding.createaccount.isEnabled = true
                        binding.createaccount.setBackgroundResource(R.drawable.create_account_btn)
                        Toast.makeText(this, "Passwords do not match.", Toast.LENGTH_SHORT).show()
                        binding.password.background = ContextCompat.getDrawable(this, R.drawable.roundedwarning)
                        binding.warningcon3.visibility = View.VISIBLE
                        binding.confirmpassword.background = ContextCompat.getDrawable(this, R.drawable.roundedwarning)
                        binding.warningcon4.visibility = View.VISIBLE
                        handler.postDelayed({
                            binding.password.background = ContextCompat.getDrawable(this, R.drawable.rounded)
                            binding.confirmpassword.background = ContextCompat.getDrawable(this, R.drawable.rounded)
                            binding.warningcon3.visibility = View.INVISIBLE
                            binding.warningcon4.visibility = View.INVISIBLE
                        }, 2000)

                    }
                } else {
                    binding.createaccount.isEnabled = true
                    binding.createaccount.setBackgroundResource(R.drawable.create_account_btn)
                    Toast.makeText(this, "Password should be at least 6 characters.", Toast.LENGTH_SHORT).show()
                        binding.password.background = ContextCompat.getDrawable(this, R.drawable.roundedwarning)
                        binding.warningcon3.visibility = View.VISIBLE
                    handler.postDelayed({
                        binding.password.background = ContextCompat.getDrawable(this, R.drawable.rounded)
                        binding.warningcon3.visibility = View.INVISIBLE
                    }, 2000)
                }
            } else {
                binding.createaccount.isEnabled = true
                binding.createaccount.setBackgroundResource(R.drawable.create_account_btn)
                Toast.makeText(this, "Empty fields are not allowed.", Toast.LENGTH_SHORT).show()

                // Set background to warning for each empty field and add a warning icon
                if (name.isEmpty()) {
                    binding.name.background = ContextCompat.getDrawable(this, R.drawable.roundedwarning)
                    binding.warningicon.visibility = View.VISIBLE
                }
                if (email.isEmpty()) {
                    binding.email.background = ContextCompat.getDrawable(this, R.drawable.roundedwarning)
                    binding.warningcon2.visibility = View.VISIBLE
                }
                if (password.isEmpty()) {
                    binding.password.background = ContextCompat.getDrawable(this, R.drawable.roundedwarning)
                    binding.warningcon3.visibility = View.VISIBLE
                }
                if (confirmpass.isEmpty()) {
                    binding.confirmpassword.background = ContextCompat.getDrawable(this, R.drawable.roundedwarning)
                    binding.warningcon4.visibility = View.VISIBLE
                }

                // Reset all backgrounds and remove warning icons after a delay
                handler.postDelayed({
                    binding.name.background = ContextCompat.getDrawable(this, R.drawable.rounded)
                    binding.email.background = ContextCompat.getDrawable(this, R.drawable.rounded)
                    binding.password.background = ContextCompat.getDrawable(this, R.drawable.rounded)
                    binding.confirmpassword.background = ContextCompat.getDrawable(this, R.drawable.rounded)
                    binding.warningicon.visibility = View.INVISIBLE
                    binding.warningcon2.visibility = View.INVISIBLE
                    binding.warningcon3.visibility = View.INVISIBLE
                    binding.warningcon4.visibility = View.INVISIBLE
                }, 2000)
            }
        }
    }

    private fun showEmailVerificationDialog() {
        binding.createaccount.isEnabled = true
        isEmailVerificationDialogVisible = true
        binding.createaccount.setBackgroundResource(R.drawable.create_account_btn)
        val alertDialogBuilder = AlertDialog.Builder(this@CreateaccActivity)
        alertDialogBuilder.setTitle("Account Created Successfully!")
        alertDialogBuilder.setCancelable(false)
        alertDialogBuilder.setMessage("An email verification has been sent to your email address. Please verify your email to activate your account.")

        // Create the AlertDialog
        val alertDialog = alertDialogBuilder.create()

        // Set action for the positive button ("Try Again")
        alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "Try Again") { dialog, which ->
            // Dismiss the dialog when "Try Again" is clicked
            dialog.dismiss()
        }

        // Show the dialog
        alertDialog.show()

        // Check email verification status after showing the dialog
        val user = firebaseAuth.currentUser
        checkEmailVerificationStatus(user)
    }





    // Function to save user's email to SharedPreferences
    private fun saveUserEmail(email: String) {
        val sharedPreferences = getSharedPreferences("MyPrefs", Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        editor.putString("userEmail", email)
        editor.apply()
    }
    override fun onResume() {
        super.onResume()

        if (isEmailVerificationDialogVisible) {
            showEmailVerificationDialog()
        }
    }


    private fun checkEmailVerificationStatus(user: FirebaseUser?) {
        user?.reload()?.addOnCompleteListener { reloadTask ->
            if (reloadTask.isSuccessful) {
                if (user.isEmailVerified) {
                    // Email is verified, dismiss the email verification dialog
                    dismissEmailVerificationDialog()

                    // Show terms and conditions dialog
                    showCustomDialogBox("Terms and Conditions")
                } else {
                    // Email is not verified yet, continue checking
                    Handler(Looper.getMainLooper()).postDelayed({
                        checkEmailVerificationStatus(user)
                    }, 2000) // Check every 2 seconds (adjust as needed)
                }
            }
        }
    }

    private fun dismissEmailVerificationDialog() {
        isEmailVerificationDialogVisible = false
       alertDialog?.dismiss()
    }



    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putBoolean("emailVerificationDialogVisible", isEmailVerificationDialogVisible)
    }



    private fun showCustomDialogBox(message: String) {
        // Dismiss the initial email verification dialog if it's currently shown
        alertDialog?.dismiss()

        val dialog = Dialog(this)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setCancelable(false)
        dialog.setContentView(R.layout.termsandcondition)
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        val terms: TextView = dialog.findViewById(R.id.termsandconditiontext)
        val accept: Button = dialog.findViewById(R.id.acceptbtn)
        val scrollView: ScrollView = dialog.findViewById(R.id.scrollView)

        accept.isEnabled = false
        accept.setBackgroundResource(R.drawable.accept_btn_cant_clicked)
        terms.text = message

        setupScrollViewListener(scrollView, accept)

        accept.setOnClickListener {
            val intent = Intent(this, createacc_pageone::class.java)
            ToSlideActivity(intent)
            dialog.dismiss()
        }



        dialog.show()
    }

    private fun setupScrollViewListener(scrollView: ScrollView, acceptButton: Button) {
        scrollView.viewTreeObserver.addOnScrollChangedListener {
            if (isScrolledToBottom(scrollView)) {
                acceptButton.isEnabled = true
                acceptButton.setBackgroundResource(R.drawable.accept)
            } else {
                acceptButton.isEnabled = false
                acceptButton.setBackgroundResource(R.drawable.accept_btn_cant_clicked)
            }
        }
    }

    private fun isScrolledToBottom(scrollView: ScrollView): Boolean {
        val scrollViewChild = scrollView.getChildAt(0)
        return (scrollView.height + scrollView.scrollY) >= scrollViewChild.height
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


    private fun checkIfEmailExists(email: String, callback: (Boolean) -> Unit) {
        // Get a reference to the "Users" node in your Firebase Realtime Database
        val usersRef = FirebaseDatabase.getInstance().getReference("Users")

        // Query the database to check if any user has the provided email address
        usersRef.orderByChild("profile/email").equalTo(email).addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                // Check if any data snapshot matches the query result
                var emailExists = false

                // Iterate through each child node to check for email match
                for (snapshot in dataSnapshot.children) {
                    val profileEmail = snapshot.child("profile/email").getValue(String::class.java)
                    if (profileEmail == email) {
                        emailExists = true
                        break
                    }
                }

                callback(emailExists)
            }

            override fun onCancelled(databaseError: DatabaseError) {
                // Handle any errors that may occur during the database query
                // For example, you can log the error or show an error message
                Log.e(TAG, "Failed to check email existence: ${databaseError.message}")
                callback(false) // Assuming it doesn't exist to avoid blocking the registration process
            }
        })
    }

    // To validate the email with following criteria
    private fun isEmailValid(email: String): Boolean {
        // Check if the email is not empty and contains '@' and '.'
        return email.isNotEmpty() && email.contains('@') && email.contains('.') && email.endsWith(".com")
    }


    private fun ToSlideActivity(intent: Intent) {
        val options = ActivityOptions.makeCustomAnimation(
            this,
            R.anim.slide_in_right, // Slide in from left
            R.anim.slide_out_left // Slide out to right
        )
        startActivity(intent, options.toBundle())
    }

    private fun backToMainActivity(intent: Intent) {
        val options = ActivityOptions.makeCustomAnimation(
            this,
            R.anim.slide_in_left, // Slide in from left
            R.anim.slide_out_right // Slide out to right
        )
        startActivity(intent, options.toBundle())
    }

    private fun createUserAccount(email: String, password: String, name: String) {
        val firebaseAuth = FirebaseAuth.getInstance()
        val firebaseDatabase = FirebaseDatabase.getInstance()

        firebaseAuth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener { authTask ->
                if (authTask.isSuccessful) {
                    // Get the current user
                    val currentUser = firebaseAuth.currentUser

                    // Store user information in Firebase Realtime Database
                    currentUser?.let { user ->
                        val userId = user.uid
                        val userReference = firebaseDatabase.reference.child("Users").child(userId)

                        // Create a Profile object to store in the database under the user's UID
                        val profileMap = HashMap<String, Any>()
                        profileMap["name"] = name
                        profileMap["email"] = email
                        profileMap["password"] = password

                        // Insert user profile information into the database under "Users/$userId/Profile"
                        val profileReference = userReference.child("profile")
                        profileReference.setValue(profileMap)
                            .addOnCompleteListener { profileTask ->
                                if (profileTask.isSuccessful) {
                                    // Database insertion successful
                                    showToast("Account created successfully!")

                                    // Send email verification link
                                    sendEmailVerification(user)
                                } else {
                                    // Database insertion failed
                                    showToast("Failed to create account. Please try again.")
                                }
                            }
                    }
                } else {
                    // Account creation failed
                    showToast("Failed to create account: ${authTask.exception?.message}")
                }
            }
    }

    private fun sendEmailVerification(user: FirebaseUser) {
        user.sendEmailVerification()
            .addOnCompleteListener { emailTask ->
                if (emailTask.isSuccessful) {
                    // Verification email sent successfully
                    showToast("Verification email sent to ${user.email}. Please check your inbox.")

                    // Wait for the user to verify the email before proceeding
                    waitForEmailVerification(user)
                } else {
                    // Failed to send verification email
                    showToast("Failed to send verification email. Please try again.")
                }
            }
    }


    private fun waitForEmailVerification(user: FirebaseUser) {
        // Check if the user has verified their email
        if (user.isEmailVerified) {
            // Navigate to the next activity
            val intent = Intent(this, createacc_pageone::class.java)
            ToSlideActivity(intent)
        } else {
            // Show message to inform the user to verify their email
            showToast("Please verify your email to proceed.")
        }
    }

    @Suppress("DEPRECATION")
    override fun onBackPressed() {
        // Create a custom function to show a confirmation dialog for exiting the application
        showExitConfirmationDialog()
    }

    private fun showExitConfirmationDialog() {
        val alertDialogBuilder = AlertDialog.Builder(this)
        alertDialogBuilder.setTitle("Exit Application?")
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

    // Function to show toast message
    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }
}