package com.example.buddy

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
import android.widget.CheckBox
import android.widget.TableLayout
import android.widget.TableRow
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityOptionsCompat
import androidx.core.content.res.ResourcesCompat
import com.google.android.material.tabs.TabItem
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
class view_all_dogrecords : AppCompatActivity() {
    private lateinit var tableLayoutRecords: TableLayout
    private lateinit var saveButton: Button
    private lateinit var  tabrow : TableRow
    private var isDeleteEnabled = false

    private lateinit var userDatabase: DatabaseReference
    private lateinit var auth: FirebaseAuth
    private lateinit var dogId: String // Declare dogId as a property
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_all_dogrecords)

        // Initialize Firebase references
        userDatabase = FirebaseDatabase.getInstance().getReference("Users")
        auth = FirebaseAuth.getInstance()
        val userId = auth.currentUser?.uid

        // Find views
        tableLayoutRecords = findViewById(R.id.tblayout)
        tabrow = findViewById(R.id.tablerow)

        dogId = intent.getStringExtra("dogId") ?: ""


        // Back button leads back to the homepage
        val backbtn : Button = findViewById(R.id.backbtn)
        backbtn.setOnClickListener{
            val intent = Intent(this, dog_profilepageold::class.java)
            intent.putExtra("dogId", dogId)
            val options = ActivityOptionsCompat.makeCustomAnimation(
                this@view_all_dogrecords,
                R.anim.slide_in_left,   // Animation for the new activity (slide up)
                R.anim.slide_out_right   // Animation for the current activity (slide down)
            )

            // Start the activity with the specified animation options
            startActivity(intent, options.toBundle())
            finish()
        }

        if (userId != null) {
            val userDogRecordsRef = userDatabase.child(userId).child("Dogs").child(dogId).child("DogRecords")
            userDogRecordsRef.addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    if (snapshot.exists()) {
                        // Remove the tabrow if there are existing records
                        tabrow.visibility = View.GONE

                        for (recordSnapshot in snapshot.children) {
                            // Get the unique record ID
                            val recordId = recordSnapshot.key ?: ""

                            // Retrieve individual record details
                            val date = recordSnapshot.child("date").getValue(String::class.java) ?: ""
                            val description = recordSnapshot.child("description").getValue(String::class.java) ?: ""
                            val veterinarian = recordSnapshot.child("veterinarian").getValue(String::class.java) ?: ""
                            val diagnosis = recordSnapshot.child("diagnosis").getValue(String::class.java) ?: ""

                            // Create a new TableRow for each record
                            val row = createTableRow(recordId, date, description, veterinarian, diagnosis)

                            // Add the TableRow to the TableLayout
                            tableLayoutRecords.addView(row)
                        }
                    } else {
                        // Show the tabrow if no records are found
                        tabrow.visibility = View.VISIBLE

                        // Handle case when no records are found
                        Toast.makeText(
                            this@view_all_dogrecords,
                            "No dog records found",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }


                override fun onCancelled(error: DatabaseError) {
                    // Handle database error
                    Toast.makeText(
                        this@view_all_dogrecords,
                        "Failed to fetch dog records",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            })
        }
    }

    private fun createTableRow(recordId: String, date: String, description: String, veterinarian: String, diagnosis: String): TableRow {
        val row = TableRow(this@view_all_dogrecords)
        // Ensure TableRow is clickable
        row.isClickable = true
        row.setOnClickListener {
            // Show confirmation dialog to delete the record
            showDeleteConfirmationDialog(recordId,date,description,veterinarian,diagnosis)
        }
        // Create TextViews for each field using custom style
        val dateTextView = createStyledTextView(date, 15f, R.color.brownspinnerchat, R.font.featherbold)
        val descriptionTextView = createStyledTextView(description, 15f, R.color.brownspinnerchat, R.font.featherbold)
        val veterinarianTextView = createStyledTextView(veterinarian, 15f, R.color.brownspinnerchat, R.font.featherbold)
        val diagnosisTextView = createStyledTextView(diagnosis, 15f, R.color.brownspinnerchat, R.font.featherbold)


        // Add TextViews to the TableRow
        row.addView(dateTextView)
        row.addView(descriptionTextView)
        row.addView(veterinarianTextView)
        row.addView(diagnosisTextView)



        return row
    }

        private fun createStyledTextView(text: String, textSize: Float, textColorResId: Int, fontResId: Int): TextView {
            val row = TableRow(this@view_all_dogrecords)
            val rowPadding = resources.getDimensionPixelSize(R.dimen.row_padding)
            row.setPadding(rowPadding, rowPadding, rowPadding, rowPadding)

            // Create a new TextView
            val textView = TextView(this@view_all_dogrecords)

            // Set text and appearance
            textView.text = text
            textView.textSize = textSize
            textView.gravity = Gravity.CENTER
            textView.setTextColor(Color.parseColor("#584E3D"))
            textView.setBackgroundResource(R.drawable.tabcorneright)

            // Set custom font using font family resource
            val font: Typeface? = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                ResourcesCompat.getFont(this@view_all_dogrecords, fontResId)
            } else {
                Typeface.create(ResourcesCompat.getFont(this@view_all_dogrecords, fontResId), Typeface.NORMAL)
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


    private fun showDeleteConfirmationDialog(recordId: String, date: String, description: String, veterinarian: String, diagnosis: String) {
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Confirm Deletion")

        // Construct the message with details of the record
        val message = "Are you sure you want to delete this dog record?\n\n" +
                "Date: $date\n" +
                "Description: $description\n" +
                "Veterinarian: $veterinarian\n" +
                "Diagnosis: $diagnosis"

        builder.setMessage(message)

        // Set up buttons for dialog
        builder.setPositiveButton("Confirm") { dialog, which ->
            // Perform deletion of the record (implement this logic)
            deleteDogRecord(recordId)
        }
        builder.setNegativeButton("Cancel") { dialog, which ->
            // Do nothing, simply dismiss the dialog
            dialog.dismiss()
        }

        // Display the dialog
        builder.show()
    }

    // Function to delete the dog record from Firebase
    private fun deleteDogRecord(recordId: String) {
        val userId = auth.currentUser?.uid
        if (userId != null) {
            val userDogRecordRef =
                userDatabase.child(userId).child("Dogs").child(this.dogId).child("DogRecords")
                    .child(recordId)
            userDogRecordRef.removeValue()
                .addOnSuccessListener {
                    Toast.makeText(this@view_all_dogrecords, "Dog record deleted successfully", Toast.LENGTH_SHORT).show()
                    // Refresh UI if needed
                    recreate() // Example: Recreate the activity to reflect changes
                }
                .addOnFailureListener { Toast.makeText(this@view_all_dogrecords, "Failed to delete dog record", Toast.LENGTH_SHORT).show()
                }
        }
    }

    @Suppress("DEPRECATION")
    override fun onBackPressed() {

        // Retrieve dog ID from the intent
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



