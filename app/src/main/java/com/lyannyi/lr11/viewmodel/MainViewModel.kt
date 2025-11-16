package com.lyannyi.lr11.viewmodel

import android.content.Context
import android.media.MediaPlayer
import android.widget.Toast
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth
import com.lyannyi.lr11.R

class MainViewModel : ViewModel() {
    private var mediaPlayer: MediaPlayer? = null

    fun startSound(context: Context) {
        if (mediaPlayer == null) {
            mediaPlayer = MediaPlayer.create(context, R.raw.book_betrayal).apply {
                isLooping = true
                start()
            }
        } else { mediaPlayer?.start() }
    }

    fun stopSound() {
        mediaPlayer?.stop()
        mediaPlayer?.release()
        mediaPlayer = null
    }
}