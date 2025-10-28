package com.lyannyi.lr8.viewmodel

import android.content.Context
import android.media.MediaPlayer
import android.net.Uri
import android.os.Build
import android.provider.MediaStore
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.lyannyi.lr8.data.Track

class PlayerViewModel : ViewModel() {
    private var mediaPlayer: MediaPlayer? = null

    val isPlaying = mutableStateOf(false)
    val selectedTrack = mutableStateOf("")
    val currentPosition = mutableIntStateOf(0)
    val duration = mutableIntStateOf(0)

    val tracks = mutableListOf<Track>()

    fun loadTracks(context: Context) {
        val collection = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            MediaStore.Audio.Media.getContentUri(MediaStore.VOLUME_EXTERNAL)
        } else {
            MediaStore.Audio.Media.EXTERNAL_CONTENT_URI
        }

        val projection = arrayOf(
            MediaStore.Audio.Media._ID,
            MediaStore.Audio.Media.DISPLAY_NAME,
            MediaStore.Audio.Media.DURATION,
        )
        val selection = "${MediaStore.Audio.Media.IS_MUSIC} != 0"
        val selectionArgs = null
        val sortOrder = "${MediaStore.Audio.Media.DATE_ADDED} DESC"

        val query = context.contentResolver.query(
            collection,
            projection,
            selection,
            selectionArgs,
            sortOrder,

            )

        query?.use { cursor ->
            val idColumn = cursor.getColumnIndexOrThrow(MediaStore.Audio.Media._ID)
            val nameColumn = cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.DISPLAY_NAME)
            val durationColumn = cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.DURATION)

            while (cursor.moveToNext()) {
                val id = cursor.getLong(idColumn)
                val name = cursor.getString(nameColumn)
                val duration = cursor.getLong(durationColumn)

                val contentUri = Uri.withAppendedPath(
                    MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,
                    id.toString()
                )

                tracks += Track(contentUri, name, duration)
            }
        }
    }

    fun selectTrack(context: Context, track: Track) {
        mediaPlayer?.release()
        mediaPlayer = MediaPlayer().apply {
            setDataSource(context, track.uri)
            isLooping = false
            prepare()
            setOnCompletionListener { this@PlayerViewModel.isPlaying.value = false }
        }

        selectedTrack.value = track.name
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