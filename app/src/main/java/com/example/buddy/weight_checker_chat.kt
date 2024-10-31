package com.example.buddy

import android.animation.ObjectAnimator
import android.os.Bundle
import androidx.activity.ComponentActivity
import android.content.Intent
import android.view.View
import android.view.ViewGroup
import android.view.animation.AlphaAnimation
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.view.animation.TranslateAnimation
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.widget.Toolbar
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityOptionsCompat

class weight_checker_chat : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        var selectedBreed: String? = null
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_weight_checker_chat)
        val gretriever = findViewById<Button>(R.id.breedbtn1)
        val poodle = findViewById<Button>(R.id.breedbtn2)
        val chihuahua = findViewById<Button>(R.id.breedbtn3)
        val shihtzu = findViewById<Button>(R.id.breedbtn4)
        val pomeranian = findViewById<Button>(R.id.breedbtn5)
        gretriever.setOnClickListener {
            selectedBreed = "Golden Retriever"
            animateAndShowHeightQuestion()
        }
        chihuahua.setOnClickListener {
            selectedBreed = "Chihuahua"
            animateAndShowHeightQuestion()
        }
        shihtzu.setOnClickListener {
            selectedBreed = "Shih Tzu"
            animateAndShowHeightQuestion()
        }
        pomeranian.setOnClickListener {
            selectedBreed = "Pomeranian"
            animateAndShowHeightQuestion()
        }
        poodle.setOnClickListener {
            animateAndShowPoodleQuestion()
        }

        val standardpoodle = findViewById<Button>(R.id.standardpoodle)
        val toypoodle = findViewById<Button>(R.id.toypoodle)
        standardpoodle.setOnClickListener {
            selectedBreed = "Standard Poodle"
            animateAndShowHeightQuestionPoodle()

        }
        toypoodle.setOnClickListener {
            selectedBreed = "Toy Poodle"
            animateAndShowHeightQuestionPoodle()
        }

        val backbtn = findViewById<Button>(R.id.button_exit)
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

        val heightinput = findViewById<EditText>(R.id.inputheight)
        val heightdone = findViewById<Button>(R.id.heightdone)
        heightdone.setOnClickListener {
            val heightStr = heightinput.text.toString()
            if (heightStr.isNotEmpty() && heightStr.matches("[0-9.]+".toRegex())) {
                animateAndShowWeightQuestion()
            } else {
                // Handle case when height input is empty or contains invalid characters
                Toast.makeText(this, "Please enter a valid value for your dog's height! Woof!", Toast.LENGTH_SHORT).show()
            }
        }

        val weightinput = findViewById<EditText>(R.id.weightinput)

        //Function for calculating the dog's bmi with the calculation of bmi = lbs / inch
        //When weight done is clicked, it will then proceed to the weight result with an intent for the selected breed string and bmi since the bmi has ranges there shouldn't be a default bmi range, the range will only depend on the breed string per breed that will tell if the dog is underweight, ideal weight or overweight
        val weightdone = findViewById<Button>(R.id.weightdone)
        // Set click listener for Weight Done button
        weightdone.setOnClickListener {
            val weightStr = weightinput.text.toString()
            val heightStr = heightinput.text.toString()

            if (weightStr.isNotEmpty() && heightStr.isNotEmpty()) {
                if (weightStr.matches("[0-9.]+".toRegex())) { // Check if weightStr contains only numeric characters and optional dot
                    val weight = weightStr.toDouble() // Assuming weight is in pounds
                    val height = heightStr.toDouble() // Assuming height is in inches

                    // Calculate BMI directly here and round off to two decimal places
                    val bmi = (weight / height).roundToTwoDecimalPlaces()

                    // Start next activity with selected breed and calculated BMI as extras in Intent
                    val intent = Intent(this, weight_checker_result::class.java)
                    intent.putExtra("selectedBreed", selectedBreed)
                    intent.putExtra("bmi", bmi)
                    // Create options for the animation
                    val options = ActivityOptionsCompat.makeCustomAnimation(
                        this,
                        R.anim.dissolve_in, // Animation for the new activity
                        R.anim.dissolve_out // No animation for the current activity
                    )
                    // Start the activity with the specified animation options
                    startActivity(intent, options.toBundle())
                    finish()
                } else {
                    // Handle case when weight input contains special characters
                    Toast.makeText(this, "Please enter a valid weight! Woof", Toast.LENGTH_SHORT).show()
                }
            } else {
                // Handle case when weight or height input is empty
                Toast.makeText(this, "Please enter weight and height", Toast.LENGTH_SHORT).show()
            }
        }


    }
    // Extension function to round off a Double to two decimal places
    fun Double.roundToTwoDecimalPlaces(): Double {
        return "%.${2}f".format(this).toDouble()
    }

    private fun animateAndShowHeightQuestionPoodle(){
        val fadeout = AnimationUtils.loadAnimation(this, R.anim.fade_out)
        val fadeIn= AnimationUtils.loadAnimation(this, R.anim.fade_in)
        val poodleicon = findViewById<TextView>(R.id.hiconpoodle)
        val poodlequestion = findViewById<TextView>(R.id.questionpoodle)
        val standardpoodle = findViewById<Button>(R.id.standardpoodle)
        val toypoodle = findViewById<Button>(R.id.toypoodle)
        poodleicon.startAnimation(fadeout)
        poodlequestion.startAnimation(fadeout)
        standardpoodle.startAnimation(fadeout)
        toypoodle.startAnimation(fadeout)
        poodleicon.isEnabled = false
        poodlequestion.isEnabled = false
        standardpoodle.isEnabled = false
        toypoodle.isEnabled = false
        poodleicon.visibility = View.GONE
        poodlequestion.visibility = View.GONE
        standardpoodle.visibility = View.GONE
        toypoodle.visibility = View.GONE
        val heighticon = findViewById<TextView>(R.id.hiconheight)
        val heightquestion = findViewById<TextView>(R.id.questionheight)
        val heightinput = findViewById<EditText>(R.id.inputheight)
        val heightdone = findViewById<Button>(R.id.heightdone)
        heighticon.startAnimation(fadeIn)
        heightinput.startAnimation(fadeIn)
        heightquestion.startAnimation(fadeIn)
        heightdone.startAnimation(fadeIn)
        heighticon.visibility = View.VISIBLE
        heightinput.visibility = View.VISIBLE
        heightquestion.visibility = View.VISIBLE
        heightdone.visibility = View.VISIBLE

    }
    private fun animateAndShowPoodleQuestion(){
        val fadeout = AnimationUtils.loadAnimation(this, R.anim.fade_out)
        val fadeIn= AnimationUtils.loadAnimation(this, R.anim.fade_in)
        val icon = findViewById<TextView>(R.id.hi1)
        val breedquestion = findViewById<TextView>(R.id.breedquestion)
        val gretriever = findViewById<Button>(R.id.breedbtn1)
        val poodle = findViewById<Button>(R.id.breedbtn2)
        val chihuahua = findViewById<Button>(R.id.breedbtn3)
        val shihtzu = findViewById<Button>(R.id.breedbtn4)
        val pomeranian = findViewById<Button>(R.id.breedbtn5)

        // Start the slide up animation for Question 1 views
        icon.startAnimation(fadeout)
        breedquestion.startAnimation(fadeout)
        gretriever.startAnimation(fadeout)
        poodle.startAnimation(fadeout)
        chihuahua.startAnimation(fadeout)
        shihtzu.startAnimation(fadeout)
        pomeranian.startAnimation(fadeout)
        icon.isEnabled = false
        breedquestion.isEnabled = false
        gretriever.isEnabled = false
        poodle.isEnabled = false
        chihuahua.isEnabled = false
        shihtzu.isEnabled = false
        pomeranian.isEnabled = false
        val poodleicon = findViewById<TextView>(R.id.hiconpoodle)
        val poodlequestion = findViewById<TextView>(R.id.questionpoodle)
        val standardpoodle = findViewById<Button>(R.id.standardpoodle)
        val toypoodle = findViewById<Button>(R.id.toypoodle)
        poodleicon.isEnabled = true
        poodlequestion.isEnabled = true
        standardpoodle.isEnabled = true
        toypoodle.isEnabled = true
        poodleicon.startAnimation(fadeIn)
        poodlequestion.startAnimation(fadeIn)
        standardpoodle.startAnimation(fadeIn)
        toypoodle.startAnimation(fadeIn)
        poodleicon.visibility = View.VISIBLE
        poodlequestion.visibility = View.VISIBLE
        standardpoodle.visibility = View.VISIBLE
        toypoodle.visibility = View.VISIBLE
    }

    private fun animateAndShowWeightQuestion(){
        // Load the animation for sliding down
        val fadeout = AnimationUtils.loadAnimation(this, R.anim.fade_out)
        val fadeInAnimation= AnimationUtils.loadAnimation(this, R.anim.fade_in)
        val heighticon = findViewById<TextView>(R.id.hiconheight)
        val heightquestion = findViewById<TextView>(R.id.questionheight)
        val heightinput = findViewById<EditText>(R.id.inputheight)
        val heightdone = findViewById<Button>(R.id.heightdone)
        heighticon.startAnimation(fadeout)
        heightquestion.startAnimation(fadeout)
        heightinput.startAnimation(fadeout)
        heightdone.startAnimation(fadeout)
        heighticon.isEnabled = false
        heightquestion.isEnabled = false
        heightinput.isEnabled = false
        heightdone.isEnabled = false
        heighticon.visibility = View.GONE
        heightquestion.visibility = View.GONE
        heightinput.visibility = View.GONE
        heightdone.visibility = View.GONE
        val weighticon = findViewById<TextView>(R.id.hiconweight)
        val weightquestion = findViewById<TextView>(R.id.questionweight)
        val weightinput = findViewById<EditText>(R.id.weightinput)
        val weightdone = findViewById<Button>(R.id.weightdone)

        weighticon.startAnimation(fadeInAnimation)
        weightquestion.startAnimation(fadeInAnimation)
        weightinput.startAnimation(fadeInAnimation)
        weightdone.startAnimation(fadeInAnimation)
        weighticon.visibility = View.VISIBLE
        weightquestion.visibility = View.VISIBLE
        weightinput.visibility = View.VISIBLE
        weightdone.visibility = View.VISIBLE

        weighticon.isEnabled = true
        weightquestion.isEnabled = true
        weightinput.isEnabled = true
        weightdone.isEnabled = true
        val poodleicon = findViewById<TextView>(R.id.hiconpoodle)
        val poodlequestion = findViewById<TextView>(R.id.questionpoodle)
        val standardpoodle = findViewById<Button>(R.id.standardpoodle)
        val toypoodle = findViewById<Button>(R.id.toypoodle)
        poodleicon.visibility = View.GONE
        poodlequestion.visibility = View.GONE
        standardpoodle.visibility = View.GONE
        toypoodle.visibility = View.GONE
    }

    private fun animateAndShowHeightQuestion() {
        // Load the animation for sliding down
        val fadeout = AnimationUtils.loadAnimation(this, R.anim.fade_out)
        val fadeInAnimation= AnimationUtils.loadAnimation(this, R.anim.fade_in)
        fadeout.fillAfter = true
        val icon = findViewById<TextView>(R.id.hi1)
        val breedquestion = findViewById<TextView>(R.id.breedquestion)
        val gretriever = findViewById<Button>(R.id.breedbtn1)
        val poodle = findViewById<Button>(R.id.breedbtn2)
        val chihuahua = findViewById<Button>(R.id.breedbtn3)
        val shihtzu = findViewById<Button>(R.id.breedbtn4)
        val pomeranian = findViewById<Button>(R.id.breedbtn5)

        // Start the slide up animation for Question 1 views
        icon.startAnimation(fadeout)
        breedquestion.startAnimation(fadeout)
        gretriever.startAnimation(fadeout)
        poodle.startAnimation(fadeout)
        chihuahua.startAnimation(fadeout)
        shihtzu.startAnimation(fadeout)
        pomeranian.startAnimation(fadeout)

        icon.isEnabled = false
        breedquestion.isEnabled = false
        gretriever.isEnabled = false
        poodle.isEnabled = false
        chihuahua.isEnabled = false
        shihtzu.isEnabled = false
        pomeranian.isEnabled = false

        val heighticon = findViewById<TextView>(R.id.hiconheight)
        val heightquestion = findViewById<TextView>(R.id.questionheight)
        val heightinput = findViewById<EditText>(R.id.inputheight)
        val heightdone = findViewById<Button>(R.id.heightdone)
        heighticon.visibility = View.VISIBLE
        heightinput.visibility = View.VISIBLE
        heightquestion.visibility = View.VISIBLE
        heightdone.visibility = View.VISIBLE
        heighticon.startAnimation(fadeInAnimation)
        heightinput.startAnimation(fadeInAnimation)
        heightquestion.startAnimation(fadeInAnimation)
        heightdone.startAnimation(fadeInAnimation)
        heighticon.isEnabled = true
        heightquestion.isEnabled = true
        heightinput.isEnabled = true
        heightdone.isEnabled = true
        val poodleicon = findViewById<TextView>(R.id.hiconpoodle)
        val poodlequestion = findViewById<TextView>(R.id.questionpoodle)
        val standardpoodle = findViewById<Button>(R.id.standardpoodle)
        val toypoodle = findViewById<Button>(R.id.toypoodle)
        poodleicon.visibility = View.GONE
        poodlequestion.visibility = View.GONE
        standardpoodle.visibility = View.GONE
        toypoodle.visibility = View.GONE
    }

    @Suppress("DEPRECATION")
    override fun onBackPressed() {
        // Start a new activity or finish the current activity
        val intent = Intent(this, healthtoolspage::class.java)
        startActivity(intent)

        // Finish the current activity to trigger the animation
        finish()

        // Define the animation to use after calling finish()
        overridePendingTransition(R.anim.dissolve_in, R.anim.dissolve_out)
    }

}

