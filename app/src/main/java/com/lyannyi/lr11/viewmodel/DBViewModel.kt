package com.lyannyi.lr11.viewmodel

import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.Firebase
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.firestore
import com.lyannyi.lr11.dao.TaskDao
import com.lyannyi.lr11.data.App
import com.lyannyi.lr11.data.FirestoreTask
import com.lyannyi.lr11.entity.Task
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class DBViewModel : ViewModel() {
    private val db = Firebase.firestore
    private val tasksCollection = db.collection("tasks")

    var tasks = mutableStateOf<List<FirestoreTask>>(emptyList())

    fun loadTasks() {
        tasksCollection.get()
            .addOnSuccessListener { result ->
                val list = result.documents.mapNotNull { doc ->
                    doc.toObject(FirestoreTask::class.java)
                }
                tasks.value = list
            }
            .addOnFailureListener {
                Log.w("db", "Error getting tasks")
            }
    }

    fun loadUnfinishedTasks() {
        tasksCollection.whereEqualTo("status", false)
            .orderBy("deadline", Query.Direction.ASCENDING)
            .get()
            .addOnSuccessListener { result ->
                val list = result.documents.mapNotNull { doc ->
                    doc.toObject(FirestoreTask::class.java)
                }
                tasks.value = list
            }
    }

    fun addTask(task: FirestoreTask) {
        tasksCollection
            .add(task)
            .addOnSuccessListener { task ->
                task.update("id", task.id)
                loadTasks()
            }
    }

    fun deleteTask(task: FirestoreTask) {
        tasksCollection
            .document(task.id)
            .delete()
            .addOnSuccessListener { loadTasks() }
    }

    fun updateTask(task: FirestoreTask) {
        tasksCollection
            .document(task.id)
            .set(task)
            .addOnSuccessListener { loadTasks() }
    }
}