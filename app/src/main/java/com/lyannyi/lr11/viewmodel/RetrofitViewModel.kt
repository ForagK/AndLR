package com.lyannyi.lr11.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.lyannyi.lr11.data.Photo
import com.lyannyi.lr11.retrofit.RetrofitClient
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.onCompletion
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch

class RetrofitViewModel : ViewModel()  {
    private val _photos = MutableStateFlow<List<Photo>>(emptyList())
    val photos: StateFlow<List<Photo>> = _photos

    private val _createdPhoto = MutableSharedFlow<Photo>()
    val createdPhoto: SharedFlow<Photo> = _createdPhoto


    private val _updatedPhoto = MutableSharedFlow<Photo>()
    val updatedPhoto: SharedFlow<Photo> = _updatedPhoto

    private val _deleteResponse = MutableSharedFlow<Boolean>()
    val deleteResponse: SharedFlow<Boolean> = _deleteResponse

    private val _errorMessage = MutableStateFlow<String?>(null)
    val errorMessage: StateFlow<String?> = _errorMessage

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    fun getPhotos(id: Int){
        viewModelScope.launch {
            flow {
                emit(RetrofitClient.api.getPhotos(id))
            }
                .onStart {
                    _isLoading.value = true
                    _errorMessage.value = null
                }
                .catch { e ->
                    _errorMessage.value = e.message
                    emit(emptyList())
                }
                .onCompletion {
                    _isLoading.value = false
                }
                .collect { photos ->
                    if (photos.isEmpty()) {
                        _errorMessage.value = "Invalid id"
                    }
                    _photos.value = photos
                }
        }
    }
    fun createPhoto(photo: Photo){
        viewModelScope.launch {
            flow {
                emit(RetrofitClient.api.createPhoto(photo))
            }
                .catch { e ->
                    _errorMessage.value = e.message
                }
                .collect { newPhoto ->
                    _createdPhoto.emit(newPhoto)
                }
        }
    }
    fun updatePhoto(photo: Photo){
        viewModelScope.launch {
            flow {
                emit(RetrofitClient.api.updatePhoto( photo.id!!, photo))
            }
                .catch { e ->
                    _errorMessage.value = e.message
                }
                .collect { newPhoto ->
                    _updatedPhoto.emit(newPhoto)
                }
        }
    }
    fun deletePhoto(id: Int){
        viewModelScope.launch {
            flow {
                val response = RetrofitClient.api.deletePhoto(id)
                if (response.isSuccessful) emit(true) else throw Exception("Delete failed")
            }
                .catch { e ->
                    _errorMessage.value = e.message
                }
                .collect { response ->
                    _deleteResponse.emit(response)
                }
        }
    }
}