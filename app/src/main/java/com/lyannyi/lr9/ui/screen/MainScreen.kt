package com.lyannyi.lr9.ui.screen

import android.content.Intent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import com.lyannyi.lr9.activity.CameraActivity
import com.lyannyi.lr9.activity.DBActivity
import com.lyannyi.lr9.activity.FileManagerActivity
import com.lyannyi.lr9.activity.PlayerActivity

@Composable
fun MainScreen() {
    val context = LocalContext.current

    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.SpaceEvenly,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Button(onClick = {
            val intent = Intent(context, CameraActivity::class.java)
            context.startActivity(intent)
        }) {
            Text(text = "Camera")
        }
        Button(onClick = {
            val intent = Intent(context, PlayerActivity::class.java)
            context.startActivity(intent)
        }) {
            Text(text = "Player")
        }
        Button(onClick = {
            val intent = Intent(context, FileManagerActivity::class.java)
            context.startActivity(intent)
        }) {
            Text(text = "File Manager")
        }
        Button(onClick = {
            val intent = Intent(context, DBActivity::class.java)
            context.startActivity(intent)
        }) {
            Text(text = "Data Base")
        }
    }
}