package com.lyannyi.lr9.ui.screen

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.lyannyi.lr9.item.FileItem
import com.lyannyi.lr9.viewmodel.FileManagerViewModel
import android.content.Context
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.LaunchedEffect
import android.content.Intent

@Composable
fun FileManagerScreen(viewModel: FileManagerViewModel, context: Context) {
    val currentDir by viewModel.currentDir
    val files by viewModel.files

    var showDialog by remember { mutableStateOf(false) }
    var newItemName by remember { mutableStateOf("") }
    var createFolder by remember { mutableStateOf(true) }

    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.OpenDocumentTree()
    ) { uri ->
        uri?.let {
            context.contentResolver.takePersistableUriPermission(
                it,
                Intent.FLAG_GRANT_READ_URI_PERMISSION or Intent.FLAG_GRANT_WRITE_URI_PERMISSION
            )
            viewModel.setRootDir(it, context)
        }
    }

    LaunchedEffect(Unit) {
        if (currentDir == null) {
            launcher.launch(null)
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
    ){
        Column(
            Modifier
                .fillMaxWidth()
                .padding(8.dp)
        ) {
            Text(
                text = currentDir?.name ?: "Root",
                modifier = Modifier.padding(8.dp)
            )

            HorizontalDivider()

            LazyColumn(Modifier.fillMaxWidth()) {
                items(files) { file ->
                    FileItem(file = file) {
                        viewModel.openDirectory(file)
                    }
                }
            }

            if (showDialog) {
                AlertDialog(
                    onDismissRequest = { showDialog = false },
                    confirmButton = {
                        TextButton(onClick = {
                            if (newItemName.isNotBlank()) {
                                viewModel.createItem(newItemName, createFolder)
                            }
                            showDialog = false
                            newItemName = ""
                        }) {
                            Text("Create")
                        }
                    },
                    dismissButton = {
                        TextButton(onClick = { showDialog = false }) {
                            Text("Cancel")
                        }
                    },
                    title = { Text("New element") },
                    text = {
                        Column {
                            OutlinedTextField(
                                value = newItemName,
                                onValueChange = { newItemName = it },
                                label = { Text("Name") },
                                singleLine = true
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                RadioButton(
                                    selected = createFolder,
                                    onClick = { createFolder = true }
                                )
                                Text("Folder")
                                Spacer(Modifier.width(16.dp))
                                RadioButton(
                                    selected = !createFolder,
                                    onClick = { createFolder = false }
                                )
                                Text("File")
                            }
                        }
                    }
                )
            }
        }

        FloatingActionButton(
            onClick = { showDialog = true },
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(16.dp),
        ) {
            Icon(Icons.Default.Add, contentDescription = "Add")
        }

        if (currentDir?.parentFile != null){
            FloatingActionButton(
                onClick = { viewModel.goBack() },
                modifier = Modifier
                    .align(Alignment.BottomStart)
                    .padding(16.dp),

                ) {
                Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
            }
        }
    }
}