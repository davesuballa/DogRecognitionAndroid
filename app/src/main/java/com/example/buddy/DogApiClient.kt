package com.example.buddy

import android.os.Handler
import android.os.Looper
import okhttp3.*
import retrofit2.Call
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import android.util.Log
import retrofit2.converter.gson.GsonConverterFactory
import java.io.File
import java.io.IOException
import java.util.concurrent.Executors
import java.util.concurrent.TimeUnit

class DogApiClient(private val isEmotionMode: Boolean) {

    private val emotionBaseUrl = "https://dogprediction.app/"
    private val postureBaseUrl = "https://dogprediction.app/"
    private val retrofit: Retrofit
    private val apiService: ApiService
    private val mainHandler = Handler(Looper.getMainLooper())
    private val backgroundExecutor = Executors.newSingleThreadExecutor()

    init {
        val baseUrl = if (isEmotionMode) emotionBaseUrl else postureBaseUrl
        retrofit = Retrofit.Builder()
            .baseUrl(baseUrl)
            .addConverterFactory(GsonConverterFactory.create())
            .client(createOkHttpClient())
            .callbackExecutor(backgroundExecutor)
            .build()
        apiService = retrofit.create(ApiService::class.java)
    }

    private fun createOkHttpClient(): OkHttpClient {
        return OkHttpClient.Builder()
            .connectTimeout(30, TimeUnit.SECONDS) // Increase timeout duration
            .readTimeout(30, TimeUnit.SECONDS) // Increase timeout duration
            .writeTimeout(30, TimeUnit.SECONDS) // Increase timeout duration
            .retryOnConnectionFailure(true) // Enable retry on failure
            .build()
    }

    fun uploadImage(photoFile: File, callback: UploadCallback) {
        val requestFile = photoFile.asRequestBody("image/jpeg".toMediaTypeOrNull())
        val body = MultipartBody.Part.createFormData("file", photoFile.name, requestFile)

        val call = if (isEmotionMode) apiService.uploadImageToEmotion(body) else apiService.uploadImageToPosture(body)

        call.enqueue(object : Callback<ResponseBody> {
            override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                if (response.isSuccessful) {
                    val responseBody = response.body()?.string() ?: ""
                    mainHandler.post { callback.onSuccess(responseBody) }
                } else {
                    val errorMessage = "Upload failed: ${response.message()}"
                    mainHandler.post { callback.onError(errorMessage) }
                }
            }

            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                val errorMessage = "Upload failed: ${t.message}"
                mainHandler.post { callback.onError(errorMessage) }
            }
        })
    }

    interface UploadCallback {
        fun onSuccess(response: String)
        fun onError(error: String)
    }
}
