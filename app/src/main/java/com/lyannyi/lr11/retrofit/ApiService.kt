package com.lyannyi.lr11.retrofit

import com.lyannyi.lr11.data.Photo
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface ApiService {
    @GET("albums/{id}/photos")
    suspend fun getPhotos(@Path("id") id: Int): List<Photo>

    @POST("photos")
    suspend fun createPhoto(@Body photo: Photo): Photo
}