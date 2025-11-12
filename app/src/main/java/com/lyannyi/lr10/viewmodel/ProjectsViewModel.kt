package com.lyannyi.lr10.viewmodel

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.lyannyi.lr10.dao.ProjectDao
import com.lyannyi.lr10.dao.TaskDao
import com.lyannyi.lr10.data.App
import com.lyannyi.lr10.entity.Project
import com.lyannyi.lr10.entity.ProjectWithTasks
import com.lyannyi.lr10.entity.Task
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class ProjectsViewModel : ViewModel() {
    val db = App.instance?.database

    val taskDao: TaskDao? = db?.taskDao()
    val projectDao: ProjectDao? = db?.projectDao()

    var projectsWithTasks = mutableStateOf<List<ProjectWithTasks>>(emptyList())

    fun loadProjectsWithTasks() {
        viewModelScope.launch(Dispatchers.IO) {
            val loadedProjects = projectDao?.getProjectsWithTasks() ?: emptyList()
            withContext(Dispatchers.Main) {
                projectsWithTasks.value = loadedProjects
            }
        }
    }

    fun updateProjectsWithTasks(projectWithTasks: ProjectWithTasks) {
        viewModelScope.launch(Dispatchers.IO) {
            projectDao?.updateProject(projectWithTasks.project)
            projectWithTasks.tasks.forEach { task ->
                if (task.taskId == 0) taskDao?.insertTask(task)
                else taskDao?.updateTask(task)
                loadProjectsWithTasks()
            }
        }
    }

    suspend fun addProject(project: Project): Long {
        val id = projectDao!!.insertProject(project)
        loadProjectsWithTasks()
        return id
    }

    fun deleteProjects(project: Project) {
        viewModelScope.launch(Dispatchers.IO) {
            projectDao?.deleteProject(project)
            loadProjectsWithTasks()
        }
    }

    fun updateProject(project: Project) {
        viewModelScope.launch(Dispatchers.IO) {
            projectDao?.updateProject(project)
            loadProjectsWithTasks()
        }
    }

    fun addTask(task: Task) {
        viewModelScope.launch(Dispatchers.IO) {
            taskDao?.insertTask(task)
            loadProjectsWithTasks()
        }
    }

    fun deleteTask(task: Task) {
        viewModelScope.launch(Dispatchers.IO) {
            taskDao?.deleteTask(task)
            loadProjectsWithTasks()
        }
    }

    fun updateTask(task: Task) {
        viewModelScope.launch(Dispatchers.IO) {
            taskDao?.updateTask(task)
            loadProjectsWithTasks()
        }
    }
}