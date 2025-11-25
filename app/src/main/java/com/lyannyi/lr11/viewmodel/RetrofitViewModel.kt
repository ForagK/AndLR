package com.lyannyi.lr11.viewmodel

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.lyannyi.lr11.data.Photo
import com.lyannyi.lr11.retrofit.RetrofitClient
import kotlinx.coroutines.launch

class RetrofitViewModel : ViewModel()  {
    private val _photos = MutableLiveData<List<Photo>>()
    val photos = _photos as LiveData<List<Photo>>
    val createdPhoto = mutableStateOf<Photo?>(null)
    var errorMessage = mutableStateOf<String?>(null)

    var isLoading = mutableStateOf(false)

    fun getPhotos(id: Int){
        viewModelScope.launch {
            try {
                isLoading.value = true
                errorMessage.value = null
                val photos = RetrofitClient.api.getPhotos(id)
                if (photos.isEmpty()) throw Exception("Invalid id")
                _photos.value = photos
            }
            catch (error: Exception){
                errorMessage.value = error.message
                _photos.value = emptyList()
            }
            finally {
                isLoading.value = false
            }
        }
    }
    fun createPhoto(photo: Photo){
        viewModelScope.launch {
            try {
                val newPhoto = RetrofitClient.api.createPhoto(photo)
                createdPhoto.value = newPhoto
            }
            catch (error: Exception){
                errorMessage.value = error.message
            }
        }
    }
}