package com.example.buddy

import android.app.AlertDialog
import android.app.DatePickerDialog
import android.os.Bundle
import androidx.activity.ComponentActivity
import android.content.Intent
import android.text.Editable
import android.text.TextWatcher
import android.widget.Button
import android.widget.DatePicker
import android.widget.EditText
import android.widget.Spinner
import android.widget.TextView
import android.widget.Toast
import androidx.core.app.ActivityOptionsCompat
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.TimeUnit

class add_new_dog : ComponentActivity(), DatePickerDialog.OnDateSetListener{
    private lateinit var selectedDateTextView: TextView
    private val calendar: Calendar = Calendar.getInstance()
    private lateinit var database: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.add_new_dog)

        database = FirebaseDatabase.getInstance().getReference("Users")


        //Back button leads back to the homepage
        val backbtn : Button = findViewById(R.id.backbtn)

        backbtn.setOnClickListener{
            val intent = Intent (this, homepageold::class.java )
            val options = ActivityOptionsCompat.makeCustomAnimation(
                this,
                R.anim.slide_in_left, // Animation for the new activity
                R.anim.slide_out_right // No animation for the current activity
            )

            // Start the activity with the specified animation options
            startActivity(intent, options.toBundle())
            finish()
        }

        setupWeightEditText()
        setupNameEditText()

        val spinner: Spinner = findViewById(R.id.breed)
        val defaultText = "Select Breed"
        val items = listOf(defaultText, "Golden Retriever", "Shi Tzu", "Chihuahua", "Poodle", "Pomeranian")
        val adapter = CustomSpinnerAdapter3(this, R.layout.spinner_item2, items)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinner.adapter = adapter

        val spinner2: Spinner = findViewById(R.id.gender)
        val defaultText2 = "Select Gender"
        val items2 = listOf(defaultText2, "Male", "Female")
        val adapter2 = CustomSpinnerAdapter3(this, R.layout.spinner_item2, items2)
        adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinner2.adapter = adapter2

        selectedDateTextView = findViewById(R.id.bday)
        selectedDateTextView.setOnClickListener {
            showDatePickerDialog()
        }


        val addButton: Button = findViewById(R.id.buttonnext)
        addButton.setOnClickListener {
            val breedSpinner: Spinner = findViewById(R.id.breed)
            val genderSpinner: Spinner = findViewById(R.id.gender)
            val breed: String = breedSpinner.selectedItem as? String ?: ""
            val gender: String = genderSpinner.selectedItem as? String ?: ""
            val name: String = findViewById<EditText>(R.id.name).text.toString()
            val weight: String = findViewById<EditText>(R.id.weight).text.toString()
            val birthday: String = selectedDateTextView.text.toString()

            // Check if any of the required fields are empty or if the breed/gender is default
            if (breed.isEmpty() || breed == "Select Breed" || gender.isEmpty() || gender == "Select Gender" ||
                name.isEmpty() || weight.isEmpty() || birthday.isEmpty()) {
                if (breed.isEmpty() || breed == "Select Breed") {
                    Toast.makeText(this@add_new_dog, "Please select a breed", Toast.LENGTH_LONG).show()
                } else if (gender.isEmpty() || gender == "Select Gender") {
                    Toast.makeText(this@add_new_dog, "Please select a gender", Toast.LENGTH_LONG).show()
                } else {
                    Toast.makeText(this@add_new_dog, "Please fill in all fields", Toast.LENGTH_LONG).show()
                }
            } else {
                val alertdialogbuilder = AlertDialog.Builder(this@add_new_dog)

                alertdialogbuilder.setTitle("Add new Dog Profile")
                alertdialogbuilder.setMessage("Are you done inputting your dog's details?")
                alertdialogbuilder.setCancelable(false)

                alertdialogbuilder.setPositiveButton("Confirm") { dialog, _ ->
                    addNewDogToDatabase()
                }

                alertdialogbuilder.setNegativeButton("Cancel") { dialog, _ ->
                    Toast.makeText(this@add_new_dog, "Dog Profile has been cancelled", Toast.LENGTH_LONG).show()
                    dialog.dismiss() // Dismiss the dialog upon negative action
                }

                // Create and show the AlertDialog
                val alertDialog = alertdialogbuilder.create()
                alertDialog.show()
            }
        }


        val scanner : Button = findViewById(R.id.login)
        scanner.setOnClickListener {
            val intent = Intent(this, ScannerActivity::class.java)
            intent.putExtra("fromQRCodePage", false)
            val options = ActivityOptionsCompat.makeCustomAnimation(
                this,
                R.anim.slide_up, // Animation for the new activity
                R.anim.dissolve_out // No animation for the current activity
            )

            // Start the activity with the specified animation options
            startActivity(intent, options.toBundle())
            finish()
        }


    }
    private fun addNewDogToDatabase() {
        val name: String = findViewById<EditText>(R.id.name).text.toString()
        val weight: String = findViewById<EditText>(R.id.weight).text.toString()
        val breed: String = (findViewById<Spinner>(R.id.breed).selectedItem as? String) ?: ""
        val gender: String = (findViewById<Spinner>(R.id.gender).selectedItem as? String) ?: ""
        val birthday: String = selectedDateTextView.text.toString()
        val newAge = calculateDogAge(birthday)
        val age = findViewById<TextView>(R.id.age)
        age.text = newAge
        val user = FirebaseAuth.getInstance().currentUser
        val userDatabase = FirebaseDatabase.getInstance().reference.child("Users").child(user?.uid ?: "")
        // Generate a unique key for the new dog
        val newDogReference = userDatabase.child("Dogs").push()
        // Set the data for the new dog using the generated key
        val newDogData = mapOf(
            "breedName" to breed,
            "dogName" to name,
            "age" to newAge,
            "weight" to weight,
            "gender" to gender,
            "birthday" to birthday
        )

        newDogReference.setValue(newDogData)
            .addOnSuccessListener {
                // Data successfully saved
                Toast.makeText(this, "Dog added successfully", Toast.LENGTH_SHORT).show()
                val intent = Intent(this, dog_avatar_creation::class.java)
                val options = ActivityOptionsCompat.makeCustomAnimation(
                    this,
                    R.anim.slide_in_right, // Animation for the new activity
                    R.anim.slide_out_left // No animation for the current activity
                )

                // Start the activity with the specified animation options
                startActivity(intent, options.toBundle())
                finish()
            }
            .addOnFailureListener {
                // Error occurred while saving data
                Toast.makeText(this, "Failed to add dog", Toast.LENGTH_SHORT).show()
            }
    }


    private fun showDatePickerDialog() {
        val datePickerDialog = DatePickerDialog(
            this,
            this,
            calendar.get(Calendar.YEAR),
            calendar.get(Calendar.MONTH),
            calendar.get(Calendar.DAY_OF_MONTH)
        )
        datePickerDialog.datePicker.maxDate = System.currentTimeMillis() // Set max date to today
        datePickerDialog.show()
    }

    override fun onDateSet(view: DatePicker?, year: Int, month: Int, dayOfMonth: Int) {
        // Format the selected date as "MM-dd-yyyy"
        val selectedDate = String.format("%02d-%02d-%d", month + 1, dayOfMonth, year)
        selectedDateTextView.text = selectedDate

        // Recalculate and update the age based on the selected date
        val newAge = calculateDogAge(selectedDate)
        val ageTextView = findViewById<TextView>(R.id.age)
        ageTextView.text = newAge
    }


    private fun calculateDogAge(birthday: String): String {
        if (birthday.isEmpty()) {
            return "Unknown age"
        }

        try {
            val dateFormat = SimpleDateFormat("MM-dd-yyyy", Locale.getDefault())
            val birthDate = dateFormat.parse(birthday)

            if (birthDate != null) {
                val birthCalendar = Calendar.getInstance()
                birthCalendar.time = birthDate

                val today = Calendar.getInstance()

                var age = today.get(Calendar.YEAR) - birthCalendar.get(Calendar.YEAR)
                if (today.get(Calendar.DAY_OF_YEAR) < birthCalendar.get(Calendar.DAY_OF_YEAR)) {
                    age--
                }

                return if (age < 1) {
                    "Less than a year old"
                } else {
                    "$age years old"
                }
            }
        } catch (e: ParseException) {
            e.printStackTrace() // Log the error for debugging purposes
        }

        return "Unknown age"
    }


    private fun setupWeightEditText() {
        val weightEditText: EditText = findViewById(R.id.weight)

        weightEditText.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                // No implementation needed
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                // No implementation needed
            }

            override fun afterTextChanged(s: Editable?) {
                s?.let {
                    val weightInput = s.toString()

                    // Check if the input is empty or exceeds 3 digits
                    if (weightInput.isNotEmpty() && (weightInput.length >= 4 || !weightInput.matches("[0-9]+".toRegex()))) {
                        // Display a toast message if weight exceeds 3 digits or contains non-numeric characters
                        Toast.makeText(this@add_new_dog, "Weight should only contain up to 3 digits!", Toast.LENGTH_SHORT).show()

                        // Clear the EditText or handle the input as needed
                        weightEditText.text.clear()
                    }
                }
            }
        })
    }


    private fun setupNameEditText() {
        val nameEditText: EditText = findViewById(R.id.name)

        nameEditText.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                // No implementation needed
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                // No implementation needed
            }

            override fun afterTextChanged(s: Editable?) {
                s?.let {
                    val nameInput = s.toString()
                    if (nameInput.isNotEmpty() && !nameInput.matches("[a-zA-Z ]+".toRegex())) {
                        // If the input contains non-alphabetic characters, display a toast message
                        Toast.makeText(this@add_new_dog, "Name should only contain letters! Woof!", Toast.LENGTH_SHORT).show()
                        // Clear the EditText or handle the input as needed
                        nameEditText.text.clear()
                    }
                }
            }
        })
    }

    @Suppress("DEPRECATION")
    override fun onBackPressed() {
        // Start a new activity or finish the current activity
        val intent = Intent(this, homepageold::class.java)
        startActivity(intent)

        // Finish the current activity to trigger the animation
        finish()

        // Define the animation to use after calling finish()
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right)
    }
}