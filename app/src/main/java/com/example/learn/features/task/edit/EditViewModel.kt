package com.example.learn.features.task.edit

import androidx.lifecycle.ViewModel
import com.example.learn.domain.entities.TaskEntity
import com.example.learn.domain.repository.TaskRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class EditViewModel(
    private val taskRepository: TaskRepository
):ViewModel(){

    val _editPageState = MutableStateFlow<EditPageState>(EditPageState.InitialState)
    val editPageState:StateFlow<EditPageState> = _editPageState.asStateFlow()

    fun saveTask(task: TaskEntity) {
        _editPageState.value = EditPageState.Loading
        taskRepository.saveTask(task, onError = {
            _editPageState.value = EditPageState.Error(it?.message ?: "Something went wrong")
            return@saveTask
        })
        _editPageState.value = EditPageState.Success

    }


}

sealed class EditPageState() {
    data object InitialState : EditPageState()
    data object Loading : EditPageState()
    data object Success : EditPageState()
    class Error(val message: String) : EditPageState()

}

