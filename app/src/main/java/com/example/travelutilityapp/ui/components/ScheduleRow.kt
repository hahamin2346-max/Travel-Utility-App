package com.example.travelutilityapp.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.LocationOn
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.travelutilityapp.R
import com.example.travelutilityapp.ui.theme.YwTextPrimary
import com.example.travelutilityapp.ui.theme.YwTextSecondary
import com.example.travelutilityapp.ui.theme.YwTextSecondaryStrong

private val TYPE_BADGE_WIDTH = 60.dp
private val ROW_GAP = 12.dp

/** Mirrors Pencil's reusable "Schedule Row" component (used on Home and Schedule screens). */
@Composable
fun ScheduleRow(
    typeLabel: String,
    typeBadgeColor: Color,
    typeLabelColor: Color,
    timeText: String,
    roomLabel: String,
    course: String = "",
    teacher: String = "",
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = 10.dp),
        verticalArrangement = Arrangement.spacedBy(2.dp)
    ) {
        Row(
            horizontalArrangement = Arrangement.spacedBy(ROW_GAP),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(width = TYPE_BADGE_WIDTH, height = 26.dp)
                    .background(typeBadgeColor, RoundedCornerShape(8.dp)),
                contentAlignment = Alignment.Center
            ) {
                Text(text = typeLabel, color = typeLabelColor, fontSize = 11.sp, fontWeight = FontWeight.SemiBold)
            }
            Text(
                text = timeText,
                color = YwTextPrimary,
                fontSize = 14.sp,
                fontWeight = FontWeight.Medium,
                textAlign = TextAlign.Center,
                modifier = Modifier.weight(1f)
            )
            Row(horizontalArrangement = Arrangement.spacedBy(4.dp), verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    imageVector = Icons.Outlined.LocationOn,
                    contentDescription = null,
                    tint = YwTextSecondary,
                    modifier = Modifier.size(12.dp)
                )
                Text(text = roomLabel, color = YwTextSecondary, fontSize = 12.sp)
            }
        }

        val courseText = course.takeIf { it.isNotBlank() }
            ?.let { stringResource(R.string.schedule_row_course_prefix, it) }
        val teacherText = teacher.takeIf { it.isNotBlank() }
            ?.let { stringResource(R.string.schedule_row_teacher_prefix, it) }
        val detailLine = listOfNotNull(courseText, teacherText).joinToString("   ")
        if (detailLine.isNotEmpty()) {
            Text(
                text = detailLine,
                color = YwTextSecondaryStrong,
                fontSize = 11.sp,
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth()
            )
        }
    }
}
