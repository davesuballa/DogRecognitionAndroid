package com.example.buddy
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.budiyev.android.codescanner.*
import android.widget.Toast
import android.content.ClipboardManager
import android.content.Context
import android.content.ClipData
import android.content.Intent
import android.util.Log
import androidx.core.app.ActivityOptionsCompat
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ServerValue
import com.google.firebase.database.ValueEventListener

class ScannerActivity : AppCompatActivity() {
    private lateinit var codeScanner: CodeScanner
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.code_scanner)

        auth = FirebaseAuth.getInstance()

        val scannerView = findViewById<CodeScannerView>(R.id.scanner_view)

        codeScanner = CodeScanner(this, scannerView)

        codeScanner.camera = CodeScanner.CAMERA_BACK // or CAMERA_FRONT or specific camera id
        codeScanner.formats = CodeScanner.ALL_FORMATS // list of type BarcodeFormat,
        // ex. listOf(BarcodeFormat.QR_CODE)
        codeScanner.autoFocusMode = AutoFocusMode.SAFE // or CONTINUOUS
        codeScanner.scanMode = ScanMode.SINGLE // or CONTINUOUS or PREVIEW
        codeScanner.isAutoFocusEnabled = true // Whether to enable auto focus or not
        codeScanner.isFlashEnabled = false // Whether to enable flash or not

        // Callbacks
        codeScanner.decodeCallback = DecodeCallback { result ->
            runOnUiThread {
                val scanResult = result.text
                Toast.makeText(this, "Scan result: $scanResult", Toast.LENGTH_LONG).show()

                // Parse the scan result to extract dog information
                val scannedData = scanResult.split("\n")

                // Ensure scannedData has at least 8 elements (minimum required for basic data)
                if (scannedData.size <= 14) {
                    val breed = scannedData.getOrNull(0) ?: ""
                    val age = scannedData.getOrNull(1) ?: ""
                    val name = scannedData.getOrNull(2) ?: ""
                    val weight = scannedData.getOrNull(3) ?: ""
                    val bday = scannedData.getOrNull(4) ?: ""
                    val gender = scannedData.getOrNull(5) ?: ""
                    val fur = scannedData.getOrNull(6) ?: ""
                    val accessory = scannedData.getOrNull(7) ?: ""

                    val userCurrentID = auth.currentUser?.uid
                    if (userCurrentID != null) {
                        val userRef = FirebaseDatabase.getInstance().getReference("Users").child(userCurrentID)
                        userRef.child("ScannedCodes").addListenerForSingleValueEvent(object : ValueEventListener {
                            override fun onDataChange(dataSnapshot: DataSnapshot) {
                                val codes = dataSnapshot.children.mapNotNull { it.getValue(String::class.java) }
                                if (codes.contains(scanResult)) {
                                    Toast.makeText(this@ScannerActivity, "QR code already scanned!", Toast.LENGTH_SHORT).show()
                                    // Handle code already scanned
                                } else {
                                    val dogData = mapOf(
                                        "breedName" to breed,
                                        "age" to age,
                                        "dogName" to name,
                                        "weight" to weight,
                                        "birthday" to bday,
                                        "gender" to gender,
                                        "furColor" to fur,
                                        "accessory" to accessory
                                    )

                                    // Push dog data under "Dogs" and retrieve the key (dogId)
                                    val newDogRef = userRef.child("Dogs").push()
                                    val dogId = newDogRef.key ?: ""
                                    newDogRef.setValue(dogData)

                                    if (dogId.isNotBlank()) {
                                        // Optionally handle additional data if available
                                        val date = scannedData.getOrNull(8) ?: ""
                                        val description = scannedData.getOrNull(9) ?: ""
                                        val vet = scannedData.getOrNull(10) ?: ""
                                        val diagnosis = scannedData.getOrNull(11) ?: ""

                                        // Prepare dog record data if available
                                        val dogRecordData = mapOf(
                                            "date" to date,
                                            "description" to description,
                                            "diagnosis" to diagnosis,
                                            "veterinarian" to vet,
                                            "timestamp" to ServerValue.TIMESTAMP
                                        )

                                        // Push dog record under the "DogRecords" child using the dogId
                                        val dogRecordsRef = newDogRef.child("DogRecords").push()
                                        dogRecordsRef.setValue(dogRecordData)

                                        // Add scan result to scanned codes
                                        userRef.child("ScannedCodes").push().setValue(scanResult)

                                        // Start new activity
                                        val intent = Intent(this@ScannerActivity, homepageold::class.java)
                                        startActivity(intent)
                                        finish()
                                    } else {
                                        Toast.makeText(this@ScannerActivity, "Failed to generate dog ID", Toast.LENGTH_SHORT).show()
                                    }
                                }
                            }

                            override fun onCancelled(databaseError: DatabaseError) {
                                val errorMessage = "Database error! ${databaseError.message}"
                                Toast.makeText(this@ScannerActivity, errorMessage, Toast.LENGTH_SHORT).show()
                                Log.e("FirebaseError", errorMessage)
                            }
                        })
                    } else {
                        Toast.makeText(this@ScannerActivity, "Connect to the internet", Toast.LENGTH_SHORT).show()
                    }
                } else {
                    Toast.makeText(this@ScannerActivity, "Invalid QR code format", Toast.LENGTH_SHORT).show()
                }
            }
        }

        codeScanner.errorCallback = ErrorCallback { // or ErrorCallback.SUPPRESS
            runOnUiThread {
                Toast.makeText(this, "Camera initialization error: ${it.message}", Toast.LENGTH_LONG).show()
            }
        }

        scannerView.setOnClickListener {
            codeScanner.startPreview()
        }
    }

    override fun onResume() {
        super.onResume()
        codeScanner.startPreview()
    }

    override fun onPause() {
        codeScanner.releaseResources()
        super.onPause()
    }
    @Suppress("DEPRECATION")
    override fun onBackPressed() {
        val dogId = intent.getStringExtra("dogId")
        val isFromQRCodePage = intent.getBooleanExtra("fromQRCodePage", false)

        // Define the destination activity based on where the user comes from
        val destinationClass = if (isFromQRCodePage) dog_profilepageold::class.java else add_new_dog::class.java

        // Start the appropriate activity
        val intent = Intent(this, destinationClass)
        if (isFromQRCodePage) {
            intent.putExtra("dogId", dogId)
            startActivity(intent)

            // Finish the current activity to trigger the animation
            finish()
        } else {
            val intent = Intent(this, add_new_dog::class.java)
            startActivity(intent)
            finish()
        }

        // Define the animation to use after calling finish()
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right)
    }



}