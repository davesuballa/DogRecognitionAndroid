package com.example.buddy

import android.app.ActivityOptions
import android.app.AlertDialog
import android.os.Bundle
import androidx.activity.ComponentActivity
import android.content.Intent
import android.graphics.Color
import android.graphics.Typeface
import android.os.Build
import android.view.Gravity
import android.view.View
import android.widget.Button
import android.widget.TableLayout
import android.widget.TableRow
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityOptionsCompat
import androidx.core.content.ContextCompat
import androidx.core.content.res.ResourcesCompat
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class dog_profilepageold : AppCompatActivity() {

    private lateinit var dogRecordsTable: TableLayout
    private lateinit var dogRow : TableRow
    private lateinit var  tabrow : TableRow
    private lateinit var auth : FirebaseAuth
    private lateinit var userDatabase: DatabaseReference // Declare as class-level property

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_dog_profilepage2)

        // Retrieve dog ID from the intent
        val dogId = intent.getStringExtra("dogId")

        // Initialize class-level userDatabase property
        userDatabase = FirebaseDatabase.getInstance().getReference("Users") // Use class-level property
        tabrow = findViewById(R.id.tabrow)

        auth = FirebaseAuth.getInstance()

        // Initialize other views and components
        dogRecordsTable = findViewById(R.id.tblayout)
        val breedtext = findViewById<TextView>(R.id.dog_breedname)
        val agetext = findViewById<TextView>(R.id.dog_agename)
        val dogname = findViewById<TextView>(R.id.dog_buddyname)
        val dogweight = findViewById<TextView>(R.id.dog_weightname)
        val dogbday = findViewById<TextView>(R.id.dog_bdayname)
        val doggender = findViewById<TextView>(R.id.dog_sexname)
        val dogavatar = findViewById<TextView>(R.id.puppy)

        // Fetch dog information from Firebase using the dog ID
        val userCurrentID = auth.currentUser?.uid
        if (userCurrentID != null && dogId != null) {
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
                        val fur = snapshot.child("furColor").getValue(String::class.java) ?: "N/A"
                        val accessory = snapshot.child("accessory").getValue(String::class.java) ?: "N/A"

                        val resourceId = getDrawableResourceId(breed, fur, accessory)

                        breedtext.text = breed
                        agetext.text = age
                        dogname.text = name
                        dogweight.text = weight
                        dogbday.text = bday
                        doggender.text = gender
                        dogavatar.setBackgroundResource(resourceId)

                        // Call function to display dog records
                        displayDogRecords(userCurrentID, dogId)


                        val settingsButton : Button = findViewById(R.id.stngs)
                        settingsButton.setOnClickListener {
                            val intent = Intent(this@dog_profilepageold, dogprofile_settings::class.java)
                            intent.putExtra("dogId", dogId)
                            intent.putExtra("breedName", breed)
                            intent.putExtra("age", age)
                            intent.putExtra("dogName", name)
                            intent.putExtra("weight", weight)
                            intent.putExtra("birthday", bday)
                            intent.putExtra("gender", gender)
                            intent.putExtra("furColor", fur)
                            intent.putExtra("accessory", accessory)
                            val options = ActivityOptionsCompat.makeCustomAnimation(
                                this@dog_profilepageold,
                                R.anim.slide_in_right,   // Animation for the new activity (slide up)
                                R.anim.slide_out_left   // Animation for the current activity (slide down)
                            )

                            // Start the activity with the specified animation options
                            startActivity(intent, options.toBundle())
                            finish()
                        }

                        val qr : Button = findViewById(R.id.export_profile)
                        qr.setOnClickListener {
                            val intent = Intent(this@dog_profilepageold, qrcodescanner::class.java)
                            intent.putExtra("dogId", dogId)
                            val options = ActivityOptionsCompat.makeCustomAnimation(
                                this@dog_profilepageold,
                                R.anim.slide_up,   // Animation for the new activity (slide up)
                                R.anim.fade_out   // Animation for the current activity (slide down)
                            )

                            // Start the activity with the specified animation options
                            startActivity(intent, options.toBundle())
                            finish()
                        }


                    }
                }


                override fun onCancelled(error: DatabaseError) {
                }
            })
        }



        // Back button leads back to the homepage
        val backbtn : Button = findViewById(R.id.backbtn)
        backbtn.setOnClickListener{
            val intent = Intent(this, homepageold::class.java)
            ToPrevious(intent)
            finish()
        }

        val deleteButton: Button = findViewById(R.id.deletedog)
        deleteButton.setOnClickListener {

            val alertdialogbuilder = AlertDialog.Builder(this@dog_profilepageold)

            alertdialogbuilder.setTitle("Do you want to proceed on deleting your Dog Profile?")
            alertdialogbuilder.setMessage("Deleting your Dog Profile will remove all of your dog's data and information.")
            alertdialogbuilder.setCancelable(false)

            alertdialogbuilder.setPositiveButton("Confirm") { dialog, _ ->

                userCurrentID?.let { userId ->
                    deleteUserDog(userDatabase, userId, dogId!!)
                }
                val intent = Intent(this@dog_profilepageold, homepageold::class.java)
                val options = ActivityOptionsCompat.makeCustomAnimation(
                    this@dog_profilepageold,
                    R.anim.slide_in_left,   // Animation for the new activity (slide up)
                    R.anim.slide_out_right   // Animation for the current activity (slide down)
                )

                // Start the activity with the specified animation options
                startActivity(intent, options.toBundle())
                finish()
                Toast.makeText(this@dog_profilepageold, "Dog Profile has been deleted", Toast.LENGTH_LONG).show()
                dialog.dismiss()
            }

            alertdialogbuilder.setNegativeButton("Cancel") { dialog, _ ->
                Toast.makeText(this@dog_profilepageold, "Dog Profile has been cancelled", Toast.LENGTH_LONG).show()
                dialog.dismiss() // Dismiss the dialog upon negative action
            }

            // Create and show the AlertDialog
            val alertDialog = alertdialogbuilder.create()
            alertDialog.show()
        }


        // Add record button
        val addRecordButton : Button = findViewById(R.id.addrecord)
        addRecordButton.setOnClickListener {
            val intent = Intent(this@dog_profilepageold, add_dog_record::class.java)
            intent.putExtra("dogId", dogId)
            // Create custom animation options for starting the new activity
            val options = ActivityOptionsCompat.makeCustomAnimation(
                this,
                R.anim.slide_up,   // Animation for the new activity (slide up)
                R.anim.fade_out   // Animation for the current activity (slide down)
            )

            // Start the activity with the specified animation options
            startActivity(intent, options.toBundle())
            finish()

        }



    }

    private fun displayDogRecords(userId: String, dogId: String) {
        val dogRecordsRef = userDatabase.child(userId).child("Dogs").child(dogId).child("DogRecords")
        dogRecordsRef.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()) {
                    // Hide the tabrow if there are existing records
                    tabrow.visibility = View.GONE

                    var count = 0  // Variable to count rows displayed
                    for (recordSnapshot in snapshot.children) {
                        if (count >= 3) {
                            break  // Stop iterating after displaying 3 rows
                        }

                        val description = recordSnapshot.child("description").getValue(String::class.java) ?: ""
                        val veterinarian = recordSnapshot.child("veterinarian").getValue(String::class.java) ?: ""
                        val date = recordSnapshot.child("date").getValue(String::class.java) ?: ""
                        val diagnosis = recordSnapshot.child("diagnosis").getValue(String::class.java) ?: ""

                        // Create a new row for each record
                        val newRow = TableRow(this@dog_profilepageold)

                        // Create TextViews for each field using custom style
                        val dateTextView = createStyledTextView(date, 14f, R.color.brownspinnerchat, R.font.featherbold)
                        val descriptionTextView = createStyledTextView(description, 14f, R.color.brownspinnerchat, R.font.featherbold)
                        val veterinarianTextView = createStyledTextView(veterinarian, 14f, R.color.brownspinnerchat, R.font.featherbold)
                        val diagnosisTextView = createStyledTextView(diagnosis, 14f, R.color.brownspinnerchat, R.font.featherbold)

                        // Add TextViews to the TableRow
                        newRow.addView(dateTextView)
                        newRow.addView(descriptionTextView)
                        newRow.addView(veterinarianTextView)
                        newRow.addView(diagnosisTextView)

                        // Add the TableRow to the TableLayout
                        dogRecordsTable.addView(newRow)

                        count++  // Increment row count
                    }
                } else {
                    // Show the tabrow if no records are found
                    tabrow.visibility = View.VISIBLE

                    // Handle case when no records are found
                    Toast.makeText(
                        this@dog_profilepageold,
                        "No dog records found",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(this@dog_profilepageold, "Failed to fetch dog records", Toast.LENGTH_SHORT).show()
            }
        })

        // View all dog records button
        val viewAllButton: Button = findViewById(R.id.btn_viewall)
        viewAllButton.setOnClickListener {
            val intent = Intent(this@dog_profilepageold, view_all_dogrecords::class.java)
            intent.putExtra("dogId", dogId)
            startActivity(intent)
            finish()
        }
    }




    private fun createStyledTextView(text: String, textSize: Float, textColorResId: Int, fontResId: Int): TextView {
        val row = TableRow(this@dog_profilepageold)
        val rowPadding = resources.getDimensionPixelSize(R.dimen.row_padding)
        row.setPadding(rowPadding, rowPadding, rowPadding, rowPadding)

        // Create a new TextView
        val textView = TextView(this@dog_profilepageold)

        // Set text and appearance
        textView.text = text
        textView.textSize = textSize
        textView.gravity = Gravity.CENTER
        textView.setTextColor(Color.parseColor("#584E3D"))
        textView.setBackgroundResource(R.drawable.tabcorneright)

        // Set custom font using font family resource
        val font: Typeface? = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            ResourcesCompat.getFont(this@dog_profilepageold, fontResId)
        } else {
            Typeface.create(ResourcesCompat.getFont(this@dog_profilepageold, fontResId), Typeface.NORMAL)
        }
        textView.typeface = font


        // Set maximum number of lines
        textView.maxLines = 100

        // Set width programmatically
        val layoutParams = TableRow.LayoutParams(
            resources.getDimensionPixelSize(R.dimen.text_view_width), // Use your desired width here (e.g., 85dp)
            TableRow.LayoutParams.MATCH_PARENT
        )
        textView.layoutParams = layoutParams

        // Set padding for better spacing
        textView.setPadding(16, 8, 16, 8)

        return textView
    }



    private fun deleteUserDog(userDatabase: DatabaseReference, userId: String, dogId: String) {
        val userDogsRef = userDatabase.child(userId).child("Dogs").child(dogId)
        userDogsRef.removeValue()
            .addOnSuccessListener {
                // Deletion successful, you can add any UI update or navigation here
                Toast.makeText(this@dog_profilepageold, "Dog profile deleted successfully", Toast.LENGTH_SHORT).show()
                finish() // Close the current activity after deletion
            }
            .addOnFailureListener { e ->
                // Handle any errors
                Toast.makeText(this@dog_profilepageold, "Failed to delete dog profile: ${e.message}", Toast.LENGTH_SHORT).show()
            }
    }

    private fun getDrawableResourceId(breed: String, fur: String, accessory: String): Int {
        // Return the resource ID of the drawable based on the breed, fur, and accessory
        return when {
            breed == "Golden Retriever" && fur == "Fur1" && accessory == "" -> R.drawable.golden1
            breed == "Golden Retriever" && fur == "Fur1" && accessory == "Collar1" -> R.drawable.golden1_collar1
            breed == "Golden Retriever" && fur == "Fur1" && accessory == "Collar2" -> R.drawable.golden1_collar2
            breed == "Golden Retriever" && fur == "Fur1" && accessory == "Scarf1" -> R.drawable.golden1_scarf1
            breed == "Golden Retriever" && fur == "Fur1" && accessory == "Scarf2" -> R.drawable.golden1_scarf2
            breed == "Golden Retriever" && fur == "Fur1" && accessory == "Ribbon1" -> R.drawable.golden1_ribbon1
            breed == "Golden Retriever" && fur == "Fur1" && accessory == "Ribbon2" -> R.drawable.golden1_ribbon2

            breed == "Golden Retriever" && fur == "Fur2" && accessory == "" -> R.drawable.golden2
            breed == "Golden Retriever" && fur == "Fur2" && accessory == "Collar1" -> R.drawable.golden2_collar1
            breed == "Golden Retriever" && fur == "Fur2" && accessory == "Collar2" -> R.drawable.golden2_collar2
            breed == "Golden Retriever" && fur == "Fur2" && accessory == "Scarf1" -> R.drawable.golden2_scarf1
            breed == "Golden Retriever" && fur == "Fur2" && accessory == "Scarf2" -> R.drawable.golden2_scarf2
            breed == "Golden Retriever" && fur == "Fur2" && accessory == "Ribbon1" -> R.drawable.golden2_ribbon1
            breed == "Golden Retriever" && fur == "Fur2" && accessory == "Ribbon2" -> R.drawable.golden2_ribbon2

            breed == "Golden Retriever" && fur == "Fur3" && accessory == "" -> R.drawable.golden3
            breed == "Golden Retriever" && fur == "Fur3" && accessory == "Collar1" -> R.drawable.golden3_collar1
            breed == "Golden Retriever" && fur == "Fur3" && accessory == "Collar2" -> R.drawable.golden3_collar2
            breed == "Golden Retriever" && fur == "Fur3" && accessory == "Scarf1" -> R.drawable.golden3_scarf1
            breed == "Golden Retriever" && fur == "Fur3" && accessory == "Scarf2" -> R.drawable.golden3_scarf2
            breed == "Golden Retriever" && fur == "Fur3" && accessory == "Ribbon1" -> R.drawable.golden3_ribbon1
            breed == "Golden Retriever" && fur == "Fur3" && accessory == "Ribbon2" -> R.drawable.golden3_ribbon2

            breed == "Golden Retriever" && fur == "Fur4" && accessory == "" -> R.drawable.golden4
            breed == "Golden Retriever" && fur == "Fur4" && accessory == "Collar1" -> R.drawable.golden4_collar1
            breed == "Golden Retriever" && fur == "Fur4" && accessory == "Collar2" -> R.drawable.golden4_collar2
            breed == "Golden Retriever" && fur == "Fur4" && accessory == "Scarf1" -> R.drawable.golden4_scarf1
            breed == "Golden Retriever" && fur == "Fur4" && accessory == "Scarf2" -> R.drawable.golden4_scarf2
            breed == "Golden Retriever" && fur == "Fur4" && accessory == "Ribbon1" -> R.drawable.golden4_ribbon1
            breed == "Golden Retriever" && fur == "Fur4" && accessory == "Ribbon2" -> R.drawable.golden4_ribbon2

            breed == "Golden Retriever" && fur == "Fur5" && accessory == "" -> R.drawable.golden5
            breed == "Golden Retriever" && fur == "Fur5" && accessory == "Collar1" -> R.drawable.golden5_collar1
            breed == "Golden Retriever" && fur == "Fur5" && accessory == "Collar2" -> R.drawable.golden5_collar2
            breed == "Golden Retriever" && fur == "Fur5" && accessory == "Scarf1" -> R.drawable.golden5_scarf1
            breed == "Golden Retriever" && fur == "Fur5" && accessory == "Scarf2" -> R.drawable.golden5_scarf2
            breed == "Golden Retriever" && fur == "Fur5" && accessory == "Ribbon1" -> R.drawable.golden5_ribbon1
            breed == "Golden Retriever" && fur == "Fur5" && accessory == "Ribbon2" -> R.drawable.golden5_ribbon2

            breed == "Golden Retriever" && fur == "Fur6" && accessory == "" -> R.drawable.golden6
            breed == "Golden Retriever" && fur == "Fur6" && accessory == "Collar1" -> R.drawable.golden6_collar1
            breed == "Golden Retriever" && fur == "Fur6" && accessory == "Collar2" -> R.drawable.golden6_collar2
            breed == "Golden Retriever" && fur == "Fur6" && accessory == "Scarf1" -> R.drawable.golden6_scarf1
            breed == "Golden Retriever" && fur == "Fur6" && accessory == "Scarf2" -> R.drawable.golden6_scarf2
            breed == "Golden Retriever" && fur == "Fur6" && accessory == "Ribbon1" -> R.drawable.golden6_ribbon1
            breed == "Golden Retriever" && fur == "Fur6" && accessory == "Ribbon2" -> R.drawable.golden6_ribbon2

            breed == "Shih Tzu" && fur == "Fur1" && accessory == "" -> R.drawable.shitzu7
            breed == "Shih Tzu" && fur == "Fur1" && accessory == "Collar1" -> R.drawable.shitzu7_collar1
            breed == "Shih Tzu" && fur == "Fur1" && accessory == "Collar2" -> R.drawable.shitzu7_collar2
            breed == "Shih Tzu" && fur == "Fur1" && accessory == "Scarf1" -> R.drawable.shitzu7_scarf1
            breed == "Shih Tzu" && fur == "Fur1" && accessory == "Scarf2" -> R.drawable.shitzu7_scarf2
            breed == "Shih Tzu" && fur == "Fur1" && accessory == "Ribbon1" -> R.drawable.shitzu7_ribbon1
            breed == "Shih Tzu" && fur == "Fur1" && accessory == "Ribbon2" -> R.drawable.shitzu7_ribbon2

            breed == "Shih Tzu" && fur == "Fur2" && accessory == "" -> R.drawable.shitzu5
            breed == "Shih Tzu" && fur == "Fur2" && accessory == "Collar1" -> R.drawable.shitzu5_collar1
            breed == "Shih Tzu" && fur == "Fur2" && accessory == "Collar2" -> R.drawable.shitzu5_collar2
            breed == "Shih Tzu" && fur == "Fur2" && accessory == "Scarf1" -> R.drawable.shitzu5_scarf1
            breed == "Shih Tzu" && fur == "Fur2" && accessory == "Scarf2" -> R.drawable.shitzu5_scarf2
            breed == "Shih Tzu" && fur == "Fur2" && accessory == "Ribbon1" -> R.drawable.shitzu5_ribbon1
            breed == "Shih Tzu" && fur == "Fur2" && accessory == "Ribbon2" -> R.drawable.shitzu5_ribbon2

            breed == "Shih Tzu" && fur == "Fur3" && accessory == "" -> R.drawable.shitzu6
            breed == "Shih Tzu" && fur == "Fur3" && accessory == "Collar1" -> R.drawable.shitzu6_collar1
            breed == "Shih Tzu" && fur == "Fur3" && accessory == "Collar2" -> R.drawable.shitzu6_collar2
            breed == "Shih Tzu" && fur == "Fur3" && accessory == "Scarf1" -> R.drawable.shitzu6_scarf1
            breed == "Shih Tzu" && fur == "Fur3" && accessory == "Scarf2" -> R.drawable.shitzu6_scarf2
            breed == "Shih Tzu" && fur == "Fur3" && accessory == "Ribbon1" -> R.drawable.shitzu6_ribbon1
            breed == "Shih Tzu" && fur == "Fur3" && accessory == "Ribbon2" -> R.drawable.shitzu6_ribbon2

            breed == "Shih Tzu" && fur == "Fur4" && accessory == "" -> R.drawable.shitzu1
            breed == "Shih Tzu" && fur == "Fur4" && accessory == "Collar1" -> R.drawable.shitzu1_collar1
            breed == "Shih Tzu" && fur == "Fur4" && accessory == "Collar2" -> R.drawable.shitzu1_collar2
            breed == "Shih Tzu" && fur == "Fur4" && accessory == "Scarf1" -> R.drawable.shitzu1_scarf1
            breed == "Shih Tzu" && fur == "Fur4" && accessory == "Scarf2" -> R.drawable.shitzu1_scarf2
            breed == "Shih Tzu" && fur == "Fur4" && accessory == "Ribbon1" -> R.drawable.shitzu1_ribbon1
            breed == "Shih Tzu" && fur == "Fur4" && accessory == "Ribbon2" -> R.drawable.shitzu1_ribbon2

            breed == "Shih Tzu" && fur == "Fur5" && accessory == "" -> R.drawable.shitzu3
            breed == "Shih Tzu" && fur == "Fur5" && accessory == "Collar1" -> R.drawable.shitzu3_collar1
            breed == "Shih Tzu" && fur == "Fur5" && accessory == "Collar2" -> R.drawable.shitzu3_collar2
            breed == "Shih Tzu" && fur == "Fur5" && accessory == "Scarf1" -> R.drawable.shitzu3_scarf1
            breed == "Shih Tzu" && fur == "Fur5" && accessory == "Scarf2" -> R.drawable.shitzu3_scarf2
            breed == "Shih Tzu" && fur == "Fur5" && accessory == "Ribbon1" -> R.drawable.shitzu3_ribbon1
            breed == "Shih Tzu" && fur == "Fur5" && accessory == "Ribbon2" -> R.drawable.shitzu3_ribbon2

            breed == "Shih Tzu" && fur == "Fur6" && accessory == "" -> R.drawable.shitzu2
            breed == "Shih Tzu" && fur == "Fur6" && accessory == "Collar1" -> R.drawable.shitzu2_collar1
            breed == "Shih Tzu" && fur == "Fur6" && accessory == "Collar2" -> R.drawable.shitzu2_collar2
            breed == "Shih Tzu" && fur == "Fur6" && accessory == "Scarf1" -> R.drawable.shitzu2_scarf1
            breed == "Shih Tzu" && fur == "Fur6" && accessory == "Scarf2" -> R.drawable.shitzu2_scarf2
            breed == "Shih Tzu" && fur == "Fur6" && accessory == "Ribbon1" -> R.drawable.shitzu2_ribbon1
            breed == "Shih Tzu" && fur == "Fur6" && accessory == "Ribbon2" -> R.drawable.shitzu2_ribbon2

            breed == "Shih Tzu" && fur == "Fur7" && accessory == "" -> R.drawable.shitzu4
            breed == "Shih Tzu" && fur == "Fur7" && accessory == "Collar1" -> R.drawable.shitzu4_collar1
            breed == "Shih Tzu" && fur == "Fur7" && accessory == "Collar2" -> R.drawable.shitzu4_collar2
            breed == "Shih Tzu" && fur == "Fur7" && accessory == "Scarf1" -> R.drawable.shitzu4_scarf1
            breed == "Shih Tzu" && fur == "Fur7" && accessory == "Scarf2" -> R.drawable.shitzu4_scarf2
            breed == "Shih Tzu" && fur == "Fur7" && accessory == "Ribbon1" -> R.drawable.shitzu4_ribbon1
            breed == "Shih Tzu" && fur == "Fur7" && accessory == "Ribbon2" -> R.drawable.shitzu4_ribbon2

            breed == "Chihuahua" && fur == "Fur1" && accessory == "" -> R.drawable.chihuahua5
            breed == "Chihuahua" && fur == "Fur1" && accessory == "Collar1" -> R.drawable.chihuahua5_collar1
            breed == "Chihuahua" && fur == "Fur1" && accessory == "Collar2" -> R.drawable.chihuahua5_collar2
            breed == "Chihuahua" && fur == "Fur1" && accessory == "Scarf1" -> R.drawable.chihuahua5_scarf1
            breed == "Chihuahua" && fur == "Fur1" && accessory == "Scarf2" -> R.drawable.chihuahua5_scarf2
            breed == "Chihuahua" && fur == "Fur1" && accessory == "Ribbon1" -> R.drawable.chihuahua5_ribbon1
            breed == "Chihuahua" && fur == "Fur1" && accessory == "Ribbon2" -> R.drawable.chihuahua5_ribbon2

            breed == "Chihuahua" && fur == "Fur2" && accessory == "" -> R.drawable.chihuahua1
            breed == "Chihuahua" && fur == "Fur2" && accessory == "Collar1" -> R.drawable.chihuahua1_collar1
            breed == "Chihuahua" && fur == "Fur2" && accessory == "Collar2" -> R.drawable.chihuahua1_collar2
            breed == "Chihuahua" && fur == "Fur2" && accessory == "Scarf1" -> R.drawable.chihuahua1_scarf1
            breed == "Chihuahua" && fur == "Fur2" && accessory == "Scarf2" -> R.drawable.chihuahua1_scarf2
            breed == "Chihuahua" && fur == "Fur2" && accessory == "Ribbon1" -> R.drawable.chihuahua1_ribbon1
            breed == "Chihuahua" && fur == "Fur2" && accessory == "Ribbon2" -> R.drawable.chihuahua1_ribbon2

            breed == "Chihuahua" && fur == "Fur3" && accessory == "" -> R.drawable.chihuahua3
            breed == "Chihuahua" && fur == "Fur3" && accessory == "Collar1" -> R.drawable.chihuahua3_collar1
            breed == "Chihuahua" && fur == "Fur3" && accessory == "Collar2" -> R.drawable.chihuahua3_collar2
            breed == "Chihuahua" && fur == "Fur3" && accessory == "Scarf1" -> R.drawable.chihuahua3_scarf1
            breed == "Chihuahua" && fur == "Fur3" && accessory == "Scarf2" -> R.drawable.chihuahua3_scarf2
            breed == "Chihuahua" && fur == "Fur3" && accessory == "Ribbon1" -> R.drawable.chihuahua3_ribbon1
            breed == "Chihuahua" && fur == "Fur3" && accessory == "Ribbon2" -> R.drawable.chihuahua3_ribbon2

            breed == "Chihuahua" && fur == "Fur4" && accessory == "" -> R.drawable.chihuahua6
            breed == "Chihuahua" && fur == "Fur4" && accessory == "Collar1" -> R.drawable.chihuahua7_collar1
            breed == "Chihuahua" && fur == "Fur4" && accessory == "Collar2" -> R.drawable.chihuahua7_collar2
            breed == "Chihuahua" && fur == "Fur4" && accessory == "Scarf1" -> R.drawable.chihuahua7_scarf1
            breed == "Chihuahua" && fur == "Fur4" && accessory == "Scarf2" -> R.drawable.chihuahua7_scarf2
            breed == "Chihuahua" && fur == "Fur4" && accessory == "Ribbon1" -> R.drawable.chihuahua7_ribbon1
            breed == "Chihuahua" && fur == "Fur4" && accessory == "Ribbon2" -> R.drawable.chihuahua7_ribbon2

            breed == "Chihuahua" && fur == "Fur5" && accessory == "" -> R.drawable.chihuahua4
            breed == "Chihuahua" && fur == "Fur5" && accessory == "Collar1" -> R.drawable.chihuahua4_collar1
            breed == "Chihuahua" && fur == "Fur5" && accessory == "Collar2" -> R.drawable.chihuahua4_collar2
            breed == "Chihuahua" && fur == "Fur5" && accessory == "Scarf1" -> R.drawable.chihuahua4_scarf1
            breed == "Chihuahua" && fur == "Fur5" && accessory == "Scarf2" -> R.drawable.chihuahua4_scarf2
            breed == "Chihuahua" && fur == "Fur5" && accessory == "Ribbon1" -> R.drawable.chihuahua4_ribbon1
            breed == "Chihuahua" && fur == "Fur5" && accessory == "Ribbon2" -> R.drawable.chihuahua4_ribbon2

            breed == "Chihuahua" && fur == "Fur6" && accessory == "" -> R.drawable.chihuahua2
            breed == "Chihuahua" && fur == "Fur6" && accessory == "Collar1" -> R.drawable.chihuahua2_collar1
            breed == "Chihuahua" && fur == "Fur6" && accessory == "Collar2" -> R.drawable.chihuahua2_collar2
            breed == "Chihuahua" && fur == "Fur6" && accessory == "Scarf1" -> R.drawable.chihuahua2_scarf1
            breed == "Chihuahua" && fur == "Fur6" && accessory == "Scarf2" -> R.drawable.chihuahua2_scarf2
            breed == "Chihuahua" && fur == "Fur6" && accessory == "Ribbon1" -> R.drawable.chihuahua2_ribbon1
            breed == "Chihuahua" && fur == "Fur6" && accessory == "Ribbon2" -> R.drawable.chihuahua2_ribbon2

            breed == "Pomeranian" && fur == "Fur1" && accessory == "" -> R.drawable.pomerenian4
            breed == "Pomeranian" && fur == "Fur1" && accessory == "Collar1" -> R.drawable.pomerenian4_collar1
            breed == "Pomeranian" && fur == "Fur1" && accessory == "Collar2" -> R.drawable.pomerenian4_collar2
            breed == "Pomeranian" && fur == "Fur1" && accessory == "Scarf1" -> R.drawable.pomerenian4_scarf1
            breed == "Pomeranian" && fur == "Fur1" && accessory == "Scarf2" -> R.drawable.pomerenian4_scarf2
            breed == "Pomeranian" && fur == "Fur1" && accessory == "Ribbon1" -> R.drawable.pomerenian4_ribbon1
            breed == "Pomeranian" && fur == "Fur1" && accessory == "Ribbon2" -> R.drawable.pomerenian4_ribbon2

            breed == "Pomeranian" && fur == "Fur2" && accessory == "" -> R.drawable.pomeranian1fb
            breed == "Pomeranian" && fur == "Fur2" && accessory == "Collar1" -> R.drawable.pomerenian1_collar1
            breed == "Pomeranian" && fur == "Fur2" && accessory == "Collar2" -> R.drawable.pomerenian1_collar2
            breed == "Pomeranian" && fur == "Fur2" && accessory == "Scarf1" -> R.drawable.pomerenian1_scarf1
            breed == "Pomeranian" && fur == "Fur2" && accessory == "Scarf2" -> R.drawable.pomerenian1_scarf2
            breed == "Pomeranian" && fur == "Fur2" && accessory == "Ribbon1" -> R.drawable.pomerenian1_ribbon1
            breed == "Pomeranian" && fur == "Fur2" && accessory == "Ribbon2" -> R.drawable.pomerenian1_ribbon2

            breed == "Pomeranian" && fur == "Fur3" && accessory == "" -> R.drawable.pomerenian5
            breed == "Pomeranian" && fur == "Fur3" && accessory == "Collar1" -> R.drawable.pomerenian5_collar1
            breed == "Pomeranian" && fur == "Fur3" && accessory == "Collar2" -> R.drawable.pomerenian5_collar2
            breed == "Pomeranian" && fur == "Fur3" && accessory == "Scarf1" -> R.drawable.pomerenian5_scarf1
            breed == "Pomeranian" && fur == "Fur3" && accessory == "Scarf2" -> R.drawable.pomerenian5_scarf2
            breed == "Pomeranian" && fur == "Fur3" && accessory == "Ribbon1" -> R.drawable.pomerenian5_ribbon1
            breed == "Pomeranian" && fur == "Fur3" && accessory == "Ribbon2" -> R.drawable.pomerenian5_ribbon2

            breed == "Pomeranian" && fur == "Fur4" && accessory == "" -> R.drawable.pomerenian2
            breed == "Pomeranian" && fur == "Fur4" && accessory == "Collar1" -> R.drawable.pomerenian2_collar1
            breed == "Pomeranian" && fur == "Fur4" && accessory == "Collar2" -> R.drawable.pomerenian2_collar2
            breed == "Pomeranian" && fur == "Fur4" && accessory == "Scarf1" -> R.drawable.pomerenian2_scarf1
            breed == "Pomeranian" && fur == "Fur4" && accessory == "Scarf2" -> R.drawable.pomerenian2_scarf2
            breed == "Pomeranian" && fur == "Fur4" && accessory == "Ribbon1" -> R.drawable.pomerenian2_ribbon1
            breed == "Pomeranian" && fur == "Fur4" && accessory == "Ribbon2" -> R.drawable.pomerenian2_ribbon2

            breed == "Pomeranian" && fur == "Fur5" && accessory == "" -> R.drawable.pomerenian6fb
            breed == "Pomeranian" && fur == "Fur5" && accessory == "Collar1" -> R.drawable.pomerenian6_collar1
            breed == "Pomeranian" && fur == "Fur5" && accessory == "Collar2" -> R.drawable.pomerenian6_collar2
            breed == "Pomeranian" && fur == "Fur5" && accessory == "Scarf1" -> R.drawable.pomerenian6_scarf1
            breed == "Pomeranian" && fur == "Fur5" && accessory == "Scarf2" -> R.drawable.pomerenian6_scarf2
            breed == "Pomeranian" && fur == "Fur5" && accessory == "Ribbon1" -> R.drawable.pomerenian6_ribbon1
            breed == "Pomeranian" && fur == "Fur5" && accessory == "Ribbon2" -> R.drawable.pomerenian6_ribbon2

            breed == "Pomeranian" && fur == "Fur6" && accessory == "" -> R.drawable.pomerenian3
            breed == "Pomeranian" && fur == "Fur6" && accessory == "Collar1" -> R.drawable.pomerenian3_collar1
            breed == "Pomeranian" && fur == "Fur6" && accessory == "Collar2" -> R.drawable.pomerenian3_collar2
            breed == "Pomeranian" && fur == "Fur6" && accessory == "Scarf1" -> R.drawable.pomerenian3_scarf1
            breed == "Pomeranian" && fur == "Fur6" && accessory == "Scarf2" -> R.drawable.pomerenian3_scarf2
            breed == "Pomeranian" && fur == "Fur6" && accessory == "Ribbon1" -> R.drawable.pomerenian3_ribbon1
            breed == "Pomeranian" && fur == "Fur6" && accessory == "Ribbon2" -> R.drawable.pomerenian3_ribbon2

            breed == "Poodle" && fur == "Fur1" && accessory == "" -> R.drawable.poodle5
            breed == "Poodle" && fur == "Fur1" && accessory == "Collar1" -> R.drawable.poodle5_collar1
            breed == "Poodle" && fur == "Fur1" && accessory == "Collar2" -> R.drawable.poodle5_collar2
            breed == "Poodle" && fur == "Fur1" && accessory == "Scarf1" -> R.drawable.poodle5_scarf1
            breed == "Poodle" && fur == "Fur1" && accessory == "Scarf2" -> R.drawable.poodle5_scarf2
            breed == "Poodle" && fur == "Fur1" && accessory == "Ribbon1" -> R.drawable.poodle5_ribbon1
            breed == "Poodle" && fur == "Fur1" && accessory == "Ribbon2" -> R.drawable.poodle5_ribbon2

            breed == "Poodle" && fur == "Fur2" && accessory == "" -> R.drawable.poodle4
            breed == "Poodle" && fur == "Fur2" && accessory == "Collar1" -> R.drawable.poodle4_collar1
            breed == "Poodle" && fur == "Fur2" && accessory == "Collar2" -> R.drawable.poodle4_collar2
            breed == "Poodle" && fur == "Fur2" && accessory == "Scarf1" -> R.drawable.poodle4_scarf1
            breed == "Poodle" && fur == "Fur2" && accessory == "Scarf2" -> R.drawable.poodle4_scarf2
            breed == "Poodle" && fur == "Fur2" && accessory == "Ribbon1" -> R.drawable.poodle4_ribbon1
            breed == "Poodle" && fur == "Fur2" && accessory == "Ribbon2" -> R.drawable.poodle4_ribbon2

            breed == "Poodle" && fur == "Fur3" && accessory == "" -> R.drawable.poodle6
            breed == "Poodle" && fur == "Fur3" && accessory == "Collar1" -> R.drawable.poodle6_collar1
            breed == "Poodle" && fur == "Fur3" && accessory == "Collar2" -> R.drawable.poodle6_collar2
            breed == "Poodle" && fur == "Fur3" && accessory == "Scarf1" -> R.drawable.poodle6_scarf1
            breed == "Poodle" && fur == "Fur3" && accessory == "Scarf2" -> R.drawable.poodle6_scarf2
            breed == "Poodle" && fur == "Fur3" && accessory == "Ribbon1" -> R.drawable.poodle6_ribbon1
            breed == "Poodle" && fur == "Fur3" && accessory == "Ribbon2" -> R.drawable.poodle6_ribbon2

            breed == "Poodle" && fur == "Fur4" && accessory == "" -> R.drawable.poodle2fb
            breed == "Poodle" && fur == "Fur4" && accessory == "Collar1" -> R.drawable.poodle2_collar1
            breed == "Poodle" && fur == "Fur4" && accessory == "Collar2" -> R.drawable.poodle2_collar2
            breed == "Poodle" && fur == "Fur4" && accessory == "Scarf1" -> R.drawable.poodle2_scarf1
            breed == "Poodle" && fur == "Fur4" && accessory == "Scarf2" -> R.drawable.poodle2_scarf2
            breed == "Poodle" && fur == "Fur4" && accessory == "Ribbon1" -> R.drawable.poodle2_ribbon1
            breed == "Poodle" && fur == "Fur4" && accessory == "Ribbon2" -> R.drawable.poodle2_ribbon2

            breed == "Poodle" && fur == "Fur5" && accessory == "" -> R.drawable.poodle3
            breed == "Poodle" && fur == "Fur5" && accessory == "Collar1" -> R.drawable.poodle3_collar1
            breed == "Poodle" && fur == "Fur5" && accessory == "Collar2" -> R.drawable.poodle3_collar2
            breed == "Poodle" && fur == "Fur5" && accessory == "Scarf1" -> R.drawable.poodle3_scarf1
            breed == "Poodle" && fur == "Fur5" && accessory == "Scarf2" -> R.drawable.poodle3_scarf2
            breed == "Poodle" && fur == "Fur5" && accessory == "Ribbon1" -> R.drawable.poodle3_ribbon1
            breed == "Poodle" && fur == "Fur5" && accessory == "Ribbon2" -> R.drawable.poodle3_ribbon2

            breed == "Poodle" && fur == "Fur6" && accessory == "" -> R.drawable.poodle1fb
            breed == "Poodle" && fur == "Fur6" && accessory == "Collar1" -> R.drawable.poodle1_collar1
            breed == "Poodle" && fur == "Fur6" && accessory == "Collar2" -> R.drawable.poodle1_collar2
            breed == "Poodle" && fur == "Fur6" && accessory == "Scarf1" -> R.drawable.poodle1_scarf1
            breed == "Poodle" && fur == "Fur6" && accessory == "Scarf2" -> R.drawable.poodle1_scarf2
            breed == "Poodle" && fur == "Fur6" && accessory == "Ribbon1" -> R.drawable.poodle1_ribbon1
            breed == "Poodle" && fur == "Fur6" && accessory == "Ribbon2" -> R.drawable.poodle1_ribbon2

            // Add more conditions for other combinations
            else -> R.drawable.bgbrown // Default image in case no specific combination matches
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
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right)
    }
}
