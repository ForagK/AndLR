package com.lyannyi.lr6.viewmodel

import android.content.Context
import android.media.MediaPlayer
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.lyannyi.lr6.R

class PlayerViewModel() : ViewModel() {
    private var mediaPlayer: MediaPlayer? = null
    val isPlaying = mutableStateOf(false)
    val selectedTrack = mutableStateOf("")
    val currentPosition = mutableIntStateOf(0)
    val duration = mutableIntStateOf(0)

    private val tracks = mapOf(
        "Eleventh Hour" to R.raw.eleventh_hour,
        "Book Betrayal" to R.raw.book_betrayal,
        "Le Fanu" to R.raw.le_fanu,
        "Humidity" to R.raw.humidity
    )

    fun getTracks(): Map<String, Int> = tracks

    fun selectTrack(context: Context, name: String) {
        val resId = tracks[name] ?: return
        mediaPlayer?.release()

        mediaPlayer = MediaPlayer.create(context, resId).apply {
            isLooping = false
            setOnCompletionListener { this@PlayerViewModel.isPlaying.value = false }
        }
        selectedTrack.value = name
        duration.value = mediaPlayer?.duration ?: 0
        currentPosition.value = 0
        isPlaying.value = false
    }

    fun switchPause() {
        mediaPlayer?.let {
            if (isPlaying.value) {
                it.pause()
                isPlaying.value = false
            } else {
                it.start()
                isPlaying.value = true
            }
        }
    }

    fun updateProgress() {
        mediaPlayer?.let {
            currentPosition.value = it.currentPosition
            duration.value = it.duration
        }
    }

    override fun onCleared() {
        super.onCleared()
        mediaPlayer?.release()
        mediaPlayer = null
    }

    fun formatTime(ms: Int): String {
        val totalSeconds = ms / 1000
        val minutes = totalSeconds / 60
        val seconds = totalSeconds % 60
        return String.format("%02d:%02d", minutes, seconds)
    }
}