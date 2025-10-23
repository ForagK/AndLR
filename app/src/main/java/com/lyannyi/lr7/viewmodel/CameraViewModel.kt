package com.lyannyi.lr7.viewmodel

import android.content.Context
import android.net.Uri
import android.os.Environment
import android.widget.Toast
import androidx.camera.core.ImageCapture
import androidx.camera.core.ImageCaptureException
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import androidx.lifecycle.ViewModel
import java.io.File

class CameraViewModel : ViewModel() {
    val lastPhotoUri: MutableState<Uri?> = mutableStateOf(null)

    fun takePhoto(context: Context, imageCapture: MutableState<ImageCapture?>) {
        val executor = ContextCompat.getMainExecutor(context)

        val photoFile = File(
            File(
                context.getExternalFilesDir(Environment.DIRECTORY_PICTURES),
                ""
            ),
            "photo_${System.currentTimeMillis()}.jpg"
        )

        val uri = FileProvider.getUriForFile(
            context,
            "${context.packageName}.provider",
            photoFile
        )

        val outputOptions = ImageCapture.OutputFileOptions.Builder(
            context.contentResolver,
            android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
            android.content.ContentValues().apply {
                put(
                    android.provider.MediaStore.Images.Media.DISPLAY_NAME,
                    "photo_${System.currentTimeMillis()}.jpg"
                )
                put(
                    android.provider.MediaStore.Images.Media.MIME_TYPE,
                    "image/jpeg"
                )
                put(
                    android.provider.MediaStore.Images.Media.RELATIVE_PATH,
                    "Pictures/savedPhotosTest"
                )
            }
        ).build()

        imageCapture.value?.takePicture(
            outputOptions,
            executor,
            object : ImageCapture.OnImageSavedCallback {
                override fun onImageSaved(outputFileResults: ImageCapture.OutputFileResults) {
                    val savedUri = outputFileResults.savedUri ?: uri
                    lastPhotoUri.value = savedUri
                    Toast.makeText(
                        context,
                        "Фото збережено: $savedUri",
                        Toast.LENGTH_SHORT
                    ).show()
                }

                override fun onError(exception: ImageCaptureException) {
                    Toast.makeText(
                        context,
                        "Помилка: ${exception.message}",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        )
    }
}