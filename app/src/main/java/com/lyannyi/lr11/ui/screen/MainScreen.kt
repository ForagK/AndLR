package com.lyannyi.lr11.ui.screen

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
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth
import com.google.firebase.crashlytics.FirebaseCrashlytics
import com.lyannyi.lr11.activity.AuthActivity
import com.lyannyi.lr11.activity.CameraActivity
import com.lyannyi.lr11.activity.DBActivity
import com.lyannyi.lr11.activity.FileManagerActivity
import com.lyannyi.lr11.activity.PlayerActivity
import com.lyannyi.lr11.activity.ProjectsActivity
import com.lyannyi.lr11.activity.RetrofitActivity

@Composable
fun MainScreen() {
    val context = LocalContext.current
    val auth: FirebaseAuth = Firebase.auth

    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.SpaceEvenly,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(text="Email: " + auth.currentUser?.email.toString())

        Button(onClick = {
            auth.signOut()
            val intent = Intent(context, AuthActivity::class.java).apply {
                flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            }
            context.startActivity(intent)
        }) {
            Text(text = "Logout")
        }
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
            Text(text = "Tasks")
        }
        Button(onClick = {
            val intent = Intent(context, ProjectsActivity::class.java)
            context.startActivity(intent)
        }) {
            Text(text = "Projects")
        }
        Button(onClick = {
            try {
                val responseCode = 500
                if (responseCode != 200) {
                    throw IllegalStateException("Invalid server response code: $responseCode")
                }
            } catch (e: Exception) {
                FirebaseCrashlytics.getInstance().recordException(e)
            }
        }) {
            Text(text = "Crash")
        }
        Button(onClick = {
            val intent = Intent(context, RetrofitActivity::class.java)
            context.startActivity(intent)
        }) {
            Text(text = "Retrofit")
        }
    }
}