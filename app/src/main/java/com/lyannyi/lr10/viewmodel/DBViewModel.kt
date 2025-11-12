package com.lyannyi.lr10.viewmodel

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.lyannyi.lr10.dao.TaskDao
import com.lyannyi.lr10.data.App
import com.lyannyi.lr10.entity.Task
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class DBViewModel : ViewModel() {
    val db = App.instance?.database

    val taskDao: TaskDao? = db?.taskDao()

    var tasks = mutableStateOf<List<Task>>(emptyList())

    var filteredTasks = mutableStateOf<List<Task>>(emptyList())

    fun loadTasks() {
        viewModelScope.launch(Dispatchers.IO) {
            val loadedTasks = taskDao?.getAllTasks() ?: emptyList()
            withContext(Dispatchers.Main) {
                tasks.value = loadedTasks
            }
        }
    }

    fun loadUnfinishedTasks() {
        viewModelScope.launch(Dispatchers.IO) {
            val unfinishedTasks = taskDao?.getAllUnfinished() ?: emptyList()
            withContext(Dispatchers.Main) {
                tasks.value = unfinishedTasks
            }
        }
    }

    fun addTask(task: Task) {
        viewModelScope.launch(Dispatchers.IO) {
            taskDao?.insertTask(task)
            loadTasks()
        }
    }

    fun deleteTask(task: Task) {
        viewModelScope.launch(Dispatchers.IO) {
            taskDao?.deleteTask(task)
            loadTasks()
        }
    }

    fun updateTask(task: Task) {
        viewModelScope.launch(Dispatchers.IO) {
            taskDao?.updateTask(task)
            loadTasks()
        }
    }
}