package com.example.learn.features.task.home

import android.widget.Toast
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.indication
import androidx.compose.foundation.interaction.InteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.learn.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    homeViewModel: HomeViewModel,
    navController: NavController
) {
    val homeState = homeViewModel.homeState.collectAsState()
    val tasks = homeViewModel.tasks.values.toList()
    val context = LocalContext.current
    LaunchedEffect(homeState) {
        when(homeState.value){
            is HomeState.Error ->Toast.makeText(context,
                (homeState.value as HomeState.Error).message,Toast.LENGTH_LONG).show()
            is HomeState.Success -> {
                Toast.makeText(context, "added successfully", Toast.LENGTH_LONG).show()
            }
            else->{}
        }
    }
    Scaffold(
        containerColor = MaterialTheme.colorScheme.secondary,
        topBar = {
            TopAppBar(
                modifier = Modifier,
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    titleContentColor = MaterialTheme.colorScheme.onBackground
                ),
                title = { Text(text = "Task") })
        },
        floatingActionButton = {
            FloatingActionButton(
                containerColor = MaterialTheme.colorScheme.primaryContainer,
                contentColor = MaterialTheme.colorScheme.onPrimaryContainer,
                onClick = { navController.navigate("edit_screen")}) {
                Icon(
                    modifier = Modifier.size(45.dp),
                    imageVector = Icons.Filled.Add, contentDescription = "floating action button"
                )

            }
        }
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .padding(horizontal = 12.dp)
                .padding(vertical = paddingValues.calculateTopPadding())
        )
        {
            item { if (homeState.value is HomeState.Loading){
                CircularProgressIndicator()
            }
            }

            items(tasks.size){index->
                Element(onChecked = { /*TODO*/ },checked = tasks[index].priority=="High",
                    text = tasks[index].title,
                    time = tasks[index].dueDate+" at " + tasks[index].dueTime+" PM",
                    onEdit = {navController.navigate("edit_screen")},
                    onMenu = {
                        homeViewModel.deleteTask(tasks[index])
                    })
            }


        }

    }

}


@Composable
fun Element(onChecked: () -> Unit,
            onEdit:()->Unit,
            onMenu:()->Unit,
            checked:Boolean,
            text: String,
            time: String) {

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 4.dp, vertical = 10.dp)
            .border(
                shape = RectangleShape, border = BorderStroke(width = 1.dp, color = Color.Gray)
            )
            .clickable {
            }
            .padding(vertical = 10.dp, horizontal = 8.dp)
            ,
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically

    ) {
        Checkbox(
            modifier = Modifier,
            colors = CheckboxDefaults.colors(
                checkedColor = MaterialTheme.colorScheme.surfaceVariant,
                uncheckedColor = Color.Gray,
                checkmarkColor = Color.White
            ),
            checked = checked,
            onCheckedChange = { onChecked() },
        )
        Column(
            modifier = Modifier.weight(1f),
            verticalArrangement = Arrangement.Center
        ) {
            Text(text = text, style = TextStyle(fontWeight = FontWeight.Bold))
            Text(text = time, style = TextStyle(fontWeight = FontWeight.W300))
        }


        Icon(
            modifier = Modifier
                .clickable { onEdit() }
                .size(25.dp),

            imageVector = Icons.Default.Edit,
            contentDescription = "left icon"
        )

        Icon(
            modifier = Modifier
                .clickable { onMenu() }
                .size(40.dp),
            imageVector = Icons.Default.MoreVert,
            contentDescription = "revert"
        )


    }
}
