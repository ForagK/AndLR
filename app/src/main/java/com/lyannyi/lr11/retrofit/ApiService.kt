package com.lyannyi.lr11.retrofit

import com.lyannyi.lr11.data.Photo
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path

interface ApiService {
    @GET("albums/{id}/photos")
    suspend fun getPhotos(@Path("id") id: Int): List<Photo>

    @POST("photos")
    suspend fun createPhoto(@Body photo: Photo): Photo

    @PUT("photos/{id}")
    suspend fun updatePhoto(
        @Path("id") id: Int,
        @Body photo: Photo
    ): Photo

    @DELETE("photos/{id}")
    suspend fun deletePhoto(
        @Path("id") id: Int,
    ): Response<Unit>
}