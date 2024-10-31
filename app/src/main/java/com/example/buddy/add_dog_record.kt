package com.example.buddy
import android.app.ActivityOptions
import android.app.AlertDialog
import android.app.DatePickerDialog
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityOptionsCompat
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ServerValue
import java.util.*

class add_dog_record : AppCompatActivity() {

    private lateinit var userDatabase: DatabaseReference
    private lateinit var auth: FirebaseAuth
    private lateinit var selectedDateTextView: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.add_dog_record)

        // Retrieve dog ID from the intent
        val dogId = intent.getStringExtra("dogId")

        // Initialize Firebase components
        userDatabase = FirebaseDatabase.getInstance().getReference("Users")
        auth = FirebaseAuth.getInstance()

        // Initialize views
        val description = findViewById<EditText>(R.id.add_description)
        selectedDateTextView = findViewById(R.id.add_date)
        val veterinarian = findViewById<EditText>(R.id.add_veterinarian)
        val diagnosis = findViewById<EditText>(R.id.add_diagnosis)

        // Back button leads back to the homepage
        val backBtn: Button = findViewById(R.id.backbtn)
        backBtn.setOnClickListener {
            val intent = Intent(this, dog_profilepageold::class.java)
            intent.putExtra("dogId", dogId)
            val options = ActivityOptions.makeCustomAnimation(
                this,
                R.anim.slide_in_left, // Slide in from right
                R.anim.slide_out_right // Slide out to left
            )
            startActivity(intent, options.toBundle())
            finish()
        }

        // Save button click listener
        val saveButton: Button = findViewById(R.id.button_save)
        saveButton.setOnClickListener {
            val desc = description.text.toString().trim()
            val vet = veterinarian.text.toString().trim()
            val diag = diagnosis.text.toString().trim()
            val selectedDate = selectedDateTextView.text.toString().trim()

            if (desc.isEmpty() || vet.isEmpty() || diag.isEmpty() || selectedDate.isEmpty()) {
                Toast.makeText(this, "All fields are required", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            // Creating a confirmation dialog
            AlertDialog.Builder(this).setMessage("Are you done with inputting your dog's record?").setPositiveButton("Confirm") { dialog, which ->
                    // Proceed to save the record
                    val userDogRecordsRef = userDatabase.child(auth.currentUser?.uid ?: "")
                        .child("Dogs")
                        .child(dogId ?: "")
                        .child("DogRecords")

                    val recordKey = userDogRecordsRef.push().key ?: ""
                    val recordData = hashMapOf(
                        "description" to desc,
                        "veterinarian" to vet,
                        "diagnosis" to diag,
                        "date" to selectedDate,
                        "timestamp" to ServerValue.TIMESTAMP
                    )

                    userDogRecordsRef.child(recordKey).setValue(recordData)
                        .addOnSuccessListener {
                            Toast.makeText(this, "Record saved successfully", Toast.LENGTH_SHORT).show()
                            val intent = Intent(this, dog_profilepageold::class.java)
                            intent.putExtra("dogId", dogId)
                            val options = ActivityOptions.makeCustomAnimation(
                                this,
                                R.anim.slide_in_right, // Slide in from right
                                R.anim.slide_out_left // Slide out to left
                            )
                            startActivity(intent, options.toBundle())
                            finish()
                        }
                        .addOnFailureListener { e ->
                            Toast.makeText(this, "Failed to save record: ${e.message}", Toast.LENGTH_SHORT).show()
                        }
                }
                .setNegativeButton("Cancel", null) // Do nothing if "No" is clicked
                .show()
        }


        // Set up click listener for date selection
        selectedDateTextView.setOnClickListener {
            showDatePickerDialog()
        }
    }

    // Function to show the date picker dialog
    private fun showDatePickerDialog() {
        val calendar = Calendar.getInstance()
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH)
        val dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH)

        val datePickerDialog = DatePickerDialog(this,
            { _, year, monthOfYear, dayOfMonth ->
                val selectedDate = "$year-${monthOfYear + 1}-$dayOfMonth" // Format: YYYY-MM-DD
                selectedDateTextView.text = selectedDate
            }, year, month, dayOfMonth)

        datePickerDialog.show()
    }

    @Suppress("DEPRECATION")
    override fun onBackPressed() {
        val dogId = intent.getStringExtra("dogId")
        // Start a new activity or finish the current activity
        val intent = Intent(this, dog_profilepageold::class.java)
        intent.putExtra("dogId", dogId)
        startActivity(intent)

        // Finish the current activity to trigger the animation
        finish()

        // Define the animation to use after calling finish()
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right)
    }
}
