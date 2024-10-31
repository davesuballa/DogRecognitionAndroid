package com.example.buddy

import okhttp3.MultipartBody
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part

interface ApiService {
    @Multipart
    @POST("predict-dog-emotion") // Change to actual endpoint for emotion detection
    fun uploadImageToEmotion(@Part file: MultipartBody.Part): Call<ResponseBody>

    @Multipart
    @POST("predict-dog-posture") // Change to actual endpoint for posture detection
    fun uploadImageToPosture(@Part file: MultipartBody.Part): Call<ResponseBody>
}
