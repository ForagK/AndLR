package com.lyannyi.lr7.ui.screen

import android.os.Environment
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.FloatingActionButtonDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.lyannyi.lr7.item.FileItem
import com.lyannyi.lr7.viewmodel.FileManagerViewModel
import java.io.File

@Composable
fun FileManagerScreen(viewModel: FileManagerViewModel) {
    val currentDir by viewModel.currentDir
    val files by viewModel.files

    var showDialog by remember { mutableStateOf(false) }
    var newItemName by remember { mutableStateOf("") }
    var createFolder by remember { mutableStateOf(true) }

    Scaffold(
        topBar = {
            Row {
                if (currentDir != viewModel.rootDir) {
                    TextButton(
                        onClick = {
                            viewModel.goBack()
                        },
                        modifier = Modifier
                            .padding(start = 8.dp, top = 4.dp)
                    ) {
                        Text("Back")
                    }
                }

                Text(
                    text = currentDir.path,
                    modifier = Modifier.padding(8.dp)
                )
            }
        },

        floatingActionButton = {
            FloatingActionButton(
                onClick = { showDialog = true }
            ) {
                Icon(Icons.Default.Add, contentDescription = "Add")
            }
        }
    ) { padding ->
        Column(
            Modifier
                .fillMaxSize()
                .padding(padding)
        ) {
            HorizontalDivider()

            LazyColumn(Modifier.fillMaxSize()) {
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
    }
}