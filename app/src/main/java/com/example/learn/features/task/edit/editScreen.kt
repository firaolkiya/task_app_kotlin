package com.example.learn.features.task.edit

import android.annotation.SuppressLint
import android.widget.Space
import android.widget.Toast
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.KeyboardArrowLeft
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ButtonElevation
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerState
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldColors
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.material3.rememberTimePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.learn.common.CustomTimePicker
import com.example.learn.common.ShowDatePicker
import com.example.learn.convertMillisToDate
import com.example.learn.data.repository_impl.TaskRepositoryImpl
import com.example.learn.domain.entities.TaskEntity
import com.example.learn.domain.repository.TaskRepository
import com.google.type.Date
import java.util.Calendar
import kotlin.time.Duration.Companion.days


@SuppressLint("DefaultLocale")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditPage(
    editViewModel: EditViewModel,
    navController: NavController
) {

    val pageState = editViewModel._editPageState.collectAsState()
    val context = LocalContext.current
    LaunchedEffect(pageState) {
        when(pageState.value){
            is EditPageState.Error -> Toast.makeText(context,
                (pageState.value as EditPageState.Error).message, Toast.LENGTH_LONG).show()
            is EditPageState.Success -> {
                Toast.makeText(context, "added successfully", Toast.LENGTH_LONG).show()
                navController.navigate("home")
            }
            else->{}
        }
    }
    val priorites= listOf("None","Low","Medium","High")
    var title by remember {
        mutableStateOf("")
    }
    var description by remember {
        mutableStateOf("")
    }
    var url by remember {
        mutableStateOf("")
    }

    var showDatePickerState by remember {
        mutableStateOf(false)
    }

    var showTimePickerState by remember {
        mutableStateOf(false)
    }
    var datePickerState  = rememberDatePickerState()

    val selectedDate:String = datePickerState.selectedDateMillis?.let { convertMillisToDate(it) }?: ""
    val currentTime = Calendar.getInstance()
    val timePickerState = rememberTimePickerState(
        initialHour = currentTime.get(Calendar.HOUR_OF_DAY),
        initialMinute = currentTime.get(Calendar.MINUTE),
        is24Hour = true
    )

// Formatting the picked time
    val pickedTime = if (timePickerState.is24hour) {
        String.format("%02d:%02d", timePickerState.hour, timePickerState.minute)
    } else {
        val amPm = if (timePickerState.hour < 12) "AM" else "PM"
        val hourIn12Format = if (timePickerState.hour % 12 == 0) 12 else timePickerState.hour % 12
        String.format("%02d:%02d %s", hourIn12Format, timePickerState.minute, amPm)
    }

    var isDropdownExpanded by remember {
        mutableStateOf(false)
    }

    var selectedIndex by remember {
        mutableStateOf(0)
    }

    Scaffold(
        containerColor = MaterialTheme.colorScheme.secondary,
        topBar = {
            TopAppBar(
                navigationIcon = { IconButton(onClick = { navController.navigate("home")}) {

                }
                    Icon(
                        modifier = Modifier
                            .size(50.dp)
                            .padding(4.dp),
                        imageVector = Icons.Default.KeyboardArrowLeft, contentDescription = "")},

                modifier = Modifier,
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    titleContentColor = MaterialTheme.colorScheme.onBackground
                ),
                title = { Text(text = "Edit Task") })
        },

    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .padding(horizontal = 12.dp)
                .padding(vertical = paddingValues.calculateTopPadding())
        )
        {

            item {
                Column(
                    modifier = Modifier
                        .fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Spacer(modifier = Modifier.height(20.dp))
                    CustomTextField(label = "Task Title",value = title) {
                        title = it
                    }
                    if (pageState.value is EditPageState.Loading){
                        CircularProgressIndicator()
                    }
                    Spacer(modifier = Modifier.height(10.dp))
                    CustomTextField(label = "Description",value = description) {
                            description = it
                    }
                    Spacer(modifier = Modifier.height(10.dp))
                    CustomTextField(label = "Url",value = url) {
                        url = it
                    }
                    Spacer(modifier = Modifier.height(10.dp))
                    Row (
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(10.dp)
                            .border(
                                shape = RoundedCornerShape(10.dp),
                                border = BorderStroke(width = 1.dp, color = Color.Gray)
                            )
                            .padding(horizontal = 10.dp, vertical = 8.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ){
                        Text(text = "Date", modifier = Modifier.weight(1f))
                        Text(text = selectedDate)
                        IconButton(onClick = {
                            showDatePickerState = !showDatePickerState
                        }) {
                            Icon(
                                modifier = Modifier.size(35.dp),
                                imageVector = Icons.Default.DateRange, contentDescription =null )
                        }


                    }
                    if (showDatePickerState){
                        ShowDatePicker(state = datePickerState) {
                            showDatePickerState = false
                        }
                    }
                    Spacer(modifier = Modifier.height(10.dp))
                    Row (
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(10.dp)
                            .border(
                                shape = RoundedCornerShape(10.dp),
                                border = BorderStroke(width = 1.dp, color = Color.Gray)
                            )
                            .padding(horizontal = 10.dp, vertical = 8.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ){
                        Text(text = "Time", modifier = Modifier.weight(1f))
                        Text(text = pickedTime)
                        IconButton(onClick = {

                        }) {
                            IconButton(onClick = {
                                showTimePickerState = true
                            }) {

                                Icon(
                                    modifier = Modifier.size(35.dp),
                                    imageVector = Icons.Default.Edit, contentDescription = null
                                )
                            }
                        }


                    }
                    if (showTimePickerState){
                        CustomTimePicker(
                            onDismiss = { showTimePickerState = false},
                            timePickerState = timePickerState
                        )
                    }

                    Spacer(modifier = Modifier.height(10.dp))
                    Row (
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(10.dp)
                            .border(
                                shape = RoundedCornerShape(10.dp),
                                border = BorderStroke(width = 1.dp, color = Color.Gray)
                            )
                            .padding(horizontal = 10.dp, vertical = 8.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ){
                        Text(text = "Priority", modifier = Modifier.weight(1f))
                        Text(text = priorites[selectedIndex])
                        IconButton(onClick = { isDropdownExpanded = !isDropdownExpanded}) {
                            Icon(
                                modifier = Modifier.size(35.dp),
                                imageVector = Icons.Default.ArrowDropDown, contentDescription ="dfn" )
                        }

                        DropdownMenu(
                            expanded =isDropdownExpanded ,
                            onDismissRequest = { isDropdownExpanded= !isDropdownExpanded })
                        {
                            priorites.forEachIndexed{index,item->
                                DropdownMenuItem(text = { Text(text = item) },
                                    onClick = { selectedIndex = index
                                        isDropdownExpanded = false
                                    })
                            }



                        }


                    }
                    Spacer(modifier = Modifier.height(20.dp))
                    ElevatedButton(
                        modifier = Modifier
                            .width(200.dp)
                            .padding(horizontal = 15.dp, vertical = 5.dp),
                        colors = ButtonDefaults.buttonColors(
                            contentColor = MaterialTheme.colorScheme.onBackground,
                            containerColor = MaterialTheme.colorScheme.primaryContainer
                        ),
                        onClick = {
                            val task = TaskEntity(
                                title = title,
                                userId = "drA9G7zGPnYFbq0YQD7SZsLLIYn2",
                                url = url,
                                dueDate = selectedDate,
                                dueTime = pickedTime,
                                priority = priorites[selectedIndex]
                            )
                            editViewModel.saveTask(task)
                            navController.navigate("home")
                        }) {
                        Text(text = "Finish")
                    }


                }
            }



        }

    }

}




@Composable
fun CustomTextField(label:String,value:String,onValueChange:(String)->Unit){
    OutlinedTextField(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 10.dp)
            .clip(shape = RoundedCornerShape(10.dp))
            .border(
                shape = RoundedCornerShape(10.dp),
                border = BorderStroke(width = 1.dp, color = Color.Gray)
            )
        ,
        colors = TextFieldDefaults.colors(
            focusedContainerColor = Color.Transparent,
            unfocusedIndicatorColor = Color.Transparent,
            focusedIndicatorColor = Color.Transparent,
            focusedLabelColor = MaterialTheme.colorScheme.onSecondary,
            unfocusedLabelColor = MaterialTheme.colorScheme.onSecondary,
            unfocusedContainerColor = MaterialTheme.colorScheme.secondary,
            cursorColor = MaterialTheme.colorScheme.onSecondary,
            focusedTextColor = MaterialTheme.colorScheme.onSecondary,
            unfocusedTextColor = MaterialTheme.colorScheme.onSecondary,
            disabledContainerColor = Color.Transparent
        ),

        maxLines = 1,
        value =value ,
        label = { Text(text = label) },
        onValueChange ={onValueChange(it)}
    )
}