package com.lyannyi.lr10.viewmodel

import android.content.Context
import android.net.Uri
import androidx.compose.runtime.mutableStateOf
import androidx.documentfile.provider.DocumentFile
import androidx.lifecycle.ViewModel

class FileManagerViewModel : ViewModel()  {
    var currentDir = mutableStateOf<DocumentFile?>(null)

    var files = mutableStateOf<List<DocumentFile>>(emptyList())

    fun setRootDir(uri: Uri, context: Context) {
        val docFile = DocumentFile.fromTreeUri(context, uri)
        currentDir.value = docFile
        files.value = docFile?.listFiles()?.toList() ?: emptyList()
    }

    fun openDirectory(dir: DocumentFile) {
        if (dir.isDirectory) {
            currentDir.value = dir
            files.value = dir.listFiles().toList()
        }
    }

    fun goBack() {
        currentDir.value?.parentFile?.let {
            currentDir.value = it
            files.value = it.listFiles().toList()
        }
    }

    fun createItem(name: String, createFolder: Boolean) {
        currentDir.value?.let { dir ->
            if (createFolder) {
                dir.createDirectory(name)
            } else {
                dir.createFile("application/octet-stream", name)
            }
            files.value = dir.listFiles().toList()
        }
    }
}