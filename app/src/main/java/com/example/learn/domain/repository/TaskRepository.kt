package com.example.learn.domain.repository

import com.example.learn.domain.entities.TaskEntity

interface TaskRepository {
    fun addListener(
        userId: String,
        onDocumentEvent: (Boolean, TaskEntity) -> Unit,
        onError: (Throwable) -> Unit
    )

    fun removeListener()
    fun getTask(taskId: String, onError: (Throwable) -> Unit, onSuccess: (TaskEntity) -> Unit)
    fun saveTask(task: TaskEntity, onError: (Throwable?) -> Unit)
    fun updateTask(task: TaskEntity, onError: (Throwable?) -> Unit)
    fun deleteTask(taskId: String, onError: (Throwable?) -> Unit)
}