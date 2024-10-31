package com.example.buddy


import Event
import android.annotation.SuppressLint
import android.app.Activity
import android.app.AlarmManager
import android.app.DatePickerDialog
import android.app.PendingIntent
import android.app.TimePickerDialog
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.Spinner
import android.widget.Switch
import android.widget.TextView
import android.widget.Toast
import androidx.activity.ComponentActivity
import java.util.Calendar
import com.example.buddy.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import java.text.SimpleDateFormat
import java.util.Locale


class addevent_calendar : ComponentActivity() {


    private lateinit var switchButton: Switch
    private lateinit var addDayTextView: TextView
    private lateinit var timeeTextView: TextView
    private lateinit var timeTextView: TextView
    private lateinit var dateTextView: TextView
    private lateinit var Line: View
    private var selectedDate: String = ""
    private var selectedTime: String = ""
    private var title: String = ""
    private var note: String = ""
    private var dropdown: String = ""
    private lateinit var eventsRef: DatabaseReference

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.addevent_calendar)
        switchButton = findViewById(R.id.switch1)
        addDayTextView = findViewById(R.id.add_day)
        timeTextView = findViewById(R.id.time)
        timeeTextView = findViewById(R.id.timee)
        dateTextView = findViewById(R.id.ddmmyy)
        Line = findViewById(R.id.line2)


        switchButton.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                val layoutParams = addDayTextView.layoutParams
                layoutParams.height = resources.getDimensionPixelSize(R.dimen.new_height)
                addDayTextView.layoutParams = layoutParams
                timeTextView.visibility = View.INVISIBLE
                timeeTextView.visibility = View.INVISIBLE
                Line.visibility = View.GONE

                selectedTime = "00:00"
                timeeTextView.text = selectedTime // Update the TextView to display the selected time
            } else {
                val layoutParams = addDayTextView.layoutParams
                layoutParams.height = resources.getDimensionPixelSize(R.dimen.original_height)
                addDayTextView.layoutParams = layoutParams
                timeTextView.visibility = View.VISIBLE
                timeeTextView.visibility = View.VISIBLE
                Line.visibility = View.VISIBLE
            }
        }


        timeeTextView.setOnClickListener {
            showTimePickerDialog()
        }

        dateTextView.setOnClickListener {
            showDatePickerDialog()
        }

        val database = FirebaseDatabase.getInstance()
        eventsRef = database.getReference("events")

        val saveButton: Button = findViewById(R.id.button_save)
        saveButton.setOnClickListener {

            saveEventData()
        }


        val back : Button = findViewById(R.id.backbtn)
        back.setOnClickListener {
            val intent = Intent(this, calendar::class.java)
            startActivity(intent)
        }

        val spinner: Spinner = findViewById(R.id.notetext)
        val options = arrayOf("Once", "Everyday", "Every week", "Every Month")
        val adapter = ArrayAdapter(this, R.layout.spinner_item, options)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinner.adapter = adapter

        spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                dropdown = options[position]
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                // Do something when nothing is selected
            }
        }
    }

    private fun showTimePickerDialog() {
        val calendar = Calendar.getInstance()
        val hour = calendar.get(Calendar.HOUR_OF_DAY)
        val minute = calendar.get(Calendar.MINUTE)

        val timePickerDialog = TimePickerDialog(
            this,
            { _, selectedHour, selectedMinute ->
                selectedTime = String.format("%02d:%02d", selectedHour, selectedMinute)
                timeeTextView.text = selectedTime
            },
            hour,
            minute,
            true
        )
        timePickerDialog.show()
    }

    private fun showDatePickerDialog() {
        val calendar = Calendar.getInstance()
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH)
        val dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH)

        val datePickerDialog = DatePickerDialog(
            this,
            { _, selectedYear, selectedMonth, selectedDayOfMonth ->
                selectedDate = String.format(
                    "%02d/%02d/%02d",
                    selectedDayOfMonth,
                    selectedMonth + 1,
                    selectedYear
                )
                dateTextView.text = selectedDate
            },
            year,
            month,
            dayOfMonth
        )
        datePickerDialog.show()
    }


        private fun saveEventData() {
            title = findViewById<EditText>(R.id.add_title).text.toString()
            note = findViewById<EditText>(R.id.add_note).text.toString()

            // Format the selected date
            val formattedDate = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(
                SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).parse(selectedDate.trim())
            )



            // Set the result to be sent back to the calendar activity
            val returnIntent = Intent().apply {
                putExtra("date", formattedDate)
                putExtra("time", selectedTime)
                putExtra("title", title)
                putExtra("note", note)
            }
            setResult(Activity.RESULT_OK, returnIntent)
            finish()


            val currentUserID = FirebaseAuth.getInstance().currentUser?.uid


            // Check if the current user is authenticated
            if (currentUserID != null) {
                // Construct the reference path to where you want to save the event data
                val userEventsRef =
                    FirebaseDatabase.getInstance().getReference("Users").child(currentUserID)
                        .child("events")

                // Create event object
                val event = Event(formattedDate, selectedTime, title, note)

                // Push event to Firebase under the current user's profile
                val eventRef = userEventsRef.push()
                eventRef.setValue(event)
                    .addOnSuccessListener {
                        Toast.makeText(this, "Event Saved", Toast.LENGTH_SHORT).show()
                        finish()
                    }
                    .addOnFailureListener { e ->
                        Toast.makeText(
                            this,
                            "Failed to save event: ${e.message}",
                            Toast.LENGTH_SHORT
                        ).show()
                    }

                // Schedule notification for the event
                scheduleNotification(event)
            } else {
                // User is not authenticated, handle accordingly (e.g., show error message)
                Toast.makeText(this, "User not authenticated", Toast.LENGTH_SHORT).show()
            }
            finish()


        when (dropdown) {
            "Once" -> {
                Toast.makeText(this, "Event Saved", Toast.LENGTH_SHORT).show()

                returnIntent.putExtra("date", formattedDate)
                returnIntent.putExtra("time", selectedTime)
                returnIntent.putExtra("title", title)
                returnIntent.putExtra("note", note)
                scheduleNotification(Event(formattedDate, selectedTime, title, note))
            }

            "Everyday" -> {
                Toast.makeText(this, "Event Saved Everyday!", Toast.LENGTH_SHORT).show()

                val calendar = Calendar.getInstance()
                val endDate = calendar.clone() as Calendar
                endDate.add(Calendar.MONTH, 1)

                while (calendar.before(endDate)) {
                    val dailyIntent = Intent()
                    dailyIntent.putExtra("date", formattedDate)
                    dailyIntent.putExtra("time", selectedTime)
                    dailyIntent.putExtra("title", title)
                    dailyIntent.putExtra("note", note)
                    setResult(Activity.RESULT_OK, dailyIntent)
                    calendar.add(Calendar.DAY_OF_MONTH, 1)
                    scheduleNotification(Event(formattedDate, selectedTime, title, note))
                }
                finish()
                return
            }


            "Every week" -> {
                Toast.makeText(this, "Event Saved Every Week!", Toast.LENGTH_SHORT).show()

                val calendar = Calendar.getInstance()
                val endDate = calendar.clone() as Calendar
                endDate.add(Calendar.MONTH, 1)

                while (calendar.before(endDate)) {
                    val weeklyintent = Intent()
                    weeklyintent.putExtra("date", formattedDate)
                    weeklyintent.putExtra("time", selectedTime)
                    weeklyintent.putExtra("title", title)
                    weeklyintent.putExtra("note", note)
                    setResult(Activity.RESULT_OK, weeklyintent)
                    calendar.add(Calendar.DAY_OF_MONTH, 7)
                    scheduleNotification(Event(formattedDate, selectedTime, title, note))

                }
                finish()
                return
            }

            "Every Month" -> {
                Toast.makeText(this, "Event Saved Every Month!", Toast.LENGTH_SHORT).show()

                val calendar = Calendar.getInstance()
                val endDate = calendar.clone() as Calendar
                endDate.add(Calendar.YEAR, 1)

                while (calendar.before(endDate)) {
                    val monthlyIntent = Intent()
                    monthlyIntent.putExtra("date", formattedDate)
                    monthlyIntent.putExtra("time", selectedTime)
                    monthlyIntent.putExtra("title", title)
                    monthlyIntent.putExtra("note", note)
                    setResult(Activity.RESULT_OK, monthlyIntent)
                    calendar.add(Calendar.MONTH, 30)
                    scheduleNotification(Event(formattedDate, selectedTime, title, note))
                }
                finish()
                return
            }

            else -> {
               
                Toast.makeText(this, "Unsupported repetition option", Toast.LENGTH_SHORT).show()

            }
        }

        setResult(Activity.RESULT_OK, returnIntent)
        finish()


    }


    private fun scheduleNotification(event: Event) {
        val alarmManager = getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val intent = Intent(this, AlarmReceiver::class.java).apply {
            putExtra("title", event.title)
            putExtra("note", event.note)
        }
        val pendingIntent = PendingIntent.getBroadcast(
            this,
            0,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE // Add FLAG_IMMUTABLE flag
        )

        // Parse event date and time
        val dateFormat = SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.getDefault())
        val eventDateTime = dateFormat.parse("${event.date} ${event.time}")
        val calendar = Calendar.getInstance().apply {
            timeInMillis = eventDateTime.time
        }

        // Set alarm
        alarmManager.set(AlarmManager.RTC_WAKEUP, calendar.timeInMillis, pendingIntent)

        Log.d("Alarm", "Scheduled alarm for: ${event.date} ${event.time}")
    }
}