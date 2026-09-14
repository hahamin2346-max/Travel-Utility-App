package com.example.travelutilityapp.ui.home

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Alarm
import androidx.compose.material.icons.outlined.EventAvailable
import androidx.compose.material.icons.outlined.EventBusy
import androidx.compose.material.icons.outlined.Weekend
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.travelutilityapp.R
import com.example.travelutilityapp.ui.components.ScheduleRow
import com.example.travelutilityapp.ui.schedule.badgeColors
import com.example.travelutilityapp.ui.schedule.timeRangeLabel
import com.example.travelutilityapp.ui.theme.YwBorderSoft
import com.example.travelutilityapp.ui.theme.YwPrimary
import com.example.travelutilityapp.ui.theme.YwSurface
import com.example.travelutilityapp.ui.theme.YwTextSecondary

@Composable
fun NextClassPanel(
    status: NextClassStatus,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .background(YwSurface, RoundedCornerShape(20.dp))
            .border(1.dp, YwBorderSoft, RoundedCornerShape(20.dp))
            .padding(horizontal = 16.dp, vertical = 14.dp),
        verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        when (status) {
            is NextClassStatus.Weekend ->
                StatusHeadline(
                    icon = Icons.Outlined.Weekend,
                    text = stringResource(R.string.next_class_weekend),
                    tint = YwTextSecondary
                )

            is NextClassStatus.NoClassesRegistered ->
                StatusHeadline(
                    icon = Icons.Outlined.EventBusy,
                    text = stringResource(R.string.next_class_none_registered),
                    tint = YwTextSecondary
                )

            is NextClassStatus.FinishedForToday ->
                StatusHeadline(
                    icon = Icons.Outlined.EventAvailable,
                    text = stringResource(R.string.next_class_finished_today),
                    tint = YwTextSecondary
                )

            is NextClassStatus.InClass -> {
                StatusHeadline(
                    icon = Icons.Outlined.Alarm,
                    text = stringResource(R.string.next_class_in_class),
                    tint = YwPrimary
                )
                Divider()
                val (badgeColor, labelColor) = status.entry.type.badgeColors()
                ScheduleRow(
                    typeLabel = status.entry.type.label,
                    typeBadgeColor = badgeColor,
                    typeLabelColor = labelColor,
                    timeText = status.entry.timeRangeLabel(),
                    roomLabel = status.entry.room,
                    course = status.entry.course,
                    teacher = status.entry.teacher
                )
            }

            is NextClassStatus.Upcoming -> {
                StatusHeadline(
                    icon = Icons.Outlined.Alarm,
                    text = stringResource(R.string.next_class_starts_in, status.minutesUntilStart),
                    tint = YwPrimary
                )
                Divider()
                val (badgeColor, labelColor) = status.entry.type.badgeColors()
                ScheduleRow(
                    typeLabel = status.entry.type.label,
                    typeBadgeColor = badgeColor,
                    typeLabelColor = labelColor,
                    timeText = status.entry.timeRangeLabel(),
                    roomLabel = status.entry.room,
                    course = status.entry.course,
                    teacher = status.entry.teacher
                )
            }
        }
    }
}

@Composable
private fun StatusHeadline(icon: ImageVector, text: String, tint: Color) {
    Row(
        horizontalArrangement = Arrangement.spacedBy(6.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(imageVector = icon, contentDescription = null, tint = tint, modifier = Modifier.size(14.dp))
        Text(text = text, color = tint, fontSize = 13.sp, fontWeight = FontWeight.SemiBold)
    }
}

@Composable
private fun Divider() {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(1.dp)
            .background(YwBorderSoft)
    )
}
