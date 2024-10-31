package com.example.buddy

import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.text.Spannable
import android.text.SpannableString
import android.text.method.LinkMovementMethod
import android.text.style.ClickableSpan
import android.view.View
import android.widget.Button
import android.widget.TextView
import androidx.activity.ComponentActivity
import androidx.core.app.ActivityOptionsCompat
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject
import java.io.IOException
import java.io.InputStream

class symptom_checker_result : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_symptom_checker_result)

        val backbtn = findViewById<Button>(R.id.go_back_button)
        backbtn.setOnClickListener {
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
        val bundle = intent.getBundleExtra("resultBundle")

        // Extract the result key from the Bundle
        val resultKey = bundle?.getString("resultKey")

        // Process the resultKey to display the corresponding result
        processResultKey(resultKey)

    }
    private fun processResultKey(resultKey: String?) {
        resultKey?.let {
            val jsonPair = loadJsonFromAssets(resultKey)
            jsonPair?.let { (jsonObject, matchedResultKey) ->
                val reminderTitle = jsonObject.getString("ReminderTitle")
                val reminderText = jsonObject.getString("ReminderText")
                val whatToExpectText = jsonObject.getString("WhatToExpectText")
                val questionToVetText = jsonObject.getString("QuestionForVetText")
                val canineFirstAid = jsonObject.getString("CanineFirstAid")

                // Update UI components with the retrieved information
                findViewById<TextView>(R.id.symptomreminder).text = reminderTitle
                findViewById<TextView>(R.id.symptomremindertext).text = reminderText
                findViewById<TextView>(R.id.symptomwhattoexpecttext).text = whatToExpectText
                findViewById<TextView>(R.id.symptomvetquestiontext).text = questionToVetText
                findViewById<TextView>(R.id.symptomcanineemergencytext).text = canineFirstAid
            }
        }
    }

    private fun loadJsonFromAssets(resultKey: String): Pair<JSONObject, String>? {
        val json: String?
        try {
            val inputStream: InputStream = assets.open("symptomchecker.json")
            json = inputStream.bufferedReader().use { it.readText() }
            val jsonArray = JSONArray(json)
            // Since your JSON is an array, search for the matching result key
            for (i in 0 until jsonArray.length()) {
                val resultObject = jsonArray.getJSONObject(i)
                val result = resultObject.getString("Result")
                if (result == resultKey) {
                    return Pair(resultObject, result)
                }
            }
        } catch (e: IOException) {
            e.printStackTrace()
        } catch (e: JSONException) {
            e.printStackTrace()
        }
        return null
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
