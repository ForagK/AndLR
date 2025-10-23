package com.lyannyi.lr7.ui.screen

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.lyannyi.lr7.viewmodel.PlayerViewModel
import kotlinx.coroutines.delay

@Composable
fun PlayerScreen(viewModel: PlayerViewModel) {
    val context = LocalContext.current
    val isPlaying by viewModel.isPlaying
    val selectedTrack by viewModel.selectedTrack
    val currentPosition by viewModel.currentPosition
    val duration by viewModel.duration
    val tracks = viewModel.tracks

    var expanded by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        viewModel.loadTracks(context)
    }

    LaunchedEffect(isPlaying) {
        while (isPlaying) {
            delay(1000)
            viewModel.updateProgress()
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Box(modifier = Modifier.fillMaxWidth()) {
            OutlinedTextField(
                value = selectedTrack,
                onValueChange = {},
                readOnly = true,
                label = { Text("Select track") },
                trailingIcon = {
                    Icon(
                        imageVector = if (expanded) Icons.Filled.KeyboardArrowUp else Icons.Filled.KeyboardArrowDown,
                        contentDescription = null,
                        modifier = Modifier.clickable { expanded = !expanded }
                    )
                },
                modifier = Modifier.fillMaxWidth()
            )

            DropdownMenu(
                expanded = expanded,
                onDismissRequest = { expanded = false },
                modifier = Modifier.fillMaxWidth()
            ) {
                tracks.forEach { track ->
                    DropdownMenuItem(
                        text = { Text(track.name) },
                        onClick = {
                            expanded = false
                            viewModel.selectTrack(context, track)
                        }
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(20.dp))

        Button(
            onClick = { viewModel.switchPause() },
            enabled = selectedTrack.isNotEmpty()
        ) {
            Text(if (isPlaying) "Pause" else "Play")
        }

        Spacer(modifier = Modifier.height(20.dp))

        Text(
            text = "${viewModel.formatTime(currentPosition)} / ${viewModel.formatTime(duration)}",
            style = MaterialTheme.typography.bodyLarge
        )
    }
}