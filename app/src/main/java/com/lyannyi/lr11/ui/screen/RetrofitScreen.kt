package com.lyannyi.lr11.ui.screen

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import com.lyannyi.lr11.viewmodel.RetrofitViewModel
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.TextButton
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
    val photos by viewModel.photos.observeAsState(emptyList())
    var createdPhoto by viewModel.createdPhoto
    val errorMessage by viewModel.errorMessage
    val isLoading by viewModel.isLoading

    var id by remember { mutableStateOf("0") }

    LaunchedEffect(Unit) {
        viewModel.getPhotos(id.toIntOrNull() ?: 0)
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
            onDismissRequest = { viewModel.createdPhoto.value = null },
            title = { Text("Photo Created") },
            text = { Text("Photo ID: ${createdPhoto!!.id}, Title: ${createdPhoto!!.title}") },
            confirmButton = {
                TextButton({ viewModel.createdPhoto.value = null }) {
                    Text("Ok")
                }
            }
        )
    }
}