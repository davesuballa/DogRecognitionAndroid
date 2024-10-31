package com.example.buddy

import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.VideoView
import androidx.fragment.app.Fragment
import android.widget.Toast
import okhttp3.ResponseBody
import java.io.File
import android.content.Intent
import org.json.JSONException
import org.json.JSONObject


class LoadingFragment : Fragment() {

    private var imagePath: String? = null
    private var isEmotionMode: Boolean = false
    private lateinit var dogApiClient: DogApiClient

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            imagePath = it.getString(ARG_IMAGE_PATH)
            isEmotionMode = it.getBoolean(ARG_IS_EMOTION_MODE)
        }
        dogApiClient = DogApiClient(isEmotionMode)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_loading, container, false)
        val videoView: VideoView = view.findViewById(R.id.videoView)
        val videoPath = "android.resource://" + requireActivity().packageName + "/" + R.raw.loading_animation
        videoView.setVideoURI(Uri.parse(videoPath))
        videoView.setOnPreparedListener { mp -> mp.isLooping = true }
        videoView.start()


        videoView.layoutParams.width = resources.displayMetrics.widthPixels
        videoView.layoutParams.height = resources.displayMetrics.heightPixels


        val imageFile = File(imagePath)
        dogApiClient.uploadImage(imageFile, object : DogApiClient.UploadCallback {
            override fun onSuccess(response: String) {
                try {
                   // Toast.makeText(activity, "Upload successful. Response: $response", Toast.LENGTH_SHORT).show()
                    val jsonResponse = JSONObject(response)
                    //Check if the response has an "error" key
                    if (jsonResponse.has("error")) {
                        val errorMessage = jsonResponse.getString("error")
                        Toast.makeText(activity, errorMessage, Toast.LENGTH_SHORT).show()
                        activity?.supportFragmentManager?.popBackStack()
                    } else {
                        moveToDisplayActivity(response, imagePath)
                    }
                } catch (e: JSONException) {
                  //  Toast.makeText(activity, "Error passing response", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onError(error: String) {
               // Toast.makeText(activity, "Upload failed: $error", Toast.LENGTH_SHORT).show()
                activity?.supportFragmentManager?.popBackStack()
            }
        })

        return view
    }

    private fun moveToDisplayActivity(response: String, imagePath: String?) {
        val intent = Intent(activity, DisplayActivity::class.java).apply {
            putExtra("response", response)
            putExtra("imagePath", imagePath)
            putExtra("isEmotionMode", isEmotionMode)
        }
        startActivity(intent)
    }


    companion object {
        private const val ARG_IMAGE_PATH = "imagePath"
        private const val ARG_IS_EMOTION_MODE = "isEmotionMode"

        @JvmStatic
        fun newInstance(imagePath: String, isEmotionMode: Boolean) =
            LoadingFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_IMAGE_PATH, imagePath)
                    putBoolean(ARG_IS_EMOTION_MODE, isEmotionMode)
                }
            }
    }
}