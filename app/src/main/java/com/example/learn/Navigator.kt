package com.example.learn

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.learn.data.repository_impl.TaskRepositoryImpl
import com.example.learn.features.task.edit.EditPage
import com.example.learn.features.task.edit.EditViewModel
import com.example.learn.features.task.home.HomeScreen
import com.example.learn.features.task.home.HomeViewModel

@Composable
fun Navigator(){
    val navController = rememberNavController()
    val taskRepository = TaskRepositoryImpl()
    val homeViewModel = HomeViewModel(taskRepository)
    val editViewModel = EditViewModel(taskRepository)

    NavHost(navController = navController, startDestination = "home") {
        composable("home"){
            HomeScreen(homeViewModel = homeViewModel,navController)
        }
        composable("edit_screen"){
            EditPage(editViewModel = editViewModel,navController)
        }

    }
}