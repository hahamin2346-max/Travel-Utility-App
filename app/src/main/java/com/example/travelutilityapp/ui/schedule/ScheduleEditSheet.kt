package com.example.travelutilityapp.ui.schedule

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Add
import androidx.compose.material.icons.outlined.Close
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.travelutilityapp.R
import com.example.travelutilityapp.ui.theme.YwBackground
import com.example.travelutilityapp.ui.theme.YwBorderSoft
import com.example.travelutilityapp.ui.theme.YwPrimary
import com.example.travelutilityapp.ui.theme.YwSurface
import com.example.travelutilityapp.ui.theme.YwTextPrimary
import com.example.travelutilityapp.ui.theme.YwTextSecondary

/** Bottom sheet for adding/editing/deleting schedule entries, opened from the Edit FAB. */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ScheduleEditSheet(
    entries: List<ScheduleEntry>,
    onDismiss: () -> Unit,
    onSave: (List<ScheduleEntry>) -> Unit,
    modifier: Modifier = Modifier
) {
    var draftEntries by remember(entries) { mutableStateOf(entries) }
    var timePickerTargetId by remember { mutableStateOf<String?>(null) }
    val sheetState = rememberModalBottomSheetState()

    ModalBottomSheet(
        onDismissRequest = onDismiss,
        sheetState = sheetState,
        containerColor = YwSurface,
        modifier = modifier
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 20.dp)
                .padding(bottom = 24.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    stringResource(R.string.schedule_edit_title),
                    color = YwTextPrimary,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.SemiBold
                )
                Box(
                    modifier = Modifier
                        .size(32.dp)
                        .background(YwBackground, CircleShape)
                        .clickable(onClick = onDismiss),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Outlined.Close,
                        contentDescription = stringResource(R.string.cd_close),
                        tint = YwTextSecondary,
                        modifier = Modifier.size(16.dp)
                    )
                }
            }

            if (draftEntries.isEmpty()) {
                Text(
                    text = stringResource(R.string.schedule_edit_empty),
                    color = YwTextSecondary,
                    fontSize = 13.sp
                )
            } else {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(YwBackground, RoundedCornerShape(18.dp))
                        .padding(horizontal = 12.dp)
                ) {
                    draftEntries.forEachIndexed { index, entry ->
                        ScheduleEditRow(
                            entry = entry,
                            onTypeChange = { type ->
                                draftEntries = draftEntries.map {
                                    if (it.id == entry.id) it.copy(type = type) else it
                                }
                            },
                            onTimeClick = { timePickerTargetId = entry.id },
                            onRoomChange = { room ->
                                draftEntries = draftEntries.map {
                                    if (it.id == entry.id) it.copy(room = room) else it
                                }
                            },
                            onCourseChange = { course ->
                                draftEntries = draftEntries.map {
                                    if (it.id == entry.id) it.copy(course = course) else it
                                }
                            },
                            onTeacherChange = { teacher ->
                                draftEntries = draftEntries.map {
                                    if (it.id == entry.id) it.copy(teacher = teacher) else it
                                }
                            },
                            onDelete = { draftEntries = draftEntries.filterNot { it.id == entry.id } }
                        )
                        if (index != draftEntries.lastIndex) {
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(1.dp)
                                    .background(YwBorderSoft)
                            )
                        }
                    }
                }
            }

            OutlinedButton(
                onClick = { draftEntries = draftEntries + ScheduleEntry() },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(44.dp),
                shape = RoundedCornerShape(12.dp),
                border = BorderStroke(1.5.dp, YwPrimary),
                colors = ButtonDefaults.outlinedButtonColors(contentColor = YwPrimary)
            ) {
                Icon(imageVector = Icons.Outlined.Add, contentDescription = null, modifier = Modifier.size(15.dp))
                Spacer(Modifier.width(6.dp))
                Text(stringResource(R.string.schedule_edit_add), fontSize = 13.sp, fontWeight = FontWeight.SemiBold)
            }

            Button(
                onClick = { onSave(draftEntries.sortedBy { it.startTime }) },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(52.dp),
                shape = RoundedCornerShape(14.dp),
                colors = ButtonDefaults.buttonColors(containerColor = YwPrimary, contentColor = Color.White)
            ) {
                Text(stringResource(R.string.schedule_edit_save), fontSize = 15.sp, fontWeight = FontWeight.SemiBold)
            }
        }
    }

    val targetId = timePickerTargetId
    val targetEntry = draftEntries.firstOrNull { it.id == targetId }
    if (targetEntry != null) {
        TimeRangePickerDialog(
            initialStart = targetEntry.startTime,
            initialEnd = targetEntry.endTime,
            onDismiss = { timePickerTargetId = null },
            onConfirm = { start, end ->
                draftEntries = draftEntries.map {
                    if (it.id == targetEntry.id) it.copy(startTime = start, endTime = end) else it
                }
                timePickerTargetId = null
            }
        )
    }
}
