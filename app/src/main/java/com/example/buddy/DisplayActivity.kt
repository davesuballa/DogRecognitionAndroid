    package com.example.buddy

    import android.app.ActivityOptions
    import android.app.AlertDialog
    import android.app.Dialog
    import android.content.ClipData
    import android.content.ClipboardManager
    import android.content.Context
    import android.content.Intent
    import android.content.res.ColorStateList
    import android.graphics.BitmapFactory
    import android.graphics.Color
    import android.graphics.drawable.GradientDrawable
    import android.graphics.drawable.RippleDrawable
    import android.os.Bundle
    import android.os.Handler
    import android.os.Looper
    import android.util.Log
    import android.view.LayoutInflater
    import android.view.View
    import android.widget.Button
    import android.widget.ImageView
    import android.widget.LinearLayout
    import android.widget.ListView
    import android.widget.TextView
    import androidx.appcompat.app.AppCompatActivity
    import androidx.cardview.widget.CardView
    import androidx.core.app.ActivityOptionsCompat
    import com.budiyev.android.circularprogressbar.CircularProgressBar
    import com.example.buddy.R
    import de.hdodenhof.circleimageview.CircleImageView
    import org.json.JSONArray
    import org.json.JSONObject
    import java.io.IOException

    class DisplayActivity : AppCompatActivity() {
        private lateinit var imageview: CircleImageView
        private lateinit var bgCard: CardView
        private lateinit var progressBar: CircularProgressBar
        private var progress = 0
        private val handler = Handler(Looper.getMainLooper())

        override fun onCreate(savedInstanceState: Bundle?) {
            super.onCreate(savedInstanceState)
            setContentView(R.layout.activity_display)

            imageview = findViewById(R.id.circleimageview1)
            bgCard = findViewById(R.id.bgCard)
            progressBar = findViewById(R.id.progress_bar)


            val back = findViewById<ImageView>(R.id.imageview2)
            back.setOnClickListener {
                val intent = Intent(this, camera::class.java)
                val options = ActivityOptions.makeCustomAnimation(
                    this,
                    R.anim.slide_in_left, // Slide in from left
                    R.anim.slide_out_right // Slide out to right
                )
                startActivity(intent, options.toBundle())
                finish()

            }
            // Referencing views from the inflated layout

            val textview2 = findViewById<TextView>(R.id.textview2)
            val textview4 = findViewById<TextView>(R.id.textview4)


            val listB1 = findViewById<MyListView>(R.id.listB1)
            val listB2 = findViewById<MyListView>(R.id.listB2)
            val listB3 = findViewById<MyListView>(R.id.listB3)
            val listB4 = findViewById<MyListView>(R.id.listB4)


            fun showLayout(layoutResId: Int) {
                // Inflate the layout
                val layout = LayoutInflater.from(this).inflate(layoutResId, null)

                // Find the container layout in display.xml
                val container = findViewById<LinearLayout>(R.id.linear3)

                // Clear existing views in the container
                container.removeAllViews()

                // Add the inflated layout to the container
                container.addView(layout)
            }


            val imagePath = intent.getStringExtra("imagePath")
            val response = intent.getStringExtra("response")
            val isEmotionMode = intent.getBooleanExtra("isEmotionMode", false)

            // Use safe call operator ?. to pass a non-null value to getTopLabels() function
            val topLabels: List<Pair<String, Double>> =
                response?.let { getTopLabels(it, isEmotionMode) } ?: emptyList()
            copyTopLabelsToClipboard(this, topLabels)
            val sentence1 = createSentenceFromLabels(topLabels)
            val sentence2 = createSentenceWithPercentages(topLabels)

            // Updating the UI with retrieved datas
            var processedText = processDetection(response, isEmotionMode) ?: "Detecting..."
            textview2.text = sentence1
            textview4.text = sentence2
            val labels = topLabels.map { it.first }
            if (isEmotionMode) {
                // Hide all ListViews
                listB1.setVisibility(View.GONE);
                listB2.setVisibility(View.GONE);
                listB3.setVisibility(View.GONE);
                listB4.setVisibility(View.GONE);
                displayEmotionLabelsReasonsAndExamples(this, labels)
            } else {
                displayLabelsReasonsAndExamples(this, labels)
            }


            // Check if imagePath is not null
            if (!imagePath.isNullOrEmpty()) {
                // Load the image from the file path
                val bitmap = BitmapFactory.decodeFile(imagePath)

                // Set the bitmap to the ImageView
                imageview.setImageBitmap(bitmap)
            }


        }

        fun getTopLabels(jsonResponse: String, isEmotionMode: Boolean): List<Pair<String, Double>> {
            // Parse the JSON response
            val jsonObject = JSONObject(jsonResponse)
        
            // Get the analysis object from the JSON
            val analysisObject = jsonObject.getJSONObject("analysis")
        
            // Initialize a list to store the labels and their percentages
            val labelList = mutableListOf<Pair<String, Double>>()
        
            // Calculate percentages for each label and add them to the list
            analysisObject.keys().forEach { label ->
                val percentage = analysisObject.getDouble(label) * 100
                if (percentage >= 1) {
                    // Modify label if it has the "Sad" key and is in emotion mode
                    val modifiedLabel = if (isEmotionMode && label.equals("Sad", ignoreCase = true)) {
                        "Sad/Normal"
                    } else {
                        label
                    }
                    labelList.add(modifiedLabel to percentage)
                }
            }
        
            // Sort the list based on percentages (highest to lowest)
            labelList.sortByDescending { it.second }
        
            // Take the top 3 labels (or fewer if there are fewer than 3, or just 1 if isEmotionMode is true)
            return if (isEmotionMode) {
                labelList.take(1)
            } else {
                labelList.take(3)
            }
        }        

        fun processDetection(response: String?, isEmotionMode: Boolean): String {
            // Parse the JSON string into a JSONObject
            val jsonObject = JSONObject(response)
            var mode = ""

            // Retrieve values from the JSONObject
            val detectionValue: String = if (isEmotionMode) {
                mode = "Emotion"
                val emotionValue = jsonObject.getString("emotion")
                if (emotionValue.equals("Sad", ignoreCase = true)) {
                    "Sad/Normal"
                } else {
                    emotionValue
                }
            } else {
                mode = "Movement"
                jsonObject.getString("posture")
            }



            startProgressBarAnimation()
            return detectionValue + " " + mode

        }

        private fun startProgressBarAnimation() {
            // Start a runnable to update the progress
            handler.post(updateProgress)
        }

        private val updateProgress = object : Runnable {
            override fun run() {
                // Update progress
                progress += 5
                progressBar.progress = progress.toFloat()

                // Repeat the process every 100 milliseconds until progress reaches 100
                if (progress < 100) {
                    handler.postDelayed(this, 100)
                }
            }
        }

        override fun onDestroy() {
            super.onDestroy()
            // Remove the runnable callbacks to prevent memory leaks
            handler.removeCallbacks(updateProgress)
        }


        fun copyTopLabelsToClipboard(context: Context, topLabels: List<Pair<String, Double>>) {
            // Convert the list of top labels to a string
            val labelsString = StringBuilder()
            topLabels.forEachIndexed { index, (label) ->
                labelsString.append("${index + 1}. $label:")
            }

            // Get the ClipboardManager system service
            val clipboardManager =
                context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager

            // Create a ClipData object to store the string
            val clipData = ClipData.newPlainText("Top Labels", labelsString.toString())

            // Set the ClipData to the clipboard
            clipboardManager.setPrimaryClip(clipData)
        }

        fun createSentenceFromLabels(topLabels: List<Pair<String, Double>>): String {
            // Check if the list is empty
            if (topLabels.isEmpty()) {
                return "No movement detected."
            }

            // Initialize a string builder to construct the sentence
            val sentenceBuilder = StringBuilder("The dog you scanned is possibly ")

            // Iterate over the top labels and append them to the sentence
            for ((index, label) in topLabels.withIndex()) {
                // Append the label
                sentenceBuilder.append(label.first)

                // Add a comma if it's not the last label
                if (index < topLabels.size - 1) {
                    // Add "and" if it's the second last label
                    if (index == topLabels.size - 2) {
                        sentenceBuilder.append(" and ")
                    } else {
                        sentenceBuilder.append(", ")
                    }
                }
            }

            // Add a period at the end of the sentence
            sentenceBuilder.append(".")

            return sentenceBuilder.toString()
        }

        fun createSentenceWithPercentages(topLabels: List<Pair<String, Double>>): String {
            // Check if the list is empty
            if (topLabels.isEmpty()) {
                return "No labels detected."
            }

            // Initialize a string builder to construct the sentence
            val sentenceBuilder = StringBuilder("It looks like you scanned ")

            // Iterate over the top labels and append them to the sentence with percentages
            for ((index, label) in topLabels.withIndex()) {
                // Append the label
                sentenceBuilder.append(label.first)

                // Append the percentage
                sentenceBuilder.append(" ${label.second.toInt()}%")

                // Add a comma if it's not the last label
                if (index < topLabels.size - 1) {
                    // Add "and" if it's the second last label
                    if (index == topLabels.size - 2) {
                        sentenceBuilder.append(", and ")
                    } else {
                        sentenceBuilder.append(", ")
                    }
                }
            }

            // Add a period at the end of the sentence
            sentenceBuilder.append(".")

            return sentenceBuilder.toString()
        }


        fun displayLabelsReasonsAndExamples(context: Context, labels: List<String>) {
            // Load JSON string from assets file
            val jsonString = loadJsonStringFromAssets(context, "dog-movement.json")
            if (jsonString.isNullOrEmpty()) {
                Log.e("Error", "JSON string is null or empty")
                return
            }

            // Convert JSON string to a list of hashmaps
            val dogMovementList = mutableListOf<Map<String, Any>>()
            try {
                val jsonArray = JSONArray(jsonString)
                for (i in 0 until jsonArray.length()) {
                    val jsonObject = jsonArray.getJSONObject(i)
                    val movementMap = mutableMapOf<String, Any>()
                    val movement = jsonObject.optString("Movement", "")
                    val reason = jsonObject.optString("Reason", "")
                    val examplesArray = jsonObject.optJSONArray("Examples")
                    val examplesList = mutableListOf<String>()
                    if (examplesArray != null) {
                        for (j in 0 until examplesArray.length()) {
                            examplesList.add(examplesArray.getString(j))
                        }
                    }
                    movementMap["Movement"] = movement
                    movementMap["Reason"] = reason
                    movementMap["Examples"] = examplesList
                    dogMovementList.add(movementMap)
                }
            } catch (e: Exception) {
                Log.e("Error", "Error parsing JSON: ${e.message}")
            }

            // Maximum number of layouts to display labels
            val maxLayouts = 4

            // List of parent layout ids
            val layoutIds = listOf(
                R.id.resultlin1,
                R.id.resultlin2,
                R.id.resultlin3,
                R.id.resultlin4
            )

            // List of TextView ids for labels inside each parent layout
            val labelTextViewIds = listOf(
                listOf(R.id.textlabel1, R.id.textinfo1),
                listOf(R.id.textlabel2, R.id.textinfo2),
                listOf(R.id.textlabel3, R.id.textinfo3),
                listOf(R.id.textlabel4, R.id.textinfo4)
            )

            // List of ListView ids for examples inside each parent layout
            val listViewIds = listOf(
                R.id.listB1,
                R.id.listB2,
                R.id.listB3,
                R.id.listB4
            )

            // Iterate over the parent layouts and update their visibility, labels, and examples
            for (i in 0 until maxLayouts) {
                val layout = findViewById<View>(layoutIds[i])
                if (i < labels.size) {
                    // If there are labels to display, set the visibility of the parent layout to visible
                    layout.visibility = View.VISIBLE

                    // Get the corresponding TextViews for label and reason inside the layout
                    val (labelTextViewId, reasonTextViewId) = labelTextViewIds[i]
                    val labelTextView = layout.findViewById<TextView>(labelTextViewId)
                    val reasonTextView = layout.findViewById<TextView>(reasonTextViewId)

                    // Convert label to uppercase
                    val label = labels[i].toUpperCase()

                    // Set the label text
                    labelTextView.text = labels[i]

                    // Find the movement map for the current label
                    val movementMap = dogMovementList.firstOrNull { it["Movement"] == label }
                    if (movementMap != null) {
                        // Get the reason for the movement
                        val reason = movementMap["Reason"] as? String ?: "No reason provided"

                        // Update the reason TextView with the reason
                        reasonTextView.text = reason

                        // Get the example list
                        val exampleList = movementMap["Examples"] as? List<String> ?: emptyList()

                        // Get the corresponding ListView inside the layout
                        val listView = layout.findViewById<ListView>(listViewIds[i])


                        // Create a list of maps with heading and explanation
                        val headingExplanationList = createHeadingExplanationList(exampleList)

                        // Populate the ListView with the heading and explanation
                        val adapter = ExampleListAdapter(context, headingExplanationList)
                        listView.adapter = adapter
                    } else {
                        // If the label is not found in the movement map, display a default message
                        reasonTextView.text = "No reason available"
                    }
                } else {
                    // If there are no labels to display, set the visibility of the parent layout to gone
                    layout.visibility = View.GONE
                }
            }
        }

        fun displayEmotionLabelsReasonsAndExamples(context: Context, labels: List<String>) {
            // Load JSON string from assets file
            val jsonString = loadJsonStringFromAssets(context, "dog-emotion.json")
            if (jsonString.isNullOrEmpty()) {
                Log.e("Error", "JSON string is null or empty")
                return
            }

            // Convert JSON string to a list of hashmaps
            val dogEmotionList = mutableListOf<Map<String, Any>>()
            try {
                val jsonArray = JSONArray(jsonString)
                for (i in 0 until jsonArray.length()) {
                    val jsonObject = jsonArray.getJSONObject(i)
                    val emotionMap = mutableMapOf<String, Any>()
                    val movement = jsonObject.optString("Emotion", "")
                    val reason = jsonObject.optString("Reason", "")
                    val recommendation = jsonObject.optString("Recommendation")
                    emotionMap["Emotion"] = movement
                    emotionMap["Reason"] = reason
                    emotionMap["Recommendation"] = recommendation
                    dogEmotionList.add(emotionMap)
                }
            } catch (e: Exception) {
                Log.e("Error", "Error parsing JSON: ${e.message}")
            }

            // Maximum number of layouts to display labels
            val maxLayouts = 4

            // List of parent layout ids
            val layoutIds = listOf(
                R.id.resultlin1,
                R.id.resultlin2,
                R.id.resultlin3,
                R.id.resultlin4
            )

            // List of TextView ids for labels inside each parent layout
            val labelTextViewIds = listOf(
                listOf(R.id.textlabel1, R.id.textinfo1),
                listOf(R.id.textlabel2, R.id.textinfo2),
                listOf(R.id.textlabel3, R.id.textinfo3),
                listOf(R.id.textlabel4, R.id.textinfo4)
            )

            // Iterate over the parent layouts and update their visibility, labels, and examples
            for (i in 0 until maxLayouts) {
                val layout = findViewById<View>(layoutIds[i])
                if (i < labels.size) {
                    // If there are labels to display, set the visibility of the parent layout to visible
                    layout.visibility = View.VISIBLE

                    // Get the corresponding TextViews for label and reason inside the layout
                    val (labelTextViewId, reasonTextViewId) = labelTextViewIds[i]
                    val labelTextView = layout.findViewById<TextView>(labelTextViewId)
                    val reasonTextView = layout.findViewById<TextView>(reasonTextViewId)

                    // Convert label to uppercase
                    val label = labels[i].toUpperCase()

                    // Set the label text
                    labelTextView.text = label

                    // Find the movement map for the current label
                    val emotionMap = dogEmotionList.firstOrNull { it["Emotion"] == label }
                    if (emotionMap != null) {
                        // Get the reason for the movement
                        val reason = emotionMap["Reason"] as? String ?: "No reason provided"
                        val recommendation =
                            emotionMap["Recommendation"] as? String ?: "No recommendation provided"
                        val emotionInfoBuilder = StringBuilder()


                        emotionInfoBuilder.append("Reason:\n")
                            .append(reason)
                            .append("\n\nRecommendation:\n")
                            .append(recommendation)
                            .append("\n\n")

                        // Update the reason TextView with the reason
                        reasonTextView.text = emotionInfoBuilder.toString()

                    } else {
                        // If the label is not found in the movement map, display a default message
                        reasonTextView.text = "No reason available"
                    }
                } else {
                    // If there are no labels to display, set the visibility of the parent layout to gone
                    layout.visibility = View.GONE
                }
            }
        }


        fun createHeadingExplanationList(examples: List<String>): List<Map<String, String>> {
            val headingExplanationList = mutableListOf<Map<String, String>>()

            for (example in examples) {
                val parts = example.split(" - ", limit = 2)
                if (parts.size == 2) {
                    val heading = parts[0]
                    val explanation = parts[1]
                    val map = mapOf("Heading" to heading, "Explanation" to explanation)
                    headingExplanationList.add(map)
                }
            }

            return headingExplanationList
        }

        // Function to load JSON string from assets file
        fun loadJsonStringFromAssets(context: Context, fileName: String): String? {
            return try {
                context.assets.open(fileName).bufferedReader().use { it.readText() }
            } catch (ioException: IOException) {
                Log.e("Error", "Error reading JSON file: ${ioException.message}")
                null
            }
        }


        fun convertListToStringAndCopyToClipboard(
            context: Context,
            dogMovementList: List<Map<String, Any>>
        ) {
            // Convert the dogMovementList to a string representation
            val jsonString = JSONObject().apply {
                put("DogMovementList", dogMovementList)
            }.toString()

            // Get the clipboard manager
            val clipboardManager =
                context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager

            // Create a ClipData object to hold the string
            val clipData = ClipData.newPlainText("DogMovementList", jsonString)

            // Set the ClipData on the clipboard
            clipboardManager.setPrimaryClip(clipData)
        }


        fun rippleRoundStroke(
            _view: View,
            _focus: String,
            _pressed: String,
            _round: Double,
            _stroke: Double,
            _strokeclr: String
        ) {
            val GG = GradientDrawable().apply {
                setColor(Color.parseColor(_focus))
                cornerRadius = _round.toFloat()
                setStroke(_stroke.toInt(), Color.parseColor("#" + _strokeclr.replace("#", "")))
            }
            val RE = RippleDrawable(
                ColorStateList.valueOf(Color.parseColor(_pressed)),
                GG,
                null
            )
            _view.background = RE
        }

        fun advancedCorners(
            _view: View,
            _color: String,
            _n1: Double,
            _n2: Double,
            _n3: Double,
            _n4: Double
        ) {
            val gd = GradientDrawable().apply {
                setColor(Color.parseColor(_color))
                cornerRadii = floatArrayOf(
                    _n1.toFloat(),
                    _n1.toFloat(),
                    _n2.toFloat(),
                    _n2.toFloat(),
                    _n4.toFloat(),
                    _n4.toFloat(),
                    _n3.toFloat(),
                    _n3.toFloat()
                )
            }
            _view.background = gd
        }


        @Suppress("DEPRECATION")
        override fun onBackPressed() {
            // Create a custom function to show a confirmation dialog for exiting the application
            showExitConfirmationDialog()
        }

        private fun showExitConfirmationDialog() {
            val alertDialogBuilder = AlertDialog.Builder(this)
            alertDialogBuilder.setTitle("Dog Recognition Result")
            alertDialogBuilder.setMessage("Do you want to leave the Dog Recognition Result? You won't be able to return to this result but you can still try another capture. \n\nTap Confirm to return to Homepage.")
            alertDialogBuilder.setPositiveButton("Confirm") { dialog, _ ->
                val intent = Intent(this, homepageold::class.java)
                val options = ActivityOptionsCompat.makeCustomAnimation(
                    this,
                    R.anim.slide_in_right, // Animation for the new activity
                    R.anim.slide_out_left // No animation for the current activity
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
