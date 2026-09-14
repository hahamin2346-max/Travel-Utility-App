package com.example.travelutilityapp.ui.schedule

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.DeleteOutline
import androidx.compose.material.icons.outlined.KeyboardArrowDown
import androidx.compose.material.icons.outlined.Schedule
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.travelutilityapp.ui.theme.YwBackground
import com.example.travelutilityapp.ui.theme.YwBorderSoft
import com.example.travelutilityapp.ui.theme.YwTextPrimary
import com.example.travelutilityapp.ui.theme.YwTextSecondary

private val TYPE_FIELD_WIDTH = 82.dp
private val ROW_GAP = 8.dp

/**
 * One editable row inside the schedule edit sheet, two lines tall:
 * type/time/room/delete on top, optional course/teacher below, indented to line up.
 */
@Composable
fun ScheduleEditRow(
    entry: ScheduleEntry,
    onTypeChange: (ClassType) -> Unit,
    onTimeClick: () -> Unit,
    onRoomChange: (String) -> Unit,
    onCourseChange: (String) -> Unit,
    onTeacherChange: (String) -> Unit,
    onDelete: () -> Unit,
    modifier: Modifier = Modifier
) {
    var typeMenuExpanded by remember { mutableStateOf(false) }
    val (badgeColor, labelColor) = entry.type.badgeColors()

    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        verticalArrangement = Arrangement.spacedBy(6.dp)
    ) {
        Row(
            horizontalArrangement = Arrangement.spacedBy(ROW_GAP),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box {
                Row(
                    modifier = Modifier
                        .width(TYPE_FIELD_WIDTH)
                        .height(36.dp)
                        .background(badgeColor, RoundedCornerShape(10.dp))
                        .clickable { typeMenuExpanded = true }
                        .padding(horizontal = 8.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(entry.type.label, color = labelColor, fontSize = 12.sp, fontWeight = FontWeight.SemiBold)
                    Icon(
                        imageVector = Icons.Outlined.KeyboardArrowDown,
                        contentDescription = null,
                        tint = labelColor,
                        modifier = Modifier.size(14.dp)
                    )
                }
                DropdownMenu(expanded = typeMenuExpanded, onDismissRequest = { typeMenuExpanded = false }) {
                    ClassType.entries.forEach { type ->
                        DropdownMenuItem(
                            text = { Text(type.label) },
                            onClick = {
                                onTypeChange(type)
                                typeMenuExpanded = false
                            }
                        )
                    }
                }
            }

            Row(
                modifier = Modifier
                    .weight(1f)
                    .height(36.dp)
                    .background(YwBackground, RoundedCornerShape(10.dp))
                    .border(1.dp, YwBorderSoft, RoundedCornerShape(10.dp))
                    .clickable(onClick = onTimeClick)
                    .padding(horizontal = 10.dp),
                horizontalArrangement = Arrangement.spacedBy(6.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = Icons.Outlined.Schedule,
                    contentDescription = null,
                    tint = YwTextSecondary,
                    modifier = Modifier.size(13.dp)
                )
                Text(entry.timeRangeLabel(), color = YwTextPrimary, fontSize = 12.sp)
            }

            Box(
                modifier = Modifier
                    .width(52.dp)
                    .height(36.dp)
                    .background(YwBackground, RoundedCornerShape(10.dp))
                    .border(1.dp, YwBorderSoft, RoundedCornerShape(10.dp)),
                contentAlignment = Alignment.Center
            ) {
                BasicTextField(
                    value = entry.room,
                    onValueChange = onRoomChange,
                    singleLine = true,
                    textStyle = TextStyle(color = YwTextPrimary, fontSize = 12.sp, textAlign = TextAlign.Center),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 4.dp)
                )
            }

            Box(
                modifier = Modifier
                    .size(28.dp)
                    .clickable(onClick = onDelete),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Outlined.DeleteOutline,
                    contentDescription = "삭제",
                    tint = YwTextSecondary,
                    modifier = Modifier.size(16.dp)
                )
            }
        }

        Row(
            horizontalArrangement = Arrangement.spacedBy(ROW_GAP),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Spacer(modifier = Modifier.width(TYPE_FIELD_WIDTH))
            CompactTextField(
                value = entry.course,
                onValueChange = onCourseChange,
                placeholder = "과목",
                modifier = Modifier.weight(1f)
            )
            CompactTextField(
                value = entry.teacher,
                onValueChange = onTeacherChange,
                placeholder = "교사",
                modifier = Modifier.weight(1f)
            )
        }
    }
}

@Composable
private fun CompactTextField(
    value: String,
    onValueChange: (String) -> Unit,
    placeholder: String,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .height(32.dp)
            .background(YwBackground, RoundedCornerShape(10.dp))
            .border(1.dp, YwBorderSoft, RoundedCornerShape(10.dp))
            .padding(horizontal = 10.dp),
        contentAlignment = Alignment.CenterStart
    ) {
        if (value.isEmpty()) {
            Text(text = placeholder, color = YwTextSecondary, fontSize = 12.sp)
        }
        BasicTextField(
            value = value,
            onValueChange = onValueChange,
            singleLine = true,
            textStyle = TextStyle(color = YwTextPrimary, fontSize = 12.sp),
            modifier = Modifier.fillMaxWidth()
        )
    }
}
