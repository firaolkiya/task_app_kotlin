package com.example.learn.common

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TimePicker
import androidx.compose.material3.TimePickerState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Popup


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CustomTimePicker(
    onDismiss:()->Unit,
    timePickerState: TimePickerState
) {
    Popup(
        onDismissRequest = {onDismiss()},
        alignment = Alignment.TopStart
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp)
                .offset(y = 64.dp)
                .shadow(elevation = 4.dp)
                .background(MaterialTheme.colorScheme.inverseOnSurface)

        ) {

            TimePicker(
                state = timePickerState,
            )
            Button(
                modifier = Modifier.padding(start = 250.dp),
                onClick = {onDismiss() }) {
                Text(text = "Ok")

            }
        }
    }
}