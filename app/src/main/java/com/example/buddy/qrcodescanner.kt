package com.example.buddy

import android.app.AlertDialog
import android.content.Intent
import android.graphics.Bitmap
import android.os.Bundle
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import android.graphics.Color
import android.util.Log
import android.widget.Button
import android.widget.TextView
import androidx.core.app.ActivityOptionsCompat
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.zxing.BarcodeFormat
import com.google.zxing.WriterException
import com.google.zxing.qrcode.QRCodeWriter


class qrcodescanner : AppCompatActivity() {

    private lateinit var imageView: ImageView
    private lateinit var userDatabase: DatabaseReference
    private lateinit var auth: FirebaseAuth


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.qrcode)

        imageView = findViewById(R.id.imageView)
        userDatabase = FirebaseDatabase.getInstance().getReference("Users")
        auth = FirebaseAuth.getInstance()

        val dogId = intent.getStringExtra("dogId")
        if (dogId != null) {
            fetchDogData(dogId)
        } else {
            Toast.makeText(this, "Failed to fetch dog data", Toast.LENGTH_SHORT).show()
            finish()
        }
        val backbtn = findViewById<Button>(R.id.backbtn)
        backbtn.setOnClickListener {
            val intent = Intent(this, dog_profilepageold::class.java)
            intent.putExtra("dogId", dogId)
            val options = ActivityOptionsCompat.makeCustomAnimation(
                this,
                R.anim.slide_in_left, // Animation for the new activity
                R.anim.slide_out_right // No animation for the current activity
            )
            // Start the activity with the specified animation options
            startActivity(intent, options.toBundle())
            finish()
        }

        val scanner = findViewById<Button>(R.id.showqr)
        scanner.setOnClickListener {
            val intent = Intent(this, ScannerActivity::class.java)
            intent.putExtra("fromQRCodePage", true)
            intent.putExtra("dogId", dogId)
            val options = ActivityOptionsCompat.makeCustomAnimation(
                this,
                R.anim.slide_in_left, // Animation for the new activity
                R.anim.slide_out_right // No animation for the current activity
            )
            // Start the activity with the specified animation options
            startActivity(intent, options.toBundle())
            finish()
        }
    }

    private fun fetchDogData(dogId: String) {
        val userId = auth.currentUser?.uid
        if (userId != null) {
            val dogRef = userDatabase.child(userId).child("Dogs").child(dogId)
            dogRef.addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    if (snapshot.exists()) {
                        // Extract dog's basic details
                        val breed = snapshot.child("breedName").getValue(String::class.java) ?: ""
                        val age = snapshot.child("age").getValue(String::class.java) ?: ""
                        val name = snapshot.child("dogName").getValue(String::class.java) ?: ""
                        val nametext = findViewById<TextView>(R.id.text2)
                        nametext.text = name
                        val weight = snapshot.child("weight").getValue(String::class.java) ?: ""
                        val bday = snapshot.child("birthday").getValue(String::class.java) ?: ""
                        val gender = snapshot.child("gender").getValue(String::class.java) ?: ""
                        val fur = snapshot.child("furColor").getValue(String::class.java) ?: ""
                        val accessory = snapshot.child("accessory").getValue(String::class.java) ?: ""

                        // Initialize a string builder to accumulate data
                        val dataBuilder = StringBuilder()

                        // Append dog's basic details to the string builder
                        dataBuilder.append("$breed\n$age\n$name\n$weight\n$bday\n$gender\n$fur\n$accessory")

                        // Iterate through each record under DogRecords
                        for (recordSnapshot in snapshot.child("DogRecords").children) {
                            val date = recordSnapshot.child("date").getValue(String::class.java) ?: ""
                            val description = recordSnapshot.child("description").getValue(String::class.java) ?: ""
                            val diagnosis = recordSnapshot.child("diagnosis").getValue(String::class.java) ?: ""
                            val veterinarian = recordSnapshot.child("veterinarian").getValue(String::class.java) ?: ""
                            val timestampLong = recordSnapshot.child("timestamp").getValue(Long::class.java) ?: 0L
                            val timestamp = timestampLong.toString()  // Convert Long to String


                            // Append each record's details to the string builder
                            dataBuilder.append("$date\n$description\n$diagnosis\n$veterinarian\n$timestamp")
                        }

                        // Generate QR code using the accumulated data
                        generateQRCode(dataBuilder.toString())

                    } else {
                        Toast.makeText(this@qrcodescanner, "Dog data not found or Data is Incomplete", Toast.LENGTH_SHORT).show()
                        finish()
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                    // Log error
                    Log.e("FetchDogData", "Failed to fetch dog data: ${error.message}")
                    // Display error message
                    Toast.makeText(this@qrcodescanner, "Failed to fetch dog data", Toast.LENGTH_SHORT).show()
                    finish()
                }
            })
        }
    }


    private fun generateQRCode(data: String) {
        val writer = QRCodeWriter()
        try {
            val bitMatrix = writer.encode(data, BarcodeFormat.QR_CODE, 512, 512)
            val width = bitMatrix.width
            val height = bitMatrix.height
            val bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.RGB_565)
            for (x in 0 until width) {
                for (y in 0 until height) {
                    val color = if (bitMatrix.get(x, y)) Color.BLACK else Color.WHITE
                    bitmap.setPixel(x, y, color)
                }
            }
            imageView.setImageBitmap(bitmap)
        } catch (e: WriterException) {
            e.printStackTrace()
            Toast.makeText(this, "Failed to generate QR code", Toast.LENGTH_SHORT).show()
            finish()
        }
    }

    @Suppress("DEPRECATION")
    override fun onBackPressed() {
        val dogId = intent.getStringExtra("dogId")
        val intent = Intent(this, dog_profilepageold::class.java)
        intent.putExtra("dogId", dogId)
        startActivity(intent)

        // Finish the current activity to trigger the animation
        finish()

        // Define the animation to use after calling finish()
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right)
    }
}
