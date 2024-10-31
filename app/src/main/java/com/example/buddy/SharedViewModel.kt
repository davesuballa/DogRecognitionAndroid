package com.example.buddy

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class SharedViewModel : ViewModel(){

    private val breedLiveData = MutableLiveData<String>()
    private val furLiveData = MutableLiveData<String>()

    fun setBreed(breed: String) {
        breedLiveData.value = breed
    }

    fun getBreed(): LiveData<String> {
        return breedLiveData
    }

    fun setfurColor(furColor: String) {
        furLiveData.value = furColor
    }

    fun getFur(): LiveData<String> {
        return furLiveData
    }
}