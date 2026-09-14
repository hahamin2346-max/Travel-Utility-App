package com.example.travelutilityapp.ui.schedule

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TimeInput
import androidx.compose.material3.rememberTimePickerState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.travelutilityapp.ui.theme.YwTextSecondary
import java.time.LocalTime

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TimeRangePickerDialog(
    initialStart: LocalTime,
    initialEnd: LocalTime,
    onDismiss: () -> Unit,
    onConfirm: (start: LocalTime, end: LocalTime) -> Unit
) {
    val startState = rememberTimePickerState(
        initialHour = initialStart.hour,
        initialMinute = initialStart.minute,
        is24Hour = true
    )
    val endState = rememberTimePickerState(
        initialHour = initialEnd.hour,
        initialMinute = initialEnd.minute,
        is24Hour = true
    )

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("수업 시간") },
        text = {
            Column(
                modifier = Modifier.verticalScroll(rememberScrollState()),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Text("시작", color = YwTextSecondary, fontSize = 12.sp, fontWeight = FontWeight.Medium)
                TimeInput(state = startState)
                Text("종료", color = YwTextSecondary, fontSize = 12.sp, fontWeight = FontWeight.Medium)
                TimeInput(state = endState)
            }
        },
        confirmButton = {
            TextButton(
                onClick = {
                    onConfirm(
                        LocalTime.of(startState.hour, startState.minute),
                        LocalTime.of(endState.hour, endState.minute)
                    )
                }
            ) {
                Text("확인")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("취소")
            }
        }
    )
}
