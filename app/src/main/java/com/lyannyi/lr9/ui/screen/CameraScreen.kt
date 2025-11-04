package com.lyannyi.lr9.ui.screen

import android.media.MediaPlayer
import android.util.Size
import androidx.camera.core.*
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.content.ContextCompat
import androidx.lifecycle.compose.LocalLifecycleOwner
import com.lyannyi.lr9.viewmodel.CameraViewModel
import android.graphics.Bitmap
import android.view.OrientationEventListener
import android.view.Surface
import androidx.compose.ui.graphics.asImageBitmap
import com.lyannyi.lr9.detector.RememberHumidityDetector
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.withContext
import com.lyannyi.lr9.R

@Composable
fun CameraScreen(viewModel: CameraViewModel) {
    val context = LocalContext.current
    val lifecycleOwner = LocalLifecycleOwner.current

    var imageCapture = remember { mutableStateOf<ImageCapture?>(null) }
    var cameraSelector = remember { mutableStateOf(CameraSelector.DEFAULT_BACK_CAMERA) }
    var currentRotation by remember { mutableStateOf(0) }
    var humidityTriggered by remember { mutableStateOf(false) }

    val previewView = remember { PreviewView(context) }
    val cameraProviderFuture = remember { ProcessCameraProvider.getInstance(context) }

    val mediaPlayer = remember {
        MediaPlayer.create(context, R.raw.camera).apply { isLooping = false }
    }

    var thumbnail by remember { mutableStateOf<Bitmap?>(null) }

    DisposableEffect(Unit) {
        val orientationListener = object : OrientationEventListener(context) {
            override fun onOrientationChanged(orientation: Int) {
                currentRotation = when (orientation) {
                    in 45..134 -> 270
                    in 135..224 -> 180
                    in 225..314 -> 90
                    else -> 0
                }
            }
        }

        orientationListener.enable()

        onDispose {
            orientationListener.disable()
            mediaPlayer.release()
        }
    }

    RememberHumidityDetector(context = context, onHumidityRise = {
        humidityTriggered = true
    })

    LaunchedEffect(humidityTriggered) {
        if (humidityTriggered) {
            delay(1000)
            humidityTriggered = false
        }
    }

    LaunchedEffect( currentRotation) {
        if (humidityTriggered) {
            imageCapture.value?.let {
                it.targetRotation = when (currentRotation) {
                    0 -> Surface.ROTATION_0
                    90 -> Surface.ROTATION_90
                    180 -> Surface.ROTATION_180
                    270 -> Surface.ROTATION_270
                    else -> Surface.ROTATION_0
                }
                viewModel.takePhoto(context, imageCapture, mediaPlayer)
            }
        }
    }

    LaunchedEffect(cameraSelector.value) {
        val cameraProvider = cameraProviderFuture.get()

        val preview = Preview.Builder().build().apply {
            setSurfaceProvider(previewView.surfaceProvider)
        }

        imageCapture.value = ImageCapture.Builder().build()

        try {
            cameraProvider.unbindAll()
            cameraProvider.bindToLifecycle(
                lifecycleOwner,
                cameraSelector.value,
                preview,
                imageCapture.value!!
            )
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    LaunchedEffect(viewModel.lastPhotoUri.value) {
        val uri = viewModel.lastPhotoUri.value

        if (uri != null) {
            thumbnail = withContext(Dispatchers.IO) {
                context.contentResolver.loadThumbnail(uri, Size(200, 200), null)
            }
        }
    }

    Box(modifier = Modifier.fillMaxSize()) {
        AndroidView(
            factory = { ctx ->
                val previewView = PreviewView(ctx).apply {
                    scaleType = PreviewView.ScaleType.FILL_CENTER
                }

                val cameraProviderFuture = ProcessCameraProvider.getInstance(ctx)

                cameraProviderFuture.addListener({
                    val cameraProvider = cameraProviderFuture.get()

                    val preview = Preview.Builder().build().also {
                        it.setSurfaceProvider(previewView.surfaceProvider)
                    }

                    imageCapture.value = ImageCapture.Builder()
                        .build()

                    try {
                        cameraProvider.unbindAll()

                        val useCases = mutableListOf<UseCase>()
                        useCases.add(preview)
                        imageCapture.value?.let { useCases.add(it) }

                        cameraProvider.bindToLifecycle(
                            lifecycleOwner,
                            cameraSelector.value,
                            *useCases.toTypedArray()
                        )
                    } catch (e: Exception) {
                        e.printStackTrace()
                    }
                }, ContextCompat.getMainExecutor(ctx))

                previewView
            },
            modifier = Modifier.fillMaxSize()
        )

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(100.dp)
                .align(Alignment.TopCenter)
                .background(Color.Black.copy(alpha = 0.5f))
                .padding(horizontal = 16.dp, vertical = 8.dp)
        )

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(160.dp)
                .align(Alignment.BottomCenter)
                .background(Color.Black.copy(alpha = 0.5f))
                .padding(16.dp)
        ) {
            thumbnail?.let {
                Image(
                    bitmap = it.asImageBitmap(),
                    contentDescription = "Thumbnail",
                )
            }
            Row(
                modifier = Modifier.align(Alignment.Center),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Button(
                    onClick = {
                        val rotation = when (currentRotation) {
                            0 -> Surface.ROTATION_0
                            90 -> Surface.ROTATION_90
                            180 -> Surface.ROTATION_180
                            270 -> Surface.ROTATION_270
                            else -> Surface.ROTATION_0
                        }

                        imageCapture.value?.targetRotation = rotation
                        viewModel.takePhoto(context, imageCapture, mediaPlayer);
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = Color.White.copy(alpha = 0.8f)),
                    shape = CircleShape,
                    modifier = Modifier.size(80.dp)
                ) {}
            }
        }
    }
}
