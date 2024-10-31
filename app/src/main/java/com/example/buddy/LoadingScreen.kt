package com.example.buddy

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.VideoView
import androidx.fragment.app.Fragment
import kotlinx.coroutines.*
import kotlin.coroutines.resume

class LoadingScreen : Fragment() {

    private lateinit var videoView: VideoView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_loadingscreen, container, false)
        videoView = view.findViewById(R.id.videoView)

        playLoadingAnimation()

        // Simulate loading data from Firebase
        loadDataFromFirebase()

        return view
    }

    private fun playLoadingAnimation() {
        val videoPath = "android.resource://" + requireActivity().packageName + "/" + R.raw.loading_animation
        videoView.setVideoURI(Uri.parse(videoPath))
        videoView.setOnPreparedListener { mp -> mp.isLooping = true }
        videoView.start()

        // Adjust video size to fill the screen
        videoView.layoutParams.width = resources.displayMetrics.widthPixels
        videoView.layoutParams.height = resources.displayMetrics.heightPixels
    }

    private fun loadDataFromFirebase() {
        // Simulate loading data from Firebase with a delay
        CoroutineScope(Dispatchers.IO).launch {
            // Simulate a network delay (replace this with your actual Firebase data loading logic)
            delay(2000) // Simulate a 3-second delay

            // Proceed to the homepage after data is loaded
            withContext(Dispatchers.Main) {
                moveToHomepage()
            }
        }
    }

    private fun moveToHomepage() {
        val intent = Intent(activity, homepageold::class.java)
        startActivity(intent)
        activity?.finish() // Optional: Finish the current activity
    }

    companion object {
        @JvmStatic
        fun newInstance() = LoadingScreen()
    }
}
