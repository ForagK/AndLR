package com.lyannyi.lr11.entity

import androidx.room.Embedded
import androidx.room.Relation

data class ProjectWithTasks(
    @Embedded val project: Project,
    @Relation(
        parentColumn = "projectId",
        entityColumn = "projectPartId"
    )
    val tasks: List<Task>
)
