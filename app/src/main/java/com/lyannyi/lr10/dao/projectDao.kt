package com.lyannyi.lr10.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Update
import com.lyannyi.lr10.entity.Project
import com.lyannyi.lr10.entity.ProjectWithTasks

@Dao
interface ProjectDao {
    @Insert
    suspend fun insertProject(project: Project): Long

    @Update
    suspend fun updateProject(project: Project)

    @Delete
    suspend fun deleteProject(task: Project)

    @Transaction
    @Query("SELECT * FROM projects")
    suspend fun getProjectsWithTasks(): List<ProjectWithTasks>
}