package com.example.buddy

import Event
import android.app.Activity
import android.app.ActivityOptions
import android.app.AlertDialog
import android.content.ContentValues.TAG
import android.content.Context
import android.content.Intent
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.util.Log
import android.widget.Button
import androidx.activity.ComponentActivity
import android.view.Window
import android.widget.TextView
import android.widget.Toast
import androidx.compose.ui.graphics.Color
import androidx.core.app.ActivityOptionsCompat
import androidx.core.content.ContextCompat
import com.google.common.reflect.TypeToken
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.prolificinteractive.materialcalendarview.CalendarDay
import com.prolificinteractive.materialcalendarview.MaterialCalendarView
import com.prolificinteractive.materialcalendarview.DayViewDecorator
import com.google.gson.Gson
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale
import kotlin.collections.mutableMapOf


class calendar : ComponentActivity() {
    private lateinit var calendarView: MaterialCalendarView
    val eventsMap = mutableMapOf<String, MutableList<Event>>()
    private val PREFS_NAME = "CalendarEvents"
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_calendar)

        //Profile button to view the user's profile
        val user_profile_btn : Button = findViewById(R.id.profile)
        user_profile_btn.setOnClickListener {
            val intent = Intent(this, profile_overview::class.java)
            val options = ActivityOptionsCompat.makeCustomAnimation(
                this,
                R.anim.dissolve_in, // Animation for the new activity
                R.anim.dissolve_out // No animation for the current activity
            )

            // Start the activity with the specified animation options
            startActivity(intent, options.toBundle())
            finish()
        }

        val home : Button = findViewById(R.id.homepage)
        home.setOnClickListener {
            val intent = Intent(this@calendar, homepageold::class.java)
            val options = ActivityOptionsCompat.makeCustomAnimation(
                this,
                R.anim.dissolve_in, // Animation for the new activity
                R.anim.dissolve_out // No animation for the current activity
            )

            // Start the activity with the specified animation options
            startActivity(intent, options.toBundle())
            finish()
        }

        //health tools button for health tools page
        val healthtools_btn : Button = findViewById(R.id.healthtools)
        healthtools_btn.setOnClickListener {
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

        calendarView = findViewById<MaterialCalendarView>(R.id.calendarView)

        val addEventButton: Button = findViewById(R.id.addevent)

        addEventButton.setOnClickListener {

            val intent = Intent(this@calendar, addevent_calendar::class.java)
            startActivityForResult(intent, Companion.ADD_EVENT_REQUEST_CODE)
        }



        val calendarDays = eventsMap.keys.map { dateString ->
            val date = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).parse(dateString)
            val calendar = Calendar.getInstance().apply { time = date }
            CalendarDay.from(calendar)
        }


        // Solution a (preferred): Pass calendarView to the constructor
        val eventDecorator = EventDecorator(this@calendar, calendarView, getColor(R.color.light))
        calendarView.addDecorators(eventDecorator)

        loadEventsFromSharedPreferences()
        updateCalendarUI()


    }

    private fun loadEventsFromSharedPreferences() {
        val sharedPreferences = getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        val gson = Gson()
        val json = sharedPreferences.getString("eventJson", null)
        val type = object : TypeToken<MutableMap<String, MutableList<Event>>>() {}.type
        eventsMap.clear()
        if (!json.isNullOrEmpty()) {
            val loadedEventsMap: MutableMap<String, MutableList<Event>> = gson.fromJson(json, type)
            eventsMap.putAll(loadedEventsMap)
        }
        Log.d(TAG, "Events loaded from SharedPreferences: $eventsMap")
    }




    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == Companion.ADD_EVENT_REQUEST_CODE && resultCode == Activity.RESULT_OK) {
            val selectedDate = data?.getStringExtra("date")
            val time = data?.getStringExtra("time")
            val title = data?.getStringExtra("title")
            val note = data?.getStringExtra("note")

            if (selectedDate != null && time != null && title != null && note != null) {

                val event = Event(selectedDate, time, title, note)


                updateEventsMap(selectedDate, event)
                updateCalendarUI()
            } else {
                Log.e(TAG, "One or more extras are null")
            }
        }
    }


    private fun updateCalendarUI() {
        calendarView.setOnDateChangedListener { widget, date, selected ->
            val selectedDate =
                SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(date.date)
            val eventList = eventsMap[selectedDate]
            if (!eventList.isNullOrEmpty()) {
                val dialogView = layoutInflater.inflate(R.layout.dialog, null)
                val dialogTitle = dialogView.findViewById<TextView>(R.id.dialog_title)
                val eventDetails = dialogView.findViewById<TextView>(R.id.event_details)
                val timeTextView = dialogView.findViewById<TextView>(R.id.timee)
                val datecalendar = dialogView.findViewById<TextView>(R.id.datetext)
                val deleteButton = dialogView.findViewById<Button>(R.id.delete_button)
// Customize dialog title and event details if needed
                dialogTitle.text = eventList.joinToString("\n") { "Event Details: ${it.title}" }
                eventDetails.text = eventList.joinToString("\n") { "Note: ${it.note}" }
                val date = eventList.firstOrNull()?.date ?: ""
                datecalendar.text = date

                // Set time
                val time = eventList.firstOrNull()?.time ?: ""
                timeTextView.text = time

                // Create AlertDialog with transparent background
                val dialogBuilder = AlertDialog.Builder(this@calendar)
                dialogBuilder.setView(dialogView)

                val dialog = dialogBuilder.create()

                // Set the background of the dialog's window to be transparent
                dialog.window?.setBackgroundDrawableResource(android.R.color.transparent)

                dialog.show()


                deleteButton.setOnClickListener {
                    dialog.dismiss()
                    val confirmDialog = AlertDialog.Builder(this@calendar)
                        .setTitle("Confirmation")
                        .setMessage("Are you sure you want to delete this event?")
                        .setPositiveButton("Yes") { _, _ ->
                            eventsMap.remove(selectedDate)


                            val currentUserID = FirebaseAuth.getInstance().currentUser?.uid
                            if (currentUserID != null) {
                                val database = FirebaseDatabase.getInstance()
                                val userEventsRef = database.getReference("Users").child(currentUserID).child("events")
                                Log.d(TAG, "Firebase Database reference: $userEventsRef")
                                userEventsRef.child(selectedDate).removeValue()
                                    .addOnSuccessListener {
                                        saveEventsToSharedPreferences()
                                        calendarView.invalidateDecorators()
                                        Toast.makeText(this@calendar, "Event deleted", Toast.LENGTH_SHORT).show()
                                        Log.d(TAG, "Event deleted from Firebase Database")
                                    }
                                    .addOnFailureListener { e ->
                                        Log.e(TAG, "Error deleting event: ${e.message}")
                                        Toast.makeText(this@calendar, "Failed to delete event", Toast.LENGTH_SHORT).show()
                                    }
                            } else {
                                Toast.makeText(this@calendar, "User not authenticated", Toast.LENGTH_SHORT).show()
                            }
                        }
                        .setNegativeButton("No") { _, _ -> }
                        .create()
                    confirmDialog.show()
                }
            }
        }
    }

    private fun updateEventsMap(selectedDate: String, event: Event) {
        if (eventsMap.containsKey(selectedDate)) {
            eventsMap[selectedDate]?.add(event)
        } else {
            eventsMap[selectedDate] = mutableListOf(event)
        }

        saveEventsToSharedPreferences()
        calendarView.invalidateDecorators()
    }


    private fun saveEventsToSharedPreferences() {
        val sharedPreferences = getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        val gson = Gson()
        val json = gson.toJson(eventsMap)
        editor.putString("eventJson", json)
        editor.apply()
    }

    companion object {
        private const val ADD_EVENT_REQUEST_CODE = 100
    }

    @Suppress("DEPRECATION")
    override fun onBackPressed() {
        // Start a new activity or finish the current activity
        val intent = Intent(this, homepageold::class.java)
        startActivity(intent)

        // Finish the current activity to trigger the animation
        finish()

        // Define the animation to use after calling finish()
        overridePendingTransition(R.anim.dissolve_in, R.anim.dissolve_out)
    }
}

