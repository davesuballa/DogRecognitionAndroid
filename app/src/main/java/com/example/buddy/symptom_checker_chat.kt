package com.example.buddy

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.view.View
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.AdapterView
import android.widget.Button
import android.widget.Spinner
import android.widget.TextView
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityOptionsCompat
import androidx.core.content.ContextCompat.startActivity

class symptom_checker_chat: ComponentActivity() {
    private var selectedSymptom: String? = null
    private val symptomActionsMap = HashMap<String, () -> Unit>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_symptom_checker_chat)
        val bckbtn: Button = findViewById(R.id.button_exit)
        bckbtn.setOnClickListener {
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
        initializeSymptoms()
        setupSymptomSpinner()

        val doneButton: Button = findViewById(R.id.donebtn)
        doneButton.setOnClickListener {
            if (selectedSymptom != null) {
                symptomActionsMap[selectedSymptom]?.invoke() ?: run {
                    Toast.makeText(
                        this,
                        "Action not implemented for this symptom",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            } else {
                Toast.makeText(this, "Please select a symptom", Toast.LENGTH_SHORT).show()
            }
        }

    }
    private fun initializeSymptoms() {
        symptomActionsMap["Aggression"] = ::questionAggression1
        symptomActionsMap["Always Hungry"] = ::displayAlwaysHungryQuestion
        symptomActionsMap["Bloated Stomach"] = ::displayBloatedStomachQuestion
        symptomActionsMap["Constipation"] = ::displayConstipationQuestion
        symptomActionsMap["Coughing"] = ::displayCoughingQuestion
        symptomActionsMap["Dandruff"] = ::displayDandruffQuestion
        symptomActionsMap["Dehydration"] = ::displayDehydrationQuestion
        symptomActionsMap["Diarrhea"] = ::displayDiarrheaQuestion
        symptomActionsMap["Drinking and Peeing a lot"] = ::displayDrinkingAndPeeingAlotQuestion
        symptomActionsMap["Eye Problem"] = ::displayEyeProblemQuestion
        symptomActionsMap["Hair Loss"] = ::displayHairLossQuestion
        symptomActionsMap["Itching"] = ::displayItchingQuestion
        symptomActionsMap["Lethargic"] = ::displayLethargicQuestion
        symptomActionsMap["Licking and Scratching"]  = ::displayLickingAndScratchingQuestion
        symptomActionsMap["Limping"] = ::displayLimpingQuestion
        symptomActionsMap["Not Eating"] = ::displayNotEatingQuestion
        symptomActionsMap["Seizure or Shaking"] = ::displaySeizureOrShakingQuestion
        symptomActionsMap["Sleeping a lot"] = ::displaySleepingALotQuestion
        symptomActionsMap["Vomiting"] = ::displayVomitingQuestion
        symptomActionsMap["Wounds"] = ::displayWoundsQuestion

        // Add more symptoms and corresponding actions as needed
    }

    private fun setupSymptomSpinner() {
        val spinner: Spinner = findViewById(R.id.dogsymptom)
        val defaultText = "Select Symptom"
        val items = listOf(
            defaultText,
            "Aggression",
            "Always Hungry",
            "Bloated Stomach",
            "Constipation",
            "Coughing",
            "Dandruff",
            "Dehydration",
            "Diarrhea",
            "Drinking and Peeing a lot",
            "Eye Problem",
            "Hair Loss",
            "Itching",
            "Lethargic",
            "Licking and Scratching",
            "Limping",
            "Not Eating",
            "Seizure or Shaking",
            "Sleeping a lot",
            "Vomiting",
            "Wounds"
            // Add other symptoms here
        )
        val adapter = CustomSpinnerAdapter2(this, R.layout.spinner_item3, items)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinner.adapter = adapter
        spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                selectedSymptom = if (position != 0) {
                    items[position]
                } else {
                    null
                }
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                selectedSymptom = null
            }
        }
    }
    private fun proceedToResult(symptom: String?, questionNumber: Int, answer: String) {
        // Create a new Intent to navigate to the SymptomCheckerResult activity
        val intent = Intent(this, symptom_checker_result::class.java)
        // Create a Bundle to store the symptom data
        val bundle = Bundle().apply {
            putString("resultKey", generateResultKey(symptom, questionNumber, answer))
        }

        // Attach the Bundle to the Intent as an extra
        intent.putExtra("resultBundle", bundle)
        val options = ActivityOptionsCompat.makeCustomAnimation(
            this,
            R.anim.dissolve_in, // Animation for the new activity
            R.anim.dissolve_out // No animation for the current activity
        )

        // Start the activity with the specified animation options
        startActivity(intent, options.toBundle())
        finish()
    }

    // Function to generate a unique result key based on symptom, question number, and answer
    private fun generateResultKey(symptom: String?, questionNumber: Int, answer: String): String {
        return "${symptom}${questionNumber}${answer}"
    }

    private fun wnq6(){
        val fadein = AnimationUtils.loadAnimation(this, R.anim.fade_in)
        val fadeout = AnimationUtils.loadAnimation(this, R.anim.fade_out)
        val icon: TextView = findViewById(R.id.hiconyesno)
        val question: TextView = findViewById(R.id.yesnoquestion)
        val yes: Button = findViewById(R.id.yesanswer)
        val no: Button = findViewById(R.id.noanswer)
        icon.startAnimation(fadeout)
        question.startAnimation(fadeout)
        yes.startAnimation(fadeout)
        no.startAnimation(fadeout)
        yes.isEnabled = false
        no.isEnabled =false
        Handler().postDelayed({
            icon.visibility = View.GONE
            question.visibility = View.GONE
            yes.visibility = View.GONE
            no.visibility = View.GONE
            val icon1: TextView = findViewById(R.id.hiconyesno2)
            val question1: TextView = findViewById(R.id.yesnoquestion2)
            question1.text = "Is there any swelling near the wound?"
            val yes1: Button = findViewById(R.id.yesanswer2)
            val no1: Button = findViewById(R.id.noanswer2)
            yes1.isEnabled = true
            no1.isEnabled = true
            icon1.startAnimation(fadein)
            question1.startAnimation(fadein)
            yes1.startAnimation(fadein)
            no1.startAnimation(fadein)
            icon1.visibility = View.VISIBLE
            question1.visibility = View.VISIBLE
            yes1.visibility = View.VISIBLE
            yes1.setOnClickListener {
                proceedToResult(selectedSymptom, 6, "Yes") // Pass parameters to proceedToResult
            }
            no1.visibility = View.VISIBLE
            no1.setOnClickListener {
                proceedToResult(selectedSymptom, 6, "No") // Pass parameters to proceedToResult
            }
        }, 1000) // Delay for 1000 milliseconds (1 second)
    }


    private fun wnq5(){
        val fadein = AnimationUtils.loadAnimation(this, R.anim.fade_in)
        val fadeout = AnimationUtils.loadAnimation(this, R.anim.fade_out)
        val icon: TextView = findViewById(R.id.hiconyesno2)
        val question: TextView = findViewById(R.id.yesnoquestion2)
        val yes: Button = findViewById(R.id.yesanswer2)
        val no: Button = findViewById(R.id.noanswer2)
        icon.startAnimation(fadeout)
        question.startAnimation(fadeout)
        yes.startAnimation(fadeout)
        no.startAnimation(fadeout)
        yes.isEnabled = false
        no.isEnabled = false
        Handler().postDelayed({
            icon.visibility = View.GONE
            question.visibility = View.GONE
            yes.visibility = View.GONE
            no.visibility = View.GONE
            val icon1: TextView = findViewById(R.id.hiconyesno)
            val question1: TextView = findViewById(R.id.yesnoquestion)
            question1.text = "Does the wound look like a small hole or puncture wound?"
            val yes1: Button = findViewById(R.id.yesanswer)
            val no1: Button = findViewById(R.id.noanswer)
            yes1.isEnabled = true
            no1.isEnabled = true
            icon1.startAnimation(fadein)
            question1.startAnimation(fadein)
            yes1.startAnimation(fadein)
            no1.startAnimation(fadein)
            icon1.visibility = View.VISIBLE
            question1.visibility = View.VISIBLE
            yes1.visibility = View.VISIBLE
            yes1.setOnClickListener {
                proceedToResult(selectedSymptom, 5, "Yes") // Pass parameters to proceedToResult
            }
            no1.visibility = View.VISIBLE
            no1.setOnClickListener {
                wnq6()
            }
        }, 1000)
    }

    private fun wnq4(){
        val fadein = AnimationUtils.loadAnimation(this, R.anim.fade_in)
        val fadeout = AnimationUtils.loadAnimation(this, R.anim.fade_out)
        val icon: TextView = findViewById(R.id.hiconyesno)
        val question: TextView = findViewById(R.id.yesnoquestion)
        val yes: Button = findViewById(R.id.yesanswer)
        val no: Button = findViewById(R.id.noanswer)
        icon.startAnimation(fadeout)
        question.startAnimation(fadeout)
        yes.startAnimation(fadeout)
        no.startAnimation(fadeout)
        yes.isEnabled = false
        no.isEnabled =false
        Handler().postDelayed({
            icon.visibility = View.GONE
            question.visibility = View.GONE
            yes.visibility = View.GONE
            no.visibility = View.GONE
            val icon1: TextView = findViewById(R.id.hiconyesno2)
            val question1: TextView = findViewById(R.id.yesnoquestion2)
            question1.text = "Is your pet excessively licking or biting the area?"
            val yes1: Button = findViewById(R.id.yesanswer2)
            val no1: Button = findViewById(R.id.noanswer2)
            yes1.isEnabled = true
            no1.isEnabled = true
            icon1.startAnimation(fadein)
            question1.startAnimation(fadein)
            yes1.startAnimation(fadein)
            no1.startAnimation(fadein)
            icon1.visibility = View.VISIBLE
            question1.visibility = View.VISIBLE
            yes1.visibility = View.VISIBLE
            yes1.setOnClickListener {
                proceedToResult(selectedSymptom, 4, "Yes") // Pass parameters to proceedToResult
            }
            no1.visibility = View.VISIBLE
            no1.setOnClickListener {
                wnq5()
            }
        }, 1000) // Delay for 1000 milliseconds (1 second)
    }

    private fun wnq3(){
        val fadein = AnimationUtils.loadAnimation(this, R.anim.fade_in)
        val fadeout = AnimationUtils.loadAnimation(this, R.anim.fade_out)
        val icon: TextView = findViewById(R.id.hiconyesno2)
        val question: TextView = findViewById(R.id.yesnoquestion2)
        val yes: Button = findViewById(R.id.yesanswer2)
        val no: Button = findViewById(R.id.noanswer2)
        icon.startAnimation(fadeout)
        question.startAnimation(fadeout)
        yes.startAnimation(fadeout)
        no.startAnimation(fadeout)
        yes.isEnabled = false
        no.isEnabled =false
        Handler().postDelayed({
            icon.visibility = View.GONE
            question.visibility = View.GONE
            yes.visibility = View.GONE
            no.visibility = View.GONE
            val icon1: TextView = findViewById(R.id.hiconyesno)
            val question1: TextView = findViewById(R.id.yesnoquestion)
            question1.text = "Is there any foul odor or discharge coming from the wound (blood, blood-tinged fluid, pus, green fluid, etc.)?"
            val yes1: Button = findViewById(R.id.yesanswer)
            val no1: Button = findViewById(R.id.noanswer)
            yes1.isEnabled = true
            no1.isEnabled = true
            icon1.startAnimation(fadein)
            question1.startAnimation(fadein)
            yes1.startAnimation(fadein)
            no1.startAnimation(fadein)
            icon1.visibility = View.VISIBLE
            question1.visibility = View.VISIBLE
            yes1.visibility = View.VISIBLE
            yes1.setOnClickListener {
                proceedToResult(selectedSymptom, 3, "Yes") // Pass parameters to proceedToResult
            }
            no1.visibility = View.VISIBLE
            no1.setOnClickListener {
                wnq4()
            }
        }, 1000) // Delay for 1000 milliseconds (1 second)
    }
    private fun wnq2(){
        val fadein = AnimationUtils.loadAnimation(this, R.anim.fade_in)
        val fadeout = AnimationUtils.loadAnimation(this, R.anim.fade_out)
        val icon: TextView = findViewById(R.id.hiconyesno)
        val question: TextView = findViewById(R.id.yesnoquestion)
        val yes: Button = findViewById(R.id.yesanswer)
        val no: Button = findViewById(R.id.noanswer)
        icon.startAnimation(fadeout)
        question.startAnimation(fadeout)
        yes.startAnimation(fadeout)
        no.startAnimation(fadeout)
        yes.isEnabled = false
        no.isEnabled =false
        Handler().postDelayed({
            icon.visibility = View.GONE
            question.visibility = View.GONE
            yes.visibility = View.GONE
            no.visibility = View.GONE
            val icon1: TextView = findViewById(R.id.hiconyesno2)
            val question1: TextView = findViewById(R.id.yesnoquestion2)
            question1.text = "Did your pet have a major fall, get hit by a car, or get in a fight with another animal?"
            val yes1: Button = findViewById(R.id.yesanswer2)
            val no1: Button = findViewById(R.id.noanswer2)
            no1.text = "No or Don't know"
            yes1.isEnabled = true
            no1.isEnabled = true
            icon1.startAnimation(fadein)
            question1.startAnimation(fadein)
            yes1.startAnimation(fadein)
            no1.startAnimation(fadein)
            icon1.visibility = View.VISIBLE
            question1.visibility = View.VISIBLE
            yes1.visibility = View.VISIBLE
            yes1.setOnClickListener {
                proceedToResult(selectedSymptom, 2, "Yes") // Pass parameters to proceedToResult
            }
            no1.visibility = View.VISIBLE
            no1.setOnClickListener {
                wnq3()
            }
        }, 1000) // Delay for 1000 milliseconds (1 second)
    }

    private fun displayWoundsQuestion() {
        val fadein = AnimationUtils.loadAnimation(this, R.anim.fade_in)
        val fadeout = AnimationUtils.loadAnimation(this, R.anim.fade_out)
        val spinner: Spinner = findViewById(R.id.dogsymptom)
        val iconsymptom: TextView = findViewById(R.id.hiconsymptom)
        val symptomq: TextView = findViewById(R.id.dogsymptomquestion)
        val btn: Button = findViewById(R.id.donebtn)
        spinner.startAnimation(fadeout)
        iconsymptom.startAnimation(fadeout)
        symptomq.startAnimation(fadeout)
        btn.startAnimation(fadeout)
        spinner.isEnabled = false
        btn.isEnabled = false
        Handler().postDelayed({

            spinner.visibility = View.GONE
            iconsymptom.visibility = View.GONE
            symptomq.visibility = View.GONE
            btn.visibility = View.GONE
            val icon: TextView = findViewById(R.id.hiconyesno)
            val question: TextView = findViewById(R.id.yesnoquestion)
            question.text = "Do any of the following apply to your pet? \n" +
                    "\n" +
                    "Having trouble breathing, \n" +
                    "Collapsed on the floor, and/or\n" +
                    "Bleeding excessively."
            val yes: Button = findViewById(R.id.yesanswer)
            val no: Button = findViewById(R.id.noanswer)
            yes.isEnabled = true
            no.isEnabled = true

            icon.startAnimation(fadein)
            question.startAnimation(fadein)
            yes.startAnimation(fadein)
            no.startAnimation(fadein)
            icon.visibility = View.VISIBLE
            question.visibility = View.VISIBLE
            yes.visibility = View.VISIBLE
            yes.setOnClickListener {
                proceedToResult(selectedSymptom, 1, "Yes") // Pass parameters to proceedToResult
            }
            no.visibility = View.VISIBLE
            no.setOnClickListener {
                wnq2()
            }
        },1000)
    }


    private fun vmq6(){
        val fadein = AnimationUtils.loadAnimation(this, R.anim.fade_in)
        val fadeout = AnimationUtils.loadAnimation(this, R.anim.fade_out)
        val icon: TextView = findViewById(R.id.hiconyesno)
        val question: TextView = findViewById(R.id.yesnoquestion)
        val yes: Button = findViewById(R.id.yesanswer)
        val no: Button = findViewById(R.id.noanswer)
        icon.startAnimation(fadeout)
        question.startAnimation(fadeout)
        yes.startAnimation(fadeout)
        no.startAnimation(fadeout)
        yes.isEnabled = false
        no.isEnabled =false
        Handler().postDelayed({
            icon.visibility = View.GONE
            question.visibility = View.GONE
            yes.visibility = View.GONE
            no.visibility = View.GONE
            val icon1: TextView = findViewById(R.id.hiconyesno2)
            val question1: TextView = findViewById(R.id.yesnoquestion2)
            question1.text = "is your pet vomiting food or water once a day or more?"
            val yes1: Button = findViewById(R.id.yesanswer2)
            val no1: Button = findViewById(R.id.noanswer2)
            yes1.isEnabled = true
            no1.isEnabled = true
            icon1.startAnimation(fadein)
            question1.startAnimation(fadein)
            yes1.startAnimation(fadein)
            no1.startAnimation(fadein)
            icon1.visibility = View.VISIBLE
            question1.visibility = View.VISIBLE
            yes1.visibility = View.VISIBLE
            yes1.setOnClickListener {
                proceedToResult(selectedSymptom, 6, "Yes") // Pass parameters to proceedToResult
            }
            no1.visibility = View.VISIBLE
            no1.setOnClickListener {
                proceedToResult(selectedSymptom, 6, "No") // Pass parameters to proceedToResult
            }
        }, 1000) // Delay for 1000 milliseconds (1 second)
    }


    private fun vmq5_1(){
        val fadein = AnimationUtils.loadAnimation(this, R.anim.fade_in)
        val fadeout = AnimationUtils.loadAnimation(this, R.anim.fade_out)
        val icon: TextView = findViewById(R.id.hiconyesno2)
        val question: TextView = findViewById(R.id.yesnoquestion2)
        val yes: Button = findViewById(R.id.yesanswer2)
        val no: Button = findViewById(R.id.noanswer2)
        icon.startAnimation(fadeout)
        question.startAnimation(fadeout)
        yes.startAnimation(fadeout)
        no.startAnimation(fadeout)
        yes.isEnabled = false
        no.isEnabled = false
        Handler().postDelayed({
            icon.visibility = View.GONE
            question.visibility = View.GONE
            yes.visibility = View.GONE
            no.visibility = View.GONE
            val icon1: TextView = findViewById(R.id.hiconyesno)
            val question1: TextView = findViewById(R.id.yesnoquestion)
            question1.text = "Could they have eaten any of these items? Items include:\n" +
                    "\n" +
                    "Human medications, \n" +
                    "An object (like a toy), \n" +
                    "Chemicals, and/or\n" +
                    "Poisonous plants."
            val yes1: Button = findViewById(R.id.yesanswer)
            val no1: Button = findViewById(R.id.noanswer)
            yes1.isEnabled = true
            no1.isEnabled = true
            icon1.startAnimation(fadein)
            question1.startAnimation(fadein)
            yes1.startAnimation(fadein)
            no1.startAnimation(fadein)
            icon1.visibility = View.VISIBLE
            question1.visibility = View.VISIBLE
            yes1.visibility = View.VISIBLE
            yes1.setOnClickListener {
                proceedToResult(selectedSymptom, 5, "Yes") // Pass parameters to proceedToResult
            }
            no1.visibility = View.VISIBLE
            no1.setOnClickListener {
                vmq6()
            }
        }, 1000)
    }

    private fun vmq4(){
        val fadein = AnimationUtils.loadAnimation(this, R.anim.fade_in)
        val fadeout = AnimationUtils.loadAnimation(this, R.anim.fade_out)
        val icon: TextView = findViewById(R.id.hiconyesno)
        val question: TextView = findViewById(R.id.yesnoquestion)
        val yes: Button = findViewById(R.id.yesanswer)
        val no: Button = findViewById(R.id.noanswer)
        icon.startAnimation(fadeout)
        question.startAnimation(fadeout)
        yes.startAnimation(fadeout)
        no.startAnimation(fadeout)
        yes.isEnabled = false
        no.isEnabled =false
        Handler().postDelayed({
            icon.visibility = View.GONE
            question.visibility = View.GONE
            yes.visibility = View.GONE
            no.visibility = View.GONE
            val icon1: TextView = findViewById(R.id.hiconyesno2)
            val question1: TextView = findViewById(R.id.yesnoquestion2)
            question1.text = "Did the vomiting start today?"
            val yes1: Button = findViewById(R.id.yesanswer2)
            val no1: Button = findViewById(R.id.noanswer2)
            yes1.isEnabled = true
            no1.isEnabled = true
            icon1.startAnimation(fadein)
            question1.startAnimation(fadein)
            yes1.startAnimation(fadein)
            no1.startAnimation(fadein)
            icon1.visibility = View.VISIBLE
            question1.visibility = View.VISIBLE
            yes1.visibility = View.VISIBLE
            yes1.setOnClickListener {
                proceedToResult(selectedSymptom, 4, "Yes") // Pass parameters to proceedToResult
            }
            no1.visibility = View.VISIBLE
            no1.setOnClickListener {
                proceedToResult(selectedSymptom, 4, "No") // Pass parameters to proceedToResult
            }
        }, 1000) // Delay for 1000 milliseconds (1 second)
    }

    private fun vmq4_1(){
        val fadein = AnimationUtils.loadAnimation(this, R.anim.fade_in)
        val fadeout = AnimationUtils.loadAnimation(this, R.anim.fade_out)
        val icon: TextView = findViewById(R.id.hiconyesno)
        val question: TextView = findViewById(R.id.yesnoquestion)
        val yes: Button = findViewById(R.id.yesanswer)
        val no: Button = findViewById(R.id.noanswer)
        icon.startAnimation(fadeout)
        question.startAnimation(fadeout)
        yes.startAnimation(fadeout)
        no.startAnimation(fadeout)
        yes.isEnabled = false
        no.isEnabled =false
        Handler().postDelayed({
            icon.visibility = View.GONE
            question.visibility = View.GONE
            yes.visibility = View.GONE
            no.visibility = View.GONE
            val icon1: TextView = findViewById(R.id.hiconyesno2)
            val question1: TextView = findViewById(R.id.yesnoquestion2)
            question1.text = "Is your pet lethargic or eating less than usual, or do they seem like they may be in pain?"
            val yes1: Button = findViewById(R.id.yesanswer2)
            val no1: Button = findViewById(R.id.noanswer2)
            yes1.isEnabled = true
            no1.isEnabled = true
            icon1.startAnimation(fadein)
            question1.startAnimation(fadein)
            yes1.startAnimation(fadein)
            no1.startAnimation(fadein)
            icon1.visibility = View.VISIBLE
            question1.visibility = View.VISIBLE
            yes1.visibility = View.VISIBLE
            yes1.setOnClickListener {
                proceedToResult(selectedSymptom, 4, "Yes") // Pass parameters to proceedToResult
            }
            no1.visibility = View.VISIBLE
            no1.setOnClickListener {
                vmq5_1()
            }
        }, 1000) // Delay for 1000 milliseconds (1 second)
    }
    private fun vmq3_1(){
        val fadein = AnimationUtils.loadAnimation(this, R.anim.fade_in)
        val fadeout = AnimationUtils.loadAnimation(this, R.anim.fade_out)
        val icon: TextView = findViewById(R.id.hiconyesno2)
        val question: TextView = findViewById(R.id.yesnoquestion2)
        val yes: Button = findViewById(R.id.yesanswer2)
        val no: Button = findViewById(R.id.noanswer2)
        icon.startAnimation(fadeout)
        question.startAnimation(fadeout)
        yes.startAnimation(fadeout)
        no.startAnimation(fadeout)
        yes.isEnabled = false
        no.isEnabled =false
        Handler().postDelayed({
            icon.visibility = View.GONE
            question.visibility = View.GONE
            yes.visibility = View.GONE
            no.visibility = View.GONE
            val icon1: TextView = findViewById(R.id.hiconyesno)
            val question1: TextView = findViewById(R.id.yesnoquestion)
            question1.text = "Do any of the following apply? \n" +
                    "\n" +
                    "No fluid or material is coming up, \n" +
                    "Vomit looks like red blood or coffee grounds, and/or\n" +
                    "Vomit is projectile."
            val yes1: Button = findViewById(R.id.yesanswer)
            yes1.text = "Yes"
            val no1: Button = findViewById(R.id.noanswer)
            no1.text = "No"
            yes1.isEnabled = true
            no1.isEnabled = true
            icon1.startAnimation(fadein)
            question1.startAnimation(fadein)
            yes1.startAnimation(fadein)
            no1.startAnimation(fadein)
            icon1.visibility = View.VISIBLE
            question1.visibility = View.VISIBLE
            yes1.visibility = View.VISIBLE
            yes1.setOnClickListener {
                proceedToResult(selectedSymptom, 3, "Yes") // Pass parameters to proceedToResult
            }
            no1.visibility = View.VISIBLE
            no1.setOnClickListener {
                vmq4_1()
            }
        }, 1000) // Delay for 1000 milliseconds (1 second)
    }
    private fun vmq3(){
        val fadein = AnimationUtils.loadAnimation(this, R.anim.fade_in)
        val fadeout = AnimationUtils.loadAnimation(this, R.anim.fade_out)
        val icon: TextView = findViewById(R.id.hiconyesno2)
        val question: TextView = findViewById(R.id.yesnoquestion2)
        val yes: Button = findViewById(R.id.yesanswer2)
        val no: Button = findViewById(R.id.noanswer2)
        icon.startAnimation(fadeout)
        question.startAnimation(fadeout)
        yes.startAnimation(fadeout)
        no.startAnimation(fadeout)
        yes.isEnabled = false
        no.isEnabled =false
        Handler().postDelayed({
            icon.visibility = View.GONE
            question.visibility = View.GONE
            yes.visibility = View.GONE
            no.visibility = View.GONE
            val icon1: TextView = findViewById(R.id.hiconyesno)
            val question1: TextView = findViewById(R.id.yesnoquestion)
            question1.text = "Is anything coming up when they vomit?"
            val yes1: Button = findViewById(R.id.yesanswer)
            yes1.text = "Yes"
            val no1: Button = findViewById(R.id.noanswer)
            no1.text = "No"
            yes1.isEnabled = true
            no1.isEnabled = true
            icon1.startAnimation(fadein)
            question1.startAnimation(fadein)
            yes1.startAnimation(fadein)
            no1.startAnimation(fadein)
            icon1.visibility = View.VISIBLE
            question1.visibility = View.VISIBLE
            yes1.visibility = View.VISIBLE
            yes1.setOnClickListener {
                vmq4()
            }
            no1.visibility = View.VISIBLE
            no1.setOnClickListener {
                proceedToResult(selectedSymptom, 3, "No") // Pass parameters to proceedToResult
            }
        }, 1000) // Delay for 1000 milliseconds (1 second)
    }


    private fun vmq5_2(){
        val fadein = AnimationUtils.loadAnimation(this, R.anim.fade_in)
        val fadeout = AnimationUtils.loadAnimation(this, R.anim.fade_out)
        val icon: TextView = findViewById(R.id.hiconyesno)
        val question: TextView = findViewById(R.id.yesnoquestion)
        val yes: Button = findViewById(R.id.yesanswer)
        val no: Button = findViewById(R.id.noanswer)
        icon.startAnimation(fadeout)
        question.startAnimation(fadeout)
        yes.startAnimation(fadeout)
        no.startAnimation(fadeout)
        yes.isEnabled = false
        no.isEnabled =false
        Handler().postDelayed({
            icon.visibility = View.GONE
            question.visibility = View.GONE
            yes.visibility = View.GONE
            no.visibility = View.GONE
            val icon1: TextView = findViewById(R.id.hiconyesno2)
            val question1: TextView = findViewById(R.id.yesnoquestion2)
            question1.text = "Did the vomiting start today?"
            val yes1: Button = findViewById(R.id.yesanswer2)
            val no1: Button = findViewById(R.id.noanswer2)
            yes1.isEnabled = true
            no1.isEnabled = true
            icon1.startAnimation(fadein)
            question1.startAnimation(fadein)
            yes1.startAnimation(fadein)
            no1.startAnimation(fadein)
            icon1.visibility = View.VISIBLE
            question1.visibility = View.VISIBLE
            yes1.visibility = View.VISIBLE
            yes1.setOnClickListener {
                proceedToResult(selectedSymptom, 4, "Yes") // Pass parameters to proceedToResult
            }
            no1.visibility = View.VISIBLE
            no1.setOnClickListener {
                proceedToResult(selectedSymptom, 4, "No") // Pass parameters to proceedToResult
            }
        }, 1000) // Delay for 1000 milliseconds (1 second)
    }

    private fun vmq4_3(){
        val fadein = AnimationUtils.loadAnimation(this, R.anim.fade_in)
        val fadeout = AnimationUtils.loadAnimation(this, R.anim.fade_out)
        val icon: TextView = findViewById(R.id.hiconyesno)
        val question: TextView = findViewById(R.id.yesnoquestion)
        val yes: Button = findViewById(R.id.yesanswer)
        val no: Button = findViewById(R.id.noanswer)
        icon.startAnimation(fadeout)
        question.startAnimation(fadeout)
        yes.startAnimation(fadeout)
        no.startAnimation(fadeout)
        yes.isEnabled = false
        no.isEnabled =false
        Handler().postDelayed({
            icon.visibility = View.GONE
            question.visibility = View.GONE
            yes.visibility = View.GONE
            no.visibility = View.GONE
            val icon1: TextView = findViewById(R.id.hiconyesno2)
            val question1: TextView = findViewById(R.id.yesnoquestion2)
            question1.text = "Is anything coming up when they vomit?"
            val yes1: Button = findViewById(R.id.yesanswer2)
            val no1: Button = findViewById(R.id.noanswer2)
            yes1.isEnabled = true
            no1.isEnabled = true
            icon1.startAnimation(fadein)
            question1.startAnimation(fadein)
            yes1.startAnimation(fadein)
            no1.startAnimation(fadein)
            icon1.visibility = View.VISIBLE
            question1.visibility = View.VISIBLE
            yes1.visibility = View.VISIBLE
            yes1.setOnClickListener {
                vmq5_2()
            }
            no1.visibility = View.VISIBLE
            no1.setOnClickListener {
                proceedToResult(selectedSymptom, 3, "No") // Pass parameters to proceedToResult
            }
        }, 1000) // Delay for 1000 milliseconds (1 second)
    }

    private fun vmq5_4(){
        val fadein = AnimationUtils.loadAnimation(this, R.anim.fade_in)
        val fadeout = AnimationUtils.loadAnimation(this, R.anim.fade_out)
        val icon: TextView = findViewById(R.id.hiconyesno2)
        val question: TextView = findViewById(R.id.yesnoquestion2)
        val yes: Button = findViewById(R.id.yesanswer2)
        val no: Button = findViewById(R.id.noanswer2)
        icon.startAnimation(fadeout)
        question.startAnimation(fadeout)
        yes.startAnimation(fadeout)
        no.startAnimation(fadeout)
        yes.isEnabled = false
        no.isEnabled =false
        Handler().postDelayed({
            icon.visibility = View.GONE
            question.visibility = View.GONE
            yes.visibility = View.GONE
            no.visibility = View.GONE
            val icon1: TextView = findViewById(R.id.hiconyesno)
            val question1: TextView = findViewById(R.id.yesnoquestion)
            question1.text = "Is your pet lethargic or eating less than usual, or do they seem like they may be in pain?"
            val yes1: Button = findViewById(R.id.yesanswer)
            val no1: Button = findViewById(R.id.noanswer)
            yes1.isEnabled = true
            no1.isEnabled = true
            icon1.startAnimation(fadein)
            question1.startAnimation(fadein)
            yes1.startAnimation(fadein)
            no1.startAnimation(fadein)
            icon1.visibility = View.VISIBLE
            question1.visibility = View.VISIBLE
            yes1.visibility = View.VISIBLE
            yes1.setOnClickListener {
                proceedToResult(selectedSymptom, 4, "Yes") // Pass parameters to proceedToResult
            }
            no1.visibility = View.VISIBLE
            no1.setOnClickListener {
                vmq5_5()
            }
        }, 1000) // Delay for 1000 milliseconds (1 second)
    }

    private fun vmq5_6(){
        val fadein = AnimationUtils.loadAnimation(this, R.anim.fade_in)
        val fadeout = AnimationUtils.loadAnimation(this, R.anim.fade_out)
        val icon: TextView = findViewById(R.id.hiconyesno2)
        val question: TextView = findViewById(R.id.yesnoquestion2)
        val yes: Button = findViewById(R.id.yesanswer2)
        val no: Button = findViewById(R.id.noanswer2)
        icon.startAnimation(fadeout)
        question.startAnimation(fadeout)
        yes.startAnimation(fadeout)
        no.startAnimation(fadeout)
        yes.isEnabled = false
        no.isEnabled =false
        Handler().postDelayed({
            icon.visibility = View.GONE
            question.visibility = View.GONE
            yes.visibility = View.GONE
            no.visibility = View.GONE
            val icon1: TextView = findViewById(R.id.hiconyesno)
            val question1: TextView = findViewById(R.id.yesnoquestion)
            question1.text = "is your pet vomiting food or water once a day or more?"
            val yes1: Button = findViewById(R.id.yesanswer)
            val no1: Button = findViewById(R.id.noanswer)
            yes1.isEnabled = true
            no1.isEnabled = true
            icon1.startAnimation(fadein)
            question1.startAnimation(fadein)
            yes1.startAnimation(fadein)
            no1.startAnimation(fadein)
            icon1.visibility = View.VISIBLE
            question1.visibility = View.VISIBLE
            yes1.visibility = View.VISIBLE
            yes1.setOnClickListener {
                proceedToResult(selectedSymptom, 6, "Yes") // Pass parameters to proceedToResult
            }
            no1.visibility = View.VISIBLE
            no1.setOnClickListener {
                proceedToResult(selectedSymptom, 6, "No") // Pass
            }
        }, 1000) // Delay for 1000 milliseconds (1 second)
    }

    private fun vmq5_5(){
        val fadein = AnimationUtils.loadAnimation(this, R.anim.fade_in)
        val fadeout = AnimationUtils.loadAnimation(this, R.anim.fade_out)
        val icon: TextView = findViewById(R.id.hiconyesno)
        val question: TextView = findViewById(R.id.yesnoquestion)
        val yes: Button = findViewById(R.id.yesanswer)
        val no: Button = findViewById(R.id.noanswer)
        icon.startAnimation(fadeout)
        question.startAnimation(fadeout)
        yes.startAnimation(fadeout)
        no.startAnimation(fadeout)
        yes.isEnabled = false
        no.isEnabled =false
        Handler().postDelayed({
            icon.visibility = View.GONE
            question.visibility = View.GONE
            yes.visibility = View.GONE
            no.visibility = View.GONE
            val icon1: TextView = findViewById(R.id.hiconyesno2)
            val question1: TextView = findViewById(R.id.yesnoquestion2)
            question1.text = "Could they have eaten any of these items? Items include:\n" +
                    "\n" +
                    "Human medications, \n" +
                    "An object (like a toy), \n" +
                    "Chemicals, and/or\n" +
                    "Poisonous plants."
            val yes1: Button = findViewById(R.id.yesanswer2)
            val no1: Button = findViewById(R.id.noanswer2)
            yes1.isEnabled = true
            no1.isEnabled = true
            icon1.startAnimation(fadein)
            question1.startAnimation(fadein)
            yes1.startAnimation(fadein)
            no1.startAnimation(fadein)
            icon1.visibility = View.VISIBLE
            question1.visibility = View.VISIBLE
            yes1.visibility = View.VISIBLE
            yes1.setOnClickListener {
                proceedToResult(selectedSymptom, 5, "Yes") // Pass parameters to proceedToResult
            }
            no1.visibility = View.VISIBLE
            no1.setOnClickListener {
                vmq5_6()
            }
        }, 1000) // Delay for 1000 milliseconds (1 second)
    }
    private fun vmq4_2(){
        val fadein = AnimationUtils.loadAnimation(this, R.anim.fade_in)
        val fadeout = AnimationUtils.loadAnimation(this, R.anim.fade_out)
        val icon: TextView = findViewById(R.id.hiconyesno)
        val question: TextView = findViewById(R.id.yesnoquestion)
        val yes: Button = findViewById(R.id.yesanswer)
        val no: Button = findViewById(R.id.noanswer)
        icon.startAnimation(fadeout)
        question.startAnimation(fadeout)
        yes.startAnimation(fadeout)
        no.startAnimation(fadeout)
        yes.isEnabled = false
        no.isEnabled =false
        Handler().postDelayed({
            icon.visibility = View.GONE
            question.visibility = View.GONE
            yes.visibility = View.GONE
            no.visibility = View.GONE
            val icon1: TextView = findViewById(R.id.hiconyesno2)
            val question1: TextView = findViewById(R.id.yesnoquestion2)
            question1.text = "Do any of the following apply? \n" +
                    "\n" +
                    "No fluid or material is coming up, \n" +
                    "Vomit looks like red blood or coffee grounds, and/or\n" +
                    "Vomit is projectile."
            val yes1: Button = findViewById(R.id.yesanswer2)
            val no1: Button = findViewById(R.id.noanswer2)
            yes1.isEnabled = true
            no1.isEnabled = true
            icon1.startAnimation(fadein)
            question1.startAnimation(fadein)
            yes1.startAnimation(fadein)
            no1.startAnimation(fadein)
            icon1.visibility = View.VISIBLE
            question1.visibility = View.VISIBLE
            yes1.visibility = View.VISIBLE
            yes1.setOnClickListener {
                proceedToResult(selectedSymptom, 3, "Yes") // Pass parameters to proceedToResult
            }
            no1.visibility = View.VISIBLE
            no1.setOnClickListener {
                vmq5_4()
            }
        }, 1000) // Delay for 1000 milliseconds (1 second)
    }

    private fun vmq3_2(){
        val fadein = AnimationUtils.loadAnimation(this, R.anim.fade_in)
        val fadeout = AnimationUtils.loadAnimation(this, R.anim.fade_out)
        val icon: TextView = findViewById(R.id.hiconyesno2)
        val question: TextView = findViewById(R.id.yesnoquestion2)
        val yes: Button = findViewById(R.id.yesanswer2)
        val no: Button = findViewById(R.id.noanswer2)
        icon.startAnimation(fadeout)
        question.startAnimation(fadeout)
        yes.startAnimation(fadeout)
        no.startAnimation(fadeout)
        yes.isEnabled = false
        no.isEnabled =false
        Handler().postDelayed({
            icon.visibility = View.GONE
            question.visibility = View.GONE
            yes.visibility = View.GONE
            no.visibility = View.GONE
            val icon1: TextView = findViewById(R.id.hiconyesno)
            val question1: TextView = findViewById(R.id.yesnoquestion)
            question1.text = "Is your pet's belly moving in and out when they vomit?"
            val yes1: Button = findViewById(R.id.yesanswer)
            yes1.text = "Yes"
            val no1: Button = findViewById(R.id.noanswer)
            no1.text = "No"
            yes1.isEnabled = true
            no1.isEnabled = true
            icon1.startAnimation(fadein)
            question1.startAnimation(fadein)
            yes1.startAnimation(fadein)
            no1.startAnimation(fadein)
            icon1.visibility = View.VISIBLE
            question1.visibility = View.VISIBLE
            yes1.visibility = View.VISIBLE
            yes1.setOnClickListener {
                vmq4_2()
            }
            no1.visibility = View.VISIBLE
            no1.setOnClickListener {
                vmq4_3()
            }
        }, 1000) // Delay for 1000 milliseconds (1 second)
    }
    private fun vmq2_1(){
        val fadein = AnimationUtils.loadAnimation(this, R.anim.fade_in)
        val fadeout = AnimationUtils.loadAnimation(this, R.anim.fade_out)
        val icon: TextView = findViewById(R.id.hiconyesno)
        val question: TextView = findViewById(R.id.yesnoquestion)
        val yes: Button = findViewById(R.id.yesanswer)
        val no: Button = findViewById(R.id.noanswer)
        icon.startAnimation(fadeout)
        question.startAnimation(fadeout)
        yes.startAnimation(fadeout)
        no.startAnimation(fadeout)
        yes.isEnabled = false
        no.isEnabled =false
        Handler().postDelayed({
            icon.visibility = View.GONE
            question.visibility = View.GONE
            yes.visibility = View.GONE
            no.visibility = View.GONE
            val icon1: TextView = findViewById(R.id.hiconyesno2)
            val question1: TextView = findViewById(R.id.yesnoquestion2)
            question1.text = "Is your pet pregnant?"
            val yes1: Button = findViewById(R.id.yesanswer2)
            val no1: Button = findViewById(R.id.noanswer2)
            yes1.isEnabled = true
            no1.isEnabled = true
            icon1.startAnimation(fadein)
            question1.startAnimation(fadein)
            yes1.startAnimation(fadein)
            no1.startAnimation(fadein)
            icon1.visibility = View.VISIBLE
            question1.visibility = View.VISIBLE
            yes1.visibility = View.VISIBLE
            yes1.setOnClickListener {
                proceedToResult(selectedSymptom, 2, "ChoiceYes") // Pass parameters to proceedToResult
            }
            no1.visibility = View.VISIBLE
            no1.setOnClickListener {
                vmq3_2()
            }
        }, 1000) // Delay for 1000 milliseconds (1 second)
    }
    private fun vmq2(){
        val fadein = AnimationUtils.loadAnimation(this, R.anim.fade_in)
        val fadeout = AnimationUtils.loadAnimation(this, R.anim.fade_out)
        val icon: TextView = findViewById(R.id.hiconyesno)
        val question: TextView = findViewById(R.id.yesnoquestion)
        val yes: Button = findViewById(R.id.yesanswer)
        val no: Button = findViewById(R.id.noanswer)
        icon.startAnimation(fadeout)
        question.startAnimation(fadeout)
        yes.startAnimation(fadeout)
        no.startAnimation(fadeout)
        yes.isEnabled = false
        no.isEnabled =false
        Handler().postDelayed({
            icon.visibility = View.GONE
            question.visibility = View.GONE
            yes.visibility = View.GONE
            no.visibility = View.GONE
            val icon1: TextView = findViewById(R.id.hiconyesno2)
            val question1: TextView = findViewById(R.id.yesnoquestion2)
            question1.text = "Is your pet's belly moving in and out when they vomit?"
            val yes1: Button = findViewById(R.id.yesanswer2)
            val no1: Button = findViewById(R.id.noanswer2)
            yes1.isEnabled = true
            no1.isEnabled = true
            icon1.startAnimation(fadein)
            question1.startAnimation(fadein)
            yes1.startAnimation(fadein)
            no1.startAnimation(fadein)
            icon1.visibility = View.VISIBLE
            question1.visibility = View.VISIBLE
            yes1.visibility = View.VISIBLE
            yes1.setOnClickListener {
                vmq3_1()
            }
            no1.visibility = View.VISIBLE
            no1.setOnClickListener {
                vmq3()
            }
        }, 1000) // Delay for 1000 milliseconds (1 second)
    }

    private fun displayVomitingQuestion() {
        val fadein = AnimationUtils.loadAnimation(this, R.anim.fade_in)
        val fadeout = AnimationUtils.loadAnimation(this, R.anim.fade_out)
        val spinner: Spinner = findViewById(R.id.dogsymptom)
        val iconsymptom: TextView = findViewById(R.id.hiconsymptom)
        val symptomq: TextView = findViewById(R.id.dogsymptomquestion)
        val btn: Button = findViewById(R.id.donebtn)
        spinner.startAnimation(fadeout)
        iconsymptom.startAnimation(fadeout)
        symptomq.startAnimation(fadeout)
        btn.startAnimation(fadeout)
        spinner.isEnabled = false
        btn.isEnabled = false
        Handler().postDelayed({

            spinner.visibility = View.GONE
            iconsymptom.visibility = View.GONE
            symptomq.visibility = View.GONE
            btn.visibility = View.GONE
            val icon: TextView = findViewById(R.id.hiconyesno)
            val question: TextView = findViewById(R.id.yesnoquestion)
            question.text = "What is your dog's gender?"
            val yes: Button = findViewById(R.id.yesanswer)
            yes.text = "Female"
            val no: Button = findViewById(R.id.noanswer)
            no.text = "Male"
            yes.isEnabled = true
            no.isEnabled = true

            icon.startAnimation(fadein)
            question.startAnimation(fadein)
            yes.startAnimation(fadein)
            no.startAnimation(fadein)
            icon.visibility = View.VISIBLE
            question.visibility = View.VISIBLE
            yes.visibility = View.VISIBLE
            yes.setOnClickListener {
                vmq2_1()
            }
            no.visibility = View.VISIBLE
            no.setOnClickListener {
                vmq2()
            }
        },1000)
    }
    private fun slq8(){
        val fadein = AnimationUtils.loadAnimation(this, R.anim.fade_in)
        val fadeout = AnimationUtils.loadAnimation(this, R.anim.fade_out)
        val icon: TextView = findViewById(R.id.hiconyesno)
        val question: TextView = findViewById(R.id.yesnoquestion)
        val yes: Button = findViewById(R.id.yesanswer)
        val no: Button = findViewById(R.id.noanswer)
        icon.startAnimation(fadeout)
        question.startAnimation(fadeout)
        yes.startAnimation(fadeout)
        no.startAnimation(fadeout)
        yes.isEnabled = false
        no.isEnabled = false
        Handler().postDelayed({
            icon.visibility = View.GONE
            question.visibility = View.GONE
            yes.visibility = View.GONE
            no.visibility = View.GONE
            val icon1: TextView = findViewById(R.id.hiconyesno2)
            val question1: TextView = findViewById(R.id.yesnoquestion2)
            question1.text = "Are they sleeping more during the day and restless/more active at night?"
            val yes1: Button = findViewById(R.id.yesanswer2)
            val no1: Button = findViewById(R.id.noanswer2)
            yes1.isEnabled = true
            no1.isEnabled = true
            icon1.startAnimation(fadein)
            question1.startAnimation(fadein)
            yes1.startAnimation(fadein)
            no1.startAnimation(fadein)
            icon1.visibility = View.VISIBLE
            question1.visibility = View.VISIBLE
            yes1.visibility = View.VISIBLE
            yes1.setOnClickListener {
                proceedToResult(selectedSymptom, 8, "Yes") // Pass parameters to proceedToResult
            }
            no1.visibility = View.VISIBLE
            no1.setOnClickListener {
                proceedToResult(selectedSymptom, 8, "No") // Pass parameters to proceedToResult
            }
        }, 1000)
    }
    private fun slq7(){
        val fadein = AnimationUtils.loadAnimation(this, R.anim.fade_in)
        val fadeout = AnimationUtils.loadAnimation(this, R.anim.fade_out)
        val icon: TextView = findViewById(R.id.hiconyesno2)
        val question: TextView = findViewById(R.id.yesnoquestion2)
        val yes: Button = findViewById(R.id.yesanswer2)
        val no: Button = findViewById(R.id.noanswer2)
        icon.startAnimation(fadeout)
        question.startAnimation(fadeout)
        yes.startAnimation(fadeout)
        no.startAnimation(fadeout)
        yes.isEnabled = false
        no.isEnabled = false
        Handler().postDelayed({
            icon.visibility = View.GONE
            question.visibility = View.GONE
            yes.visibility = View.GONE
            no.visibility = View.GONE
            val icon1: TextView = findViewById(R.id.hiconyesno)
            val question1: TextView = findViewById(R.id.yesnoquestion)
            question1.text = "Has your pet had more stimulation than usual in the last 72 hours? Stimulation can include:\n" +
                    "\n" +
                    "A roadtrip,\n" +
                    "Houseguests, and/or\n" +
                    "Exercise."
            val yes1: Button = findViewById(R.id.yesanswer)
            val no1: Button = findViewById(R.id.noanswer)
            yes1.isEnabled = true
            no1.isEnabled = true
            icon1.startAnimation(fadein)
            question1.startAnimation(fadein)
            yes1.startAnimation(fadein)
            no1.startAnimation(fadein)
            icon1.visibility = View.VISIBLE
            question1.visibility = View.VISIBLE
            yes1.visibility = View.VISIBLE
            yes1.setOnClickListener {
                proceedToResult(selectedSymptom, 7, "Yes") // Pass parameters to proceedToResult
            }
            no1.visibility = View.VISIBLE
            no1.setOnClickListener {
                slq8()
            }
        }, 1000)
    }

    private fun slq6(){
        val fadein = AnimationUtils.loadAnimation(this, R.anim.fade_in)
        val fadeout = AnimationUtils.loadAnimation(this, R.anim.fade_out)
        val icon: TextView = findViewById(R.id.hiconyesno)
        val question: TextView = findViewById(R.id.yesnoquestion)
        val yes: Button = findViewById(R.id.yesanswer)
        val no: Button = findViewById(R.id.noanswer)
        icon.startAnimation(fadeout)
        question.startAnimation(fadeout)
        yes.startAnimation(fadeout)
        no.startAnimation(fadeout)
        yes.isEnabled = false
        no.isEnabled =false
        Handler().postDelayed({
            icon.visibility = View.GONE
            question.visibility = View.GONE
            yes.visibility = View.GONE
            no.visibility = View.GONE
            val icon1: TextView = findViewById(R.id.hiconyesno2)
            val question1: TextView = findViewById(R.id.yesnoquestion2)
            question1.text = "Does your pet have arthritis or other orthopedic issues? Other issues can include:\n" +
                    "\n" +
                    "Hip dysplasia, \n" +
                    "Cruciate tear, and/or\n" +
                    "Osteosarcoma."
            val yes1: Button = findViewById(R.id.yesanswer2)
            val no1: Button = findViewById(R.id.noanswer2)
            yes1.isEnabled = true
            no1.isEnabled = true
            icon1.startAnimation(fadein)
            question1.startAnimation(fadein)
            yes1.startAnimation(fadein)
            no1.startAnimation(fadein)
            icon1.visibility = View.VISIBLE
            question1.visibility = View.VISIBLE
            yes1.visibility = View.VISIBLE
            yes1.setOnClickListener {
                proceedToResult(selectedSymptom, 6, "Yes") // Pass parameters to proceedToResult
            }
            no1.visibility = View.VISIBLE
            no1.setOnClickListener {
                slq7()
            }
        }, 1000) // Delay for 1000 milliseconds (1 second)
    }


    private fun slq5(){
        val fadein = AnimationUtils.loadAnimation(this, R.anim.fade_in)
        val fadeout = AnimationUtils.loadAnimation(this, R.anim.fade_out)
        val icon: TextView = findViewById(R.id.hiconyesno2)
        val question: TextView = findViewById(R.id.yesnoquestion2)
        val yes: Button = findViewById(R.id.yesanswer2)
        val no: Button = findViewById(R.id.noanswer2)
        icon.startAnimation(fadeout)
        question.startAnimation(fadeout)
        yes.startAnimation(fadeout)
        no.startAnimation(fadeout)
        yes.isEnabled = false
        no.isEnabled = false
        Handler().postDelayed({
            icon.visibility = View.GONE
            question.visibility = View.GONE
            yes.visibility = View.GONE
            no.visibility = View.GONE
            val icon1: TextView = findViewById(R.id.hiconyesno)
            val question1: TextView = findViewById(R.id.yesnoquestion)
            question1.text = "Has your pet had a vaccination in the last 24 hours?"
            val yes1: Button = findViewById(R.id.yesanswer)
            val no1: Button = findViewById(R.id.noanswer)
            yes1.isEnabled = true
            no1.isEnabled = true
            icon1.startAnimation(fadein)
            question1.startAnimation(fadein)
            yes1.startAnimation(fadein)
            no1.startAnimation(fadein)
            icon1.visibility = View.VISIBLE
            question1.visibility = View.VISIBLE
            yes1.visibility = View.VISIBLE
            yes1.setOnClickListener {
                proceedToResult(selectedSymptom, 5, "Yes") // Pass parameters to proceedToResult
            }
            no1.visibility = View.VISIBLE
            no1.setOnClickListener {
                slq6()
            }
        }, 1000)
    }

    private fun slq4(){
        val fadein = AnimationUtils.loadAnimation(this, R.anim.fade_in)
        val fadeout = AnimationUtils.loadAnimation(this, R.anim.fade_out)
        val icon: TextView = findViewById(R.id.hiconyesno)
        val question: TextView = findViewById(R.id.yesnoquestion)
        val yes: Button = findViewById(R.id.yesanswer)
        val no: Button = findViewById(R.id.noanswer)
        icon.startAnimation(fadeout)
        question.startAnimation(fadeout)
        yes.startAnimation(fadeout)
        no.startAnimation(fadeout)
        yes.isEnabled = false
        no.isEnabled =false
        Handler().postDelayed({
            icon.visibility = View.GONE
            question.visibility = View.GONE
            yes.visibility = View.GONE
            no.visibility = View.GONE
            val icon1: TextView = findViewById(R.id.hiconyesno2)
            val question1: TextView = findViewById(R.id.yesnoquestion2)
            question1.text = "Does your pet have kidney or liver disease?"
            val yes1: Button = findViewById(R.id.yesanswer2)
            val no1: Button = findViewById(R.id.noanswer2)
            yes1.isEnabled = true
            no1.isEnabled = true
            icon1.startAnimation(fadein)
            question1.startAnimation(fadein)
            yes1.startAnimation(fadein)
            no1.startAnimation(fadein)
            icon1.visibility = View.VISIBLE
            question1.visibility = View.VISIBLE
            yes1.visibility = View.VISIBLE
            yes1.setOnClickListener {
                proceedToResult(selectedSymptom, 4, "Yes") // Pass parameters to proceedToResult
            }
            no1.visibility = View.VISIBLE
            no1.setOnClickListener {
                slq5()
            }
        }, 1000) // Delay for 1000 milliseconds (1 second)
    }

    private fun slq3(){
        val fadein = AnimationUtils.loadAnimation(this, R.anim.fade_in)
        val fadeout = AnimationUtils.loadAnimation(this, R.anim.fade_out)
        val icon: TextView = findViewById(R.id.hiconyesno2)
        val question: TextView = findViewById(R.id.yesnoquestion2)
        val yes: Button = findViewById(R.id.yesanswer2)
        val no: Button = findViewById(R.id.noanswer2)
        icon.startAnimation(fadeout)
        question.startAnimation(fadeout)
        yes.startAnimation(fadeout)
        no.startAnimation(fadeout)
        yes.isEnabled = false
        no.isEnabled =false
        Handler().postDelayed({
            icon.visibility = View.GONE
            question.visibility = View.GONE
            yes.visibility = View.GONE
            no.visibility = View.GONE
            val icon1: TextView = findViewById(R.id.hiconyesno)
            val question1: TextView = findViewById(R.id.yesnoquestion)
            question1.text = "Does your pet have a respiratory problem? Respiratory issues could include:\n" +
                    "\n" +
                    "History of pneumonia, and/or\n" +
                    "Asthma."
            val yes1: Button = findViewById(R.id.yesanswer)
            val no1: Button = findViewById(R.id.noanswer)
            yes1.isEnabled = true
            no1.isEnabled = true
            icon1.startAnimation(fadein)
            question1.startAnimation(fadein)
            yes1.startAnimation(fadein)
            no1.startAnimation(fadein)
            icon1.visibility = View.VISIBLE
            question1.visibility = View.VISIBLE
            yes1.visibility = View.VISIBLE
            yes1.setOnClickListener {
                proceedToResult(selectedSymptom, 3, "Yes") // Pass parameters to proceedToResult
            }
            no1.visibility = View.VISIBLE
            no1.setOnClickListener {
                slq4()
            }
        }, 1000) // Delay for 1000 milliseconds (1 second)
    }
    private fun slq2(){
        val fadein = AnimationUtils.loadAnimation(this, R.anim.fade_in)
        val fadeout = AnimationUtils.loadAnimation(this, R.anim.fade_out)
        val icon: TextView = findViewById(R.id.hiconyesno)
        val question: TextView = findViewById(R.id.yesnoquestion)
        val yes: Button = findViewById(R.id.yesanswer)
        val no: Button = findViewById(R.id.noanswer)
        icon.startAnimation(fadeout)
        question.startAnimation(fadeout)
        yes.startAnimation(fadeout)
        no.startAnimation(fadeout)
        yes.isEnabled = false
        no.isEnabled =false
        Handler().postDelayed({
            icon.visibility = View.GONE
            question.visibility = View.GONE
            yes.visibility = View.GONE
            no.visibility = View.GONE
            val icon1: TextView = findViewById(R.id.hiconyesno2)
            val question1: TextView = findViewById(R.id.yesnoquestion2)
            question1.text = "Does your pet have a heart problem? Heart problems could include: \n" +
                    "\n" +
                    "Murmur, and/or\n" +
                    "History of heart failure."
            val yes1: Button = findViewById(R.id.yesanswer2)
            val no1: Button = findViewById(R.id.noanswer2)
            yes1.isEnabled = true
            no1.isEnabled = true
            icon1.startAnimation(fadein)
            question1.startAnimation(fadein)
            yes1.startAnimation(fadein)
            no1.startAnimation(fadein)
            icon1.visibility = View.VISIBLE
            question1.visibility = View.VISIBLE
            yes1.visibility = View.VISIBLE
            yes1.setOnClickListener {
                proceedToResult(selectedSymptom, 2, "Yes") // Pass parameters to proceedToResult
            }
            no1.visibility = View.VISIBLE
            no1.setOnClickListener {
                slq3()
            }
        }, 1000) // Delay for 1000 milliseconds (1 second)
    }

    private fun displaySleepingALotQuestion() {
        val fadein = AnimationUtils.loadAnimation(this, R.anim.fade_in)
        val fadeout = AnimationUtils.loadAnimation(this, R.anim.fade_out)
        val spinner: Spinner = findViewById(R.id.dogsymptom)
        val iconsymptom: TextView = findViewById(R.id.hiconsymptom)
        val symptomq: TextView = findViewById(R.id.dogsymptomquestion)
        val btn: Button = findViewById(R.id.donebtn)
        spinner.startAnimation(fadeout)
        iconsymptom.startAnimation(fadeout)
        symptomq.startAnimation(fadeout)
        btn.startAnimation(fadeout)
        spinner.isEnabled = false
        btn.isEnabled = false
        Handler().postDelayed({

            spinner.visibility = View.GONE
            iconsymptom.visibility = View.GONE
            symptomq.visibility = View.GONE
            btn.visibility = View.GONE
            val icon: TextView = findViewById(R.id.hiconyesno)
            val question: TextView = findViewById(R.id.yesnoquestion)
            question.text = "Is your pet responding to you, getting up and moving around, and interested in food and water?"
            val yes: Button = findViewById(R.id.yesanswer)
            val no: Button = findViewById(R.id.noanswer)
            yes.isEnabled = true
            no.isEnabled = true

            icon.startAnimation(fadein)
            question.startAnimation(fadein)
            yes.startAnimation(fadein)
            no.startAnimation(fadein)
            icon.visibility = View.VISIBLE
            question.visibility = View.VISIBLE
            yes.visibility = View.VISIBLE
            yes.setOnClickListener {
                slq2()
            }
            no.visibility = View.VISIBLE
            no.setOnClickListener {
                proceedToResult(selectedSymptom, 1, "No") // Pass parameters to proceedToResult
            }
        },1000)
    }

    private fun  ssq4(){
        val fadein = AnimationUtils.loadAnimation(this, R.anim.fade_in)
        val fadeout = AnimationUtils.loadAnimation(this, R.anim.fade_out)
        val icon: TextView = findViewById(R.id.iconchoice5)
        val question: TextView = findViewById(R.id.questionchoice5)
        val yes: Button = findViewById(R.id.ch1ch5)
        val no: Button = findViewById(R.id.ch2ch5)
        val yes1: Button = findViewById(R.id.ch3ch5)
        val no1: Button = findViewById(R.id.ch4ch5)
        val yes2: Button = findViewById(R.id.ch5ch5)
        icon.startAnimation(fadeout)
        question.startAnimation(fadeout)
        yes.startAnimation(fadeout)
        no.startAnimation(fadeout)
        yes1.startAnimation(fadeout)
        no1.startAnimation(fadeout)
        yes2.startAnimation(fadeout)
        icon.visibility = View.GONE
        question.visibility = View.GONE
        yes.visibility = View.GONE
        no.visibility = View.GONE
        yes1.visibility = View.GONE
        no1.visibility = View.GONE
        yes2.visibility = View.GONE
        yes.isEnabled = false
        no.isEnabled = false
        yes1.isEnabled = false
        no1.isEnabled = false
        yes2.isEnabled=false
        Handler().postDelayed({
            val icon1: TextView = findViewById(R.id.hiconyesno2)
            val question1: TextView = findViewById(R.id.yesnoquestion2)
            question1.text = "Has your pet been diagnosed before with a seizure problem?"
            val yes1: Button = findViewById(R.id.yesanswer2)
            val no1: Button = findViewById(R.id.noanswer2)
            yes1.isEnabled = true
            no1.isEnabled = true
            icon1.startAnimation(fadein)
            question1.startAnimation(fadein)
            yes1.text = "Yes"
            yes1.startAnimation(fadein)
            no1.text = "No"
            no1.startAnimation(fadein)
            icon1.visibility = View.VISIBLE
            question1.visibility = View.VISIBLE
            yes1.visibility = View.VISIBLE
            yes1.setOnClickListener {
                proceedToResult(selectedSymptom, 4, "Yes") // Pass parameters to proceedToResult
            }
            no1.visibility = View.VISIBLE
            no1.setOnClickListener {
                proceedToResult(selectedSymptom, 4, "No")
            }
        },1000)
    }
    private fun  ssq4_1(){
        val fadein = AnimationUtils.loadAnimation(this, R.anim.fade_in)
        val fadeout = AnimationUtils.loadAnimation(this, R.anim.fade_out)
        val icon: TextView = findViewById(R.id.iconchoice5)
        val question: TextView = findViewById(R.id.questionchoice5)
        val yes: Button = findViewById(R.id.ch1ch5)
        val no: Button = findViewById(R.id.ch2ch5)
        val no1 : Button = findViewById(R.id.ch3ch5)
        val yes1 : Button = findViewById(R.id.ch4ch5)
        val yes2 :Button = findViewById(R.id.ch5ch5)
        icon.startAnimation(fadeout)
        question.startAnimation(fadeout)
        yes.startAnimation(fadeout)
        no.startAnimation(fadeout)
        no1.startAnimation(fadeout)
        yes1.startAnimation(fadeout)
        yes2.startAnimation(fadeout)
        icon.visibility = View.GONE
        question.visibility = View.GONE
        yes.visibility = View.GONE
        no.visibility = View.GONE
        no1.visibility = View.GONE
        yes1.visibility = View.GONE
        yes2.visibility = View.GONE
        yes.isEnabled = false
        no.isEnabled = false
        no1.isEnabled = false
        yes1.isEnabled = false
        yes2.isEnabled = false
        Handler().postDelayed({
            val icon1: TextView = findViewById(R.id.hiconyesno2)
            val question1: TextView = findViewById(R.id.yesnoquestion2)
            question1.text = "Has your pet been diagnosed before with a heart problem?"
            val yes1: Button = findViewById(R.id.yesanswer2)
            val no1: Button = findViewById(R.id.noanswer2)
            yes1.isEnabled = true
            no1.isEnabled = true
            icon1.startAnimation(fadein)
            question1.startAnimation(fadein)
            yes1.text = "Yes"
            yes1.startAnimation(fadein)
            no1.text = "No"
            no1.startAnimation(fadein)
            icon1.visibility = View.VISIBLE
            question1.visibility = View.VISIBLE
            yes1.visibility = View.VISIBLE
            yes1.setOnClickListener {
                proceedToResult(selectedSymptom, 4, "ChoiceYes") // Pass parameters to proceedToResult
            }
            no1.visibility = View.VISIBLE
            no1.setOnClickListener {
                proceedToResult(selectedSymptom, 4, "ChoiceNo")
            }
        },1000)
    }

    private fun ssq3(){
        val fadein = AnimationUtils.loadAnimation(this, R.anim.fade_in)
        val fadeout = AnimationUtils.loadAnimation(this, R.anim.fade_out)
        val icon: TextView = findViewById(R.id.hiconyesno2)
        val question: TextView = findViewById(R.id.yesnoquestion2)
        val yes: Button = findViewById(R.id.yesanswer2)
        val no: Button = findViewById(R.id.noanswer2)
        icon.startAnimation(fadeout)
        question.startAnimation(fadeout)
        yes.startAnimation(fadeout)
        no.startAnimation(fadeout)
        yes.isEnabled = false
        no.isEnabled = false
        Handler().postDelayed({
            icon.visibility = View.GONE
            question.visibility = View.GONE
            yes.visibility = View.GONE
            no.visibility = View.GONE
            val icon1: TextView = findViewById(R.id.iconchoice5)
            val question1: TextView = findViewById(R.id.questionchoice5)
            question1.text = "Which best describes your pet's behavior?"
            val yes1: Button = findViewById(R.id.ch1ch5)
            yes1.text = "Alert, cowering, trembling in full body or just legs (maybe trying to hide); able to sit and stand; pet seems in control of body; may be in stressful situation."
            val yes2 : Button = findViewById(R.id.ch2ch5)
            yes2.text = "Body twitching; don't seem in control of it; rapid and spastic; muscle spasms; pet is alert but eyes wide; agitated/uncomfortable."
            val no1: Button = findViewById(R.id.ch3ch5)
            no1.text = "Pet lying on the floor; whole body twitching or convulsing; paddling with front legs; might pee or poop on themselves; does not seem alert; might be vocal (growl/howl/bark)"
            val no2: Button = findViewById(R.id.ch4ch5)
            no2.text = "Pet lying on floor; body seems stiff or rigid; pet comes out of it within 30 seconds or so; looks like they passed out or fell over but alert."
            val no3: Button = findViewById(R.id.ch5ch5)
            no3.text = "Legs shaking only (could be when they are pooping or after exercising)."
            yes1.isEnabled = true
            no1.isEnabled = true
            yes2.isEnabled = true
            no2.isEnabled = true
            no3.isEnabled = true
            icon1.startAnimation(fadein)
            question1.startAnimation(fadein)
            yes1.startAnimation(fadein)
            yes2.startAnimation(fadein)
            no1.startAnimation(fadein)
            no2.startAnimation(fadein)
            no3.startAnimation(fadein)
            icon1.visibility = View.VISIBLE
            question1.visibility = View.VISIBLE
            yes1.visibility = View.VISIBLE
            yes1.setOnClickListener {
                proceedToResult(selectedSymptom, 3, "Choice1") // Pass parameters to proceedToResult
            }
            yes2.visibility = View.VISIBLE
            yes2.setOnClickListener {
                proceedToResult(selectedSymptom, 3, "Choice2") // Pass parameters to proceedToResult
            }
            no1.visibility = View.VISIBLE
            no1.setOnClickListener {
                ssq4()
            }
            no2.visibility = View.VISIBLE
            no2.setOnClickListener {
                ssq4_1()
            }
            no3.visibility = View.VISIBLE
            no3.setOnClickListener {
                proceedToResult(selectedSymptom, 3, "Choice5") // Pass parameters to proceedToResult
            }
        },1000)
    }
    private fun  ssq2(){
        val fadein = AnimationUtils.loadAnimation(this, R.anim.fade_in)
        val fadeout = AnimationUtils.loadAnimation(this, R.anim.fade_out)
        val icon: TextView = findViewById(R.id.hiconyesno)
        val question: TextView = findViewById(R.id.yesnoquestion)
        val yes: Button = findViewById(R.id.yesanswer)
        val no: Button = findViewById(R.id.noanswer)
        icon.startAnimation(fadeout)
        question.startAnimation(fadeout)
        yes.startAnimation(fadeout)
        no.startAnimation(fadeout)
        yes.isEnabled = false
        no.isEnabled = false
        Handler().postDelayed({

            icon.visibility = View.GONE
            question.visibility = View.GONE
            yes.visibility = View.GONE
            no.visibility = View.GONE
            val icon1: TextView = findViewById(R.id.hiconyesno2)
            val question1: TextView = findViewById(R.id.yesnoquestion2)
            question1.text = "Could they have gotten into anything toxic? Toxins could include:\n" +
                    "\n" +
                    "Medication\n" +
                    "Chemicals\n" +
                    "Cleaner\n" +
                    "Fertilizer\n" +
                    "Plants, and/or\n" +
                    "Essential oils."
            val yes1: Button = findViewById(R.id.yesanswer2)
            val no1: Button = findViewById(R.id.noanswer2)
            yes1.isEnabled = true
            no1.isEnabled = true

            icon1.startAnimation(fadein)
            question1.startAnimation(fadein)
            yes1.startAnimation(fadein)
            no1.startAnimation(fadein)
            icon1.visibility = View.VISIBLE
            question1.visibility = View.VISIBLE
            yes1.visibility = View.VISIBLE
            yes1.setOnClickListener {
                proceedToResult(selectedSymptom, 2, "Yes") // Pass parameters to proceedToResult
            }
            no1.visibility = View.VISIBLE
            no1.setOnClickListener {
                ssq3()
            }
        },1000)
    }

    private fun displaySeizureOrShakingQuestion() {
        val fadein = AnimationUtils.loadAnimation(this, R.anim.fade_in)
        val fadeout = AnimationUtils.loadAnimation(this, R.anim.fade_out)
        val spinner: Spinner = findViewById(R.id.dogsymptom)
        val iconsymptom: TextView = findViewById(R.id.hiconsymptom)
        val symptomq: TextView = findViewById(R.id.dogsymptomquestion)
        val btn: Button = findViewById(R.id.donebtn)
        spinner.startAnimation(fadeout)
        iconsymptom.startAnimation(fadeout)
        symptomq.startAnimation(fadeout)
        btn.startAnimation(fadeout)
        spinner.isEnabled = false
        btn.isEnabled = false
        Handler().postDelayed({

            spinner.visibility = View.GONE
            iconsymptom.visibility = View.GONE
            symptomq.visibility = View.GONE
            btn.visibility = View.GONE
            val icon: TextView = findViewById(R.id.hiconyesno)
            val question: TextView = findViewById(R.id.yesnoquestion)
            question.text =
                "Is your pet conscious and responsive (alert)?"
            val yes: Button = findViewById(R.id.yesanswer)
            val no: Button = findViewById(R.id.noanswer)
            yes.isEnabled = true
            no.isEnabled = true

            icon.startAnimation(fadein)
            question.startAnimation(fadein)
            yes.startAnimation(fadein)
            no.startAnimation(fadein)
            icon.visibility = View.VISIBLE
            question.visibility = View.VISIBLE
            yes.visibility = View.VISIBLE
            yes.setOnClickListener {
                ssq2()
            }
            no.visibility = View.VISIBLE
            no.setOnClickListener {
                proceedToResult(selectedSymptom, 1, "No") // Pass parameters to proceedToResult
            }
        },1000)
    }

    private fun neq5(){
        val fadein = AnimationUtils.loadAnimation(this, R.anim.fade_in)
        val fadeout = AnimationUtils.loadAnimation(this, R.anim.fade_out)
        val icon: TextView = findViewById(R.id.hiconyesno2)
        val question: TextView = findViewById(R.id.yesnoquestion2)
        val yes: Button = findViewById(R.id.yesanswer2)
        val no: Button = findViewById(R.id.noanswer2)
        icon.startAnimation(fadeout)
        question.startAnimation(fadeout)
        yes.startAnimation(fadeout)
        no.startAnimation(fadeout)
        yes.isEnabled = false
        no.isEnabled = false
        Handler().postDelayed({
            icon.visibility = View.GONE
            question.visibility = View.GONE
            yes.visibility = View.GONE
            no.visibility = View.GONE
            val icon1: TextView = findViewById(R.id.hiconyesno)
            val question1: TextView = findViewById(R.id.yesnoquestion)
            question1.text = "Does your pet have any of the following signs? Signs can include:\n" +
                    "\n" +
                    "Bad breath,\n" +
                    "Red gums,\n" +
                    "Dental tartar, and/or \n" +
                    "Do they drop their food while eating."
            val yes1: Button = findViewById(R.id.yesanswer)
            val no1: Button = findViewById(R.id.noanswer)
            yes1.isEnabled = true
            no1.isEnabled = true
            icon1.startAnimation(fadein)
            question1.startAnimation(fadein)
            yes1.startAnimation(fadein)
            no1.startAnimation(fadein)
            icon1.visibility = View.VISIBLE
            question1.visibility = View.VISIBLE
            yes1.visibility = View.VISIBLE
            yes1.setOnClickListener {
                proceedToResult(selectedSymptom, 5, "Yes") // Pass parameters to proceedToResult
            }
            no1.visibility = View.VISIBLE
            no1.setOnClickListener {
                proceedToResult(selectedSymptom, 5, "No") // Pass parameters to proceedToResult
            }
        }, 1000)
    }

    private fun neq4(){
        val fadein = AnimationUtils.loadAnimation(this, R.anim.fade_in)
        val fadeout = AnimationUtils.loadAnimation(this, R.anim.fade_out)
        val icon: TextView = findViewById(R.id.hiconyesno)
        val question: TextView = findViewById(R.id.yesnoquestion)
        val yes: Button = findViewById(R.id.yesanswer)
        val no: Button = findViewById(R.id.noanswer)
        icon.startAnimation(fadeout)
        question.startAnimation(fadeout)
        yes.startAnimation(fadeout)
        no.startAnimation(fadeout)
        yes.isEnabled = false
        no.isEnabled =false
        Handler().postDelayed({
            icon.visibility = View.GONE
            question.visibility = View.GONE
            yes.visibility = View.GONE
            no.visibility = View.GONE
            val icon1: TextView = findViewById(R.id.hiconyesno2)
            val question1: TextView = findViewById(R.id.yesnoquestion2)
            question1.text = "Has your pet been losing weight, or can you feel their ribs or spine more easily?"
            val yes1: Button = findViewById(R.id.yesanswer2)
            val no1: Button = findViewById(R.id.noanswer2)
            yes1.isEnabled = true
            no1.isEnabled = true
            icon1.startAnimation(fadein)
            question1.startAnimation(fadein)
            yes1.startAnimation(fadein)
            no1.startAnimation(fadein)
            icon1.visibility = View.VISIBLE
            question1.visibility = View.VISIBLE
            yes1.visibility = View.VISIBLE
            yes1.setOnClickListener {
                proceedToResult(selectedSymptom, 4, "Yes") // Pass parameters to proceedToResult
            }
            no1.visibility = View.VISIBLE
            no1.setOnClickListener {
                neq5()
            }
        }, 1000) // Delay for 1000 milliseconds (1 second)
    }

    private fun neq3(){
        val fadein = AnimationUtils.loadAnimation(this, R.anim.fade_in)
        val fadeout = AnimationUtils.loadAnimation(this, R.anim.fade_out)
        val icon: TextView = findViewById(R.id.hiconyesno2)
        val question: TextView = findViewById(R.id.yesnoquestion2)
        val yes: Button = findViewById(R.id.yesanswer2)
        val no: Button = findViewById(R.id.noanswer2)
        icon.startAnimation(fadeout)
        question.startAnimation(fadeout)
        yes.startAnimation(fadeout)
        no.startAnimation(fadeout)
        yes.isEnabled = false
        no.isEnabled =false
        Handler().postDelayed({
            icon.visibility = View.GONE
            question.visibility = View.GONE
            yes.visibility = View.GONE
            no.visibility = View.GONE
            val icon1: TextView = findViewById(R.id.hiconyesno)
            val question1: TextView = findViewById(R.id.yesnoquestion)
            question1.text = "Is your pet drinking more or urinating more than usual?"
            val yes1: Button = findViewById(R.id.yesanswer)
            val no1: Button = findViewById(R.id.noanswer)
            yes1.isEnabled = true
            no1.isEnabled = true
            icon1.startAnimation(fadein)
            question1.startAnimation(fadein)
            yes1.startAnimation(fadein)
            no1.startAnimation(fadein)
            icon1.visibility = View.VISIBLE
            question1.visibility = View.VISIBLE
            yes1.visibility = View.VISIBLE
            yes1.setOnClickListener {
                proceedToResult(selectedSymptom, 3, "Yes") // Pass parameters to proceedToResult
            }
            no1.visibility = View.VISIBLE
            no1.setOnClickListener {
                neq4()
            }
        }, 1000) // Delay for 1000 milliseconds (1 second)
    }
    private fun neq2(){
        val fadein = AnimationUtils.loadAnimation(this, R.anim.fade_in)
        val fadeout = AnimationUtils.loadAnimation(this, R.anim.fade_out)
        val icon: TextView = findViewById(R.id.hiconyesno)
        val question: TextView = findViewById(R.id.yesnoquestion)
        val yes: Button = findViewById(R.id.yesanswer)
        val no: Button = findViewById(R.id.noanswer)
        icon.startAnimation(fadeout)
        question.startAnimation(fadeout)
        yes.startAnimation(fadeout)
        no.startAnimation(fadeout)
        yes.isEnabled = false
        no.isEnabled =false
        Handler().postDelayed({
            icon.visibility = View.GONE
            question.visibility = View.GONE
            yes.visibility = View.GONE
            no.visibility = View.GONE
            val icon1: TextView = findViewById(R.id.hiconyesno2)
            val question1: TextView = findViewById(R.id.yesnoquestion2)
            question1.text = "Is your pet doing any of the following behaviors?  Behaviors can include:\n" +
                    "\n" +
                    "Drinking less water, \n" +
                    "Lethargic, \n" +
                    "Having any diarrhea, and/or \n" +
                    "Vomiting."
            val yes1: Button = findViewById(R.id.yesanswer2)
            val no1: Button = findViewById(R.id.noanswer2)
            yes1.isEnabled = true
            no1.isEnabled = true
            icon1.startAnimation(fadein)
            question1.startAnimation(fadein)
            yes1.startAnimation(fadein)
            no1.startAnimation(fadein)
            icon1.visibility = View.VISIBLE
            question1.visibility = View.VISIBLE
            yes1.visibility = View.VISIBLE
            yes1.setOnClickListener {
                proceedToResult(selectedSymptom, 2, "Yes") // Pass parameters to proceedToResult
            }
            no1.visibility = View.VISIBLE
            no1.setOnClickListener {
                neq3()
            }
        }, 1000) // Delay for 1000 milliseconds (1 second)
    }
    private fun neq2_1(){
        val fadein = AnimationUtils.loadAnimation(this, R.anim.fade_in)
        val fadeout = AnimationUtils.loadAnimation(this, R.anim.fade_out)
        val icon: TextView = findViewById(R.id.hiconyesno)
        val question: TextView = findViewById(R.id.yesnoquestion)
        val yes: Button = findViewById(R.id.yesanswer)
        val no: Button = findViewById(R.id.noanswer)
        icon.startAnimation(fadeout)
        question.startAnimation(fadeout)
        yes.startAnimation(fadeout)
        no.startAnimation(fadeout)
        yes.isEnabled = false
        no.isEnabled =false
        Handler().postDelayed({
            icon.visibility = View.GONE
            question.visibility = View.GONE
            yes.visibility = View.GONE
            no.visibility = View.GONE
            val icon1: TextView = findViewById(R.id.hiconyesno2)
            val question1: TextView = findViewById(R.id.yesnoquestion2)
            question1.text = "How long has this been going on?"
            val yes1: Button = findViewById(R.id.yesanswer2)
            yes1.text = "1-2 days"
            val no1: Button = findViewById(R.id.noanswer2)
            no1.text = "3 days or longer"
            yes1.isEnabled = true
            no1.isEnabled = true
            icon1.startAnimation(fadein)
            question1.startAnimation(fadein)
            yes1.startAnimation(fadein)
            no1.startAnimation(fadein)
            icon1.visibility = View.VISIBLE
            question1.visibility = View.VISIBLE
            yes1.visibility = View.VISIBLE
            yes1.setOnClickListener {
                proceedToResult(selectedSymptom, 2, "Choice1") // Pass parameters to proceedToResult
            }
            no1.visibility = View.VISIBLE
            no1.setOnClickListener {
                proceedToResult(selectedSymptom, 2, "Choice2") // Pass parameters to proceedToResult
            }
        }, 1000) // Delay for 1000 milliseconds (1 second)
    }

    private fun displayNotEatingQuestion() {
        val fadein = AnimationUtils.loadAnimation(this, R.anim.fade_in)
        val fadeout = AnimationUtils.loadAnimation(this, R.anim.fade_out)
        val spinner: Spinner = findViewById(R.id.dogsymptom)
        val iconsymptom: TextView = findViewById(R.id.hiconsymptom)
        val symptomq: TextView = findViewById(R.id.dogsymptomquestion)
        val btn: Button = findViewById(R.id.donebtn)
        spinner.startAnimation(fadeout)
        iconsymptom.startAnimation(fadeout)
        symptomq.startAnimation(fadeout)
        btn.startAnimation(fadeout)
        spinner.isEnabled = false
        btn.isEnabled = false
        Handler().postDelayed({

            spinner.visibility = View.GONE
            iconsymptom.visibility = View.GONE
            symptomq.visibility = View.GONE
            btn.visibility = View.GONE
            val icon: TextView = findViewById(R.id.hiconyesno)
            val question: TextView = findViewById(R.id.yesnoquestion)
            question.text = "Is your pet eating nothing / very small amounts of treats or food?"
            val yes: Button = findViewById(R.id.yesanswer)
            val no: Button = findViewById(R.id.noanswer)
            yes.isEnabled = true
            no.isEnabled = true

            icon.startAnimation(fadein)
            question.startAnimation(fadein)
            yes.startAnimation(fadein)
            no.startAnimation(fadein)
            icon.visibility = View.VISIBLE
            question.visibility = View.VISIBLE
            yes.visibility = View.VISIBLE
            yes.setOnClickListener {
                neq2_1()
            }
            no.visibility = View.VISIBLE
            no.setOnClickListener {
                neq2()
            }
        },1000)
    }

    private fun lpq4(){
        val fadein = AnimationUtils.loadAnimation(this, R.anim.fade_in)
        val fadeout = AnimationUtils.loadAnimation(this, R.anim.fade_out)
        val icon: TextView = findViewById(R.id.hiconyesno)
        val question: TextView = findViewById(R.id.yesnoquestion)
        val yes: Button = findViewById(R.id.yesanswer)
        val no: Button = findViewById(R.id.noanswer)
        icon.startAnimation(fadeout)
        question.startAnimation(fadeout)
        yes.startAnimation(fadeout)
        no.startAnimation(fadeout)
        icon.visibility = View.GONE
        question.visibility = View.GONE
        yes.visibility = View.GONE
        no.visibility = View.GONE
        yes.isEnabled = false
        no.isEnabled = false
        Handler().postDelayed({

            val icon1: TextView = findViewById(R.id.hiconyesno2)
            val question1: TextView = findViewById(R.id.yesnoquestion2)
            question1.text = "Was there any trauma or an accident? Examples can include:\n" +
                    "\n" +
                    "Your pet falling, \n" +
                    "Getting hit by something, \n" +
                    "Landing on their leg funny, and/or\n" +
                    "Yelping after jumping."
            val yes1: Button = findViewById(R.id.yesanswer2)
            val no1: Button = findViewById(R.id.noanswer2)
            yes1.isEnabled = true
            no1.isEnabled = true

            icon1.startAnimation(fadein)
            question1.startAnimation(fadein)
            yes1.text = "Yes"
            yes1.startAnimation(fadein)
            no1.text = "No"
            no1.startAnimation(fadein)
            icon1.visibility = View.VISIBLE
            question1.visibility = View.VISIBLE
            yes1.visibility = View.VISIBLE
            yes1.setOnClickListener {
                proceedToResult(selectedSymptom, 4, "Yes") // Pass parameters to proceedToResult
            }
            no1.visibility = View.VISIBLE
            no1.setOnClickListener {
                proceedToResult(selectedSymptom, 4, "No")
            }
        },1000)
    }
    private fun lpq3(){
        val fadein = AnimationUtils.loadAnimation(this, R.anim.fade_in)
        val fadeout = AnimationUtils.loadAnimation(this, R.anim.fade_out)
        val icon: TextView = findViewById(R.id.hiconyesno2)
        val question: TextView = findViewById(R.id.yesnoquestion2)
        val yes: Button = findViewById(R.id.yesanswer2)
        val no: Button = findViewById(R.id.noanswer2)
        icon.startAnimation(fadeout)
        question.startAnimation(fadeout)
        yes.startAnimation(fadeout)
        no.startAnimation(fadeout)
        yes.isEnabled = false
        no.isEnabled = false
        Handler().postDelayed({
            icon.visibility = View.GONE
            question.visibility = View.GONE
            yes.visibility = View.GONE
            no.visibility = View.GONE
            val icon1: TextView = findViewById(R.id.hiconyesno)
            val question1: TextView = findViewById(R.id.yesnoquestion)
            question1.text = "How long has your pet been limping?"
            val yes1: Button = findViewById(R.id.yesanswer)
            yes1.text = "More than 24 hours"
            val no1: Button = findViewById(R.id.noanswer)
            no1.text ="Less than 24 hours"
            yes1.isEnabled = true
            no1.isEnabled = true

            icon1.startAnimation(fadein)
            question1.startAnimation(fadein)
            yes1.startAnimation(fadein)
            no1.startAnimation(fadein)
            icon1.visibility = View.VISIBLE
            question1.visibility = View.VISIBLE
            yes1.visibility = View.VISIBLE
            yes1.setOnClickListener {
                proceedToResult(selectedSymptom, 3, "Choice1") // Pass parameters to proceedToResult
            }
            no1.visibility = View.VISIBLE
            no1.setOnClickListener {
                lpq4()
            }
        },1000)
    }
    private fun  lpq2(){
        val fadein = AnimationUtils.loadAnimation(this, R.anim.fade_in)
        val fadeout = AnimationUtils.loadAnimation(this, R.anim.fade_out)
        val icon: TextView = findViewById(R.id.hiconyesno)
        val question: TextView = findViewById(R.id.yesnoquestion)
        val yes: Button = findViewById(R.id.yesanswer)
        val no: Button = findViewById(R.id.noanswer)
        icon.startAnimation(fadeout)
        question.startAnimation(fadeout)
        yes.startAnimation(fadeout)
        no.startAnimation(fadeout)
        yes.isEnabled = false
        no.isEnabled = false
        Handler().postDelayed({
            icon.visibility = View.GONE
            question.visibility = View.GONE
            yes.visibility = View.GONE
            no.visibility = View.GONE
            val icon1: TextView = findViewById(R.id.hiconyesno2)
            val question1: TextView = findViewById(R.id.yesnoquestion2)
            question1.text = "Are either of the following true:\n" +
                    "\n" +
                    "The leg looks swollen, and/or\n" +
                    "Your pet won't let you touch the leg (they might yelp)?"
            val yes1: Button = findViewById(R.id.yesanswer2)
            val no1: Button = findViewById(R.id.noanswer2)
            yes1.isEnabled = true
            no1.isEnabled = true
            icon1.startAnimation(fadein)
            question1.startAnimation(fadein)
            yes1.startAnimation(fadein)
            no1.startAnimation(fadein)
            icon1.visibility = View.VISIBLE
            question1.visibility = View.VISIBLE
            yes1.visibility = View.VISIBLE
            yes1.setOnClickListener {
                proceedToResult(selectedSymptom, 2, "Yes") // Pass parameters to proceedToResult
            }
            no1.visibility = View.VISIBLE
            no1.setOnClickListener {
                lpq3()
            }
        },1000)
    }
    private fun  lpq3_1(){
        val fadein = AnimationUtils.loadAnimation(this, R.anim.fade_in)
        val fadeout = AnimationUtils.loadAnimation(this, R.anim.fade_out)
        val icon: TextView = findViewById(R.id.hiconyesno2)
        val question: TextView = findViewById(R.id.yesnoquestion2)
        val yes: Button = findViewById(R.id.yesanswer2)
        val no: Button = findViewById(R.id.noanswer2)
        icon.startAnimation(fadeout)
        question.startAnimation(fadeout)
        yes.startAnimation(fadeout)
        no.startAnimation(fadeout)
        yes.isEnabled = false
        no.isEnabled = false
        Handler().postDelayed({
            icon.visibility = View.GONE
            question.visibility = View.GONE
            yes.visibility = View.GONE
            no.visibility = View.GONE
            val icon1: TextView = findViewById(R.id.hiconyesno)
            val question1: TextView = findViewById(R.id.yesnoquestion)
            question1.text = "Was there any trauma or an accident? Examples can include:\n" +
                    "\n" +
                    "Your pet falling, \n" +
                    "Getting hit by something, \n" +
                    "Landing on their leg funny, and/or\n" +
                    "Yelping after jumping."
            val yes1: Button = findViewById(R.id.yesanswer)
            yes1.text = "Yes"
            val no1: Button = findViewById(R.id.noanswer)
            no1.text = "No"
            yes1.isEnabled = true
            no1.isEnabled = true

            icon1.startAnimation(fadein)
            question1.startAnimation(fadein)
            yes1.startAnimation(fadein)
            no1.startAnimation(fadein)
            icon1.visibility = View.VISIBLE
            question1.visibility = View.VISIBLE
            yes1.visibility = View.VISIBLE
            yes1.setOnClickListener {
                proceedToResult(selectedSymptom, 3, "Yes") // Pass parameters to proceedToResult
            }
            no1.visibility = View.VISIBLE
            no1.setOnClickListener {
                proceedToResult(selectedSymptom, 3, "No") // Pass parameters to proceedToResult
            }
        },1000)
    }


    private fun  lpq2_1(){
        val fadein = AnimationUtils.loadAnimation(this, R.anim.fade_in)
        val fadeout = AnimationUtils.loadAnimation(this, R.anim.fade_out)
        val icon: TextView = findViewById(R.id.hiconyesno)
        val question: TextView = findViewById(R.id.yesnoquestion)
        val yes: Button = findViewById(R.id.yesanswer)
        val no: Button = findViewById(R.id.noanswer)
        icon.startAnimation(fadeout)
        question.startAnimation(fadeout)
        yes.startAnimation(fadeout)
        no.startAnimation(fadeout)
        yes.isEnabled = false
        no.isEnabled = false
        Handler().postDelayed({
            icon.visibility = View.GONE
            question.visibility = View.GONE
            yes.visibility = View.GONE
            no.visibility = View.GONE
            val icon1: TextView = findViewById(R.id.hiconyesno2)
            val question1: TextView = findViewById(R.id.yesnoquestion2)
            question1.text = "How long has your pet been limping?"
            val yes1: Button = findViewById(R.id.yesanswer2)
            yes1.text = "More than 24 hours"
            val no1: Button = findViewById(R.id.noanswer2)
            no1.text = "Less than 24 hours"
            yes1.isEnabled = true
            no1.isEnabled = true

            icon1.startAnimation(fadein)
            question1.startAnimation(fadein)
            yes1.startAnimation(fadein)
            no1.startAnimation(fadein)
            icon1.visibility = View.VISIBLE
            question1.visibility = View.VISIBLE
            yes1.visibility = View.VISIBLE
            yes1.setOnClickListener {
                proceedToResult(selectedSymptom, 2, "Choice1") // Pass parameters to proceedToResult
            }
            no1.visibility = View.VISIBLE
            no1.setOnClickListener {
                lpq3_1()
            }
        },1000)
    }

    private fun displayLimpingQuestion() {
        val fadein = AnimationUtils.loadAnimation(this, R.anim.fade_in)
        val fadeout = AnimationUtils.loadAnimation(this, R.anim.fade_out)
        val spinner: Spinner = findViewById(R.id.dogsymptom)
        val iconsymptom: TextView = findViewById(R.id.hiconsymptom)
        val symptomq: TextView = findViewById(R.id.dogsymptomquestion)
        val btn: Button = findViewById(R.id.donebtn)
        spinner.startAnimation(fadeout)
        iconsymptom.startAnimation(fadeout)
        symptomq.startAnimation(fadeout)
        btn.startAnimation(fadeout)
        spinner.isEnabled = false
        btn.isEnabled = false
        Handler().postDelayed({

            spinner.visibility = View.GONE
            iconsymptom.visibility = View.GONE
            symptomq.visibility = View.GONE
            btn.visibility = View.GONE
            val icon: TextView = findViewById(R.id.hiconyesno)
            val question: TextView = findViewById(R.id.yesnoquestion)
            question.text = "Can your pet walk on their leg or put it down (without holding it up)?"
            val yes: Button = findViewById(R.id.yesanswer)
            val no: Button = findViewById(R.id.noanswer)
            yes.isEnabled = true
            no.isEnabled = true

            icon.startAnimation(fadein)
            question.startAnimation(fadein)
            yes.startAnimation(fadein)
            no.startAnimation(fadein)
            icon.visibility = View.VISIBLE
            question.visibility = View.VISIBLE
            yes.visibility = View.VISIBLE
            yes.setOnClickListener {
                lpq2_1()
            }
            no.visibility = View.VISIBLE
            no.setOnClickListener {
                lpq2()
            }
        },1000)
    }
    private fun lsq5(){
        val fadein = AnimationUtils.loadAnimation(this, R.anim.fade_in)
        val fadeout = AnimationUtils.loadAnimation(this, R.anim.fade_out)
        val icon: TextView = findViewById(R.id.hiconyesno2)
        val question: TextView = findViewById(R.id.yesnoquestion2)
        val yes: Button = findViewById(R.id.yesanswer2)
        val no: Button = findViewById(R.id.noanswer2)
        icon.startAnimation(fadeout)
        question.startAnimation(fadeout)
        yes.startAnimation(fadeout)
        no.startAnimation(fadeout)
        yes.isEnabled = false
        no.isEnabled = false
        Handler().postDelayed({
            icon.visibility = View.GONE
            question.visibility = View.GONE
            yes.visibility = View.GONE
            no.visibility = View.GONE
            val icon1: TextView = findViewById(R.id.iconchoices4)
            val question1: TextView = findViewById(R.id.questionchoice4)
            question1.text = "What area is your pet licking and scratching?"
            val yes1: Button = findViewById(R.id.ch1ch4)
            yes1.text = "Near the base of the tail (you may see hair loss on their back near their tail)"
            val yes2 : Button = findViewById(R.id.ch2ch4)
            yes2.text = "Licking and biting their paws excessively"
            val no1: Button = findViewById(R.id.ch3ch4)
            no1.text = "Around their mouth or anus"
            val no2 : Button = findViewById(R.id.ch4ch4)
            no2.text = "All over or multiple areas"
            yes1.isEnabled = true
            yes2.isEnabled =true
            no1.isEnabled = true
            no2.isEnabled = true
            icon1.startAnimation(fadein)
            question1.startAnimation(fadein)
            yes1.startAnimation(fadein)
            yes2.startAnimation(fadein)
            no1.startAnimation(fadein)
            no2.startAnimation(fadein)
            icon1.visibility = View.VISIBLE
            question1.visibility = View.VISIBLE
            yes1.visibility = View.VISIBLE
            yes1.setOnClickListener {
                proceedToResult(selectedSymptom, 5, "Choice1") // Pass parameters to proceedToResult
            }
            yes2.visibility = View.VISIBLE
            yes2.setOnClickListener {
                proceedToResult(selectedSymptom, 5, "Choice2") // Pass parameters to proceedToResult
            }
            no1.visibility = View.VISIBLE
            no1.setOnClickListener {
                proceedToResult(selectedSymptom, 5, "Choice3") // Pass parameters to proceedToResult
            }
            no2.visibility = View.VISIBLE
            no2.setOnClickListener {
                proceedToResult(selectedSymptom, 5, "Choice4") // Pass parameters to proceedToResult
            }
        }, 1000)
    }

    private fun lsq4(){
        val fadein = AnimationUtils.loadAnimation(this, R.anim.fade_in)
        val fadeout = AnimationUtils.loadAnimation(this, R.anim.fade_out)
        val icon: TextView = findViewById(R.id.hiconyesno)
        val question: TextView = findViewById(R.id.yesnoquestion)
        val yes: Button = findViewById(R.id.yesanswer)
        val no: Button = findViewById(R.id.noanswer)
        icon.startAnimation(fadeout)
        question.startAnimation(fadeout)
        yes.startAnimation(fadeout)
        no.startAnimation(fadeout)
        yes.isEnabled = false
        no.isEnabled =false
        Handler().postDelayed({
            icon.visibility = View.GONE
            question.visibility = View.GONE
            yes.visibility = View.GONE
            no.visibility = View.GONE
            val icon1: TextView = findViewById(R.id.hiconyesno2)
            val question1: TextView = findViewById(R.id.yesnoquestion2)
            question1.text = "Is there patchy hair loss around your pet's face, ears, torso, or legs?"
            val yes1: Button = findViewById(R.id.yesanswer2)
            val no1: Button = findViewById(R.id.noanswer2)
            yes1.isEnabled = true
            no1.isEnabled = true
            icon1.startAnimation(fadein)
            question1.startAnimation(fadein)
            yes1.startAnimation(fadein)
            no1.startAnimation(fadein)
            icon1.visibility = View.VISIBLE
            question1.visibility = View.VISIBLE
            yes1.visibility = View.VISIBLE
            yes1.setOnClickListener {
                proceedToResult(selectedSymptom, 4, "Yes") // Pass parameters to proceedToResult
            }
            no1.visibility = View.VISIBLE
            no1.setOnClickListener {
                lsq5()
            }
        }, 1000) // Delay for 1000 milliseconds (1 second)
    }

    private fun lsq3(){
        val fadein = AnimationUtils.loadAnimation(this, R.anim.fade_in)
        val fadeout = AnimationUtils.loadAnimation(this, R.anim.fade_out)
        val icon: TextView = findViewById(R.id.hiconyesno2)
        val question: TextView = findViewById(R.id.yesnoquestion2)
        val yes: Button = findViewById(R.id.yesanswer2)
        val no: Button = findViewById(R.id.noanswer2)
        icon.startAnimation(fadeout)
        question.startAnimation(fadeout)
        yes.startAnimation(fadeout)
        no.startAnimation(fadeout)
        yes.isEnabled = false
        no.isEnabled =false
        Handler().postDelayed({
            icon.visibility = View.GONE
            question.visibility = View.GONE
            yes.visibility = View.GONE
            no.visibility = View.GONE
            val icon1: TextView = findViewById(R.id.hiconyesno)
            val question1: TextView = findViewById(R.id.yesnoquestion)
            question1.text = "Has your pet created a wound from scratching themselves?"
            val yes1: Button = findViewById(R.id.yesanswer)
            val no1: Button = findViewById(R.id.noanswer)
            yes1.isEnabled = true
            no1.isEnabled = true
            icon1.startAnimation(fadein)
            question1.startAnimation(fadein)
            yes1.startAnimation(fadein)
            no1.startAnimation(fadein)
            icon1.visibility = View.VISIBLE
            question1.visibility = View.VISIBLE
            yes1.visibility = View.VISIBLE
            yes1.setOnClickListener {
                proceedToResult(selectedSymptom, 3, "Yes") // Pass parameters to proceedToResult
            }
            no1.visibility = View.VISIBLE
            no1.setOnClickListener {
                lsq4()
            }
        }, 1000) // Delay for 1000 milliseconds (1 second)
    }
    private fun lsq2(){
        val fadein = AnimationUtils.loadAnimation(this, R.anim.fade_in)
        val fadeout = AnimationUtils.loadAnimation(this, R.anim.fade_out)
        val icon: TextView = findViewById(R.id.hiconyesno)
        val question: TextView = findViewById(R.id.yesnoquestion)
        val yes: Button = findViewById(R.id.yesanswer)
        val no: Button = findViewById(R.id.noanswer)
        icon.startAnimation(fadeout)
        question.startAnimation(fadeout)
        yes.startAnimation(fadeout)
        no.startAnimation(fadeout)
        yes.isEnabled = false
        no.isEnabled =false
        Handler().postDelayed({
            icon.visibility = View.GONE
            question.visibility = View.GONE
            yes.visibility = View.GONE
            no.visibility = View.GONE
            val icon1: TextView = findViewById(R.id.hiconyesno2)
            val question1: TextView = findViewById(R.id.yesnoquestion2)
            question1.text = "Has your pet been in contact with any skin irritants? Skin irritants may include:\n" +
                    "\n" +
                    "Essential oils, \n" +
                    "Soaps, \n" +
                    "Perfumes, \n" +
                    "Household cleaners, and/or\n" +
                    "Poisonous plants."
            val yes1: Button = findViewById(R.id.yesanswer2)
            val no1: Button = findViewById(R.id.noanswer2)
            yes1.isEnabled = true
            no1.isEnabled = true
            icon1.startAnimation(fadein)
            question1.startAnimation(fadein)
            yes1.startAnimation(fadein)
            no1.startAnimation(fadein)
            icon1.visibility = View.VISIBLE
            question1.visibility = View.VISIBLE
            yes1.visibility = View.VISIBLE
            yes1.setOnClickListener {
                proceedToResult(selectedSymptom, 2, "Yes") // Pass parameters to proceedToResult
            }
            no1.visibility = View.VISIBLE
            no1.setOnClickListener {
                lsq3()
            }
        }, 1000) // Delay for 1000 milliseconds (1 second)
    }

    private fun displayLickingAndScratchingQuestion() {
        val fadein = AnimationUtils.loadAnimation(this, R.anim.fade_in)
        val fadeout = AnimationUtils.loadAnimation(this, R.anim.fade_out)
        val spinner: Spinner = findViewById(R.id.dogsymptom)
        val iconsymptom: TextView = findViewById(R.id.hiconsymptom)
        val symptomq: TextView = findViewById(R.id.dogsymptomquestion)
        val btn: Button = findViewById(R.id.donebtn)
        spinner.startAnimation(fadeout)
        iconsymptom.startAnimation(fadeout)
        symptomq.startAnimation(fadeout)
        btn.startAnimation(fadeout)
        spinner.isEnabled = false
        btn.isEnabled = false
        Handler().postDelayed({

            spinner.visibility = View.GONE
            iconsymptom.visibility = View.GONE
            symptomq.visibility = View.GONE
            btn.visibility = View.GONE
            val icon: TextView = findViewById(R.id.hiconyesno)
            val question: TextView = findViewById(R.id.yesnoquestion)
            question.text = "Does your pet have any of the following symptoms? Symptoms include:\n" +
                    "\n" +
                    "Swollen face, \n" +
                    "Swollen eye area, \n" +
                    "Swollen lips, \n" +
                    "Hives anywhere (check the belly), \n" +
                    "Excessive panting, \n" +
                    "Vomiting, and/or\n" +
                    "Diarrhea."
            val yes: Button = findViewById(R.id.yesanswer)
            val no: Button = findViewById(R.id.noanswer)
            yes.isEnabled = true
            no.isEnabled = true

            icon.startAnimation(fadein)
            question.startAnimation(fadein)
            yes.startAnimation(fadein)
            no.startAnimation(fadein)
            icon.visibility = View.VISIBLE
            question.visibility = View.VISIBLE
            yes.visibility = View.VISIBLE
            yes.setOnClickListener {
                proceedToResult(selectedSymptom, 1, "Yes") // Pass parameters to proceedToResult
            }
            no.visibility = View.VISIBLE
            no.setOnClickListener {
                lsq2()
            }
        },1000)
    }


    private fun displayLethargicQuestion() {
        val fadein = AnimationUtils.loadAnimation(this, R.anim.fade_in)
        val fadeout = AnimationUtils.loadAnimation(this, R.anim.fade_out)
        val spinner: Spinner = findViewById(R.id.dogsymptom)
        val iconsymptom: TextView = findViewById(R.id.hiconsymptom)
        val symptomq: TextView = findViewById(R.id.dogsymptomquestion)
        val btn: Button = findViewById(R.id.donebtn)
        spinner.startAnimation(fadeout)
        iconsymptom.startAnimation(fadeout)
        symptomq.startAnimation(fadeout)
        btn.startAnimation(fadeout)
        spinner.isEnabled = false
        btn.isEnabled = false
        Handler().postDelayed({

            spinner.visibility = View.GONE
            iconsymptom.visibility = View.GONE
            symptomq.visibility = View.GONE
            btn.visibility = View.GONE
            val icon: TextView = findViewById(R.id.hiconyesno)
            val question: TextView = findViewById(R.id.yesnoquestion)
            question.text = "Is your pet responding to you, getting up and moving around, and interested in food and water?"
            val yes: Button = findViewById(R.id.yesanswer)
            val no: Button = findViewById(R.id.noanswer)
            yes.isEnabled = true
            no.isEnabled = true

            icon.startAnimation(fadein)
            question.startAnimation(fadein)
            yes.startAnimation(fadein)
            no.startAnimation(fadein)
            icon.visibility = View.VISIBLE
            question.visibility = View.VISIBLE
            yes.visibility = View.VISIBLE
            yes.setOnClickListener {
                proceedToResult(selectedSymptom, 1, "Yes") // Pass parameters to proceedToResult
            }
            no.visibility = View.VISIBLE
            no.setOnClickListener {
                proceedToResult(selectedSymptom, 1, "No") // Pass parameters to proceedToResult
            }
        },1000)
    }

    private fun ihq6(){
        val fadein = AnimationUtils.loadAnimation(this, R.anim.fade_in)
        val fadeout = AnimationUtils.loadAnimation(this, R.anim.fade_out)
        val icon: TextView = findViewById(R.id.hiconyesno)
        val question: TextView = findViewById(R.id.yesnoquestion)
        val yes: Button = findViewById(R.id.yesanswer)
        val no: Button = findViewById(R.id.noanswer)
        icon.startAnimation(fadeout)
        question.startAnimation(fadeout)
        yes.startAnimation(fadeout)
        no.startAnimation(fadeout)
        yes.isEnabled = false
        no.isEnabled =false
        Handler().postDelayed({
            icon.visibility = View.GONE
            question.visibility = View.GONE
            yes.visibility = View.GONE
            no.visibility = View.GONE
            val icon1: TextView = findViewById(R.id.iconchoices4)
            val question1: TextView = findViewById(R.id.questionchoice4)
            question1.text = "Which spot(s) are they scratching and/or licking?"
            val yes1: Button = findViewById(R.id.ch1ch4)
            yes1.text = "Near the base of their tail (you may see hair loss above the tail on their back)"
            val yes2 : Button = findViewById(R.id.ch2ch4)
            yes2.text = "Around their ears/neck"
            val no1: Button = findViewById(R.id.ch3ch4)
            no1.text = "Around their mouth or anus"
            val no2 : Button = findViewById(R.id.ch4ch4)
            no2.text = "Licking their paws excessively"
            yes1.isEnabled = true
            yes2.isEnabled = true
            no2.isEnabled = true
            no1.isEnabled = true
            icon1.startAnimation(fadein)
            question1.startAnimation(fadein)
            yes1.startAnimation(fadein)
            yes2.startAnimation(fadein)
            no2.startAnimation(fadein)
            no1.startAnimation(fadein)
            icon1.visibility = View.VISIBLE
            question1.visibility = View.VISIBLE
            yes1.visibility = View.VISIBLE
            yes1.setOnClickListener {
                proceedToResult(selectedSymptom, 6, "Choice1") // Pass parameters to proceedToResult
            }
            yes2.visibility = View.VISIBLE
            yes2.setOnClickListener {
                proceedToResult(selectedSymptom, 6, "Choice2") // Pass parameters to proceedToResult
            }
            no1.visibility = View.VISIBLE
            no1.setOnClickListener {
                proceedToResult(selectedSymptom, 6, "Choice3") // Pass parameters to proceedToResult
            }
            no2.visibility = View.VISIBLE
            no2.setOnClickListener {
                proceedToResult(selectedSymptom, 6, "Choice4") // Pass parameters to proceedToResult
            }
        }, 1000) // Delay for 1000 milliseconds (1 second)
    }


    private fun ihq5(){
        val fadein = AnimationUtils.loadAnimation(this, R.anim.fade_in)
        val fadeout = AnimationUtils.loadAnimation(this, R.anim.fade_out)
        val icon: TextView = findViewById(R.id.hiconyesno2)
        val question: TextView = findViewById(R.id.yesnoquestion2)
        val yes: Button = findViewById(R.id.yesanswer2)
        val no: Button = findViewById(R.id.noanswer2)
        icon.startAnimation(fadeout)
        question.startAnimation(fadeout)
        yes.startAnimation(fadeout)
        no.startAnimation(fadeout)
        yes.isEnabled = false
        no.isEnabled = false
        Handler().postDelayed({
            icon.visibility = View.GONE
            question.visibility = View.GONE
            yes.visibility = View.GONE
            no.visibility = View.GONE
            val icon1: TextView = findViewById(R.id.hiconyesno)
            val question1: TextView = findViewById(R.id.yesnoquestion)
            question1.text = "Is your pet scratching just one or two spots or all over?"
            val yes1: Button = findViewById(R.id.yesanswer)
            yes1.text = "All Over"
            val no1: Button = findViewById(R.id.noanswer)
            no1.text = "One or Two Spots"
            yes1.isEnabled = true
            no1.isEnabled = true
            icon1.startAnimation(fadein)
            question1.startAnimation(fadein)
            yes1.startAnimation(fadein)
            no1.startAnimation(fadein)
            icon1.visibility = View.VISIBLE
            question1.visibility = View.VISIBLE
            yes1.visibility = View.VISIBLE
            yes1.setOnClickListener {
                proceedToResult(selectedSymptom, 5, "Choice1") // Pass parameters to proceedToResult
            }
            no1.visibility = View.VISIBLE
            no1.setOnClickListener {
                ihq6()
            }
        }, 1000)
    }

    private fun ihq4(){
        val fadein = AnimationUtils.loadAnimation(this, R.anim.fade_in)
        val fadeout = AnimationUtils.loadAnimation(this, R.anim.fade_out)
        val icon: TextView = findViewById(R.id.hiconyesno)
        val question: TextView = findViewById(R.id.yesnoquestion)
        val yes: Button = findViewById(R.id.yesanswer)
        val no: Button = findViewById(R.id.noanswer)
        icon.startAnimation(fadeout)
        question.startAnimation(fadeout)
        yes.startAnimation(fadeout)
        no.startAnimation(fadeout)
        yes.isEnabled = false
        no.isEnabled =false
        Handler().postDelayed({
            icon.visibility = View.GONE
            question.visibility = View.GONE
            yes.visibility = View.GONE
            no.visibility = View.GONE
            val icon1: TextView = findViewById(R.id.hiconyesno2)
            val question1: TextView = findViewById(R.id.yesnoquestion2)
            question1.text = "Is there patchy hair loss on your pet's face/ears, torso (body), or legs?"
            val yes1: Button = findViewById(R.id.yesanswer2)
            yes1.text = "Yes"
            val no1: Button = findViewById(R.id.noanswer2)
            yes1.isEnabled = true
            no1.isEnabled = true
            icon1.startAnimation(fadein)
            question1.startAnimation(fadein)
            yes1.startAnimation(fadein)
            no1.startAnimation(fadein)
            icon1.visibility = View.VISIBLE
            question1.visibility = View.VISIBLE
            yes1.visibility = View.VISIBLE
            yes1.setOnClickListener {
                proceedToResult(selectedSymptom, 4, "Yes") // Pass parameters to proceedToResult
            }
            no1.visibility = View.VISIBLE
            no1.setOnClickListener {
                ihq5()
            }
        }, 1000) // Delay for 1000 milliseconds (1 second)
    }

    private fun ihq3(){
        val fadein = AnimationUtils.loadAnimation(this, R.anim.fade_in)
        val fadeout = AnimationUtils.loadAnimation(this, R.anim.fade_out)
        val icon: TextView = findViewById(R.id.hiconyesno2)
        val question: TextView = findViewById(R.id.yesnoquestion2)
        val yes: Button = findViewById(R.id.yesanswer2)
        val no: Button = findViewById(R.id.noanswer2)
        icon.startAnimation(fadeout)
        question.startAnimation(fadeout)
        yes.startAnimation(fadeout)
        no.startAnimation(fadeout)
        yes.isEnabled = false
        no.isEnabled =false
        Handler().postDelayed({
            icon.visibility = View.GONE
            question.visibility = View.GONE
            yes.visibility = View.GONE
            no.visibility = View.GONE
            val icon1: TextView = findViewById(R.id.hiconyesno)
            val question1: TextView = findViewById(R.id.yesnoquestion)
            question1.text = "Has your pet been in contact with any skin irritants? Skin irritants may include: \n" +
                    "\n" +
                    "Essential oils, \n" +
                    "Soaps, \n" +
                    "Perfumes, \n" +
                    "Poisonous plants, and/or\n" +
                    "Household cleaners."
            val yes1: Button = findViewById(R.id.yesanswer)
            val no1: Button = findViewById(R.id.noanswer)
            yes1.isEnabled = true
            no1.isEnabled = true
            icon1.startAnimation(fadein)
            question1.startAnimation(fadein)
            yes1.startAnimation(fadein)
            no1.startAnimation(fadein)
            icon1.visibility = View.VISIBLE
            question1.visibility = View.VISIBLE
            yes1.visibility = View.VISIBLE
            yes1.setOnClickListener {
                proceedToResult(selectedSymptom, 3, "Yes") // Pass parameters to proceedToResult
            }
            no1.visibility = View.VISIBLE
            no1.setOnClickListener {
                ihq4()
            }
        }, 1000) // Delay for 1000 milliseconds (1 second)
    }
    private fun ihq2(){
        val fadein = AnimationUtils.loadAnimation(this, R.anim.fade_in)
        val fadeout = AnimationUtils.loadAnimation(this, R.anim.fade_out)
        val icon: TextView = findViewById(R.id.hiconyesno)
        val question: TextView = findViewById(R.id.yesnoquestion)
        val yes: Button = findViewById(R.id.yesanswer)
        val no: Button = findViewById(R.id.noanswer)
        icon.startAnimation(fadeout)
        question.startAnimation(fadeout)
        yes.startAnimation(fadeout)
        no.startAnimation(fadeout)
        yes.isEnabled = false
        no.isEnabled =false
        Handler().postDelayed({
            icon.visibility = View.GONE
            question.visibility = View.GONE
            yes.visibility = View.GONE
            no.visibility = View.GONE
            val icon1: TextView = findViewById(R.id.hiconyesno2)
            val question1: TextView = findViewById(R.id.yesnoquestion2)
            question1.text = "Has your pet created a wound from scratching their skin?"
            val yes1: Button = findViewById(R.id.yesanswer2)
            val no1: Button = findViewById(R.id.noanswer2)
            yes1.isEnabled = true
            no1.isEnabled = true
            icon1.startAnimation(fadein)
            question1.startAnimation(fadein)
            yes1.startAnimation(fadein)
            no1.startAnimation(fadein)
            icon1.visibility = View.VISIBLE
            question1.visibility = View.VISIBLE
            yes1.visibility = View.VISIBLE
            yes1.setOnClickListener {
                proceedToResult(selectedSymptom, 2, "Yes") // Pass parameters to proceedToResult
            }
            no1.visibility = View.VISIBLE
            no1.setOnClickListener {
                ihq3()
            }
        }, 1000) // Delay for 1000 milliseconds (1 second)
    }

    private fun displayItchingQuestion() {
        val fadein = AnimationUtils.loadAnimation(this, R.anim.fade_in)
        val fadeout = AnimationUtils.loadAnimation(this, R.anim.fade_out)
        val spinner: Spinner = findViewById(R.id.dogsymptom)
        val iconsymptom: TextView = findViewById(R.id.hiconsymptom)
        val symptomq: TextView = findViewById(R.id.dogsymptomquestion)
        val btn: Button = findViewById(R.id.donebtn)
        spinner.startAnimation(fadeout)
        iconsymptom.startAnimation(fadeout)
        symptomq.startAnimation(fadeout)
        btn.startAnimation(fadeout)
        spinner.isEnabled = false
        btn.isEnabled = false
        Handler().postDelayed({

            spinner.visibility = View.GONE
            iconsymptom.visibility = View.GONE
            symptomq.visibility = View.GONE
            btn.visibility = View.GONE
            val icon: TextView = findViewById(R.id.hiconyesno)
            val question: TextView = findViewById(R.id.yesnoquestion)
            question.text = "Does your pet have any of these signs? Signs can include: \n" +
                    "\n" +
                    "Swollen face, \n" +
                    "Swollen lips,\n" +
                    "Swollen eye area, \n" +
                    "Hives anywhere (be sure to look on the belly), and/or\n" +
                    "Excessive panting."
            val yes: Button = findViewById(R.id.yesanswer)
            val no: Button = findViewById(R.id.noanswer)
            yes.isEnabled = true
            no.isEnabled = true

            icon.startAnimation(fadein)
            question.startAnimation(fadein)
            yes.startAnimation(fadein)
            no.startAnimation(fadein)
            icon.visibility = View.VISIBLE
            question.visibility = View.VISIBLE
            yes.visibility = View.VISIBLE
            yes.setOnClickListener {
                proceedToResult(selectedSymptom, 1, "Yes") // Pass parameters to proceedToResult
            }
            no.visibility = View.VISIBLE
            no.setOnClickListener {
                ihq2()
            }
        },1000)
    }


    private fun hlq8(){
        val fadein = AnimationUtils.loadAnimation(this, R.anim.fade_in)
        val fadeout = AnimationUtils.loadAnimation(this, R.anim.fade_out)
        val icon: TextView = findViewById(R.id.hiconyesno)
        val question: TextView = findViewById(R.id.yesnoquestion)
        val yes: Button = findViewById(R.id.yesanswer)
        val no: Button = findViewById(R.id.noanswer)
        icon.startAnimation(fadeout)
        question.startAnimation(fadeout)
        yes.startAnimation(fadeout)
        no.startAnimation(fadeout)
        yes.isEnabled = false
        no.isEnabled = false
        Handler().postDelayed({
            icon.visibility = View.GONE
            question.visibility = View.GONE
            yes.visibility = View.GONE
            no.visibility = View.GONE
            val icon1: TextView = findViewById(R.id.iconchoices4)
            val question1: TextView = findViewById(R.id.questionchoice4)
            question1.text = "Where is the hair loss?"
            val yes1: Button = findViewById(R.id.ch1ch4)
            yes1.text = "Near the base of the tail or around the head/neck"
            val yes2 : Button = findViewById(R.id.ch2ch4)
            yes2.text = "On the trunk, on both sides of spine, or near hip area on both sides"
            val no1: Button = findViewById(R.id.ch3ch4)
            no1.text = "All over or other areas"
            val no2 : Button = findViewById(R.id.ch4ch4)
            no2.text = "Patchy spots in different areas of the body"
            yes1.isEnabled = true
            no1.isEnabled = true
            yes2.isEnabled = true
            no2.isEnabled = true
            icon1.startAnimation(fadein)
            question1.startAnimation(fadein)
            yes1.startAnimation(fadein)
            no1.startAnimation(fadein)
            yes2.startAnimation(fadein)
            no2.startAnimation(fadein)
            icon1.visibility = View.VISIBLE
            question1.visibility = View.VISIBLE
            yes1.visibility = View.VISIBLE
            yes1.setOnClickListener {
                proceedToResult(selectedSymptom, 8, "Choice1") // Pass parameters to proceedToResult
            }
            yes2.visibility = View.VISIBLE
            yes2.setOnClickListener {
                proceedToResult(selectedSymptom, 8, "Choice2") // Pass parameters to proceedToResult
            }
            no1.visibility = View.VISIBLE
            no1.setOnClickListener {
                proceedToResult(selectedSymptom, 8, "Choice3")
            }
            no2.visibility = View.VISIBLE
            no2.setOnClickListener {
                proceedToResult(selectedSymptom, 8, "Choice4")
            }
        }, 1000)
    }
    private fun hlq7(){
        val fadein = AnimationUtils.loadAnimation(this, R.anim.fade_in)
        val fadeout = AnimationUtils.loadAnimation(this, R.anim.fade_out)
        val icon: TextView = findViewById(R.id.hiconyesno2)
        val question: TextView = findViewById(R.id.yesnoquestion2)
        val yes: Button = findViewById(R.id.yesanswer2)
        val no: Button = findViewById(R.id.noanswer2)
        icon.startAnimation(fadeout)
        question.startAnimation(fadeout)
        yes.startAnimation(fadeout)
        no.startAnimation(fadeout)
        yes.isEnabled = false
        no.isEnabled = false
        Handler().postDelayed({
            icon.visibility = View.GONE
            question.visibility = View.GONE
            yes.visibility = View.GONE
            no.visibility = View.GONE
            val icon1: TextView = findViewById(R.id.hiconyesno)
            val question1: TextView = findViewById(R.id.yesnoquestion)
            question1.text = "Does the hair loss only happen at certain times of the year?"
            val yes1: Button = findViewById(R.id.yesanswer)
            val no1: Button = findViewById(R.id.noanswer)
            yes1.isEnabled = true
            no1.isEnabled = true
            icon1.startAnimation(fadein)
            question1.startAnimation(fadein)
            yes1.startAnimation(fadein)
            no1.startAnimation(fadein)
            icon1.visibility = View.VISIBLE
            question1.visibility = View.VISIBLE
            yes1.visibility = View.VISIBLE
            yes1.setOnClickListener {
                proceedToResult(selectedSymptom, 7, "Yes") // Pass parameters to proceedToResult
            }
            no1.visibility = View.VISIBLE
            no1.setOnClickListener {
                hlq8() // Pass parameters to proceedToResult
            }
        }, 1000)
    }

    private fun hlq6(){
        val fadein = AnimationUtils.loadAnimation(this, R.anim.fade_in)
        val fadeout = AnimationUtils.loadAnimation(this, R.anim.fade_out)
        val icon: TextView = findViewById(R.id.hiconyesno)
        val question: TextView = findViewById(R.id.yesnoquestion)
        val yes: Button = findViewById(R.id.yesanswer)
        val no: Button = findViewById(R.id.noanswer)
        icon.startAnimation(fadeout)
        question.startAnimation(fadeout)
        yes.startAnimation(fadeout)
        no.startAnimation(fadeout)
        yes.isEnabled = false
        no.isEnabled =false
        Handler().postDelayed({
            icon.visibility = View.GONE
            question.visibility = View.GONE
            yes.visibility = View.GONE
            no.visibility = View.GONE
            val icon1: TextView = findViewById(R.id.hiconyesno2)
            val question1: TextView = findViewById(R.id.yesnoquestion2)
            question1.text = "Has your pet gained weight or become lethargic, or do they have dull coat?"
            val yes1: Button = findViewById(R.id.yesanswer2)
            yes1.text = "Yes"
            val no1: Button = findViewById(R.id.noanswer2)
            yes1.isEnabled = true
            no1.isEnabled = true
            icon1.startAnimation(fadein)
            question1.startAnimation(fadein)
            yes1.startAnimation(fadein)
            no1.startAnimation(fadein)
            icon1.visibility = View.VISIBLE
            question1.visibility = View.VISIBLE
            yes1.visibility = View.VISIBLE
            yes1.setOnClickListener {
                proceedToResult(selectedSymptom, 6, "Yes") // Pass parameters to proceedToResult
            }
            no1.visibility = View.VISIBLE
            no1.setOnClickListener {
                hlq7()
            }
        }, 1000) // Delay for 1000 milliseconds (1 second)
    }


    private fun hlq5(){
        val fadein = AnimationUtils.loadAnimation(this, R.anim.fade_in)
        val fadeout = AnimationUtils.loadAnimation(this, R.anim.fade_out)
        val icon: TextView = findViewById(R.id.hiconyesno2)
        val question: TextView = findViewById(R.id.yesnoquestion2)
        val yes: Button = findViewById(R.id.yesanswer2)
        val no: Button = findViewById(R.id.noanswer2)
        icon.startAnimation(fadeout)
        question.startAnimation(fadeout)
        yes.startAnimation(fadeout)
        no.startAnimation(fadeout)
        yes.isEnabled = false
        no.isEnabled = false
        Handler().postDelayed({
            icon.visibility = View.GONE
            question.visibility = View.GONE
            yes.visibility = View.GONE
            no.visibility = View.GONE
            val icon1: TextView = findViewById(R.id.hiconyesno)
            val question1: TextView = findViewById(R.id.yesnoquestion)
            question1.text = "Has your pet been recently vaccinated in that area, or have you applied anything (like topical flea/tick solution) to that area recently?"
            val yes1: Button = findViewById(R.id.yesanswer)
            val no1: Button = findViewById(R.id.noanswer)
            yes1.isEnabled = true
            no1.isEnabled = true
            icon1.startAnimation(fadein)
            question1.startAnimation(fadein)
            yes1.startAnimation(fadein)
            no1.startAnimation(fadein)
            icon1.visibility = View.VISIBLE
            question1.visibility = View.VISIBLE
            yes1.visibility = View.VISIBLE
            yes1.setOnClickListener {
                proceedToResult(selectedSymptom, 5, "Yes") // Pass parameters to proceedToResult
            }
            no1.visibility = View.VISIBLE
            no1.setOnClickListener {
                hlq6()
            }
        }, 1000)
    }

    private fun hlq4(){
        val fadein = AnimationUtils.loadAnimation(this, R.anim.fade_in)
        val fadeout = AnimationUtils.loadAnimation(this, R.anim.fade_out)
        val icon: TextView = findViewById(R.id.hiconyesno)
        val question: TextView = findViewById(R.id.yesnoquestion)
        val yes: Button = findViewById(R.id.yesanswer)
        val no: Button = findViewById(R.id.noanswer)
        icon.startAnimation(fadeout)
        question.startAnimation(fadeout)
        yes.startAnimation(fadeout)
        no.startAnimation(fadeout)
        yes.isEnabled = false
        no.isEnabled =false
        Handler().postDelayed({
            icon.visibility = View.GONE
            question.visibility = View.GONE
            yes.visibility = View.GONE
            no.visibility = View.GONE
            val icon1: TextView = findViewById(R.id.hiconyesno2)
            val question1: TextView = findViewById(R.id.yesnoquestion2)
            question1.text = "Does your pet seem uncomfortable? Examples would include:\n" +
                    "\n" +
                    "Excessively licking, \n" +
                    "\n" +
                    "Scratching, and/or\n" +
                    "\n" +
                    "Chewing the area."
            val yes1: Button = findViewById(R.id.yesanswer2)
            yes1.text = "Yes"
            val no1: Button = findViewById(R.id.noanswer2)
            yes1.isEnabled = true
            no1.isEnabled = true
            icon1.startAnimation(fadein)
            question1.startAnimation(fadein)
            yes1.startAnimation(fadein)
            no1.startAnimation(fadein)
            icon1.visibility = View.VISIBLE
            question1.visibility = View.VISIBLE
            yes1.visibility = View.VISIBLE
            yes1.setOnClickListener {
                proceedToResult(selectedSymptom, 4, "Yes") // Pass parameters to proceedToResult
            }
            no1.visibility = View.VISIBLE
            no1.setOnClickListener {
                hlq5()
            }
        }, 1000) // Delay for 1000 milliseconds (1 second)
    }

    private fun hlq3(){
        val fadein = AnimationUtils.loadAnimation(this, R.anim.fade_in)
        val fadeout = AnimationUtils.loadAnimation(this, R.anim.fade_out)
        val icon: TextView = findViewById(R.id.hiconyesno2)
        val question: TextView = findViewById(R.id.yesnoquestion2)
        val yes: Button = findViewById(R.id.yesanswer2)
        val no: Button = findViewById(R.id.noanswer2)
        icon.startAnimation(fadeout)
        question.startAnimation(fadeout)
        yes.startAnimation(fadeout)
        no.startAnimation(fadeout)
        yes.isEnabled = false
        no.isEnabled =false
        Handler().postDelayed({
            icon.visibility = View.GONE
            question.visibility = View.GONE
            yes.visibility = View.GONE
            no.visibility = View.GONE
            val icon1: TextView = findViewById(R.id.hiconyesno)
            val question1: TextView = findViewById(R.id.yesnoquestion)
            question1.text = "Has your pet been bitten by an insect or spider?"
            val yes1: Button = findViewById(R.id.yesanswer)
            val no1: Button = findViewById(R.id.noanswer)
            yes1.isEnabled = true
            no1.isEnabled = true
            icon1.startAnimation(fadein)
            question1.startAnimation(fadein)
            yes1.startAnimation(fadein)
            no1.startAnimation(fadein)
            icon1.visibility = View.VISIBLE
            question1.visibility = View.VISIBLE
            yes1.visibility = View.VISIBLE
            yes1.setOnClickListener {
                proceedToResult(selectedSymptom, 3, "Yes") // Pass parameters to proceedToResult
            }
            no1.visibility = View.VISIBLE
            no1.setOnClickListener {
                hlq4()
            }
        }, 1000) // Delay for 1000 milliseconds (1 second)
    }
    private fun hlq2(){
        val fadein = AnimationUtils.loadAnimation(this, R.anim.fade_in)
        val fadeout = AnimationUtils.loadAnimation(this, R.anim.fade_out)
        val icon: TextView = findViewById(R.id.hiconyesno)
        val question: TextView = findViewById(R.id.yesnoquestion)
        val yes: Button = findViewById(R.id.yesanswer)
        val no: Button = findViewById(R.id.noanswer)
        icon.startAnimation(fadeout)
        question.startAnimation(fadeout)
        yes.startAnimation(fadeout)
        no.startAnimation(fadeout)
        yes.isEnabled = false
        no.isEnabled =false
        Handler().postDelayed({
            icon.visibility = View.GONE
            question.visibility = View.GONE
            yes.visibility = View.GONE
            no.visibility = View.GONE
            val icon1: TextView = findViewById(R.id.hiconyesno2)
            val question1: TextView = findViewById(R.id.yesnoquestion2)
            question1.text = "Is there any discharge or oozing skin?"
            val yes1: Button = findViewById(R.id.yesanswer2)
            yes1.text = "Yes"
            val no1: Button = findViewById(R.id.noanswer2)
            yes1.isEnabled = true
            no1.isEnabled = true
            icon1.startAnimation(fadein)
            question1.startAnimation(fadein)
            yes1.startAnimation(fadein)
            no1.startAnimation(fadein)
            icon1.visibility = View.VISIBLE
            question1.visibility = View.VISIBLE
            yes1.visibility = View.VISIBLE
            yes1.setOnClickListener {
                proceedToResult(selectedSymptom, 2, "Yes") // Pass parameters to proceedToResult
            }
            no1.visibility = View.VISIBLE
            no1.setOnClickListener {
                hlq3()
            }
        }, 1000) // Delay for 1000 milliseconds (1 second)
    }

    private fun displayHairLossQuestion() {
        val fadein = AnimationUtils.loadAnimation(this, R.anim.fade_in)
        val fadeout = AnimationUtils.loadAnimation(this, R.anim.fade_out)
        val spinner: Spinner = findViewById(R.id.dogsymptom)
        val iconsymptom: TextView = findViewById(R.id.hiconsymptom)
        val symptomq: TextView = findViewById(R.id.dogsymptomquestion)
        val btn: Button = findViewById(R.id.donebtn)
        spinner.startAnimation(fadeout)
        iconsymptom.startAnimation(fadeout)
        symptomq.startAnimation(fadeout)
        btn.startAnimation(fadeout)
        spinner.isEnabled = false
        btn.isEnabled = false
        Handler().postDelayed({

            spinner.visibility = View.GONE
            iconsymptom.visibility = View.GONE
            symptomq.visibility = View.GONE
            btn.visibility = View.GONE
            val icon: TextView = findViewById(R.id.hiconyesno)
            val question: TextView = findViewById(R.id.yesnoquestion)
            question.text = "Does your pet have any deep/raw sores or excessive bleeding?"
            val yes: Button = findViewById(R.id.yesanswer)
            val no: Button = findViewById(R.id.noanswer)
            yes.isEnabled = true
            no.isEnabled = true

            icon.startAnimation(fadein)
            question.startAnimation(fadein)
            yes.startAnimation(fadein)
            no.startAnimation(fadein)
            icon.visibility = View.VISIBLE
            question.visibility = View.VISIBLE
            yes.visibility = View.VISIBLE
            yes.setOnClickListener {
                proceedToResult(selectedSymptom, 1, "Yes") // Pass parameters to proceedToResult
            }
            no.visibility = View.VISIBLE
            no.setOnClickListener {
                hlq2()
            }
        },1000)
    }


    private fun epq7(){
        val fadein = AnimationUtils.loadAnimation(this, R.anim.fade_in)
        val fadeout = AnimationUtils.loadAnimation(this, R.anim.fade_out)
        val icon: TextView = findViewById(R.id.hiconyesno2)
        val question: TextView = findViewById(R.id.yesnoquestion2)
        val yes: Button = findViewById(R.id.yesanswer2)
        val no: Button = findViewById(R.id.noanswer2)
        icon.startAnimation(fadeout)
        question.startAnimation(fadeout)
        yes.startAnimation(fadeout)
        no.startAnimation(fadeout)
        yes.isEnabled = false
        no.isEnabled = false
        Handler().postDelayed({
            icon.visibility = View.GONE
            question.visibility = View.GONE
            yes.visibility = View.GONE
            no.visibility = View.GONE
            val icon1: TextView = findViewById(R.id.iconchoices4)
            val question1: TextView = findViewById(R.id.questionchoice4)
            question1.text = "Check the eyelid/skin around the eye. Do you see:\n" +
                    "\n" +
                    "Swelling,\n" +
                    "A growth, and/or\n" +
                    "Fur loss"
            val yes1: Button = findViewById(R.id.ch1ch4)
            yes1.text = "Swelling"
            val yes2 : Button = findViewById(R.id.ch2ch4)
            yes2.text = "A growth, and/or"
            val no1: Button = findViewById(R.id.ch3ch4)
            no1.text = "Fur Loss"
            yes1.isEnabled = true
            yes2.isEnabled = true
            no1.isEnabled = true
            icon1.startAnimation(fadein)
            question1.startAnimation(fadein)
            yes1.startAnimation(fadein)
            no1.startAnimation(fadein)
            yes2.startAnimation(fadein)
            icon1.visibility = View.VISIBLE
            question1.visibility = View.VISIBLE
            yes1.visibility = View.VISIBLE
            yes1.setOnClickListener {
                proceedToResult(selectedSymptom, 7, "Choice1") // Pass parameters to proceedToResult
            }
            yes2.visibility = View.VISIBLE
            yes2.setOnClickListener {
                proceedToResult(selectedSymptom, 7, "Choice2") // Pass parameters to proceedToResult
            }
            no1.visibility = View.VISIBLE
            no1.setOnClickListener {
                proceedToResult(selectedSymptom, 7, "Choice3") // Pass parameters to proceedToResult
            }
        }, 1000)
    }

    private fun epq6(){
        val fadein = AnimationUtils.loadAnimation(this, R.anim.fade_in)
        val fadeout = AnimationUtils.loadAnimation(this, R.anim.fade_out)
        val icon: TextView = findViewById(R.id.iconchoices4)
        val question: TextView = findViewById(R.id.questionchoice4)
        val yes: Button = findViewById(R.id.ch1ch4)
        val no: Button = findViewById(R.id.ch2ch4)
        val yes1 : Button = findViewById(R.id.ch3ch4)
        val no1 : Button = findViewById(R.id.ch4ch4)
        icon.startAnimation(fadeout)
        question.startAnimation(fadeout)
        yes.startAnimation(fadeout)
        yes1.startAnimation(fadeout)
        no.startAnimation(fadeout)
        no1.startAnimation(fadeout)
        yes.isEnabled = false
        yes1.isEnabled = false
        no.isEnabled =false
        no1.isEnabled = false
        Handler().postDelayed({
            icon.visibility = View.GONE
            question.visibility = View.GONE
            yes.visibility = View.GONE
            yes1.visibility = View.GONE
            no.visibility = View.GONE
            no1.visibility = View.GONE
            val icon1: TextView = findViewById(R.id.hiconyesno2)
            val question1: TextView = findViewById(R.id.yesnoquestion2)
            question1.text = "Are there white spots on the clear part of the eye?"
            val yes1: Button = findViewById(R.id.yesanswer2)
            val no1: Button = findViewById(R.id.noanswer2)
            yes1.isEnabled = true
            no1.isEnabled = true
            icon1.startAnimation(fadein)
            question1.startAnimation(fadein)
            yes1.startAnimation(fadein)
            no1.startAnimation(fadein)
            icon1.visibility = View.VISIBLE
            question1.visibility = View.VISIBLE
            yes1.visibility = View.VISIBLE
            yes1.setOnClickListener {
                proceedToResult(selectedSymptom, 6, "Yes") // Pass parameters to proceedToResult
            }
            no1.visibility = View.VISIBLE
            no1.setOnClickListener {
                epq7()
            }
        }, 1000) // Delay for 1000 milliseconds (1 second)
    }


    private fun epq5(){
        val fadein = AnimationUtils.loadAnimation(this, R.anim.fade_in)
        val fadeout = AnimationUtils.loadAnimation(this, R.anim.fade_out)
        val icon: TextView = findViewById(R.id.hiconyesno2)
        val question: TextView = findViewById(R.id.yesnoquestion2)
        val yes: Button = findViewById(R.id.yesanswer2)
        val no: Button = findViewById(R.id.noanswer2)
        icon.startAnimation(fadeout)
        question.startAnimation(fadeout)
        yes.startAnimation(fadeout)
        no.startAnimation(fadeout)
        yes.isEnabled = false
        no.isEnabled = false
        Handler().postDelayed({
            icon.visibility = View.GONE
            question.visibility = View.GONE
            yes.visibility = View.GONE
            no.visibility = View.GONE
            val icon1: TextView = findViewById(R.id.iconchoices4)
            val question1: TextView = findViewById(R.id.questionchoice4)
            question1.text = "Is there any discharge from the eye?"
            val yes1: Button = findViewById(R.id.ch1ch4)
            yes1.text = "Watery, clear, or rust-colored discharge below the tear ducts"
            val yes2 : Button = findViewById(R.id.ch2ch4)
            yes2.text = "Green/yellow discharge"
            val no1: Button = findViewById(R.id.ch3ch4)
            no1.text = "No discharge"
            yes1.isEnabled = true
            yes2.isEnabled = true
            no1.isEnabled = true
            icon1.startAnimation(fadein)
            question1.startAnimation(fadein)
            yes1.startAnimation(fadein)
            yes2.startAnimation(fadein)
            no1.startAnimation(fadein)
            icon1.visibility = View.VISIBLE
            question1.visibility = View.VISIBLE
            yes1.visibility = View.VISIBLE
            yes1.setOnClickListener {
                proceedToResult(selectedSymptom, 5, "Choice1") // Pass parameters to proceedToResult
            }
            yes2.visibility = View.VISIBLE
            yes2.setOnClickListener {
                proceedToResult(selectedSymptom, 5, "Choice2") // Pass parameters to proceedToResult
            }
            no1.visibility = View.VISIBLE
            no1.setOnClickListener {
                epq6()
            }
        }, 1000)
    }

    private fun epq4(){
        val fadein = AnimationUtils.loadAnimation(this, R.anim.fade_in)
        val fadeout = AnimationUtils.loadAnimation(this, R.anim.fade_out)
        val icon: TextView = findViewById(R.id.hiconyesno)
        val question: TextView = findViewById(R.id.yesnoquestion)
        val yes: Button = findViewById(R.id.yesanswer)
        val no: Button = findViewById(R.id.noanswer)
        icon.startAnimation(fadeout)
        question.startAnimation(fadeout)
        yes.startAnimation(fadeout)
        no.startAnimation(fadeout)
        yes.isEnabled = false
        no.isEnabled =false
        Handler().postDelayed({
            icon.visibility = View.GONE
            question.visibility = View.GONE
            yes.visibility = View.GONE
            no.visibility = View.GONE
            val icon1: TextView = findViewById(R.id.hiconyesno2)
            val question1: TextView = findViewById(R.id.yesnoquestion2)
            question1.text = "Do any of the following apply?\n" +
                    "\n" +
                    "Pet is squinting, \n" +
                    "Pawing at their eyes, or blinking a lot, \n" +
                    "There is redness in the white part of the eye, and/or\n" +
                    "The eye is covered by tissue (third eyelid)."
            val yes1: Button = findViewById(R.id.yesanswer2)
            val no1: Button = findViewById(R.id.noanswer2)
            yes1.isEnabled = true
            no1.isEnabled = true
            icon1.startAnimation(fadein)
            question1.startAnimation(fadein)
            yes1.startAnimation(fadein)
            no1.startAnimation(fadein)
            icon1.visibility = View.VISIBLE
            question1.visibility = View.VISIBLE
            yes1.visibility = View.VISIBLE
            yes1.setOnClickListener {
                proceedToResult(selectedSymptom, 4, "Yes") // Pass parameters to proceedToResult
            }
            no1.visibility = View.VISIBLE
            no1.setOnClickListener {
                epq5()
            }
        }, 1000) // Delay for 1000 milliseconds (1 second)
    }

    private fun epq3(){
        val fadein = AnimationUtils.loadAnimation(this, R.anim.fade_in)
        val fadeout = AnimationUtils.loadAnimation(this, R.anim.fade_out)
        val icon: TextView = findViewById(R.id.hiconyesno2)
        val question: TextView = findViewById(R.id.yesnoquestion2)
        val yes: Button = findViewById(R.id.yesanswer2)
        val no: Button = findViewById(R.id.noanswer2)
        icon.startAnimation(fadeout)
        question.startAnimation(fadeout)
        yes.startAnimation(fadeout)
        no.startAnimation(fadeout)
        yes.isEnabled = false
        no.isEnabled =false
        Handler().postDelayed({
            icon.visibility = View.GONE
            question.visibility = View.GONE
            yes.visibility = View.GONE
            no.visibility = View.GONE
            val icon1: TextView = findViewById(R.id.hiconyesno)
            val question1: TextView = findViewById(R.id.yesnoquestion)
            question1.text = "Do you see abnormal eye movement or changes in the sizes of the pupils (black circles in the center of the eye), such as one pupil smaller than the other?"
            val yes1: Button = findViewById(R.id.yesanswer)
            val no1: Button = findViewById(R.id.noanswer)
            yes1.isEnabled = true
            no1.isEnabled = true
            icon1.startAnimation(fadein)
            question1.startAnimation(fadein)
            yes1.startAnimation(fadein)
            no1.startAnimation(fadein)
            icon1.visibility = View.VISIBLE
            question1.visibility = View.VISIBLE
            yes1.visibility = View.VISIBLE
            yes1.setOnClickListener {
                proceedToResult(selectedSymptom, 3, "Yes") // Pass parameters to proceedToResult
            }
            no1.visibility = View.VISIBLE
            no1.setOnClickListener {
                epq4()
            }
        }, 1000) // Delay for 1000 milliseconds (1 second)
    }
    private fun epq2(){
        val fadein = AnimationUtils.loadAnimation(this, R.anim.fade_in)
        val fadeout = AnimationUtils.loadAnimation(this, R.anim.fade_out)
        val icon: TextView = findViewById(R.id.hiconyesno)
        val question: TextView = findViewById(R.id.yesnoquestion)
        val yes: Button = findViewById(R.id.yesanswer)
        val no: Button = findViewById(R.id.noanswer)
        icon.startAnimation(fadeout)
        question.startAnimation(fadeout)
        yes.startAnimation(fadeout)
        no.startAnimation(fadeout)
        yes.isEnabled = false
        no.isEnabled =false
        Handler().postDelayed({
            icon.visibility = View.GONE
            question.visibility = View.GONE
            yes.visibility = View.GONE
            no.visibility = View.GONE
            val icon1: TextView = findViewById(R.id.hiconyesno2)
            val question1: TextView = findViewById(R.id.yesnoquestion2)
            question1.text = "Is there blood, pus, or white debris in the clear part of the eye (the bubble over the pupil and iris, not the white part of the eye)?"
            val yes1: Button = findViewById(R.id.yesanswer2)
            val no1: Button = findViewById(R.id.noanswer2)
            yes1.isEnabled = true
            no1.isEnabled = true
            icon1.startAnimation(fadein)
            question1.startAnimation(fadein)
            yes1.startAnimation(fadein)
            no1.startAnimation(fadein)
            icon1.visibility = View.VISIBLE
            question1.visibility = View.VISIBLE
            yes1.visibility = View.VISIBLE
            yes1.setOnClickListener {
                proceedToResult(selectedSymptom, 2, "Yes") // Pass parameters to proceedToResult
            }
            no1.visibility = View.VISIBLE
            no1.setOnClickListener {
                epq3()
            }
        }, 1000) // Delay for 1000 milliseconds (1 second)
    }

    private fun displayEyeProblemQuestion() {
        val fadein = AnimationUtils.loadAnimation(this, R.anim.fade_in)
        val fadeout = AnimationUtils.loadAnimation(this, R.anim.fade_out)
        val spinner: Spinner = findViewById(R.id.dogsymptom)
        val iconsymptom: TextView = findViewById(R.id.hiconsymptom)
        val symptomq: TextView = findViewById(R.id.dogsymptomquestion)
        val btn: Button = findViewById(R.id.donebtn)
        spinner.startAnimation(fadeout)
        iconsymptom.startAnimation(fadeout)
        symptomq.startAnimation(fadeout)
        btn.startAnimation(fadeout)
        spinner.isEnabled = false
        btn.isEnabled = false
        Handler().postDelayed({

            spinner.visibility = View.GONE
            iconsymptom.visibility = View.GONE
            symptomq.visibility = View.GONE
            btn.visibility = View.GONE
            val icon: TextView = findViewById(R.id.hiconyesno)
            val question: TextView = findViewById(R.id.yesnoquestion)
            question.text = "Do any of the following apply?\n" +
                    "\n" +
                    "The eye is bulging or swollen, \n" +
                    "The eye appears to be coming out of the socket, and/or\n" +
                    "Your pet has suddenly lost their vision."
            val yes: Button = findViewById(R.id.yesanswer)
            val no: Button = findViewById(R.id.noanswer)
            yes.isEnabled = true
            no.isEnabled = true

            icon.startAnimation(fadein)
            question.startAnimation(fadein)
            yes.startAnimation(fadein)
            no.startAnimation(fadein)
            icon.visibility = View.VISIBLE
            question.visibility = View.VISIBLE
            yes.visibility = View.VISIBLE
            yes.setOnClickListener {
                proceedToResult(selectedSymptom, 1, "Yes") // Pass parameters to proceedToResult
            }
            no.visibility = View.VISIBLE
            no.setOnClickListener {
                epq2()
            }
        },1000)
    }

    private fun dpq7(){
        val fadein = AnimationUtils.loadAnimation(this, R.anim.fade_in)
        val fadeout = AnimationUtils.loadAnimation(this, R.anim.fade_out)
        val icon: TextView = findViewById(R.id.hiconyesno2)
        val question: TextView = findViewById(R.id.yesnoquestion2)
        val yes: Button = findViewById(R.id.yesanswer2)
        val no: Button = findViewById(R.id.noanswer2)
        icon.startAnimation(fadeout)
        question.startAnimation(fadeout)
        yes.startAnimation(fadeout)
        no.startAnimation(fadeout)
        yes.isEnabled = false
        no.isEnabled = false
        Handler().postDelayed({
            icon.visibility = View.GONE
            question.visibility = View.GONE
            yes.visibility = View.GONE
            no.visibility = View.GONE
            val icon1: TextView = findViewById(R.id.hiconyesno)
            val question1: TextView = findViewById(R.id.yesnoquestion)
            question1.text = "Is your pet on a medication (such as steroids, phenobarbitol, diuretic, thyroid supplement)?"
            val yes1: Button = findViewById(R.id.yesanswer)
            val no1: Button = findViewById(R.id.noanswer)
            yes1.isEnabled = true
            no1.isEnabled = true
            icon1.startAnimation(fadein)
            question1.startAnimation(fadein)
            yes1.startAnimation(fadein)
            no1.startAnimation(fadein)
            icon1.visibility = View.VISIBLE
            question1.visibility = View.VISIBLE
            yes1.visibility = View.VISIBLE
            yes1.setOnClickListener {
                proceedToResult(selectedSymptom, 7, "Yes") // Pass parameters to proceedToResult
            }
            no1.visibility = View.VISIBLE
            no1.setOnClickListener {
                proceedToResult(selectedSymptom, 7, "No") // Pass parameters to proceedToResult
            }
        }, 1000)
    }

    private fun dpq6(){
        val fadein = AnimationUtils.loadAnimation(this, R.anim.fade_in)
        val fadeout = AnimationUtils.loadAnimation(this, R.anim.fade_out)
        val icon: TextView = findViewById(R.id.hiconyesno)
        val question: TextView = findViewById(R.id.yesnoquestion)
        val yes: Button = findViewById(R.id.yesanswer)
        val no: Button = findViewById(R.id.noanswer)
        icon.startAnimation(fadeout)
        question.startAnimation(fadeout)
        yes.startAnimation(fadeout)
        no.startAnimation(fadeout)
        yes.isEnabled = false
        no.isEnabled =false
        Handler().postDelayed({
            icon.visibility = View.GONE
            question.visibility = View.GONE
            yes.visibility = View.GONE
            no.visibility = View.GONE
            val icon1: TextView = findViewById(R.id.hiconyesno2)
            val question1: TextView = findViewById(R.id.yesnoquestion2)
            question1.text = "Has your pet shown any of the following signs lately? Signs can include:\n" +
                    "\n" +
                    "Reduced appetite, \n" +
                    "Bad breath, \n" +
                    "Weight loss, and/or\n" +
                    "Occasional vomiting."
            val yes1: Button = findViewById(R.id.yesanswer2)
            yes1.text = "Yes"
            val no1: Button = findViewById(R.id.noanswer2)
            yes1.isEnabled = true
            no1.isEnabled = true
            icon1.startAnimation(fadein)
            question1.startAnimation(fadein)
            yes1.startAnimation(fadein)
            no1.startAnimation(fadein)
            icon1.visibility = View.VISIBLE
            question1.visibility = View.VISIBLE
            yes1.visibility = View.VISIBLE
            yes1.setOnClickListener {
                proceedToResult(selectedSymptom, 6, "Yes") // Pass parameters to proceedToResult
            }
            no1.visibility = View.VISIBLE
            no1.setOnClickListener {
                dpq7()
            }
        }, 1000) // Delay for 1000 milliseconds (1 second)
    }


    private fun dpq5(){
        val fadein = AnimationUtils.loadAnimation(this, R.anim.fade_in)
        val fadeout = AnimationUtils.loadAnimation(this, R.anim.fade_out)
        val icon: TextView = findViewById(R.id.hiconyesno2)
        val question: TextView = findViewById(R.id.yesnoquestion2)
        val yes: Button = findViewById(R.id.yesanswer2)
        val no: Button = findViewById(R.id.noanswer2)
        icon.startAnimation(fadeout)
        question.startAnimation(fadeout)
        yes.startAnimation(fadeout)
        no.startAnimation(fadeout)
        yes.isEnabled = false
        no.isEnabled = false
        Handler().postDelayed({
            icon.visibility = View.GONE
            question.visibility = View.GONE
            yes.visibility = View.GONE
            no.visibility = View.GONE
            val icon1: TextView = findViewById(R.id.hiconyesno)
            val question1: TextView = findViewById(R.id.yesnoquestion)
            question1.text = "Has your pet shown any of the following signs lately? Signs can include:\n" +
                    "\n" +
                    "Reduced appetite, \n" +
                    "Bad breath, \n" +
                    "Weight loss, and/or\n" +
                    "Occasional vomiting.\n"
            val yes1: Button = findViewById(R.id.yesanswer)
            val no1: Button = findViewById(R.id.noanswer)
            yes1.isEnabled = true
            no1.isEnabled = true
            icon1.startAnimation(fadein)
            question1.startAnimation(fadein)
            yes1.startAnimation(fadein)
            no1.startAnimation(fadein)
            icon1.visibility = View.VISIBLE
            question1.visibility = View.VISIBLE
            yes1.visibility = View.VISIBLE
            yes1.setOnClickListener {
                proceedToResult(selectedSymptom, 5, "Yes") // Pass parameters to proceedToResult
            }
            no1.visibility = View.VISIBLE
            no1.setOnClickListener {
                dpq6()
            }
        }, 1000)
    }

    private fun dpq4(){
        val fadein = AnimationUtils.loadAnimation(this, R.anim.fade_in)
        val fadeout = AnimationUtils.loadAnimation(this, R.anim.fade_out)
        val icon: TextView = findViewById(R.id.hiconyesno)
        val question: TextView = findViewById(R.id.yesnoquestion)
        val yes: Button = findViewById(R.id.yesanswer)
        val no: Button = findViewById(R.id.noanswer)
        icon.startAnimation(fadeout)
        question.startAnimation(fadeout)
        yes.startAnimation(fadeout)
        no.startAnimation(fadeout)
        yes.isEnabled = false
        no.isEnabled =false
        Handler().postDelayed({
            icon.visibility = View.GONE
            question.visibility = View.GONE
            yes.visibility = View.GONE
            no.visibility = View.GONE
            val icon1: TextView = findViewById(R.id.hiconyesno2)
            val question1: TextView = findViewById(R.id.yesnoquestion2)
            question1.text = "Is this a sudden increase in drinking and peeing?"
            val yes1: Button = findViewById(R.id.yesanswer2)
            yes1.text = "Yes"
            val no1: Button = findViewById(R.id.noanswer2)
            yes1.isEnabled = true
            no1.isEnabled = true
            icon1.startAnimation(fadein)
            question1.startAnimation(fadein)
            yes1.startAnimation(fadein)
            no1.startAnimation(fadein)
            icon1.visibility = View.VISIBLE
            question1.visibility = View.VISIBLE
            yes1.visibility = View.VISIBLE
            yes1.setOnClickListener {
                proceedToResult(selectedSymptom, 4, "Yes") // Pass parameters to proceedToResult
            }
            no1.visibility = View.VISIBLE
            no1.setOnClickListener {
                dpq5()
            }
        }, 1000) // Delay for 1000 milliseconds (1 second)
    }

    private fun dpq3(){
        val fadein = AnimationUtils.loadAnimation(this, R.anim.fade_in)
        val fadeout = AnimationUtils.loadAnimation(this, R.anim.fade_out)
        val icon: TextView = findViewById(R.id.hiconyesno2)
        val question: TextView = findViewById(R.id.yesnoquestion2)
        val yes: Button = findViewById(R.id.yesanswer2)
        val no: Button = findViewById(R.id.noanswer2)
        icon.startAnimation(fadeout)
        question.startAnimation(fadeout)
        yes.startAnimation(fadeout)
        no.startAnimation(fadeout)
        yes.isEnabled = false
        no.isEnabled =false
        Handler().postDelayed({
            icon.visibility = View.GONE
            question.visibility = View.GONE
            yes.visibility = View.GONE
            no.visibility = View.GONE
            val icon1: TextView = findViewById(R.id.hiconyesno)
            val question1: TextView = findViewById(R.id.yesnoquestion)
            question1.text = "Is there a change in the color of their urine (darker, lighter, brown, drops of blood in urine)?"
            val yes1: Button = findViewById(R.id.yesanswer)
            val no1: Button = findViewById(R.id.noanswer)
            yes1.isEnabled = true
            no1.isEnabled = true
            icon1.startAnimation(fadein)
            question1.startAnimation(fadein)
            yes1.startAnimation(fadein)
            no1.startAnimation(fadein)
            icon1.visibility = View.VISIBLE
            question1.visibility = View.VISIBLE
            yes1.visibility = View.VISIBLE
            yes1.setOnClickListener {
                proceedToResult(selectedSymptom, 3, "Yes") // Pass parameters to proceedToResult
            }
            no1.visibility = View.VISIBLE
            no1.setOnClickListener {
                dpq4()
            }
        }, 1000) // Delay for 1000 milliseconds (1 second)
    }
    private fun dpq2(){
        val fadein = AnimationUtils.loadAnimation(this, R.anim.fade_in)
        val fadeout = AnimationUtils.loadAnimation(this, R.anim.fade_out)
        val icon: TextView = findViewById(R.id.hiconyesno)
        val question: TextView = findViewById(R.id.yesnoquestion)
        val yes: Button = findViewById(R.id.yesanswer)
        val no: Button = findViewById(R.id.noanswer)
        icon.startAnimation(fadeout)
        question.startAnimation(fadeout)
        yes.startAnimation(fadeout)
        no.startAnimation(fadeout)
        yes.isEnabled = false
        no.isEnabled =false
        Handler().postDelayed({
            icon.visibility = View.GONE
            question.visibility = View.GONE
            yes.visibility = View.GONE
            no.visibility = View.GONE
            val icon1: TextView = findViewById(R.id.hiconyesno2)
            val question1: TextView = findViewById(R.id.yesnoquestion2)
            question1.text = "Could your pet have gotten into anything toxic, such as: \n" +
                    "\n" +
                    "Medications, \n" +
                    "Cleaners, \n" +
                    "Chemicals, \n" +
                    "Fertilizers, \n" +
                    "Plants that are poisonous to pets, \n" +
                    "Essential oils, \n" +
                    "Air fresheners, and/or\n" +
                    "Diffusers."
            val yes1: Button = findViewById(R.id.yesanswer2)
            yes1.text = "Yes or Don't know"
            val no1: Button = findViewById(R.id.noanswer2)
            yes1.isEnabled = true
            no1.isEnabled = true
            icon1.startAnimation(fadein)
            question1.startAnimation(fadein)
            yes1.startAnimation(fadein)
            no1.startAnimation(fadein)
            icon1.visibility = View.VISIBLE
            question1.visibility = View.VISIBLE
            yes1.visibility = View.VISIBLE
            yes1.setOnClickListener {
                proceedToResult(selectedSymptom, 2, "Yes") // Pass parameters to proceedToResult
            }
            no1.visibility = View.VISIBLE
            no1.setOnClickListener {
                dpq3()
            }
        }, 1000) // Delay for 1000 milliseconds (1 second)
    }

    private fun displayDrinkingAndPeeingAlotQuestion() {
        val fadein = AnimationUtils.loadAnimation(this, R.anim.fade_in)
        val fadeout = AnimationUtils.loadAnimation(this, R.anim.fade_out)
        val spinner: Spinner = findViewById(R.id.dogsymptom)
        val iconsymptom: TextView = findViewById(R.id.hiconsymptom)
        val symptomq: TextView = findViewById(R.id.dogsymptomquestion)
        val btn: Button = findViewById(R.id.donebtn)
        spinner.startAnimation(fadeout)
        iconsymptom.startAnimation(fadeout)
        symptomq.startAnimation(fadeout)
        btn.startAnimation(fadeout)
        spinner.isEnabled = false
        btn.isEnabled = false
        Handler().postDelayed({

            spinner.visibility = View.GONE
            iconsymptom.visibility = View.GONE
            symptomq.visibility = View.GONE
            btn.visibility = View.GONE
            val icon: TextView = findViewById(R.id.hiconyesno)
            val question: TextView = findViewById(R.id.yesnoquestion)
            question.text = "Is your pet doing any of the following behaviors? Behaviors can include:\n" +
                    "\n" +
                    "Acting lethargic, \n" +
                    "Having frequent diarrhea, \n" +
                    "Vomiting frequently, and/or\n" +
                    "Acting distressed or anxious."
            val yes: Button = findViewById(R.id.yesanswer)
            val no: Button = findViewById(R.id.noanswer)
            yes.isEnabled = true
            no.isEnabled = true

            icon.startAnimation(fadein)
            question.startAnimation(fadein)
            yes.startAnimation(fadein)
            no.startAnimation(fadein)
            icon.visibility = View.VISIBLE
            question.visibility = View.VISIBLE
            yes.visibility = View.VISIBLE
            yes.setOnClickListener {
                proceedToResult(selectedSymptom, 1, "Yes") // Pass parameters to proceedToResult
            }
            no.visibility = View.VISIBLE
            no.setOnClickListener {
                dpq2()
            }
        },1000)
    }

    private fun drq8(){
        val fadein = AnimationUtils.loadAnimation(this, R.anim.fade_in)
        val fadeout = AnimationUtils.loadAnimation(this, R.anim.fade_out)
        val icon: TextView = findViewById(R.id.hiconyesno)
        val question: TextView = findViewById(R.id.yesnoquestion)
        val yes: Button = findViewById(R.id.yesanswer)
        val no: Button = findViewById(R.id.noanswer)
        icon.startAnimation(fadeout)
        question.startAnimation(fadeout)
        yes.startAnimation(fadeout)
        no.startAnimation(fadeout)
        yes.isEnabled = false
        no.isEnabled = false
        Handler().postDelayed({
            icon.visibility = View.GONE
            question.visibility = View.GONE
            yes.visibility = View.GONE
            no.visibility = View.GONE
            val icon1: TextView = findViewById(R.id.hiconyesno2)
            val question1: TextView = findViewById(R.id.yesnoquestion2)
            question1.text = "Do the other pets in the house also have diarrhea?"
            val yes1: Button = findViewById(R.id.yesanswer2)
            val no1: Button = findViewById(R.id.noanswer2)
            yes1.isEnabled = true
            no1.isEnabled = true
            icon1.startAnimation(fadein)
            question1.startAnimation(fadein)
            yes1.startAnimation(fadein)
            no1.startAnimation(fadein)
            icon1.visibility = View.VISIBLE
            question1.visibility = View.VISIBLE
            yes1.visibility = View.VISIBLE
            yes1.setOnClickListener {
                proceedToResult(selectedSymptom, 8, "Yes") // Pass parameters to proceedToResult
            }
            no1.visibility = View.VISIBLE
            no1.setOnClickListener {
                proceedToResult(selectedSymptom, 8, "No") // Pass parameters to proceedToResult
            }
        }, 1000)
    }
    private fun drq7(){
        val fadein = AnimationUtils.loadAnimation(this, R.anim.fade_in)
        val fadeout = AnimationUtils.loadAnimation(this, R.anim.fade_out)
        val icon: TextView = findViewById(R.id.hiconyesno2)
        val question: TextView = findViewById(R.id.yesnoquestion2)
        val yes: Button = findViewById(R.id.yesanswer2)
        val no: Button = findViewById(R.id.noanswer2)
        icon.startAnimation(fadeout)
        question.startAnimation(fadeout)
        yes.startAnimation(fadeout)
        no.startAnimation(fadeout)
        yes.isEnabled = false
        no.isEnabled = false
        Handler().postDelayed({
            icon.visibility = View.GONE
            question.visibility = View.GONE
            yes.visibility = View.GONE
            no.visibility = View.GONE
            val icon1: TextView = findViewById(R.id.hiconyesno)
            val question1: TextView = findViewById(R.id.yesnoquestion)
            question1.text = "Does your pet consume a raw diet?"
            val yes1: Button = findViewById(R.id.yesanswer)
            val no1: Button = findViewById(R.id.noanswer)
            yes1.isEnabled = true
            no1.isEnabled = true
            icon1.startAnimation(fadein)
            question1.startAnimation(fadein)
            yes1.startAnimation(fadein)
            no1.startAnimation(fadein)
            icon1.visibility = View.VISIBLE
            question1.visibility = View.VISIBLE
            yes1.visibility = View.VISIBLE
            yes1.setOnClickListener {
                proceedToResult(selectedSymptom, 7, "Yes") // Pass parameters to proceedToResult
            }
            no1.visibility = View.VISIBLE
            no1.setOnClickListener {
                drq8()
            }
        }, 1000)
    }

    private fun drq6(){
        val fadein = AnimationUtils.loadAnimation(this, R.anim.fade_in)
        val fadeout = AnimationUtils.loadAnimation(this, R.anim.fade_out)
        val icon: TextView = findViewById(R.id.hiconyesno)
        val question: TextView = findViewById(R.id.yesnoquestion)
        val yes: Button = findViewById(R.id.yesanswer)
        val no: Button = findViewById(R.id.noanswer)
        icon.startAnimation(fadeout)
        question.startAnimation(fadeout)
        yes.startAnimation(fadeout)
        no.startAnimation(fadeout)
        yes.isEnabled = false
        no.isEnabled =false
        Handler().postDelayed({
            icon.visibility = View.GONE
            question.visibility = View.GONE
            yes.visibility = View.GONE
            no.visibility = View.GONE
            val icon1: TextView = findViewById(R.id.hiconyesno2)
            val question1: TextView = findViewById(R.id.yesnoquestion2)
            question1.text = "Could any of these have been consumed by them or come into contact with them? The following items are included:\n" +
                    "\n" +
                    "Human medications, \n" +
                    "Chemicals, and/or\n" +
                    "Poisonous plants."
            val yes1: Button = findViewById(R.id.yesanswer2)
            val no1: Button = findViewById(R.id.noanswer2)
            yes1.isEnabled = true
            no1.isEnabled = true
            icon1.startAnimation(fadein)
            question1.startAnimation(fadein)
            yes1.startAnimation(fadein)
            no1.startAnimation(fadein)
            icon1.visibility = View.VISIBLE
            question1.visibility = View.VISIBLE
            yes1.visibility = View.VISIBLE
            yes1.setOnClickListener {
                proceedToResult(selectedSymptom, 6, "Yes") // Pass parameters to proceedToResult
            }
            no1.visibility = View.VISIBLE
            no1.setOnClickListener {
                drq7()
            }
        }, 1000) // Delay for 1000 milliseconds (1 second)
    }


    private fun drq5(){
        val fadein = AnimationUtils.loadAnimation(this, R.anim.fade_in)
        val fadeout = AnimationUtils.loadAnimation(this, R.anim.fade_out)
        val icon: TextView = findViewById(R.id.hiconyesno2)
        val question: TextView = findViewById(R.id.yesnoquestion2)
        val yes: Button = findViewById(R.id.yesanswer2)
        val no: Button = findViewById(R.id.noanswer2)
        icon.startAnimation(fadeout)
        question.startAnimation(fadeout)
        yes.startAnimation(fadeout)
        no.startAnimation(fadeout)
        yes.isEnabled = false
        no.isEnabled = false
        Handler().postDelayed({
            icon.visibility = View.GONE
            question.visibility = View.GONE
            yes.visibility = View.GONE
            no.visibility = View.GONE
            val icon1: TextView = findViewById(R.id.hiconyesno)
            val question1: TextView = findViewById(R.id.yesnoquestion)
            question1.text = "Could they have consumed something that wasn't food?"
            val yes1: Button = findViewById(R.id.yesanswer)
            val no1: Button = findViewById(R.id.noanswer)
            yes1.isEnabled = true
            no1.isEnabled = true
            icon1.startAnimation(fadein)
            question1.startAnimation(fadein)
            yes1.startAnimation(fadein)
            no1.startAnimation(fadein)
            icon1.visibility = View.VISIBLE
            question1.visibility = View.VISIBLE
            yes1.visibility = View.VISIBLE
            yes1.setOnClickListener {
                proceedToResult(selectedSymptom, 5, "Yes") // Pass parameters to proceedToResult
            }
            no1.visibility = View.VISIBLE
            no1.setOnClickListener {
                drq6()
            }
        }, 1000)
    }

    private fun drq4(){
        val fadein = AnimationUtils.loadAnimation(this, R.anim.fade_in)
        val fadeout = AnimationUtils.loadAnimation(this, R.anim.fade_out)
        val icon: TextView = findViewById(R.id.hiconyesno)
        val question: TextView = findViewById(R.id.yesnoquestion)
        val yes: Button = findViewById(R.id.yesanswer)
        val no: Button = findViewById(R.id.noanswer)
        icon.startAnimation(fadeout)
        question.startAnimation(fadeout)
        yes.startAnimation(fadeout)
        no.startAnimation(fadeout)
        yes.isEnabled = false
        no.isEnabled =false
        Handler().postDelayed({
            icon.visibility = View.GONE
            question.visibility = View.GONE
            yes.visibility = View.GONE
            no.visibility = View.GONE
            val icon1: TextView = findViewById(R.id.hiconyesno2)
            val question1: TextView = findViewById(R.id.yesnoquestion2)
            question1.text = "Does your pet also vomit at least once a day?"
            val yes1: Button = findViewById(R.id.yesanswer2)
            val no1: Button = findViewById(R.id.noanswer2)
            yes1.isEnabled = true
            no1.isEnabled = true
            icon1.startAnimation(fadein)
            question1.startAnimation(fadein)
            yes1.startAnimation(fadein)
            no1.startAnimation(fadein)
            icon1.visibility = View.VISIBLE
            question1.visibility = View.VISIBLE
            yes1.visibility = View.VISIBLE
            yes1.setOnClickListener {
                proceedToResult(selectedSymptom, 4, "Yes") // Pass parameters to proceedToResult
            }
            no1.visibility = View.VISIBLE
            no1.setOnClickListener {
                drq5()
            }
        }, 1000) // Delay for 1000 milliseconds (1 second)
    }

    private fun drq3(){
        val fadein = AnimationUtils.loadAnimation(this, R.anim.fade_in)
        val fadeout = AnimationUtils.loadAnimation(this, R.anim.fade_out)
        val icon: TextView = findViewById(R.id.hiconyesno2)
        val question: TextView = findViewById(R.id.yesnoquestion2)
        val yes: Button = findViewById(R.id.yesanswer2)
        val no: Button = findViewById(R.id.noanswer2)
        icon.startAnimation(fadeout)
        question.startAnimation(fadeout)
        yes.startAnimation(fadeout)
        no.startAnimation(fadeout)
        yes.isEnabled = false
        no.isEnabled =false
        Handler().postDelayed({
            icon.visibility = View.GONE
            question.visibility = View.GONE
            yes.visibility = View.GONE
            no.visibility = View.GONE
            val icon1: TextView = findViewById(R.id.hiconyesno)
            val question1: TextView = findViewById(R.id.yesnoquestion)
            question1.text = "Does the diarrhea occur more than three times in the past twelve hours, or is it incredibly watery or explosive?"
            val yes1: Button = findViewById(R.id.yesanswer)
            val no1: Button = findViewById(R.id.noanswer)
            yes1.isEnabled = true
            no1.isEnabled = true
            icon1.startAnimation(fadein)
            question1.startAnimation(fadein)
            yes1.startAnimation(fadein)
            no1.startAnimation(fadein)
            icon1.visibility = View.VISIBLE
            question1.visibility = View.VISIBLE
            yes1.visibility = View.VISIBLE
            yes1.setOnClickListener {
                proceedToResult(selectedSymptom, 3, "Yes") // Pass parameters to proceedToResult
            }
            no1.visibility = View.VISIBLE
            no1.setOnClickListener {
                drq4()
            }
        }, 1000) // Delay for 1000 milliseconds (1 second)
    }
    private fun drq2(){
        val fadein = AnimationUtils.loadAnimation(this, R.anim.fade_in)
        val fadeout = AnimationUtils.loadAnimation(this, R.anim.fade_out)
        val icon: TextView = findViewById(R.id.hiconyesno)
        val question: TextView = findViewById(R.id.yesnoquestion)
        val yes: Button = findViewById(R.id.yesanswer)
        val no: Button = findViewById(R.id.noanswer)
        icon.startAnimation(fadeout)
        question.startAnimation(fadeout)
        yes.startAnimation(fadeout)
        no.startAnimation(fadeout)
        yes.isEnabled = false
        no.isEnabled =false
        Handler().postDelayed({
            icon.visibility = View.GONE
            question.visibility = View.GONE
            yes.visibility = View.GONE
            no.visibility = View.GONE
            val icon1: TextView = findViewById(R.id.hiconyesno2)
            val question1: TextView = findViewById(R.id.yesnoquestion2)
            question1.text = "Does the diarrhea appear red, have clots of blood in it, or resemble coffee grounds?"
            val yes1: Button = findViewById(R.id.yesanswer2)
            val no1: Button = findViewById(R.id.noanswer2)
            yes1.isEnabled = true
            no1.isEnabled = true
            icon1.startAnimation(fadein)
            question1.startAnimation(fadein)
            yes1.startAnimation(fadein)
            no1.startAnimation(fadein)
            icon1.visibility = View.VISIBLE
            question1.visibility = View.VISIBLE
            yes1.visibility = View.VISIBLE
            yes1.setOnClickListener {
                proceedToResult(selectedSymptom, 2, "Yes") // Pass parameters to proceedToResult
            }
            no1.visibility = View.VISIBLE
            no1.setOnClickListener {
                drq3()
            }
        }, 1000) // Delay for 1000 milliseconds (1 second)
    }

    private fun displayDiarrheaQuestion() {
        val fadein = AnimationUtils.loadAnimation(this, R.anim.fade_in)
        val fadeout = AnimationUtils.loadAnimation(this, R.anim.fade_out)
        val spinner: Spinner = findViewById(R.id.dogsymptom)
        val iconsymptom: TextView = findViewById(R.id.hiconsymptom)
        val symptomq: TextView = findViewById(R.id.dogsymptomquestion)
        val btn: Button = findViewById(R.id.donebtn)
        spinner.startAnimation(fadeout)
        iconsymptom.startAnimation(fadeout)
        symptomq.startAnimation(fadeout)
        btn.startAnimation(fadeout)
        spinner.isEnabled = false
        btn.isEnabled = false
        Handler().postDelayed({

            spinner.visibility = View.GONE
            iconsymptom.visibility = View.GONE
            symptomq.visibility = View.GONE
            btn.visibility = View.GONE
            val icon: TextView = findViewById(R.id.hiconyesno)
            val question: TextView = findViewById(R.id.yesnoquestion)
            question.text = "Is your pet lethargic, or do they have a reduced appetite?"
            val yes: Button = findViewById(R.id.yesanswer)
            val no: Button = findViewById(R.id.noanswer)
            yes.isEnabled = true
            no.isEnabled = true

            icon.startAnimation(fadein)
            question.startAnimation(fadein)
            yes.startAnimation(fadein)
            no.startAnimation(fadein)
            icon.visibility = View.VISIBLE
            question.visibility = View.VISIBLE
            yes.visibility = View.VISIBLE
            yes.setOnClickListener {
                proceedToResult(selectedSymptom, 1, "Yes") // Pass parameters to proceedToResult
            }
            no.visibility = View.VISIBLE
            no.setOnClickListener {
                drq2()
            }
        },1000)
    }

    private fun dhq5(){
        val fadein = AnimationUtils.loadAnimation(this, R.anim.fade_in)
        val fadeout = AnimationUtils.loadAnimation(this, R.anim.fade_out)
        val icon: TextView = findViewById(R.id.hiconyesno2)
        val question: TextView = findViewById(R.id.yesnoquestion2)
        val yes: Button = findViewById(R.id.yesanswer2)
        val no: Button = findViewById(R.id.noanswer2)
        icon.startAnimation(fadeout)
        question.startAnimation(fadeout)
        yes.startAnimation(fadeout)
        no.startAnimation(fadeout)
        yes.isEnabled = false
        no.isEnabled = false
        Handler().postDelayed({
            icon.visibility = View.GONE
            question.visibility = View.GONE
            yes.visibility = View.GONE
            no.visibility = View.GONE
            val icon1: TextView = findViewById(R.id.hiconyesno)
            val question1: TextView = findViewById(R.id.yesnoquestion)
            question1.text = "Is your pet experiencing diarrhea or vomiting more frequently than twice a week?"
            val yes1: Button = findViewById(R.id.yesanswer)
            val no1: Button = findViewById(R.id.noanswer)
            yes1.isEnabled = true
            no1.isEnabled = true
            icon1.startAnimation(fadein)
            question1.startAnimation(fadein)
            yes1.startAnimation(fadein)
            no1.startAnimation(fadein)
            icon1.visibility = View.VISIBLE
            question1.visibility = View.VISIBLE
            yes1.visibility = View.VISIBLE
            yes1.setOnClickListener {
                proceedToResult(selectedSymptom, 5, "Yes") // Pass parameters to proceedToResult
            }
            no1.visibility = View.VISIBLE
            no1.setOnClickListener {
                proceedToResult(selectedSymptom, 5, "No") // Pass parameters to proceedToResult
            }
        }, 1000)
    }

    private fun dhq4(){
        val fadein = AnimationUtils.loadAnimation(this, R.anim.fade_in)
        val fadeout = AnimationUtils.loadAnimation(this, R.anim.fade_out)
        val icon: TextView = findViewById(R.id.hiconyesno)
        val question: TextView = findViewById(R.id.yesnoquestion)
        val yes: Button = findViewById(R.id.yesanswer)
        val no: Button = findViewById(R.id.noanswer)
        icon.startAnimation(fadeout)
        question.startAnimation(fadeout)
        yes.startAnimation(fadeout)
        no.startAnimation(fadeout)
        yes.isEnabled = false
        no.isEnabled =false
        Handler().postDelayed({
            icon.visibility = View.GONE
            question.visibility = View.GONE
            yes.visibility = View.GONE
            no.visibility = View.GONE
            val icon1: TextView = findViewById(R.id.hiconyesno2)
            val question1: TextView = findViewById(R.id.yesnoquestion2)
            question1.text = "Does your pet turn away from the water bowl after approaching it, or are they drooling, gagging, or gulping?"
            val yes1: Button = findViewById(R.id.yesanswer2)
            val no1: Button = findViewById(R.id.noanswer2)
            yes1.isEnabled = true
            no1.isEnabled = true
            icon1.startAnimation(fadein)
            question1.startAnimation(fadein)
            yes1.startAnimation(fadein)
            no1.startAnimation(fadein)
            icon1.visibility = View.VISIBLE
            question1.visibility = View.VISIBLE
            yes1.visibility = View.VISIBLE
            yes1.setOnClickListener {
                proceedToResult(selectedSymptom, 4, "Yes") // Pass parameters to proceedToResult
            }
            no1.visibility = View.VISIBLE
            no1.setOnClickListener {
                dhq5()
            }
        }, 1000) // Delay for 1000 milliseconds (1 second)
    }

    private fun dhq3(){
        val fadein = AnimationUtils.loadAnimation(this, R.anim.fade_in)
        val fadeout = AnimationUtils.loadAnimation(this, R.anim.fade_out)
        val icon: TextView = findViewById(R.id.hiconyesno2)
        val question: TextView = findViewById(R.id.yesnoquestion2)
        val yes: Button = findViewById(R.id.yesanswer2)
        val no: Button = findViewById(R.id.noanswer2)
        icon.startAnimation(fadeout)
        question.startAnimation(fadeout)
        yes.startAnimation(fadeout)
        no.startAnimation(fadeout)
        yes.isEnabled = false
        no.isEnabled =false
        Handler().postDelayed({
            icon.visibility = View.GONE
            question.visibility = View.GONE
            yes.visibility = View.GONE
            no.visibility = View.GONE
            val icon1: TextView = findViewById(R.id.hiconyesno)
            val question1: TextView = findViewById(R.id.yesnoquestion)
            question1.text = "Is your pet lethargic, vomiting, or having diarrhea?"
            val yes1: Button = findViewById(R.id.yesanswer)
            val no1: Button = findViewById(R.id.noanswer)
            yes1.isEnabled = true
            no1.isEnabled = true
            icon1.startAnimation(fadein)
            question1.startAnimation(fadein)
            yes1.startAnimation(fadein)
            no1.startAnimation(fadein)
            icon1.visibility = View.VISIBLE
            question1.visibility = View.VISIBLE
            yes1.visibility = View.VISIBLE
            yes1.setOnClickListener {
                proceedToResult(selectedSymptom, 3, "Yes") // Pass parameters to proceedToResult
            }
            no1.visibility = View.VISIBLE
            no1.setOnClickListener {
                dhq4()
            }
        }, 1000) // Delay for 1000 milliseconds (1 second)
    }
    private fun dhq2(){
        val fadein = AnimationUtils.loadAnimation(this, R.anim.fade_in)
        val fadeout = AnimationUtils.loadAnimation(this, R.anim.fade_out)
        val icon: TextView = findViewById(R.id.hiconyesno)
        val question: TextView = findViewById(R.id.yesnoquestion)
        val yes: Button = findViewById(R.id.yesanswer)
        val no: Button = findViewById(R.id.noanswer)
        icon.startAnimation(fadeout)
        question.startAnimation(fadeout)
        yes.startAnimation(fadeout)
        no.startAnimation(fadeout)
        yes.isEnabled = false
        no.isEnabled =false
        Handler().postDelayed({
            icon.visibility = View.GONE
            question.visibility = View.GONE
            yes.visibility = View.GONE
            no.visibility = View.GONE
            val icon1: TextView = findViewById(R.id.hiconyesno2)
            val question1: TextView = findViewById(R.id.yesnoquestion2)
            question1.text = "Does your pet vomit water?"
            val yes1: Button = findViewById(R.id.yesanswer2)
            val no1: Button = findViewById(R.id.noanswer2)
            yes1.isEnabled = true
            no1.isEnabled = true
            icon1.startAnimation(fadein)
            question1.startAnimation(fadein)
            yes1.startAnimation(fadein)
            no1.startAnimation(fadein)
            icon1.visibility = View.VISIBLE
            question1.visibility = View.VISIBLE
            yes1.visibility = View.VISIBLE
            yes1.setOnClickListener {
                proceedToResult(selectedSymptom, 2, "Yes") // Pass parameters to proceedToResult
            }
            no1.visibility = View.VISIBLE
            no1.setOnClickListener {
                dhq3()
            }
        }, 1000) // Delay for 1000 milliseconds (1 second)
    }

    private fun displayDehydrationQuestion() {
        val fadein = AnimationUtils.loadAnimation(this, R.anim.fade_in)
        val fadeout = AnimationUtils.loadAnimation(this, R.anim.fade_out)
        val spinner: Spinner = findViewById(R.id.dogsymptom)
        val iconsymptom: TextView = findViewById(R.id.hiconsymptom)
        val symptomq: TextView = findViewById(R.id.dogsymptomquestion)
        val btn: Button = findViewById(R.id.donebtn)
        spinner.startAnimation(fadeout)
        iconsymptom.startAnimation(fadeout)
        symptomq.startAnimation(fadeout)
        btn.startAnimation(fadeout)
        spinner.isEnabled = false
        btn.isEnabled = false
        Handler().postDelayed({

            spinner.visibility = View.GONE
            iconsymptom.visibility = View.GONE
            symptomq.visibility = View.GONE
            btn.visibility = View.GONE
            val icon: TextView = findViewById(R.id.hiconyesno)
            val question: TextView = findViewById(R.id.yesnoquestion)
            question.text =
                "Do any of the following behaviors apply to your pet? Actions may consist of:\n" +
                        "\n" +
                        "Drooling, \n" +
                        "Acting weird, \n" +
                        "Being aggressive, \n" +
                        "Walking differently, \n" +
                        "Having trouble getting up, \n" +
                        "Not eating as much, and/or\n" +
                        "Seeming to be paralyzed."
            val yes: Button = findViewById(R.id.yesanswer)
            val no: Button = findViewById(R.id.noanswer)
            yes.isEnabled = true
            no.isEnabled = true

            icon.startAnimation(fadein)
            question.startAnimation(fadein)
            yes.startAnimation(fadein)
            no.startAnimation(fadein)
            icon.visibility = View.VISIBLE
            question.visibility = View.VISIBLE
            yes.visibility = View.VISIBLE
            yes.setOnClickListener {
                proceedToResult(selectedSymptom, 1, "Yes") // Pass parameters to proceedToResult
            }
            no.visibility = View.VISIBLE
            no.setOnClickListener {
                dhq2()
            }
        },1000)
    }


    private fun dfq4(){
        val fadein = AnimationUtils.loadAnimation(this, R.anim.fade_in)
        val fadeout = AnimationUtils.loadAnimation(this, R.anim.fade_out)
        val icon: TextView = findViewById(R.id.hiconyesno)
        val question: TextView = findViewById(R.id.yesnoquestion)
        val yes: Button = findViewById(R.id.yesanswer)
        val no: Button = findViewById(R.id.noanswer)
        icon.startAnimation(fadeout)
        question.startAnimation(fadeout)
        yes.startAnimation(fadeout)
        no.startAnimation(fadeout)
        icon.visibility = View.GONE
        question.visibility = View.GONE
        yes.visibility = View.GONE
        no.visibility = View.GONE
        yes.isEnabled = false
        no.isEnabled = false
        Handler().postDelayed({

            val icon1: TextView = findViewById(R.id.hiconyesno2)
            val question1: TextView = findViewById(R.id.yesnoquestion2)
            question1.text = "Does your pet bite or lick the area?"
            val yes1: Button = findViewById(R.id.yesanswer2)
            val no1: Button = findViewById(R.id.noanswer2)
            yes1.isEnabled = true
            no1.isEnabled = true

            icon1.startAnimation(fadein)
            question1.startAnimation(fadein)
            yes1.text = "Yes"
            yes1.startAnimation(fadein)
            no1.text = "No"
            no1.startAnimation(fadein)
            icon1.visibility = View.VISIBLE
            question1.visibility = View.VISIBLE
            yes1.visibility = View.VISIBLE
            yes1.setOnClickListener {
                proceedToResult(selectedSymptom, 4, "Yes") // Pass parameters to proceedToResult
            }
            no1.visibility = View.VISIBLE
            no1.setOnClickListener {
                proceedToResult(selectedSymptom, 4, "No")
            }
        },1000)
    }
    private fun dfq3(){
        val fadein = AnimationUtils.loadAnimation(this, R.anim.fade_in)
        val fadeout = AnimationUtils.loadAnimation(this, R.anim.fade_out)
        val icon: TextView = findViewById(R.id.hiconyesno2)
        val question: TextView = findViewById(R.id.yesnoquestion2)
        val yes: Button = findViewById(R.id.yesanswer2)
        val no: Button = findViewById(R.id.noanswer2)
        icon.startAnimation(fadeout)
        question.startAnimation(fadeout)
        yes.startAnimation(fadeout)
        no.startAnimation(fadeout)
        yes.isEnabled = false
        no.isEnabled = false
        Handler().postDelayed({
            icon.visibility = View.GONE
            question.visibility = View.GONE
            yes.visibility = View.GONE
            no.visibility = View.GONE
            val icon1: TextView = findViewById(R.id.hiconyesno)
            val question1: TextView = findViewById(R.id.yesnoquestion)
            question1.text = "Does the area create any discharge?"
            val yes1: Button = findViewById(R.id.yesanswer)
            val no1: Button = findViewById(R.id.noanswer)
            yes1.isEnabled = true
            no1.isEnabled = true

            icon1.startAnimation(fadein)
            question1.startAnimation(fadein)
            yes1.startAnimation(fadein)
            no1.startAnimation(fadein)
            icon1.visibility = View.VISIBLE
            question1.visibility = View.VISIBLE
            yes1.visibility = View.VISIBLE
            yes1.setOnClickListener {
                proceedToResult(selectedSymptom, 3, "Yes") // Pass parameters to proceedToResult
            }
            no1.visibility = View.VISIBLE
            no1.setOnClickListener {
                dfq4()
            }
        },1000)
    }
    private fun dfq2(){
        val fadein = AnimationUtils.loadAnimation(this, R.anim.fade_in)
        val fadeout = AnimationUtils.loadAnimation(this, R.anim.fade_out)
        val icon: TextView = findViewById(R.id.hiconyesno)
        val question: TextView = findViewById(R.id.yesnoquestion)
        val yes: Button = findViewById(R.id.yesanswer)
        val no: Button = findViewById(R.id.noanswer)
        icon.startAnimation(fadeout)
        question.startAnimation(fadeout)
        yes.startAnimation(fadeout)
        no.startAnimation(fadeout)
        yes.isEnabled = false
        no.isEnabled = false
        Handler().postDelayed({

            icon.visibility = View.GONE
            question.visibility = View.GONE
            yes.visibility = View.GONE
            no.visibility = View.GONE
            val icon1: TextView = findViewById(R.id.hiconyesno2)
            val question1: TextView = findViewById(R.id.yesnoquestion2)
            question1.text = "Where's the flaky area?"
            val yes1: Button = findViewById(R.id.yesanswer2)
            yes1.text = "Whole body"
            val no1: Button = findViewById(R.id.noanswer2)
            no1.text = "One area/spot"
            yes1.isEnabled = true
            no1.isEnabled = true

            icon1.startAnimation(fadein)
            question1.startAnimation(fadein)
            yes1.startAnimation(fadein)
            no1.startAnimation(fadein)
            icon1.visibility = View.VISIBLE
            question1.visibility = View.VISIBLE
            yes1.visibility = View.VISIBLE
            yes1.setOnClickListener {
                proceedToResult(
                    selectedSymptom,
                    2,
                    "Choice1"
                ) // Pass parameters to proceedToResult
            }
            no1.visibility = View.VISIBLE
            no1.setOnClickListener {
                dfq3()
            }
        },1000)
    }

    private fun displayDandruffQuestion() {
        val fadein = AnimationUtils.loadAnimation(this, R.anim.fade_in)
        val fadeout = AnimationUtils.loadAnimation(this, R.anim.fade_out)
        val spinner: Spinner = findViewById(R.id.dogsymptom)
        val iconsymptom: TextView = findViewById(R.id.hiconsymptom)
        val symptomq: TextView = findViewById(R.id.dogsymptomquestion)
        val btn: Button = findViewById(R.id.donebtn)
        spinner.startAnimation(fadeout)
        iconsymptom.startAnimation(fadeout)
        symptomq.startAnimation(fadeout)
        btn.startAnimation(fadeout)
        spinner.isEnabled = false
        btn.isEnabled = false
        Handler().postDelayed({

            spinner.visibility = View.GONE
            iconsymptom.visibility = View.GONE
            symptomq.visibility = View.GONE
            btn.visibility = View.GONE
            val icon: TextView = findViewById(R.id.hiconyesno)
            val question: TextView = findViewById(R.id.yesnoquestion)
            question.text =
                "Are there any visible wounds or ongoing bleeding in the affected area?"
            val yes: Button = findViewById(R.id.yesanswer)
            val no: Button = findViewById(R.id.noanswer)
            yes.isEnabled = true
            no.isEnabled = true

            icon.startAnimation(fadein)
            question.startAnimation(fadein)
            yes.startAnimation(fadein)
            no.startAnimation(fadein)
            icon.visibility = View.VISIBLE
            question.visibility = View.VISIBLE
            yes.visibility = View.VISIBLE
            yes.setOnClickListener {
                proceedToResult(selectedSymptom, 1, "Yes") // Pass parameters to proceedToResult
            }
            no.visibility = View.VISIBLE
            no.setOnClickListener {
                dfq2()
            }
        },1000)
    }
    private fun chq6(){
        val fadein = AnimationUtils.loadAnimation(this, R.anim.fade_in)
        val fadeout = AnimationUtils.loadAnimation(this, R.anim.fade_out)
        val icon: TextView = findViewById(R.id.hiconyesno)
        val question: TextView = findViewById(R.id.yesnoquestion)
        val yes: Button = findViewById(R.id.yesanswer)
        val no: Button = findViewById(R.id.noanswer)
        icon.startAnimation(fadeout)
        question.startAnimation(fadeout)
        yes.startAnimation(fadeout)
        no.startAnimation(fadeout)
        yes.isEnabled = false
        no.isEnabled = false
        Handler().postDelayed({
            icon.visibility = View.GONE
            question.visibility = View.GONE
            yes.visibility = View.GONE
            no.visibility = View.GONE
            val icon1: TextView = findViewById(R.id.hiconyesno2)
            val question1: TextView = findViewById(R.id.yesnoquestion2)
            question1.text = "Is your pet up to date on heartworm prevention and vaccinations?"
            val yes1: Button = findViewById(R.id.yesanswer2)
            val no1: Button = findViewById(R.id.noanswer2)
            yes1.isEnabled = true
            no1.isEnabled = true

            icon1.startAnimation(fadein)
            question1.startAnimation(fadein)
            yes1.startAnimation(fadein)
            no1.startAnimation(fadein)
            icon1.visibility = View.VISIBLE
            question1.visibility = View.VISIBLE
            yes1.visibility = View.VISIBLE
            yes1.setOnClickListener {
                proceedToResult(selectedSymptom, 6, "Yes") // Pass parameters to proceedToResult
            }
            no1.visibility = View.VISIBLE
            no1.setOnClickListener {
                proceedToResult(selectedSymptom, 6, "No")
            }
        },1000)
    }

    private fun chq5(){
        val fadein = AnimationUtils.loadAnimation(this, R.anim.fade_in)
        val fadeout = AnimationUtils.loadAnimation(this, R.anim.fade_out)
        val icon: TextView = findViewById(R.id.hiconyesno2)
        val question: TextView = findViewById(R.id.yesnoquestion2)
        val yes: Button = findViewById(R.id.yesanswer2)
        val no: Button = findViewById(R.id.noanswer2)
        icon.startAnimation(fadeout)
        question.startAnimation(fadeout)
        yes.startAnimation(fadeout)
        no.startAnimation(fadeout)
        yes.isEnabled = false
        no.isEnabled = false
        Handler().postDelayed({

            icon.visibility = View.GONE
            question.visibility = View.GONE
            yes.visibility = View.GONE
            no.visibility = View.GONE
            val icon1: TextView = findViewById(R.id.hiconyesno)
            val question1: TextView = findViewById(R.id.yesnoquestion)
            question1.text =
                "Has your pet encountered any of the following situations? Exposures may consist of:\n" +
                        "\n" +
                        "Sick animals,\n" +
                        "Smoke, \n" +
                        "Air pollution, \n" +
                        "A boarding facility, \n" +
                        "A groomer, \n" +
                        "A kennel, and/or\n" +
                        "A dog daycare"
            val yes1: Button = findViewById(R.id.yesanswer)
            val no1: Button = findViewById(R.id.noanswer)
            yes1.isEnabled = true
            no1.isEnabled = true

            icon1.startAnimation(fadein)
            question1.startAnimation(fadein)
            yes1.startAnimation(fadein)
            no1.startAnimation(fadein)
            icon1.visibility = View.VISIBLE
            question1.visibility = View.VISIBLE
            yes1.text = "Yes"
            yes1.visibility = View.VISIBLE
            yes1.setOnClickListener {
                proceedToResult(selectedSymptom, 5, "Yes") // Pass parameters to proceedToResult
            }
            no1.visibility = View.VISIBLE
            no1.text = "No"
            no1.setOnClickListener {
                chq6()
            }
        },1000)
    }
    private fun chq4(){
        val fadein = AnimationUtils.loadAnimation(this, R.anim.fade_in)
        val fadeout = AnimationUtils.loadAnimation(this, R.anim.fade_out)
        val icon: TextView = findViewById(R.id.hiconyesno)
        val question: TextView = findViewById(R.id.yesnoquestion)
        val yes: Button = findViewById(R.id.yesanswer)
        val no: Button = findViewById(R.id.noanswer)
        icon.startAnimation(fadeout)
        question.startAnimation(fadeout)
        yes.startAnimation(fadeout)
        no.startAnimation(fadeout)
        yes.isEnabled = false
        no.isEnabled = false
        Handler().postDelayed({

            icon.visibility = View.GONE
            question.visibility = View.GONE
            yes.visibility = View.GONE
            no.visibility = View.GONE
            val icon1: TextView = findViewById(R.id.hiconyesno2)
            val question1: TextView = findViewById(R.id.yesnoquestion2)
            question1.text =
                "Does your dog already have any respiratory or cardiac issues? Possible conditions could be:\n" +
                        "\n" +
                        "Asthma, \n" +
                        "COPD, \n" +
                        "Laryngeal paralysis, \n" +
                        "Tracheal collapse, \n" +
                        "Pneumonia, and/or\n" +
                        "Allergies.\n"
            val yes1: Button = findViewById(R.id.yesanswer2)
            yes1.text = "Yes"
            val no1: Button = findViewById(R.id.noanswer2)
            no1.text = "No"
            yes1.isEnabled = true
            no1.isEnabled = true

            icon1.startAnimation(fadein)
            question1.startAnimation(fadein)
            yes1.startAnimation(fadein)
            no1.startAnimation(fadein)
            icon1.visibility = View.VISIBLE
            question1.visibility = View.VISIBLE
            yes1.visibility = View.VISIBLE
            yes1.setOnClickListener {
                proceedToResult(selectedSymptom, 4, "Yes") // Pass parameters to proceedToResult
            }
            no1.visibility = View.VISIBLE
            no1.setOnClickListener {
                chq5()
            }
        },1000)
    }

    private fun chq3(){
        val fadein = AnimationUtils.loadAnimation(this, R.anim.fade_in)
        val fadeout = AnimationUtils.loadAnimation(this, R.anim.fade_out)
        val icon: TextView = findViewById(R.id.iconchoices4)
        val question: TextView = findViewById(R.id.questionchoice4)
        val yes: Button = findViewById(R.id.ch1ch4)
        val yes1 : Button = findViewById(R.id.ch2ch4)
        val no: Button = findViewById(R.id.ch3ch4)
        icon.startAnimation(fadeout)
        question.startAnimation(fadeout)
        yes.startAnimation(fadeout)
        yes1.startAnimation(fadeout)
        no.startAnimation(fadeout)
        yes.isEnabled = false
        no.isEnabled = false
        Handler().postDelayed({

            icon.visibility = View.GONE
            question.visibility = View.GONE
            yes.visibility = View.GONE
            yes1.visibility = View.GONE
            no.visibility = View.GONE
            val icon1: TextView = findViewById(R.id.hiconyesno)
            val question1: TextView = findViewById(R.id.yesnoquestion)
            question1.text = "How often does the cough happen?"
            val yes2: Button = findViewById(R.id.yesanswer)
            yes2.text = "Several or more times throughout the day"
            val no1: Button = findViewById(R.id.noanswer)
            no1.isEnabled = true
            yes2.isEnabled = true
            no1.text = "Just a few times"
            icon1.startAnimation(fadein)
            question1.startAnimation(fadein)
            yes2.startAnimation(fadein)
            no1.startAnimation(fadein)
            icon1.visibility = View.VISIBLE
            question1.visibility = View.VISIBLE
            yes2.visibility = View.VISIBLE
            yes2.setOnClickListener {
                proceedToResult(
                    selectedSymptom,
                    3,
                    "Choice1"
                ) // Pass parameters to proceedToResult
            }
            no1.visibility = View.VISIBLE
            no1.setOnClickListener {
                chq4()
            }
        },1000)
    }
    private fun chq2(){
        val fadein = AnimationUtils.loadAnimation(this, R.anim.fade_in)
        val fadeout = AnimationUtils.loadAnimation(this, R.anim.fade_out)
        val icon: TextView = findViewById(R.id.hiconyesno)
        val question: TextView = findViewById(R.id.yesnoquestion)
        val yes: Button = findViewById(R.id.yesanswer)
        val no: Button = findViewById(R.id.noanswer)
        icon.startAnimation(fadeout)
        question.startAnimation(fadeout)
        yes.startAnimation(fadeout)
        no.startAnimation(fadeout)
        yes.isEnabled = false
        no.isEnabled = false
        Handler().postDelayed({
            icon.visibility = View.GONE
            question.visibility = View.GONE
            yes.visibility = View.GONE
            no.visibility = View.GONE
            val icon1: TextView = findViewById(R.id.iconchoices4)
            val question1: TextView = findViewById(R.id.questionchoice4)
            question1.text = "Is anything coming up from their cough?"
            val yes1: Button = findViewById(R.id.ch1ch4)
            yes1.text = "Saliva that has a red tint or is bloody (could be foamy)"
            val yes2: Button = findViewById(R.id.ch2ch4)
            yes2.text = "Something that looks like coffee grounds"
            val no1: Button = findViewById(R.id.ch3ch4)
            no1.text = "Phlegm (white, thick, and possibly foamy) OR Nothing"
            icon1.startAnimation(fadein)
            question1.startAnimation(fadein)
            yes1.isEnabled = true
            no1.isEnabled = true
            yes2.isEnabled = true
            yes1.startAnimation(fadein)
            yes2.startAnimation(fadein)
            no1.startAnimation(fadein)
            icon1.visibility = View.VISIBLE
            question1.visibility = View.VISIBLE
            yes1.visibility = View.VISIBLE
            yes1.setOnClickListener {
                proceedToResult(
                    selectedSymptom,
                    2,
                    "Choice1"
                ) // Pass parameters to proceedToResult
            }
            yes2.visibility = View.VISIBLE
            yes2.setOnClickListener {
                proceedToResult(
                    selectedSymptom,
                    2,
                    "Choice2"
                ) // Pass parameters to proceedToResult
            }
            no1.visibility = View.VISIBLE
            no1.setOnClickListener {
                chq3()
            }
        },1000)
    }

    private fun displayCoughingQuestion() {
        val fadein = AnimationUtils.loadAnimation(this, R.anim.fade_in)
        val fadeout = AnimationUtils.loadAnimation(this, R.anim.fade_out)
        val spinner: Spinner = findViewById(R.id.dogsymptom)
        val iconsymptom: TextView = findViewById(R.id.hiconsymptom)
        val symptomq: TextView = findViewById(R.id.dogsymptomquestion)
        val btn: Button = findViewById(R.id.donebtn)
        spinner.startAnimation(fadeout)
        iconsymptom.startAnimation(fadeout)
        symptomq.startAnimation(fadeout)
        btn.startAnimation(fadeout)
        spinner.isEnabled = false
        btn.isEnabled = false
        Handler().postDelayed({

            spinner.visibility = View.GONE
            iconsymptom.visibility = View.GONE
            symptomq.visibility = View.GONE
            btn.visibility = View.GONE
            val icon: TextView = findViewById(R.id.hiconyesno)
            val question: TextView = findViewById(R.id.yesnoquestion)
            question.text =
                "Regarding your pet, are any of the following true? Examples include:\n" +
                        "\n" +
                        "Seems lethargic,\n" +
                        "Has a fever,\n" +
                        "Has green/yellow discharge from their nostrils, and/or\n" +
                        "Feels warm around their ears/head?\n"
            val yes: Button = findViewById(R.id.yesanswer)
            val no: Button = findViewById(R.id.noanswer)
            yes.isEnabled = true
            no.isEnabled = true

            icon.startAnimation(fadein)
            question.startAnimation(fadein)
            yes.startAnimation(fadein)
            no.startAnimation(fadein)
            icon.visibility = View.VISIBLE
            question.visibility = View.VISIBLE
            yes.visibility = View.VISIBLE
            yes.setOnClickListener {
                proceedToResult(selectedSymptom, 1, "Yes") // Pass parameters to proceedToResult
            }
            no.visibility = View.VISIBLE
            no.setOnClickListener {
                chq2()
            }
        },1000)
    }


    private fun cpq5(){
        val fadein = AnimationUtils.loadAnimation(this, R.anim.fade_in)
        val fadeout = AnimationUtils.loadAnimation(this, R.anim.fade_out)
        val icon: TextView = findViewById(R.id.hiconyesno2)
        val question: TextView = findViewById(R.id.yesnoquestion2)
        val yes: Button = findViewById(R.id.yesanswer2)
        val no: Button = findViewById(R.id.noanswer2)
        icon.startAnimation(fadeout)
        question.startAnimation(fadeout)
        yes.startAnimation(fadeout)
        no.startAnimation(fadeout)
        yes.isEnabled = false
        no.isEnabled = false
        Handler().postDelayed({


            icon.visibility = View.GONE
            question.visibility = View.GONE
            yes.visibility = View.GONE
            no.visibility = View.GONE
            val icon1: TextView = findViewById(R.id.hiconyesno)
            val question1: TextView = findViewById(R.id.yesnoquestion)
            question1.text = "Has your pet had a chance for a poo in the past 24 hours?"
            val yes1: Button = findViewById(R.id.yesanswer)
            val no1: Button = findViewById(R.id.noanswer)
            yes1.isEnabled = true
            no1.isEnabled = true

            icon1.startAnimation(fadein)
            question1.startAnimation(fadein)
            yes1.startAnimation(fadein)
            no1.startAnimation(fadein)
            icon1.visibility = View.VISIBLE
            question1.visibility = View.VISIBLE
            yes1.visibility = View.VISIBLE
            yes1.setOnClickListener {
                proceedToResult(selectedSymptom, 5, "Yes") // Pass parameters to proceedToResult
            }
            no1.visibility = View.VISIBLE
            no1.setOnClickListener {
                proceedToResult(selectedSymptom, 5, "No")
            }
        },1000)
    }
    private fun cpq4(){
        val fadein = AnimationUtils.loadAnimation(this, R.anim.fade_in)
        val fadeout = AnimationUtils.loadAnimation(this, R.anim.fade_out)
        val icon: TextView = findViewById(R.id.hiconyesno)
        val question: TextView = findViewById(R.id.yesnoquestion)
        val yes: Button = findViewById(R.id.yesanswer)
        val no: Button = findViewById(R.id.noanswer)
        icon.startAnimation(fadeout)
        question.startAnimation(fadeout)
        yes.startAnimation(fadeout)
        no.startAnimation(fadeout)
        icon.visibility = View.GONE
        question.visibility = View.GONE
        yes.visibility = View.GONE
        no.visibility = View.GONE
        yes.isEnabled = false
        no.isEnabled = false
        Handler().postDelayed({


            val icon1: TextView = findViewById(R.id.hiconyesno2)
            val question1: TextView = findViewById(R.id.yesnoquestion2)
            question1.text =
                "When they attempt to poop, is there a tiny amount of red blood? (They might or might not poop.)"
            val yes1: Button = findViewById(R.id.yesanswer2)
            val no1: Button = findViewById(R.id.noanswer2)
            yes1.isEnabled = true
            no1.isEnabled = true

            icon1.startAnimation(fadein)
            question1.startAnimation(fadein)
            yes1.startAnimation(fadein)
            no1.startAnimation(fadein)
            icon1.visibility = View.VISIBLE
            question1.visibility = View.VISIBLE
            yes1.visibility = View.VISIBLE
            yes1.setOnClickListener {
                proceedToResult(selectedSymptom, 4, "Yes") // Pass parameters to proceedToResult
            }
            no1.visibility = View.VISIBLE
            no1.setOnClickListener {
                cpq5()
            }
        },1000)
    }
    private fun cpq3(){
        val fadein = AnimationUtils.loadAnimation(this, R.anim.fade_in)
        val fadeout = AnimationUtils.loadAnimation(this, R.anim.fade_out)
        val icon: TextView = findViewById(R.id.hiconyesno2)
        val question: TextView = findViewById(R.id.yesnoquestion2)
        val yes: Button = findViewById(R.id.yesanswer2)
        val no: Button = findViewById(R.id.noanswer2)
        icon.startAnimation(fadeout)
        question.startAnimation(fadeout)
        yes.startAnimation(fadeout)
        no.startAnimation(fadeout)
        yes.isEnabled = false
        no.isEnabled = false
        Handler().postDelayed({


            icon.visibility = View.GONE
            question.visibility = View.GONE
            yes.visibility = View.GONE
            no.visibility = View.GONE
            val icon1: TextView = findViewById(R.id.hiconyesno)
            val question1: TextView = findViewById(R.id.yesnoquestion)
            question1.text = "When your pet tries to poop, is there a lot of red blood?"
            val yes1: Button = findViewById(R.id.yesanswer)
            val no1: Button = findViewById(R.id.noanswer)
            yes1.isEnabled = true
            no1.isEnabled = true

            icon1.startAnimation(fadein)
            question1.startAnimation(fadein)
            yes1.startAnimation(fadein)
            no1.startAnimation(fadein)
            icon1.visibility = View.VISIBLE
            question1.visibility = View.VISIBLE
            yes1.visibility = View.VISIBLE
            yes1.setOnClickListener {
                proceedToResult(selectedSymptom, 3, "Yes") // Pass parameters to proceedToResult
            }
            no1.visibility = View.VISIBLE
            no1.setOnClickListener {
                cpq4()
            }
        },1000)
    }
    private fun cpq2(){
        val fadein = AnimationUtils.loadAnimation(this, R.anim.fade_in)
        val fadeout = AnimationUtils.loadAnimation(this, R.anim.fade_out)
        val icon: TextView = findViewById(R.id.hiconyesno)
        val question: TextView = findViewById(R.id.yesnoquestion)
        val yes: Button = findViewById(R.id.yesanswer)
        val no: Button = findViewById(R.id.noanswer)
        icon.startAnimation(fadeout)
        question.startAnimation(fadeout)
        yes.startAnimation(fadeout)
        no.startAnimation(fadeout)
        yes.isEnabled = false
        no.isEnabled = false
        Handler().postDelayed({

            icon.visibility = View.GONE
            question.visibility = View.GONE
            yes.visibility = View.GONE
            no.visibility = View.GONE
            val icon1: TextView = findViewById(R.id.hiconyesno2)
            val question1: TextView = findViewById(R.id.yesnoquestion2)
            question1.text = "Has your pet vomited more than once in the last 24 hours?"
            val yes1: Button = findViewById(R.id.yesanswer2)
            val no1: Button = findViewById(R.id.noanswer2)
            yes1.isEnabled = true
            no1.isEnabled = true

            icon1.startAnimation(fadein)
            question1.startAnimation(fadein)
            yes1.startAnimation(fadein)
            no1.startAnimation(fadein)
            icon1.visibility = View.VISIBLE
            question1.visibility = View.VISIBLE
            yes1.visibility = View.VISIBLE
            yes1.setOnClickListener {
                proceedToResult(selectedSymptom, 2, "Yes") // Pass parameters to proceedToResult
            }
            no1.visibility = View.VISIBLE
            no1.setOnClickListener {
                cpq3()
            }
        },1000)
    }

    private fun displayConstipationQuestion() {
        val fadein = AnimationUtils.loadAnimation(this, R.anim.fade_in)
        val fadeout = AnimationUtils.loadAnimation(this, R.anim.fade_out)
        val spinner: Spinner = findViewById(R.id.dogsymptom)
        val iconsymptom: TextView = findViewById(R.id.hiconsymptom)
        val symptomq: TextView = findViewById(R.id.dogsymptomquestion)
        val btn: Button = findViewById(R.id.donebtn)
        spinner.startAnimation(fadeout)
        iconsymptom.startAnimation(fadeout)
        symptomq.startAnimation(fadeout)
        btn.startAnimation(fadeout)
        spinner.isEnabled = false
        btn.isEnabled = false
        Handler().postDelayed({
            spinner.visibility = View.GONE
            iconsymptom.visibility = View.GONE
            symptomq.visibility = View.GONE
            btn.visibility = View.GONE
            val icon: TextView = findViewById(R.id.hiconyesno)
            val question: TextView = findViewById(R.id.yesnoquestion)
            question.text = "Does your pet seem lethargic or do they have a reduced appetite?"
            val yes: Button = findViewById(R.id.yesanswer)
            val no: Button = findViewById(R.id.noanswer)
            yes.isEnabled = true
            no.isEnabled = true

            icon.startAnimation(fadein)
            question.startAnimation(fadein)
            yes.startAnimation(fadein)
            no.startAnimation(fadein)
            icon.visibility = View.VISIBLE
            question.visibility = View.VISIBLE
            yes.visibility = View.VISIBLE
            yes.setOnClickListener {
                proceedToResult(selectedSymptom, 1, "Yes") // Pass parameters to proceedToResult
            }
            no.visibility = View.VISIBLE
            no.setOnClickListener {
                cpq2()
            }
        },1000)
    }

    private fun bsq6(){
        val fadein = AnimationUtils.loadAnimation(this, R.anim.fade_in)
        val fadeout = AnimationUtils.loadAnimation(this, R.anim.fade_out)
        val icon: TextView = findViewById(R.id.hiconyesno)
        val question: TextView = findViewById(R.id.yesnoquestion)
        val yes: Button = findViewById(R.id.yesanswer)
        val no: Button = findViewById(R.id.noanswer)
        icon.startAnimation(fadeout)
        question.startAnimation(fadeout)
        yes.startAnimation(fadeout)
        no.startAnimation(fadeout)
        yes.isEnabled = false
        no.isEnabled = false
        Handler().postDelayed({
            icon.visibility = View.GONE
            question.visibility = View.GONE
            yes.visibility = View.GONE
            no.visibility = View.GONE
            val icon1: TextView = findViewById(R.id.hiconyesno2)
            val question1: TextView = findViewById(R.id.yesnoquestion2)
            question1.text =
                "Has your pet increased its water intake in the last three to six months?"
            val yes1: Button = findViewById(R.id.yesanswer2)
            val no1: Button = findViewById(R.id.noanswer2)
            yes1.isEnabled = true
            no1.isEnabled = true

            icon1.startAnimation(fadein)
            question1.startAnimation(fadein)
            yes1.startAnimation(fadein)
            no1.startAnimation(fadein)
            icon1.visibility = View.VISIBLE
            question1.visibility = View.VISIBLE
            yes1.visibility = View.VISIBLE
            yes1.setOnClickListener {
                proceedToResult(selectedSymptom, 6, "Yes")
            }
            no1.visibility = View.VISIBLE
            no1.setOnClickListener {
                proceedToResult(selectedSymptom, 6, "No")
            }
        },1000)
    }
    private fun bsq5(){
        val fadein = AnimationUtils.loadAnimation(this, R.anim.fade_in)
        val fadeout = AnimationUtils.loadAnimation(this, R.anim.fade_out)
        val icon: TextView = findViewById(R.id.hiconyesno2)
        val question: TextView = findViewById(R.id.yesnoquestion2)
        val yes: Button = findViewById(R.id.yesanswer2)
        val no: Button = findViewById(R.id.noanswer2)
        icon.startAnimation(fadeout)
        question.startAnimation(fadeout)
        yes.startAnimation(fadeout)
        no.startAnimation(fadeout)
        yes.isEnabled = false
        no.isEnabled = false
        Handler().postDelayed({
            icon.visibility = View.GONE
            question.visibility = View.GONE
            yes.visibility = View.GONE
            no.visibility = View.GONE
            val icon1: TextView = findViewById(R.id.hiconyesno)
            val question1: TextView = findViewById(R.id.yesnoquestion)
            question1.text =
                "Does your pet receive monthly heartworm prevention, or did they just receive a dewormer?"
            val yes1: Button = findViewById(R.id.yesanswer)
            val no1: Button = findViewById(R.id.noanswer)
            yes1.isEnabled = true
            no1.isEnabled = true

            icon1.startAnimation(fadein)
            question1.startAnimation(fadein)
            yes1.startAnimation(fadein)
            no1.startAnimation(fadein)
            icon1.visibility = View.VISIBLE
            question1.visibility = View.VISIBLE
            yes1.visibility = View.VISIBLE
            yes1.setOnClickListener {
                bsq6()
            }
            no1.visibility = View.VISIBLE
            no1.setOnClickListener {
                proceedToResult(selectedSymptom, 5, "No")
            }
        },1000)
    }
    private fun bsq4(){
        val fadein = AnimationUtils.loadAnimation(this, R.anim.fade_in)
        val fadeout = AnimationUtils.loadAnimation(this, R.anim.fade_out)
        val icon: TextView = findViewById(R.id.hiconyesno)
        val question: TextView = findViewById(R.id.yesnoquestion)
        val yes: Button = findViewById(R.id.yesanswer)
        val no: Button = findViewById(R.id.noanswer)
        icon.startAnimation(fadeout)
        question.startAnimation(fadeout)
        yes.startAnimation(fadeout)
        no.startAnimation(fadeout)
        yes.isEnabled = false
        no.isEnabled = false
        Handler().postDelayed({
            icon.visibility = View.GONE
            question.visibility = View.GONE
            yes.visibility = View.GONE
            no.visibility = View.GONE
            val icon1: TextView = findViewById(R.id.hiconyesno2)
            val question1: TextView = findViewById(R.id.yesnoquestion2)
            question1.text = "Does your pet poop or urinate less frequently, or not at all?"
            val yes1: Button = findViewById(R.id.yesanswer2)
            val no1: Button = findViewById(R.id.noanswer2)
            yes1.isEnabled = true
            no1.isEnabled = true

            icon1.startAnimation(fadein)
            question1.startAnimation(fadein)
            yes1.startAnimation(fadein)
            no1.startAnimation(fadein)
            icon1.visibility = View.VISIBLE
            question1.visibility = View.VISIBLE
            yes1.visibility = View.VISIBLE
            yes1.setOnClickListener {
                proceedToResult(selectedSymptom, 4, "Yes") // Pass parameters to proceedToResult
            }
            no1.visibility = View.VISIBLE
            no1.setOnClickListener {
                bsq5() // Pass parameters to proceedToResult
            }
        },1000)
    }
    private fun bsq3(){
        val fadein = AnimationUtils.loadAnimation(this, R.anim.fade_in)
        val fadeout = AnimationUtils.loadAnimation(this, R.anim.fade_out)
        val icon: TextView = findViewById(R.id.hiconyesno2)
        val question: TextView = findViewById(R.id.yesnoquestion2)
        val yes: Button = findViewById(R.id.yesanswer2)
        val no: Button = findViewById(R.id.noanswer2)
        icon.startAnimation(fadeout)
        question.startAnimation(fadeout)
        yes.startAnimation(fadeout)
        no.startAnimation(fadeout)
        yes.isEnabled = false
        no.isEnabled = false
        Handler().postDelayed({
            icon.visibility = View.GONE
            question.visibility = View.GONE
            yes.visibility = View.GONE
            no.visibility = View.GONE
            val icon1: TextView = findViewById(R.id.hiconyesno)
            val question1: TextView = findViewById(R.id.yesnoquestion)
            question1.text =
                "Are any of these behaviors shown by your pet? Some examples of behaviors are:\n" +
                        "\n" +
                        "Not eating or not eating much, \n" +
                        "Acting lethargic, \n" +
                        "Vomiting frequently, \n" +
                        "Retching, and/or\n" +
                        "Having diarrhea."
            val yes1: Button = findViewById(R.id.yesanswer)
            val no1: Button = findViewById(R.id.noanswer)
            yes1.isEnabled = true
            no1.isEnabled = true

            icon1.startAnimation(fadein)
            question1.startAnimation(fadein)
            yes1.startAnimation(fadein)
            no1.startAnimation(fadein)
            icon1.visibility = View.VISIBLE
            question1.visibility = View.VISIBLE
            yes1.visibility = View.VISIBLE
            yes1.setOnClickListener {
                proceedToResult(selectedSymptom, 3, "Yes") // Pass parameters to proceedToResult
            }
            no1.visibility = View.VISIBLE
            no1.setOnClickListener {
                bsq4()
            }
        },1000)
    }
    private fun bsq2(){
        val fadein = AnimationUtils.loadAnimation(this, R.anim.fade_in)
        val fadeout = AnimationUtils.loadAnimation(this, R.anim.fade_out)
        val icon: TextView = findViewById(R.id.hiconyesno)
        val question: TextView = findViewById(R.id.yesnoquestion)
        val yes: Button = findViewById(R.id.yesanswer)
        val no: Button = findViewById(R.id.noanswer)
        icon.startAnimation(fadeout)
        question.startAnimation(fadeout)
        yes.startAnimation(fadeout)
        no.startAnimation(fadeout)
        yes.isEnabled = false
        no.isEnabled = false
        Handler().postDelayed({

            icon.visibility = View.GONE
            question.visibility = View.GONE
            yes.visibility = View.GONE
            no.visibility = View.GONE
            val icon1: TextView = findViewById(R.id.hiconyesno2)
            val question1: TextView = findViewById(R.id.yesnoquestion2)
            question1.text =
                "Has your pet's stomach suddenly gotten bigger within the last one to three days?"
            val yes1: Button = findViewById(R.id.yesanswer2)
            val no1: Button = findViewById(R.id.noanswer2)
            yes1.isEnabled = true
            no1.isEnabled = true

            icon1.startAnimation(fadein)
            question1.startAnimation(fadein)
            yes1.startAnimation(fadein)
            no1.startAnimation(fadein)
            icon1.visibility = View.VISIBLE
            question1.visibility = View.VISIBLE
            yes1.visibility = View.VISIBLE
            yes1.setOnClickListener {
                proceedToResult(selectedSymptom, 2, "Yes") // Pass parameters to proceedToResult
            }
            no1.visibility = View.VISIBLE
            no1.setOnClickListener {
                bsq3()
            }
        },1000)
    }

    private fun displayBloatedStomachQuestion() {
        val fadein = AnimationUtils.loadAnimation(this, R.anim.fade_in)
        val fadeout = AnimationUtils.loadAnimation(this, R.anim.fade_out)
        val spinner: Spinner = findViewById(R.id.dogsymptom)
        val iconsymptom: TextView = findViewById(R.id.hiconsymptom)
        val symptomq: TextView = findViewById(R.id.dogsymptomquestion)
        val btn: Button = findViewById(R.id.donebtn)
        spinner.startAnimation(fadeout)
        iconsymptom.startAnimation(fadeout)
        symptomq.startAnimation(fadeout)
        btn.startAnimation(fadeout)
        spinner.isEnabled = false
        btn.isEnabled = false
        Handler().postDelayed({
            spinner.visibility = View.GONE
            iconsymptom.visibility = View.GONE
            symptomq.visibility = View.GONE
            btn.visibility = View.GONE
            val icon: TextView = findViewById(R.id.hiconyesno)
            val question: TextView = findViewById(R.id.yesnoquestion)
            question.text = "Are either true:\n" +
                    "\n" +
                    "Your pet is breathing through their neck or tummy muscles, and/or\n" +
                    "Are the gums on your pet gray, white, blue, or pale pink?"
            val yes: Button = findViewById(R.id.yesanswer)
            val no: Button = findViewById(R.id.noanswer)
            yes.isEnabled = true
            no.isEnabled = true
            icon.startAnimation(fadein)
            question.startAnimation(fadein)
            yes.startAnimation(fadein)
            no.startAnimation(fadein)
            icon.visibility = View.VISIBLE
            question.visibility = View.VISIBLE
            yes.visibility = View.VISIBLE
            yes.setOnClickListener {
                proceedToResult(selectedSymptom, 1, "Yes") // Pass parameters to proceedToResult
            }
            no.visibility = View.VISIBLE
            no.setOnClickListener {
                bsq2()
            }
        },1000)
    }



    private fun ahq4(){
        val fadein = AnimationUtils.loadAnimation(this, R.anim.fade_in)
        val fadeout = AnimationUtils.loadAnimation(this, R.anim.fade_out)
        val icon: TextView = findViewById(R.id.hiconyesno)
        val question: TextView = findViewById(R.id.yesnoquestion)
        val yes: Button = findViewById(R.id.yesanswer)
        val no: Button = findViewById(R.id.noanswer)
        icon.startAnimation(fadeout)
        question.startAnimation(fadeout)
        yes.startAnimation(fadeout)
        no.startAnimation(fadeout)
        yes.isEnabled = false
        no.isEnabled = false

        Handler().postDelayed({
            icon.visibility = View.GONE
            question.visibility = View.GONE
            yes.visibility = View.GONE
            no.visibility = View.GONE
            val icon1: TextView = findViewById(R.id.hiconyesno2)
            val question1: TextView = findViewById(R.id.yesnoquestion2)
            question1.text =
                "Does your dog take any medications (prednisone, anti-seizure medications)?"
            val yes1: Button = findViewById(R.id.yesanswer2)
            val no1: Button = findViewById(R.id.noanswer2)
            yes1.isEnabled = true
            no1.isEnabled = true
            icon1.startAnimation(fadein)
            question1.startAnimation(fadein)
            yes1.startAnimation(fadein)
            no1.startAnimation(fadein)
            icon1.visibility = View.VISIBLE
            question1.visibility = View.VISIBLE
            yes1.visibility = View.VISIBLE
            yes1.setOnClickListener {
                proceedToResult(selectedSymptom, 4, "Yes") // Pass parameters to proceedToResult
            }
            no1.visibility = View.VISIBLE
            no1.setOnClickListener {
                proceedToResult(selectedSymptom, 4, "No") // Pass parameters to proceedToResult
            }
        },1000)
    }
    private fun ahq3(){
        val fadein = AnimationUtils.loadAnimation(this, R.anim.fade_in)
        val fadeout = AnimationUtils.loadAnimation(this, R.anim.fade_out)
        val icon: TextView = findViewById(R.id.hiconyesno2)
        val question: TextView = findViewById(R.id.yesnoquestion2)
        val yes: Button = findViewById(R.id.yesanswer2)
        val no: Button = findViewById(R.id.noanswer2)
        icon.startAnimation(fadeout)
        question.startAnimation(fadeout)
        yes.startAnimation(fadeout)
        no.startAnimation(fadeout)
        yes.isEnabled = false
        no.isEnabled = false

        Handler().postDelayed({
            icon.visibility = View.GONE
            question.visibility = View.GONE
            yes.visibility = View.GONE
            no.visibility = View.GONE
            val icon1: TextView = findViewById(R.id.hiconyesno)
            val question1: TextView = findViewById(R.id.yesnoquestion)
            question1.text =
                "Is your pet recovering from an illness, surgery, parasites, or malnourishment?"
            val yes1: Button = findViewById(R.id.yesanswer)
            val no1: Button = findViewById(R.id.noanswer)
            yes1.isEnabled = true
            no1.isEnabled = true
            icon1.startAnimation(fadein)
            question1.startAnimation(fadein)
            yes1.startAnimation(fadein)
            no1.startAnimation(fadein)
            icon1.visibility = View.VISIBLE
            question1.visibility = View.VISIBLE
            yes1.visibility = View.VISIBLE
            yes1.setOnClickListener {
                proceedToResult(selectedSymptom, 3, "Yes") // Pass parameters to proceedToResult
            }
            no1.visibility = View.VISIBLE
            no1.setOnClickListener {
                ahq4()
            }
        },1000)
    }

    private fun ahq2(){
        val fadein = AnimationUtils.loadAnimation(this, R.anim.fade_in)
        val fadeout = AnimationUtils.loadAnimation(this, R.anim.fade_out)
        val icon: TextView = findViewById(R.id.hiconyesno)
        val question: TextView = findViewById(R.id.yesnoquestion)
        val yes: Button = findViewById(R.id.yesanswer)
        val no: Button = findViewById(R.id.noanswer)
        icon.startAnimation(fadeout)
        question.startAnimation(fadeout)
        yes.startAnimation(fadeout)
        no.startAnimation(fadeout)
        yes.isEnabled = false
        no.isEnabled = false
        Handler().postDelayed({
            icon.visibility = View.GONE
            question.visibility = View.GONE
            yes.visibility = View.GONE
            no.visibility = View.GONE
            val icon1: TextView = findViewById(R.id.hiconyesno2)
            val question1: TextView = findViewById(R.id.yesnoquestion2)
            question1.text =
                "Is your dog drinking more or urinating more frequently than normal?"
            val yes1: Button = findViewById(R.id.yesanswer2)
            val no1: Button = findViewById(R.id.noanswer2)
            yes1.isEnabled = true
            no1.isEnabled = true
            icon1.startAnimation(fadein)
            question1.startAnimation(fadein)
            yes1.startAnimation(fadein)
            no1.startAnimation(fadein)
            icon1.visibility = View.VISIBLE
            question1.visibility = View.VISIBLE
            yes1.visibility = View.VISIBLE
            yes1.setOnClickListener {
                proceedToResult(selectedSymptom, 2, "Yes") // Pass parameters to proceedToResult
            }
            no1.visibility = View.VISIBLE
            no1.setOnClickListener {
                ahq3()
            }
        },1000)
    }

    private fun displayAlwaysHungryQuestion() {
        val fadein = AnimationUtils.loadAnimation(this, R.anim.fade_in)
        val fadeout = AnimationUtils.loadAnimation(this, R.anim.fade_out)
        val spinner: Spinner = findViewById(R.id.dogsymptom)
        val iconsymptom: TextView = findViewById(R.id.hiconsymptom)
        val symptomq: TextView = findViewById(R.id.dogsymptomquestion)
        val btn: Button = findViewById(R.id.donebtn)
        spinner.startAnimation(fadeout)
        iconsymptom.startAnimation(fadeout)
        symptomq.startAnimation(fadeout)
        btn.startAnimation(fadeout)
        spinner.isEnabled = false
        btn.isEnabled = false
        Handler().postDelayed({
            spinner.visibility = View.GONE
            iconsymptom.visibility = View.GONE
            symptomq.visibility = View.GONE
            btn.visibility = View.GONE
            val icon: TextView = findViewById(R.id.hiconyesno)
            val question: TextView = findViewById(R.id.yesnoquestion)
            question.text =
                "Do any of the following behaviors apply to your pet? Actions may consist of:\n" +
                        "\n" +
                        "Drinking less than usual, \n" +
                        "Having frequent diarrhea, \n" +
                        "Acting lethargic, and/or\n" +
                        "Vomiting.\n"
            val yes: Button = findViewById(R.id.yesanswer)
            val no: Button = findViewById(R.id.noanswer)
            yes.isEnabled = true
            no.isEnabled = true
            icon.startAnimation(fadein)
            question.startAnimation(fadein)
            yes.startAnimation(fadein)
            no.startAnimation(fadein)
            icon.visibility = View.VISIBLE
            question.visibility = View.VISIBLE
            yes.visibility = View.VISIBLE
            yes.setOnClickListener {
                proceedToResult(selectedSymptom, 1, "Yes") // Pass parameters to proceedToResult
            }
            no.visibility = View.VISIBLE
            no.setOnClickListener {
                ahq2()
            }
        },1000)
    }


    private fun agq5(){
        val fadein = AnimationUtils.loadAnimation(this, R.anim.fade_in)
        val fadeout = AnimationUtils.loadAnimation(this, R.anim.fade_out)
        val icon: TextView = findViewById(R.id.iconchoices4)
        val question: TextView = findViewById(R.id.questionchoice4)
        val yes: Button = findViewById(R.id.ch1ch4)
        val yes1 : Button = findViewById(R.id.ch2ch4)
        val no: Button = findViewById(R.id.ch3ch4)
        val no1 : Button = findViewById(R.id.ch4ch4)
        yes1.isEnabled = false
        yes.isEnabled = false
        no1.isEnabled = false
        no.isEnabled = false
        icon.startAnimation(fadeout)
        question.startAnimation(fadeout)
        yes.startAnimation(fadeout)
        yes1.startAnimation(fadeout)
        no.startAnimation(fadeout)
        no1.startAnimation(fadeout)
        Handler().postDelayed({
            icon.visibility = View.GONE
            question.visibility = View.GONE
            yes.visibility = View.GONE
            yes1.visibility = View.GONE
            no.visibility = View.GONE
            no1.visibility = View.GONE
            val icon2: TextView = findViewById(R.id.hiconyesno)
            val question2: TextView = findViewById(R.id.yesnoquestion)
            question2.text = "Do you see the following signs?\n" +
                    "\n" +
                    "Tail tucked, \n" +
                    "Ears back, \n" +
                    "Crouching, \n" +
                    "Urinating, \n" +
                    "Rolling on their back, \n" +
                    "Moving/backing away (from a person or place), \n" +
                    "Making themselves seem smaller, and/or\n" +
                    "Snapping the air.\n"
            val yes2: Button = findViewById(R.id.yesanswer)
            val no2: Button = findViewById(R.id.noanswer)
            yes2.isEnabled = true
            no2.isEnabled = true

            icon2.startAnimation(fadein)
            question2.startAnimation(fadein)
            yes2.startAnimation(fadein)
            no2.startAnimation(fadein)
            icon2.visibility = View.VISIBLE
            question2.visibility = View.VISIBLE
            yes2.visibility = View.VISIBLE
            yes2.setOnClickListener {
                proceedToResult(selectedSymptom, 5, "Yes") // Pass parameters to proceedToResult
            }
            no2.visibility = View.VISIBLE
            no2.setOnClickListener {
                proceedToResult(selectedSymptom, 5, "No")
            }
        },1000)

    }
    private fun agq4(){
        val fadein = AnimationUtils.loadAnimation(this, R.anim.fade_in)
        val fadeout = AnimationUtils.loadAnimation(this, R.anim.fade_out)
        val icon: TextView = findViewById(R.id.hiconyesno)
        val question: TextView = findViewById(R.id.yesnoquestion)
        val yes: Button = findViewById(R.id.yesanswer)
        val no: Button = findViewById(R.id.noanswer)
        icon.startAnimation(fadeout)
        question.startAnimation(fadeout)
        yes.startAnimation(fadeout)
        no.startAnimation(fadeout)
        yes.isEnabled = false
        no.isEnabled = false
        Handler().postDelayed({
            icon.visibility = View.GONE
            question.visibility = View.GONE
            yes.visibility = View.GONE
            no.visibility = View.GONE
            val icon1: TextView = findViewById(R.id.iconchoices4)
            val question1: TextView = findViewById(R.id.questionchoice4)
            question1.text = "When does the aggressive behavior happen?"
            val yes1: Button = findViewById(R.id.ch1ch4)
            yes1.text = "Around certain people or places"
            val yes2: Button = findViewById(R.id.ch2ch4)
            yes2.text = "Around other pets"
            val no1: Button = findViewById(R.id.ch3ch4)
            no1.text =
                "When your dog is guarding something: food, toys, owner, other pets, or an area of the house, like the yard or a certain room (look for low growling, raising their lip, yawning, whale eye or side eye, crouching over certain things)"
            val no2: Button = findViewById(R.id.ch4ch4)
            no2.text = "When you touch a certain part of their body"
            icon1.startAnimation(fadein)
            question1.startAnimation(fadein)
            yes1.startAnimation(fadein)
            yes2.startAnimation(fadein)
            no1.startAnimation(fadein)
            no2.startAnimation(fadein)
            icon1.visibility = View.VISIBLE
            question1.visibility = View.VISIBLE
            yes1.visibility = View.VISIBLE
            yes1.setOnClickListener {
                agq5()
            }
            yes2.visibility = View.VISIBLE
            yes2.setOnClickListener {
                agq5()
            }
            no1.visibility = View.VISIBLE
            no1.setOnClickListener {
                proceedToResult(selectedSymptom, 4, "Choice3")
            }
            no2.visibility = View.VISIBLE
            no2.setOnClickListener {
                proceedToResult(
                    selectedSymptom,
                    4,
                    "Choice4"
                ) // Pass parameters to proceedToResult
            }
        }, 1000)

    }
    private fun agq3(){
        val fadein = AnimationUtils.loadAnimation(this, R.anim.fade_in)
        val fadeout = AnimationUtils.loadAnimation(this, R.anim.fade_out)
        val icon: TextView = findViewById(R.id.hiconyesno2)
        val question: TextView = findViewById(R.id.yesnoquestion2)
        val yes: Button = findViewById(R.id.yesanswer2)
        val no: Button = findViewById(R.id.noanswer2)
        icon.startAnimation(fadeout)
        question.startAnimation(fadeout)
        yes.startAnimation(fadeout)
        no.startAnimation(fadeout)
        yes.isEnabled = false
        no.isEnabled = false
        Handler().postDelayed({
            icon.visibility = View.GONE
            question.visibility = View.GONE
            yes.visibility = View.GONE
            no.visibility = View.GONE
            val icon1: TextView = findViewById(R.id.hiconyesno)
            val question1: TextView = findViewById(R.id.yesnoquestion)
            question1.text = "Has your dog changed all of a sudden?"
            val yes1: Button = findViewById(R.id.yesanswer)
            val no1: Button = findViewById(R.id.noanswer)
            yes1.isEnabled = true
            no1.isEnabled = true
            icon1.startAnimation(fadein)
            question1.startAnimation(fadein)
            yes1.startAnimation(fadein)
            no1.startAnimation(fadein)
            icon1.visibility = View.VISIBLE
            question1.visibility = View.VISIBLE
            yes1.visibility = View.VISIBLE
            yes1.setOnClickListener {
                proceedToResult(selectedSymptom, 3, "Yes") // Pass parameters to proceedToResult
            }
            no1.visibility = View.VISIBLE
            no1.setOnClickListener {
                agq4()
            }
        }, 1000)
    }

    private fun questionAggression2(){
        val fadein = AnimationUtils.loadAnimation(this, R.anim.fade_in)
        val fadeout = AnimationUtils.loadAnimation(this, R.anim.fade_out)
        val icon: TextView = findViewById(R.id.hiconyesno)
        val question: TextView = findViewById(R.id.yesnoquestion)
        val yes: Button = findViewById(R.id.yesanswer)
        val no: Button = findViewById(R.id.noanswer)
        icon.startAnimation(fadeout)
        question.startAnimation(fadeout)
        yes.startAnimation(fadeout)
        no.startAnimation(fadeout)
        Handler().postDelayed({
            icon.visibility = View.GONE
            question.visibility = View.GONE
            yes.visibility = View.GONE
            no.visibility = View.GONE
            val icon1: TextView = findViewById(R.id.hiconyesno2)
            val question1: TextView = findViewById(R.id.yesnoquestion2)
            question1.text = "Are they doing any of the following all of a sudden?\n" +
                    "Plunging, Aiming sharply at the air, and/or Pinning down, concerning, going after humans or animals in the household."
            val yes1: Button = findViewById(R.id.yesanswer2)
            val no1: Button = findViewById(R.id.noanswer2)
            yes1.isEnabled = true
            no1.isEnabled = true
            icon1.startAnimation(fadein)
            question1.startAnimation(fadein)
            yes1.startAnimation(fadein)
            no1.startAnimation(fadein)
            icon1.visibility = View.VISIBLE
            question1.visibility = View.VISIBLE
            yes1.visibility = View.VISIBLE
            yes1.setOnClickListener {
                proceedToResult(selectedSymptom, 2, "Yes") // Pass parameters to proceedToResult
            }
            no1.visibility = View.VISIBLE
            no1.setOnClickListener {
                agq3()
            }
        }, 1000) // Delay for 1000 milliseconds (1 second)
    }

    private fun questionAggression1() {
        val fadein = AnimationUtils.loadAnimation(this, R.anim.fade_in)
        val fadeout = AnimationUtils.loadAnimation(this, R.anim.fade_out)
        val spinner: Spinner = findViewById(R.id.dogsymptom)
        val iconsymptom: TextView = findViewById(R.id.hiconsymptom)
        val symptomq: TextView = findViewById(R.id.dogsymptomquestion)
        val btn: Button = findViewById(R.id.donebtn)

        // Start fade out animations
        spinner.startAnimation(fadeout)
        iconsymptom.startAnimation(fadeout)
        symptomq.startAnimation(fadeout)
        btn.startAnimation(fadeout)

        spinner.isEnabled = false
        btn.isEnabled = false
        // Set visibility to GONE after fade out
        Handler().postDelayed({
            spinner.visibility = View.GONE
            iconsymptom.visibility = View.GONE
            symptomq.visibility = View.GONE
            btn.visibility = View.GONE

            // Start fade in animations after a 1-second delay
            val icon: TextView = findViewById(R.id.hiconyesno)
            val question: TextView = findViewById(R.id.yesnoquestion)
            val yes: Button = findViewById(R.id.yesanswer)
            val no: Button = findViewById(R.id.noanswer)

            icon.startAnimation(fadein)
            question.startAnimation(fadein)
            yes.startAnimation(fadein)
            no.startAnimation(fadein)

            // Set visibility to VISIBLE after fade in
            icon.visibility = View.VISIBLE
            question.visibility = View.VISIBLE
            yes.visibility = View.VISIBLE
            no.visibility = View.VISIBLE

            // Set onClickListeners for yes and no buttons
            yes.setOnClickListener {
                proceedToResult(selectedSymptom, 1, "Yes") // Pass parameters to proceedToResult
            }
            no.setOnClickListener {
                questionAggression2()
            }

            // Update question text
            question.text = "Does your pet have any chronic health issues? Has it been in any accidents, traumas, or toxic environments recently?"
        }, 1000) // Delay for 1000 milliseconds (1 second)

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

