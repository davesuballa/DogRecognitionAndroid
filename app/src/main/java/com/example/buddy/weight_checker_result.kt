package com.example.buddy

import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.activity.ComponentActivity
import androidx.core.app.ActivityOptionsCompat

class weight_checker_result : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_weight_checker_result)

        // Get extras from intent
        val selectedBreed = intent.getStringExtra("selectedBreed")
        val bmi = intent.getDoubleExtra("bmi", Double.NaN)

        // Calculate BMI category and description
        val (bmiCategory, bmiDescription) = getBMICategory(selectedBreed ?: "", bmi)

        // Display BMI category and description
        val bmiCategoryTextView = findViewById<TextView>(R.id.weightresult)
        bmiCategoryTextView.text = bmiCategory

        val backbtn = findViewById<Button>(R.id.go_back_button)
        backbtn.setOnClickListener {
            val intent = Intent(this, healthtoolspage::class.java)

            // Create options for the animation
            val options = ActivityOptionsCompat.makeCustomAnimation(
                this,
                R.anim.dissolve_in, // Animation for the new activity
                R.anim.dissolve_out // No animation for the current activity
            )

            // Start the activity with the specified animation options
            startActivity(intent, options.toBundle())
            finish()
        }

        val bmiDescriptionTextView = findViewById<TextView>(R.id.weightdescription)
        bmiDescriptionTextView.text = bmiDescription

        // Format and display BMI value
        val formattedBMI = if (bmi == 0.0) {
            "Unidentified"
        } else {
            String.format("%.2f", bmi)
        }
        val bmiValueTextView = findViewById<TextView>(R.id.bmitext)
        bmiValueTextView.text = "YOUR DOG's BMI: $formattedBMI"
    }

    // Function to determine BMI category and description based on breed and BMI value
    private fun getBMICategory(breed: String, bmi: Double): Pair<String, String> {
        return if (bmi.isNaN()) {
            "Unidentified" to "Oh no! It seems like the height and weight of your dog does not match up!"
        } else {
            when (breed) {
                "Golden Retriever" -> {
                    when {
                        bmi <= 2.82 -> "Underweight" to "It seems like your pet needs to adjust their nutrition, or they can be underweight due to an unidentified medical condition. This may be equally as unhealthy as a fat or overweight dog. Schedule a visit with your veterinarian to assess your pet's physical health and create a strategy to help them get back on track."
                        bmi <= 3.26 -> "Ideal weight" to "It looks like your pet is the perfect weight. Naturally, you should still speak with a veterinarian to find out your pet's condition and make sure their food is suitable for their age and way of life. You can also get advice from your veterinarian on how to keep your pet at a healthy weight."
                        else -> "Overweight" to "Regretfully, it seems that Your pet would benefit from losing a little weight. Dogs that are overweight are more likely to develop diabetes and arthritis, among other conditions. Schedule a visit with your veterinarian to assess your pet's physical health and create a strategy to help them get back on track."
                    }
                }

                "Chihuahua" -> {
                    when {
                        bmi <= 0.75 -> "Underweight" to "It seems like your pet needs to adjust their nutrition, or they can be underweight due to an unidentified medical condition. This may be equally as unhealthy as a fat or overweight dog. Schedule a visit with your veterinarian to assess your pet's physical health and create a strategy to help them get back on track."
                        bmi <= 1.21 -> "Ideal weight" to "It looks like your pet is the perfect weight. Naturally, you should still speak with a veterinarian to find out your pet's condition and make sure their food is suitable for their age and way of life. You can also get advice from your veterinarian on how to keep your pet at a healthy weight."
                        else -> "Overweight" to "Regretfully, it seems that your pet would benefit from losing a little weight. Dogs that are overweight are more likely to develop diabetes and arthritis, among other conditions. Schedule a visit with your veterinarian to assess your pet's physical health and create a strategy to help them get back on track."
                    }
                }

                "Shih Tzu" -> {
                    when {
                        bmi <= 0.99 -> "Underweight" to "It seems like your pet needs to adjust their nutrition, or they can be underweight due to an unidentified medical condition. This may be equally as unhealthy as a fat or overweight dog. Schedule a visit with your veterinarian to assess your pet's physical health and create a strategy to help them get back on track."
                        bmi <= 1.78 -> "Ideal weight" to "It looks like your pet is the perfect weight. Naturally, you should still speak with a veterinarian to find out your pet's condition and make sure their food is suitable for their age and way of life. You can also get advice from your veterinarian on how to keep your pet at a healthy weight."
                        else -> "Overweight" to "Regretfully, it seems that your pet would benefit from losing a little weight. Dogs that are overweight are more likely to develop diabetes and arthritis, among other conditions. Schedule a visit with your veterinarian to assess your pet's physical health and create a strategy to help them get back on track."
                    }
                }

                "Toy Poodle" -> {
                    when {
                        bmi <= 0.49 -> "Underweight" to "It seems like your pet needs to adjust their nutrition, or they can be underweight due to an unidentified medical condition. This may be equally as unhealthy as a fat or overweight dog. Schedule a visit with your veterinarian to assess your pet's physical health and create a strategy to help them get back on track."
                        bmi <= 0.75 -> "Ideal weight" to "It looks like your pet is the perfect weight. Naturally, you should still speak with a veterinarian to find out your pet's condition and make sure their food is suitable for their age and way of life. You can also get advice from your veterinarian on how to keep your pet at a healthy weight."
                        else -> "Overweight" to "Regretfully, it seems that your pet would benefit from losing a little weight. Dogs that are overweight are more likely to develop diabetes and arthritis, among other conditions. Schedule a visit with your veterinarian to assess your pet's physical health and create a strategy to help them get back on track."
                    }
                }

                "Standard Poodle" -> {
                    when {
                        bmi <= 3.99 -> "Underweight" to "It seems like your pet needs to adjust their nutrition, or they can be underweight due to an unidentified medical condition. This may be equally as unhealthy as a fat or overweight dog. Schedule a visit with your veterinarian to assess your pet's physical health and create a strategy to help them get back on track."
                        bmi <= 4.67 -> "Ideal weight" to "It looks like your pet is the perfect weight. Naturally, you should still speak with a veterinarian to find out your pet's condition and make sure their food is suitable for their age and way of life. You can also get advice from your veterinarian on how to keep your pet at a healthy weight."
                        else -> "Overweight" to "Regretfully, it seems that your pet would benefit from losing a little weight. Dogs that are overweight are more likely to develop diabetes and arthritis, among other conditions. Schedule a visit with your veterinarian to assess your pet's physical health and create a strategy to help them get back on track."
                    }
                }

                "Pomeranian" -> {
                    when {
                        bmi <= 0.49 -> "Underweight" to "It seems like your pet needs to adjust their nutrition, or they can be underweight due to an unidentified medical condition. This may be equally as unhealthy as a fat or overweight dog. Schedule a visit with your veterinarian to assess your pet's physical health and create a strategy to help them get back on track."
                        bmi <= 1.17 -> "Ideal weight" to "It looks like your pet is the perfect weight. Naturally, you should still speak with a veterinarian to find out your pet's condition and make sure their food is suitable for their age and way of life. You can also get advice from your veterinarian on how to keep your pet at a healthy weight."
                        else -> "Overweight" to "Regretfully, it seems that your pet would benefit from losing a little weight. Dogs that are overweight are more likely to develop diabetes and arthritis, among other conditions. Schedule a visit with your veterinarian to assess your pet's physical health and create a strategy to help them get back on track."
                    }
                }

                else -> "Unknown" to "No information available"
            }
        }
    }


    @Suppress("DEPRECATION")
    override fun onBackPressed() {
        // Create a custom function to show a confirmation dialog for exiting the application
        showExitConfirmationDialog()
    }

    private fun showExitConfirmationDialog() {
        val alertDialogBuilder = AlertDialog.Builder(this)
        alertDialogBuilder.setMessage("Are you sure you want to return to Health Tools Page?")
        alertDialogBuilder.setPositiveButton("Confirm") { dialog, _ ->
            val intent = Intent(this, healthtoolspage::class.java)
            val options = ActivityOptionsCompat.makeCustomAnimation(
                this,
                R.anim.dissolve_in, // Animation for the new activity
                R.anim.dissolve_out // No animation for the current activity
            )

            // Start the activity with the specified animation options
            startActivity(intent, options.toBundle())
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
