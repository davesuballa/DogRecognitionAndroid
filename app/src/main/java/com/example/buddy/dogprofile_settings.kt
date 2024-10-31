package com.example.buddy

import android.app.ActivityOptions
import android.app.AlertDialog
import android.app.DatePickerDialog
import android.content.DialogInterface
import android.os.Bundle
import androidx.activity.ComponentActivity
import android.content.Intent
import android.util.Log
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
import java.util.Calendar
import java.util.Locale
import java.util.concurrent.TimeUnit

class dogprofile_settings : ComponentActivity(),DatePickerDialog.OnDateSetListener {
        private lateinit var firebaseAuth: FirebaseAuth
        private lateinit var database: DatabaseReference
        private val calendar: Calendar = Calendar.getInstance()
        private lateinit var selectedDateTextView: TextView
        private lateinit var dogname: EditText
        private lateinit var gendername: Spinner
        private lateinit var weightname: EditText
        private lateinit var selectedDogId: String
        private lateinit var agename: EditText
        private lateinit var breedname: Spinner
        override fun onCreate(savedInstanceState: Bundle?) {
            super.onCreate(savedInstanceState)
            setContentView(R.layout.activity_dogprofile_settings)


            firebaseAuth = FirebaseAuth.getInstance()
            database = FirebaseDatabase.getInstance().getReference("Users")

            // Retrieve dog information from intent extras
            selectedDogId = intent.getStringExtra("dogId") ?: ""
            val breedName = intent.getStringExtra("breedName") ?: ""
            val age = intent.getStringExtra("age") ?: ""
            val dogName = intent.getStringExtra("dogName") ?: ""
            val weight = intent.getStringExtra("weight") ?: ""
            val birthday = intent.getStringExtra("birthday") ?: ""
            val gender = intent.getStringExtra("gender") ?: ""
            val furColor = intent.getStringExtra("furColor") ?: ""
            val accessory = intent.getStringExtra("accessory") ?: ""

            val resourceId = getDrawableResourceId(breedName,furColor, accessory)
            val profile = findViewById<TextView>(R.id.profileicon)
            profile.setBackgroundResource(resourceId)


            val toavatar = findViewById<TextView>(R.id.change_avatar)


            toavatar.setOnClickListener {
                val alertDialogBuilder = AlertDialog.Builder(this)
                alertDialogBuilder.setTitle("Dog Avatar Customization")
                alertDialogBuilder.setMessage("Do you want to proceed on changing your dog's avatar? " +
                        "You won't be able to return to the dog's information page after the confirmation.")
                alertDialogBuilder.setCancelable(false)

                alertDialogBuilder.setPositiveButton("Confirm") { dialogInterface: DialogInterface, _: Int ->
                    dialogInterface.dismiss()

                    // Proceed to dog avatar customization activity
                    val intent = Intent(this@dogprofile_settings, dog_avatar_creation2::class.java)
                    intent.putExtra("dogId", selectedDogId)
                    intent.putExtra("breedName", breedName)
                    intent.putExtra("age", age)
                    intent.putExtra("dogName", dogName)
                    intent.putExtra("weight", weight)
                    intent.putExtra("birthday", birthday)
                    intent.putExtra("gender", gender)
                    intent.putExtra("furColor", furColor)
                    intent.putExtra("accessory", accessory)
                    val options = ActivityOptionsCompat.makeCustomAnimation(
                        this@dogprofile_settings,
                        R.anim.slide_in_right,   // Animation for the new activity (slide up)
                        R.anim.slide_out_left   // Animation for the current activity (slide down)
                    )

                    // Start the activity with the specified animation options
                    startActivity(intent, options.toBundle())
                    finish()
                }

                alertDialogBuilder.setNegativeButton("Cancel") { dialogInterface: DialogInterface, _: Int ->
                    dialogInterface.dismiss()
                }

                val alertDialog = alertDialogBuilder.create()
                alertDialog.show()
            }
            // Initialize views
            dogname = findViewById(R.id.name)
            dogname.setText(dogName)

            // Initialize breed Spinner and set selection
            breedname = findViewById(R.id.edit_breed)

            weightname = findViewById(R.id.edit_weight)
            weightname.setText(weight)
            selectedDateTextView = findViewById(R.id.edit_birth)
            selectedDateTextView.text = birthday



            // Initialize breed Spinner and set selection
            breedname = findViewById(R.id.edit_breed)
            val defaultText = "Select Breed"
            val items = listOf(defaultText, "Golden Retriever", "Shih Tzu", "Chihuahua", "Poodle", "Pomeranian")
            val adapter = CustomSpinnerAdapter(this, R.layout.spinner_item2, items)
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            breedname.adapter = adapter
            val breedIndex = getIndex(breedname, breedName)
            breedname.setSelection(breedIndex)

            // Initialize gender Spinner and set selection
            gendername = findViewById<Spinner>(R.id.edit_gender)
            val defaultGenderText = "Select Gender"
            val genderItems = listOf(defaultGenderText, "Male", "Female")
            val genderAdapter = CustomSpinnerAdapter(this, R.layout.spinner_item2, genderItems)
            genderAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            gendername.adapter = genderAdapter
            val genderIndex = getIndex(gendername, gender)
            gendername.setSelection(genderIndex)

            selectedDateTextView = findViewById(R.id.edit_birth)
            selectedDateTextView.setOnClickListener {
                showDatePickerDialog()
            }
            val saveBtn = findViewById<Button>(R.id.btn_save)
            saveBtn.setOnClickListener {
                val newDogName = dogname.text.toString()
                val newBreed = breedname.selectedItem.toString()
                val newGender = gendername.selectedItem.toString()
                val newWeight = weightname.text.toString()
                val newFurColor = furColor  // Assuming furColor is already defined
                val newAccessory = accessory  // Assuming accessory is already defined
                val newBirthday = selectedDateTextView.text.toString()

                // Validate newDogName (should contain only letters)
                if (!newDogName.matches(Regex("^[a-zA-Z]+$"))) {
                    // Show toast message for invalid name
                    Toast.makeText(this@dogprofile_settings, "Name contain letters only", Toast.LENGTH_SHORT).show()
                    return@setOnClickListener  // Stop further execution
                }
                // Validate newWeight (should contain only numbers and max 3 digits)
                if (!newWeight.matches(Regex("^[0-9]{1,3}$"))) {
                    // Show toast message for invalid weight format
                    Toast.makeText(this@dogprofile_settings, "Weight must contain numbers only (max 3 digits)", Toast.LENGTH_SHORT).show()
                    return@setOnClickListener  // Stop further execution
                }

                // Validate numeric value of newWeight (must be >= 3)
                val weightValue = newWeight.toIntOrNull()
                if (weightValue == null || weightValue < 3) {
                    // Show toast message for invalid weight value
                    Toast.makeText(this@dogprofile_settings, "Weight must be 3 digits only", Toast.LENGTH_SHORT).show()
                    return@setOnClickListener  // Stop further execution
                }
                 updateDogInformation(newDogName, newBreed, newGender, newWeight, newFurColor, newAccessory, newBirthday)
            }





            //Back button leads back to the homepage
            val backbtn : Button = findViewById(R.id.backbtn)
            backbtn.setOnClickListener{
                val intent = Intent (this@dogprofile_settings, dog_profilepageold::class.java )
                intent.putExtra("dogId", selectedDogId)
                intent.putExtra("breedName", breedName)
                intent.putExtra("age", age)
                intent.putExtra("dogName", dogName)
                intent.putExtra("weight", weight)
                intent.putExtra("birthday", birthday)
                intent.putExtra("gender", gender)
                intent.putExtra("furColor", furColor)
                intent.putExtra("accessory", accessory)
                val options = ActivityOptionsCompat.makeCustomAnimation(
                    this@dogprofile_settings,
                    R.anim.slide_in_left,   // Animation for the new activity (slide up)
                    R.anim.slide_out_right   // Animation for the current activity (slide down)
                )

                // Start the activity with the specified animation options
                startActivity(intent, options.toBundle())
                finish()
            }



        }

    private fun updateDogInformation(
        newDogName: String,
        newBreed: String,
        newGender: String,
        newWeight: String,
        newFurColor: String,
        newAccessory: String,
        selectedDate: String
    ) {
        val currentUser = firebaseAuth.currentUser
        currentUser?.let { user ->
            val userId = user.uid
            val dogRef = database.child(userId).child("Dogs").child(selectedDogId)

            val newBirthday = selectedDateTextView.text.toString()

            // Calculate the age based on the selected birthday
            val newAge = calculateDogAge(newBirthday)

            val updates = hashMapOf<String, Any>(
                "dogName" to newDogName,
                "breedName" to newBreed,
                "gender" to newGender,
                "weight" to newWeight,
                "age" to newAge, // Update the age field
                "furColor" to newFurColor,
                "accessory" to newAccessory,
                "birthday" to selectedDate
            )

            // Update dog information in the database
            dogRef.updateChildren(updates)
                .addOnSuccessListener {
                    val intent = Intent(this@dogprofile_settings, dog_profilepageold::class.java)
                    intent.putExtra("dogId", selectedDogId)
                    intent.putExtra("breedName", newBreed)
                    intent.putExtra("age", newAge.toString()) // Assuming newAge is calculated properly
                    intent.putExtra("dogName", newDogName)
                    intent.putExtra("weight", newWeight)
                    intent.putExtra("birthday", selectedDate)
                    intent.putExtra("gender", newGender)
                    intent.putExtra("furColor", newFurColor)
                    intent.putExtra("accessory", newAccessory)
                    val options = ActivityOptionsCompat.makeCustomAnimation(
                        this@dogprofile_settings,
                        R.anim.slide_in_left,   // Animation for the new activity (slide up)
                        R.anim.slide_out_right   // Animation for the current activity (slide down)
                    )

                    // Start the activity with the specified animation options
                    startActivity(intent, options.toBundle())
                    finish()
                    Toast.makeText(this, "Dog information updated successfully", Toast.LENGTH_SHORT).show()
                }
                .addOnFailureListener { e ->
                    Toast.makeText(this, "Failed to update dog information: ${e.message}", Toast.LENGTH_SHORT).show()
                }
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


    private fun ToPrevious(intent: Intent) {
        val options = ActivityOptions.makeCustomAnimation(
            this,
            R.anim.slide_in_left, // Slide in from left
            R.anim.slide_out_right // Slide out to right
        )
        startActivity(intent, options.toBundle())
    }
    // Function to get the index of the value in the Spinner dataset
    private fun getIndex(spinner: Spinner, value: String): Int {
        for (i in 0 until spinner.count) {
            if (spinner.getItemAtPosition(i).toString() == value) {
                return i
            }
        }
        return 0 // Default to the first item if not found
    }

    // Private method to determine the drawable resource ID based on breed, fur, and accessory
    private fun getDrawableResourceId(breed: String, fur: String, accessory: String): Int {
        // Return the resource ID of the drawable based on the breed, fur, and accessory
        return when {
            breed == "Golden Retriever" && fur == "Fur1" && accessory == "" -> R.drawable.golden1_icon
            breed == "Golden Retriever" && fur == "Fur1" && accessory == "Collar1" -> R.drawable.golden1_collar1_icon
            breed == "Golden Retriever" && fur == "Fur1" && accessory == "Collar2" -> R.drawable.golden1_collar2_icon
            breed == "Golden Retriever" && fur == "Fur1" && accessory == "Scarf1" -> R.drawable.golden1_scarf1_icon
            breed == "Golden Retriever" && fur == "Fur1" && accessory == "Scarf2" -> R.drawable.golden1_scarf2_icon
            breed == "Golden Retriever" && fur == "Fur1" && accessory == "Ribbon1" -> R.drawable.golden1_ribbon1_icon
            breed == "Golden Retriever" && fur == "Fur1" && accessory == "Ribbon2" -> R.drawable.golden1_ribbon2_icon

            breed == "Golden Retriever" && fur == "Fur2" && accessory == "" -> R.drawable.golden2_icon
            breed == "Golden Retriever" && fur == "Fur2" && accessory == "Collar1" -> R.drawable.golden2_collar1_icon
            breed == "Golden Retriever" && fur == "Fur2" && accessory == "Collar2" -> R.drawable.golden2_collar2_icon
            breed == "Golden Retriever" && fur == "Fur2" && accessory == "Scarf1" -> R.drawable.golden2_scarf1_icon
            breed == "Golden Retriever" && fur == "Fur2" && accessory == "Scarf2" -> R.drawable.golden2_scarf2_icon
            breed == "Golden Retriever" && fur == "Fur2" && accessory == "Ribbon1" -> R.drawable.golden2_ribbon1_icon
            breed == "Golden Retriever" && fur == "Fur2" && accessory == "Ribbon2" -> R.drawable.golden2_ribbon2_icon

            breed == "Golden Retriever" && fur == "Fur3" && accessory == "" -> R.drawable.golden3_icon
            breed == "Golden Retriever" && fur == "Fur3" && accessory == "Collar1" -> R.drawable.golden3_collar1_icon
            breed == "Golden Retriever" && fur == "Fur3" && accessory == "Collar2" -> R.drawable.golden3_collar2_icon
            breed == "Golden Retriever" && fur == "Fur3" && accessory == "Scarf1" -> R.drawable.golden3_scarf1_icon
            breed == "Golden Retriever" && fur == "Fur3" && accessory == "Scarf2" -> R.drawable.golden3_scarf2_icon
            breed == "Golden Retriever" && fur == "Fur3" && accessory == "Ribbon1" -> R.drawable.golden3_ribbon1_icon
            breed == "Golden Retriever" && fur == "Fur3" && accessory == "Ribbon2" -> R.drawable.golden3_ribbon2_icon

            breed == "Golden Retriever" && fur == "Fur4" && accessory == "" -> R.drawable.golden4_icon
            breed == "Golden Retriever" && fur == "Fur4" && accessory == "Collar1" -> R.drawable.golden4_collar1_icon
            breed == "Golden Retriever" && fur == "Fur4" && accessory == "Collar2" -> R.drawable.golden4_collar2_icon
            breed == "Golden Retriever" && fur == "Fur4" && accessory == "Scarf1" -> R.drawable.golden4_scarf1_icon
            breed == "Golden Retriever" && fur == "Fur4" && accessory == "Scarf2" -> R.drawable.golden4_scarf2_icon
            breed == "Golden Retriever" && fur == "Fur4" && accessory == "Ribbon1" -> R.drawable.golden4_ribbon1_icon
            breed == "Golden Retriever" && fur == "Fur4" && accessory == "Ribbon2" -> R.drawable.golden4_ribbon2_icon

            breed == "Golden Retriever" && fur == "Fur5" && accessory == "" -> R.drawable.golden5_icon
            breed == "Golden Retriever" && fur == "Fur5" && accessory == "Collar1" -> R.drawable.golden5_collar1_icon
            breed == "Golden Retriever" && fur == "Fur5" && accessory == "Collar2" -> R.drawable.golden5_collar2_icon
            breed == "Golden Retriever" && fur == "Fur5" && accessory == "Scarf1" -> R.drawable.golden5_scarf1_icon
            breed == "Golden Retriever" && fur == "Fur5" && accessory == "Scarf2" -> R.drawable.golden5_scarf2_icon
            breed == "Golden Retriever" && fur == "Fur5" && accessory == "Ribbon1" -> R.drawable.golden5_ribbon1_icon
            breed == "Golden Retriever" && fur == "Fur5" && accessory == "Ribbon2" -> R.drawable.golden5_ribbon2_icon

            breed == "Golden Retriever" && fur == "Fur6" && accessory == "" -> R.drawable.golden6_icon
            breed == "Golden Retriever" && fur == "Fur6" && accessory == "Collar1" -> R.drawable.golden6_collar1_icon
            breed == "Golden Retriever" && fur == "Fur6" && accessory == "Collar2" -> R.drawable.golden6_collar2_icon
            breed == "Golden Retriever" && fur == "Fur6" && accessory == "Scarf1" -> R.drawable.golden6_scarf1_icon
            breed == "Golden Retriever" && fur == "Fur6" && accessory == "Scarf2" -> R.drawable.golden6_scarf2_icon
            breed == "Golden Retriever" && fur == "Fur6" && accessory == "Ribbon1" -> R.drawable.golden6_ribbon1_icon
            breed == "Golden Retriever" && fur == "Fur6" && accessory == "Ribbon2" -> R.drawable.golden6_ribbon2_icon

            breed == "Shih Tzu" && fur == "Fur1" && accessory == "" -> R.drawable.shitzu7_icon
            breed == "Shih Tzu" && fur == "Fur1" && accessory == "Collar1" -> R.drawable.shitzu7_collar1_icon
            breed == "Shih Tzu" && fur == "Fur1" && accessory == "Collar2" -> R.drawable.shitzu7_collar2_icon
            breed == "Shih Tzu" && fur == "Fur1" && accessory == "Scarf1" -> R.drawable.shitzu7_scarf1_icon
            breed == "Shih Tzu" && fur == "Fur1" && accessory == "Scarf2" -> R.drawable.shitzu7_scarf2_icon
            breed == "Shih Tzu" && fur == "Fur1" && accessory == "Ribbon1" -> R.drawable.shitzu7_ribbon1_icon
            breed == "Shih Tzu" && fur == "Fur1" && accessory == "Ribbon2" -> R.drawable.shitzu7_ribbon2_icon

            breed == "Shih Tzu" && fur == "Fur2" && accessory == "" -> R.drawable.shitzu5_icon
            breed == "Shih Tzu" && fur == "Fur2" && accessory == "Collar1" -> R.drawable.shitzu5_collar1_icon
            breed == "Shih Tzu" && fur == "Fur2" && accessory == "Collar2" -> R.drawable.shitzu5_collar2_icon
            breed == "Shih Tzu" && fur == "Fur2" && accessory == "Scarf1" -> R.drawable.shitzu5_scarf1_icon
            breed == "Shih Tzu" && fur == "Fur2" && accessory == "Scarf2" -> R.drawable.shitzu5_scarf2_icon
            breed == "Shih Tzu" && fur == "Fur2" && accessory == "Ribbon1" -> R.drawable.shitzu5_ribbon1_icon
            breed == "Shih Tzu" && fur == "Fur2" && accessory == "Ribbon2" -> R.drawable.shitzu5_ribbon2_icon

            breed == "Shih Tzu" && fur == "Fur3" && accessory == "" -> R.drawable.shitzu6_icon
            breed == "Shih Tzu" && fur == "Fur3" && accessory == "Collar1" -> R.drawable.shitzu6_collar1_icon
            breed == "Shih Tzu" && fur == "Fur3" && accessory == "Collar2" -> R.drawable.shitzu6_collar2_icon
            breed == "Shih Tzu" && fur == "Fur3" && accessory == "Scarf1" -> R.drawable.shitzu6_scarf1_icon
            breed == "Shih Tzu" && fur == "Fur3" && accessory == "Scarf2" -> R.drawable.shitzu6_scarf2_icon
            breed == "Shih Tzu" && fur == "Fur3" && accessory == "Ribbon1" -> R.drawable.shitzu6_ribbon1_icon
            breed == "Shih Tzu" && fur == "Fur3" && accessory == "Ribbon2" -> R.drawable.shitzu6_ribbon2_icon

            breed == "Shih Tzu" && fur == "Fur4" && accessory == "" -> R.drawable.shitzu1_icon
            breed == "Shih Tzu" && fur == "Fur4" && accessory == "Collar1" -> R.drawable.shitzu1_collar1_icon
            breed == "Shih Tzu" && fur == "Fur4" && accessory == "Collar2" -> R.drawable.shitzu1_collar2_icon
            breed == "Shih Tzu" && fur == "Fur4" && accessory == "Scarf1" -> R.drawable.shitzu1_scarf1_icon
            breed == "Shih Tzu" && fur == "Fur4" && accessory == "Scarf2" -> R.drawable.shitzu1_scarf2_icon
            breed == "Shih Tzu" && fur == "Fur4" && accessory == "Ribbon1" -> R.drawable.shitzu1_ribbon1_icon
            breed == "Shih Tzu" && fur == "Fur4" && accessory == "Ribbon2" -> R.drawable.shitzu1_ribbon2_icon

            breed == "Shih Tzu" && fur == "Fur5" && accessory == "" -> R.drawable.shitzu3_icon
            breed == "Shih Tzu" && fur == "Fur5" && accessory == "Collar1" -> R.drawable.shitzu3_collar1_icon
            breed == "Shih Tzu" && fur == "Fur5" && accessory == "Collar2" -> R.drawable.shitzu3_collar2_icon
            breed == "Shih Tzu" && fur == "Fur5" && accessory == "Scarf1" -> R.drawable.shitzu3_scarf1_icon
            breed == "Shih Tzu" && fur == "Fur5" && accessory == "Scarf2" -> R.drawable.shitzu3_scarf2_icon
            breed == "Shih Tzu" && fur == "Fur5" && accessory == "Ribbon1" -> R.drawable.shitzu3_ribbon1_icon
            breed == "Shih Tzu" && fur == "Fur5" && accessory == "Ribbon2" -> R.drawable.shitzu3_ribbon2_icon

            breed == "Shih Tzu" && fur == "Fur6" && accessory == "" -> R.drawable.shitzu2_icon
            breed == "Shih Tzu" && fur == "Fur6" && accessory == "Collar1" -> R.drawable.shitzu2_collar1_icon
            breed == "Shih Tzu" && fur == "Fur6" && accessory == "Collar2" -> R.drawable.shitzu2_collar2_icon
            breed == "Shih Tzu" && fur == "Fur6" && accessory == "Scarf1" -> R.drawable.shitzu2_scarf1_icon
            breed == "Shih Tzu" && fur == "Fur6" && accessory == "Scarf2" -> R.drawable.shitzu2_scarf2_icon
            breed == "Shih Tzu" && fur == "Fur6" && accessory == "Ribbon1" -> R.drawable.shitzu2_ribbon1_icon
            breed == "Shih Tzu" && fur == "Fur6" && accessory == "Ribbon2" -> R.drawable.shitzu2_ribbon2_icon

            breed == "Shih Tzu" && fur == "Fur7" && accessory == "" -> R.drawable.shitzu4_icon
            breed == "Shih Tzu" && fur == "Fur7" && accessory == "Collar1" -> R.drawable.shitzu4_collar1_icon
            breed == "Shih Tzu" && fur == "Fur7" && accessory == "Collar2" -> R.drawable.shitzu4_collar2_icon
            breed == "Shih Tzu" && fur == "Fur7" && accessory == "Scarf1" -> R.drawable.shitzu4_scarf1_icon
            breed == "Shih Tzu" && fur == "Fur7" && accessory == "Scarf2" -> R.drawable.shitzu4_scarf2_icon
            breed == "Shih Tzu" && fur == "Fur7" && accessory == "Ribbon1" -> R.drawable.shitzu4_ribbon1_icon
            breed == "Shih Tzu" && fur == "Fur7" && accessory == "Ribbon2" -> R.drawable.shitzu4_ribbon2_icon

            breed == "Chihuahua" && fur == "Fur1" && accessory == "" -> R.drawable.chihuahua5_icon
            breed == "Chihuahua" && fur == "Fur1" && accessory == "Collar1" -> R.drawable.chihuahua5_collar1_icon
            breed == "Chihuahua" && fur == "Fur1" && accessory == "Collar2" -> R.drawable.chihuahua5_collar2_icon
            breed == "Chihuahua" && fur == "Fur1" && accessory == "Scarf1" -> R.drawable.chihuahua5_scarf1_icon
            breed == "Chihuahua" && fur == "Fur1" && accessory == "Scarf2" -> R.drawable.chihuahua5_scarf2_icon
            breed == "Chihuahua" && fur == "Fur1" && accessory == "Ribbon1" -> R.drawable.chihuahua5_ribbon1_icon
            breed == "Chihuahua" && fur == "Fur1" && accessory == "Ribbon2" -> R.drawable.chihuahua5_ribbon2_icon

            breed == "Chihuahua" && fur == "Fur2" && accessory == "" -> R.drawable.chihuahua1_icon
            breed == "Chihuahua" && fur == "Fur2" && accessory == "Collar1" -> R.drawable.chihuahua1_collar1_icon
            breed == "Chihuahua" && fur == "Fur2" && accessory == "Collar2" -> R.drawable.chihuahua1_collar2_icon
            breed == "Chihuahua" && fur == "Fur2" && accessory == "Scarf1" -> R.drawable.chihuahua1_scarf1_icon
            breed == "Chihuahua" && fur == "Fur2" && accessory == "Scarf2" -> R.drawable.chihuahua1_scarf2_icon
            breed == "Chihuahua" && fur == "Fur2" && accessory == "Ribbon1" -> R.drawable.chihuahua1_ribbon1_icon
            breed == "Chihuahua" && fur == "Fur2" && accessory == "Ribbon2" -> R.drawable.chihuahua1_ribbon2_icon

            breed == "Chihuahua" && fur == "Fur3" && accessory == "" -> R.drawable.chihuahua3_icon
            breed == "Chihuahua" && fur == "Fur3" && accessory == "Collar1" -> R.drawable.chihuahua3_collar1_icon
            breed == "Chihuahua" && fur == "Fur3" && accessory == "Collar2" -> R.drawable.chihuahua3_collar2_icon
            breed == "Chihuahua" && fur == "Fur3" && accessory == "Scarf1" -> R.drawable.chihuahua3_scarf1_icon
            breed == "Chihuahua" && fur == "Fur3" && accessory == "Scarf2" -> R.drawable.chihuahua3_scarf2_icon
            breed == "Chihuahua" && fur == "Fur3" && accessory == "Ribbon1" -> R.drawable.chihuahua3_ribbon1_icon
            breed == "Chihuahua" && fur == "Fur3" && accessory == "Ribbon2" -> R.drawable.chihuahua3_ribbon2_icon

            breed == "Chihuahua" && fur == "Fur4" && accessory == "" -> R.drawable.chihuahua7_icon
            breed == "Chihuahua" && fur == "Fur4" && accessory == "Collar1" -> R.drawable.chihuahua7_collar1_icon
            breed == "Chihuahua" && fur == "Fur4" && accessory == "Collar2" -> R.drawable.chihuahua7_collar2_icon
            breed == "Chihuahua" && fur == "Fur4" && accessory == "Scarf1" -> R.drawable.chihuahua7_scarf1_icon
            breed == "Chihuahua" && fur == "Fur4" && accessory == "Scarf2" -> R.drawable.chihuahua7_scarf2_icon
            breed == "Chihuahua" && fur == "Fur4" && accessory == "Ribbon1" -> R.drawable.chihuahua7_ribbon1_icon
            breed == "Chihuahua" && fur == "Fur4" && accessory == "Ribbon2" -> R.drawable.chihuahua7_ribbon2_icon

            breed == "Chihuahua" && fur == "Fur5" && accessory == "" -> R.drawable.chihuahua4_icon
            breed == "Chihuahua" && fur == "Fur5" && accessory == "Collar1" -> R.drawable.chihuahua4_collar1_icon
            breed == "Chihuahua" && fur == "Fur5" && accessory == "Collar2" -> R.drawable.chihuahua4_collar2_icon
            breed == "Chihuahua" && fur == "Fur5" && accessory == "Scarf1" -> R.drawable.chihuahua4_scarf1_icon
            breed == "Chihuahua" && fur == "Fur5" && accessory == "Scarf2" -> R.drawable.chihuahua4_scarf2_icon
            breed == "Chihuahua" && fur == "Fur5" && accessory == "Ribbon1" -> R.drawable.chihuahua4_ribbon1_icon
            breed == "Chihuahua" && fur == "Fur5" && accessory == "Ribbon2" -> R.drawable.chihuahua4_ribbon2_icon

            breed == "Chihuahua" && fur == "Fur6" && accessory == "" -> R.drawable.chihuahua2_icon
            breed == "Chihuahua" && fur == "Fur6" && accessory == "Collar1" -> R.drawable.chihuahua2_collar1_icon
            breed == "Chihuahua" && fur == "Fur6" && accessory == "Collar2" -> R.drawable.chihuahua2_collar2_icon
            breed == "Chihuahua" && fur == "Fur6" && accessory == "Scarf1" -> R.drawable.chihuahua2_scarf1_icon
            breed == "Chihuahua" && fur == "Fur6" && accessory == "Scarf2" -> R.drawable.chihuahua2_scarf2_icon
            breed == "Chihuahua" && fur == "Fur6" && accessory == "Ribbon1" -> R.drawable.chihuahua2_ribbon1_icon
            breed == "Chihuahua" && fur == "Fur6" && accessory == "Ribbon2" -> R.drawable.chihuahua2_ribbon2_icon

            breed == "Pomeranian" && fur == "Fur1" && accessory == "" -> R.drawable.pomerenian4_icon
            breed == "Pomeranian" && fur == "Fur1" && accessory == "Collar1" -> R.drawable.pomerenian4_collar1_icon
            breed == "Pomeranian" && fur == "Fur1" && accessory == "Collar2" -> R.drawable.pomerenian4_collar2_icon
            breed == "Pomeranian" && fur == "Fur1" && accessory == "Scarf1" -> R.drawable.pomerenian4_scarf1_icon
            breed == "Pomeranian" && fur == "Fur1" && accessory == "Scarf2" -> R.drawable.pomerenian4_scarf2_icon
            breed == "Pomeranian" && fur == "Fur1" && accessory == "Ribbon1" -> R.drawable.pomerenian4_ribbon1_icon
            breed == "Pomeranian" && fur == "Fur1" && accessory == "Ribbon2" -> R.drawable.pomerenian4_ribbon2_icon

            breed == "Pomeranian" && fur == "Fur2" && accessory == "" -> R.drawable.pomerenian1_icon
            breed == "Pomeranian" && fur == "Fur2" && accessory == "Collar1" -> R.drawable.pomerenian1_collar1_icon
            breed == "Pomeranian" && fur == "Fur2" && accessory == "Collar2" -> R.drawable.pomerenian1_collar2_icon
            breed == "Pomeranian" && fur == "Fur2" && accessory == "Scarf1" -> R.drawable.pomerenian1_scarf1_icon
            breed == "Pomeranian" && fur == "Fur2" && accessory == "Scarf2" -> R.drawable.pomerenian1_scarf2_icon
            breed == "Pomeranian" && fur == "Fur2" && accessory == "Ribbon1" -> R.drawable.pomerenian1_ribbon1_icon
            breed == "Pomeranian" && fur == "Fur2" && accessory == "Ribbon2" -> R.drawable.pomerenian1_ribbon2_icon

            breed == "Pomeranian" && fur == "Fur3" && accessory == "" -> R.drawable.pomerenian5_icon
            breed == "Pomeranian" && fur == "Fur3" && accessory == "Collar1" -> R.drawable.pomerenian5_collar1_icon
            breed == "Pomeranian" && fur == "Fur3" && accessory == "Collar2" -> R.drawable.pomerenian5_collar2_icon
            breed == "Pomeranian" && fur == "Fur3" && accessory == "Scarf1" -> R.drawable.pomerenian5_scarf1_icon
            breed == "Pomeranian" && fur == "Fur3" && accessory == "Scarf2" -> R.drawable.pomerenian5_scarf2_icon
            breed == "Pomeranian" && fur == "Fur3" && accessory == "Ribbon1" -> R.drawable.pomerenian5_ribbon1_icon
            breed == "Pomeranian" && fur == "Fur3" && accessory == "Ribbon2" -> R.drawable.pomerenian5_ribbon2_icon

            breed == "Pomeranian" && fur == "Fur4" && accessory == "" -> R.drawable.pomerenian2_icon
            breed == "Pomeranian" && fur == "Fur4" && accessory == "Collar1" -> R.drawable.pomerenian2_collar1_icon
            breed == "Pomeranian" && fur == "Fur4" && accessory == "Collar2" -> R.drawable.pomerenian2_collar2_icon
            breed == "Pomeranian" && fur == "Fur4" && accessory == "Scarf1" -> R.drawable.pomerenian2_scarf1_icon
            breed == "Pomeranian" && fur == "Fur4" && accessory == "Scarf2" -> R.drawable.pomerenian2_scarf2_icon
            breed == "Pomeranian" && fur == "Fur4" && accessory == "Ribbon1" -> R.drawable.pomerenian2_ribbon1_icon
            breed == "Pomeranian" && fur == "Fur4" && accessory == "Ribbon2" -> R.drawable.pomerenian2_ribbon2_icon

            breed == "Pomeranian" && fur == "Fur5" && accessory == "" -> R.drawable.pomerenian6_icon
            breed == "Pomeranian" && fur == "Fur5" && accessory == "Collar1" -> R.drawable.pomerenian6_collar1_icon
            breed == "Pomeranian" && fur == "Fur5" && accessory == "Collar2" -> R.drawable.pomerenian6_collar2_icon
            breed == "Pomeranian" && fur == "Fur5" && accessory == "Scarf1" -> R.drawable.pomerenian6_scarf1_icon
            breed == "Pomeranian" && fur == "Fur5" && accessory == "Scarf2" -> R.drawable.pomerenian6_scarf2_icon
            breed == "Pomeranian" && fur == "Fur5" && accessory == "Ribbon1" -> R.drawable.pomerenian6_ribbon1_icon
            breed == "Pomeranian" && fur == "Fur5" && accessory == "Ribbon2" -> R.drawable.pomerenian6_ribbon2_icon

            breed == "Pomeranian" && fur == "Fur6" && accessory == "" -> R.drawable.pomerenian3_icon
            breed == "Pomeranian" && fur == "Fur6" && accessory == "Collar1" -> R.drawable.pomerenian3_collar1_icon
            breed == "Pomeranian" && fur == "Fur6" && accessory == "Collar2" -> R.drawable.pomerenian3_collar2_icon
            breed == "Pomeranian" && fur == "Fur6" && accessory == "Scarf1" -> R.drawable.pomerenian3_scarf1_icon
            breed == "Pomeranian" && fur == "Fur6" && accessory == "Scarf2" -> R.drawable.pomerenian3_scarf2_icon
            breed == "Pomeranian" && fur == "Fur6" && accessory == "Ribbon1" -> R.drawable.pomerenian3_ribbon1_icon
            breed == "Pomeranian" && fur == "Fur6" && accessory == "Ribbon2" -> R.drawable.pomerenian3_ribbon2_icon

            breed == "Poodle" && fur == "Fur1" && accessory == "" -> R.drawable.poodle5_icon
            breed == "Poodle" && fur == "Fur1" && accessory == "Collar1" -> R.drawable.poodle5_collar1_icon
            breed == "Poodle" && fur == "Fur1" && accessory == "Collar2" -> R.drawable.poodle5_collar2_icon
            breed == "Poodle" && fur == "Fur1" && accessory == "Scarf1" -> R.drawable.poodle5_scarf1_icon
            breed == "Poodle" && fur == "Fur1" && accessory == "Scarf2" -> R.drawable.poodle5_scarf2_icon
            breed == "Poodle" && fur == "Fur1" && accessory == "Ribbon1" -> R.drawable.poodle5_ribbon1_icon
            breed == "Poodle" && fur == "Fur1" && accessory == "Ribbon2" -> R.drawable.poodle5_ribbon2_icon

            breed == "Poodle" && fur == "Fur2" && accessory == "" -> R.drawable.poodle4_icon
            breed == "Poodle" && fur == "Fur2" && accessory == "Collar1" -> R.drawable.poodle4_collar1_icon
            breed == "Poodle" && fur == "Fur2" && accessory == "Collar2" -> R.drawable.poodle4_collar2_icon
            breed == "Poodle" && fur == "Fur2" && accessory == "Scarf1" -> R.drawable.poodle4_scarf1_icon
            breed == "Poodle" && fur == "Fur2" && accessory == "Scarf2" -> R.drawable.poodle4_scarf2_icon
            breed == "Poodle" && fur == "Fur2" && accessory == "Ribbon1" -> R.drawable.poodle4_ribbon1_icon
            breed == "Poodle" && fur == "Fur2" && accessory == "Ribbon2" -> R.drawable.poodle4_ribbon2_icon

            breed == "Poodle" && fur == "Fur3" && accessory == "" -> R.drawable.poodle6_icon
            breed == "Poodle" && fur == "Fur3" && accessory == "Collar1" -> R.drawable.poodle6_collar1_icon
            breed == "Poodle" && fur == "Fur3" && accessory == "Collar2" -> R.drawable.poodle6_collar2_icon
            breed == "Poodle" && fur == "Fur3" && accessory == "Scarf1" -> R.drawable.poodle6_scarf1_icon
            breed == "Poodle" && fur == "Fur3" && accessory == "Scarf2" -> R.drawable.poodle6_scarf2_icon
            breed == "Poodle" && fur == "Fur3" && accessory == "Ribbon1" -> R.drawable.poodle6_ribbon1_icon
            breed == "Poodle" && fur == "Fur3" && accessory == "Ribbon2" -> R.drawable.poodle6_ribbon2_icon

            breed == "Poodle" && fur == "Fur4" && accessory == "" -> R.drawable.poodle2_icon
            breed == "Poodle" && fur == "Fur4" && accessory == "Collar1" -> R.drawable.poodle2_collar1_icon
            breed == "Poodle" && fur == "Fur4" && accessory == "Collar2" -> R.drawable.poodle2_collar2_icon
            breed == "Poodle" && fur == "Fur4" && accessory == "Scarf1" -> R.drawable.poodle2_scarf1_icon
            breed == "Poodle" && fur == "Fur4" && accessory == "Scarf2" -> R.drawable.poodle2_scarf2_icon
            breed == "Poodle" && fur == "Fur4" && accessory == "Ribbon1" -> R.drawable.poodle2_ribbon1_icon
            breed == "Poodle" && fur == "Fur4" && accessory == "Ribbon2" -> R.drawable.poodle2_ribbon2_icon

            breed == "Poodle" && fur == "Fur5" && accessory == "" -> R.drawable.poodle3_icon
            breed == "Poodle" && fur == "Fur5" && accessory == "Collar1" -> R.drawable.poodle3_collar1_icon
            breed == "Poodle" && fur == "Fur5" && accessory == "Collar2" -> R.drawable.poodle3_collar2_icon
            breed == "Poodle" && fur == "Fur5" && accessory == "Scarf1" -> R.drawable.poodle3_scarf1_icon
            breed == "Poodle" && fur == "Fur5" && accessory == "Scarf2" -> R.drawable.poodle3_scarf2_icon
            breed == "Poodle" && fur == "Fur5" && accessory == "Ribbon1" -> R.drawable.poodle3_ribbon1_icon
            breed == "Poodle" && fur == "Fur5" && accessory == "Ribbon2" -> R.drawable.poodle3_ribbon2_icon

            breed == "Poodle" && fur == "Fur6" && accessory == "" -> R.drawable.poodle1_icon
            breed == "Poodle" && fur == "Fur6" && accessory == "Collar1" -> R.drawable.poodle1_collar1_icon
            breed == "Poodle" && fur == "Fur6" && accessory == "Collar2" -> R.drawable.poodle1_collar2_icon
            breed == "Poodle" && fur == "Fur6" && accessory == "Scarf1" -> R.drawable.poodle1_scarf1_icon
            breed == "Poodle" && fur == "Fur6" && accessory == "Scarf2" -> R.drawable.poodle1_scarf2_icon
            breed == "Poodle" && fur == "Fur6" && accessory == "Ribbon1" -> R.drawable.poodle1_ribbon1_icon
            breed == "Poodle" && fur == "Fur6" && accessory == "Ribbon2" -> R.drawable.poodle1_ribbon2_icon

            // Add more conditions for other combinations
            else -> R.drawable.profile // Default image in case no specific combination matches
        }
    }

    @Suppress("DEPRECATION")
    override fun onBackPressed() {
        // Start a new activity or finish the current activity
        val intent = Intent(this, dog_profilepageold::class.java)
        intent.putExtra("dogId", selectedDogId)
        startActivity(intent)

        // Finish the current activity to trigger the animation
        finish()

        // Define the animation to use after calling finish()
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right)
    }
}