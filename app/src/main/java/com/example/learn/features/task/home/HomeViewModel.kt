package com.example.learn.features.task.home

import androidx.compose.runtime.mutableStateMapOf
import androidx.lifecycle.ViewModel
import com.example.learn.domain.entities.TaskEntity
import com.example.learn.domain.repository.TaskRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class HomeViewModel(
    private val taskRepository: TaskRepository
) : ViewModel() {
    private val _homeState = MutableStateFlow<HomeState>(HomeState.InitialState)
    val homeState: StateFlow<HomeState> = _homeState.asStateFlow()

    var tasks = mutableStateMapOf<String, TaskEntity>()
        private set

   init {
       addListener("drA9G7zGPnYFbq0YQD7SZsLLIYn2")
   }

    fun addListener(userId: String) {
        taskRepository.addListener(userId, onError = {_homeState.value =
            HomeState.Error(it.message ?: "Something went wrong")
        },
            onDocumentEvent = { wasDeleted, task ->
                if(wasDeleted){
                    tasks.remove(task.id)
                }
                else{
                    tasks[task.id] = task
                }

        })
    }

    fun saveTask(task: TaskEntity) {
        _homeState.value = HomeState.Loading
        taskRepository.saveTask(task, onError = {
            _homeState.value = HomeState.Error(it?.message ?: "Something went wrong")
            return@saveTask
        })
        _homeState.value = HomeState.Success

    }

    fun deleteTask(task: TaskEntity) {
        _homeState.value = HomeState.Loading
        taskRepository.deleteTask(task.id, onError = {
            _homeState.value = HomeState.Error(it?.message ?: "Something went wrong")
            return@deleteTask
        })
        _homeState.value = HomeState.Success

    }



}

sealed class HomeState() {
    data object InitialState : HomeState()
    data object Loading : HomeState()
    data object Success : HomeState()
    class Error(val message: String) : HomeState()

}