package com.lyannyi.lr11.ui.screen

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import com.lyannyi.lr11.viewmodel.RetrofitViewModel
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.TextButton
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.lyannyi.lr11.data.Photo

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RetrofitScreen(viewModel: RetrofitViewModel) {
    val photos by viewModel.photos.collectAsState()
    val errorMessage by viewModel.errorMessage.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()
    var createdPhoto by remember { mutableStateOf<Photo?>(null) }
    var updatedPhoto by remember { mutableStateOf<Photo?>(null) }
    var deleteResponse by remember { mutableStateOf(false) }

    var id by remember { mutableStateOf("0") }

    LaunchedEffect(Unit) {
        viewModel.getPhotos(id.toIntOrNull() ?: 0)
    }

    LaunchedEffect(Unit) {
        viewModel.createdPhoto.collect { photo ->
            createdPhoto = photo
        }
    }

    LaunchedEffect(Unit) {
        viewModel.updatedPhoto.collect { photo ->
            updatedPhoto = photo
        }
    }

    LaunchedEffect(Unit) {
        viewModel.deleteResponse.collect { response ->
            deleteResponse = response
        }
    }

    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier
            .fillMaxSize()
            .padding(8.dp)
    ){
        Column(
            modifier = Modifier
                .fillMaxSize()
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ){
                OutlinedTextField(
                    value = id,
                    onValueChange = {
                        id = it
                        viewModel.getPhotos(id.toIntOrNull() ?: 0)
                    },
                    label = { Text("Id") },
                    modifier = Modifier.weight(1f)
                )
                Button(
                    onClick = {
                        val newPhoto = Photo(
                            albumId = 1,
                            title = "New Photo",
                            url = "",
                            thumbnailUrl = ""
                        )
                        viewModel.createPhoto(newPhoto)
                    },
                    modifier = Modifier.weight(1f)
                ) {
                    Text(text = "Create Photo")
                }
            }
            Row(
                modifier = Modifier
                    .fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ){
                Button(
                    onClick = {
                        val newPhoto = Photo(
                            albumId = 1,
                            id = 1,
                            title = "New Photo",
                            url = "",
                            thumbnailUrl = ""
                        )
                        viewModel.updatePhoto(newPhoto)
                    },
                    modifier = Modifier.weight(1f)
                ) {
                    Text(text = "Update Photo 1")
                }

                Button(
                    onClick = {
                        viewModel.deletePhoto(1)
                    },
                    modifier = Modifier.weight(1f)
                ) {
                    Text(text = "Delete Photo 1")
                }
            }
            LazyColumn{
                items(photos) { photo ->
                    Text(text = "Photo ID: ${photo.id}, Title: ${photo.title}")
                }
            }
        }
        if (errorMessage != null) {
            Surface(
                shape = RoundedCornerShape(16.dp),
                color = Color.White,
                modifier = Modifier
                    .padding(32.dp)
                    .shadow(
                        elevation = 16.dp,
                        shape = RoundedCornerShape(16.dp),
                        ambientColor = Color.Red,
                        spotColor = Color.Red
                    )
            ) {
                Box(
                    contentAlignment = Alignment.Center,
                    modifier = Modifier.padding(32.dp)
                ) {
                    Text(text = "Error: $errorMessage")
                }
            }
        }
        if (isLoading) {
            Text(text = "Loading...")
        }
    }
    if (createdPhoto != null) {
        AlertDialog(
            onDismissRequest = { createdPhoto = null },
            title = { Text("Photo Created") },
            text = { Text("Photo ID: ${createdPhoto!!.id}, Title: ${createdPhoto!!.title}") },
            confirmButton = {
                TextButton({ createdPhoto = null }) {
                    Text("Ok")
                }
            }
        )
    }
    if (updatedPhoto != null) {
        AlertDialog(
            onDismissRequest = { updatedPhoto = null },
            title = { Text("Photo Updated") },
            text = { Text("Photo ID: ${updatedPhoto!!.id}, Title: ${updatedPhoto!!.title}") },
            confirmButton = {
                TextButton({ updatedPhoto = null }) {
                    Text("Ok")
                }
            }
        )
    }
    if (deleteResponse) {
        AlertDialog(
            onDismissRequest = { deleteResponse = false },
            title = { Text("Photo Deleted") },
            text = { Text("Delete response: $deleteResponse") },
            confirmButton = {
                TextButton({ deleteResponse = false }) {
                    Text("Ok")
                }
            }
        )
    }
}