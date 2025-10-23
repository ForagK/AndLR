package com.lyannyi.lr7.viewmodel

import android.os.Environment
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import java.io.File

class FileManagerViewModel : ViewModel()  {
    val rootDir = Environment.getExternalStorageDirectory()
    var currentDir = mutableStateOf(rootDir)
    var files = mutableStateOf(rootDir.listFiles()?.toList() ?: emptyList())

    fun openDirectory(dir: File) {
        if (dir.isDirectory) {
            currentDir.value = dir
            files.value = dir.listFiles()?.toList() ?: emptyList()
        }
    }

    fun goBack() {
        currentDir.value.parentFile?.let {
            currentDir.value = it
            files.value = it.listFiles()?.toList() ?: emptyList()
        }
    }

    fun createItem(name: String, createFolder: Boolean) {
        val newFile = File(currentDir.value, name)
        if (createFolder) newFile.mkdir() else newFile.createNewFile()
        files.value = currentDir.value.listFiles()?.toList() ?: emptyList()
    }
}