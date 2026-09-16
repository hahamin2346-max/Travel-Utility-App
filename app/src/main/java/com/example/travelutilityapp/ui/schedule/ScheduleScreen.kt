package com.example.travelutilityapp.ui.schedule

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.CloudDownload
import androidx.compose.material.icons.outlined.Edit
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.travelutilityapp.R
import com.example.travelutilityapp.data.ScheduleRepository
import com.example.travelutilityapp.notification.ClassReminderScheduler
import com.example.travelutilityapp.ui.components.AppTab
import com.example.travelutilityapp.ui.components.AppTabBar
import com.example.travelutilityapp.ui.components.ScheduleRow
import com.example.travelutilityapp.ui.theme.TravelUtilityAppTheme
import com.example.travelutilityapp.ui.theme.YwBackground
import com.example.travelutilityapp.ui.theme.YwBorderSoft
import com.example.travelutilityapp.ui.theme.YwPrimary
import com.example.travelutilityapp.ui.theme.YwSurface
import com.example.travelutilityapp.ui.theme.YwTextPrimary
import com.example.travelutilityapp.ui.theme.YwTextSecondary

@Composable
fun ScheduleScreen(
    onNavigateHome: () -> Unit = {},
    onNavigateToChecklist: () -> Unit = {},
    onNavigateToVocab: () -> Unit = {},
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val repository = remember { ScheduleRepository(context) }

    var entries by remember { mutableStateOf(repository.load()) }
    var selectedTab by remember { mutableStateOf(AppTab.Schedule) }
    var showEditSheet by remember { mutableStateOf(false) }
    var showLmsImport by remember { mutableStateOf(false) }

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(YwBackground)
    ) {
        Column(modifier = Modifier.fillMaxSize()) {
            Column(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth()
                    .verticalScroll(rememberScrollState())
                    .padding(start = 20.dp, end = 20.dp, top = 12.dp),
                verticalArrangement = Arrangement.spacedBy(18.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column(verticalArrangement = Arrangement.spacedBy(2.dp)) {
                        Text(
                            text = stringResource(R.string.nav_schedule),
                            color = YwTextPrimary,
                            fontSize = 20.sp,
                            fontWeight = FontWeight.SemiBold
                        )
                        Text(
                            text = stringResource(R.string.schedule_screen_subtitle),
                            color = YwTextSecondary,
                            fontSize = 13.sp
                        )
                    }
                    Box(
                        modifier = Modifier
                            .size(38.dp)
                            .background(YwSurface, CircleShape)
                            .border(1.dp, YwBorderSoft, CircleShape)
                            .clickable { showLmsImport = true },
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Outlined.CloudDownload,
                            contentDescription = stringResource(R.string.cd_lms_import),
                            tint = YwTextSecondary,
                            modifier = Modifier.size(18.dp)
                        )
                    }
                }

                if (entries.isEmpty()) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(YwSurface, RoundedCornerShape(20.dp))
                            .border(1.dp, YwBorderSoft, RoundedCornerShape(20.dp))
                            .padding(24.dp),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        Text(
                            text = stringResource(R.string.schedule_screen_empty_title),
                            color = YwTextSecondary,
                            fontSize = 13.sp
                        )
                        Text(
                            text = stringResource(R.string.schedule_screen_empty_subtitle),
                            color = YwTextSecondary,
                            fontSize = 12.sp
                        )
                    }
                } else {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(YwSurface, RoundedCornerShape(20.dp))
                            .border(1.dp, YwBorderSoft, RoundedCornerShape(20.dp))
                            .padding(horizontal = 16.dp)
                    ) {
                        entries.forEachIndexed { index, entry ->
                            val (badgeColor, labelColor) = entry.type.badgeColors()
                            ScheduleRow(
                                typeLabel = entry.type.label,
                                typeBadgeColor = badgeColor,
                                typeLabelColor = labelColor,
                                timeText = entry.timeRangeLabel(),
                                roomLabel = entry.room,
                                course = entry.course,
                                teacher = entry.teacher
                            )
                            if (index != entries.lastIndex) {
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
            }
            AppTabBar(
                selectedTab = selectedTab,
                onTabSelected = { tab ->
                    selectedTab = tab
                    when (tab) {
                        AppTab.Home -> onNavigateHome()
                        AppTab.Checklist -> onNavigateToChecklist()
                        AppTab.Vocab -> onNavigateToVocab()
                        else -> {}
                    }
                },
                modifier = Modifier.padding(horizontal = 16.dp, vertical = 12.dp)
            )
        }

        Box(
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(end = 20.dp, bottom = 88.dp)
                .size(56.dp)
                .background(YwPrimary, CircleShape)
                .clickable { showEditSheet = true },
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = Icons.Outlined.Edit,
                contentDescription = stringResource(R.string.cd_edit_schedule),
                tint = Color.White,
                modifier = Modifier.size(22.dp)
            )
        }
    }

    if (showEditSheet) {
        ScheduleEditSheet(
            entries = entries,
            onDismiss = { showEditSheet = false },
            onSave = { updated ->
                entries = updated
                repository.save(updated)
                ClassReminderScheduler.reschedule(context)
                showEditSheet = false
            }
        )
    }

    if (showLmsImport) {
        LmsImportDialog(
            onDismiss = { showLmsImport = false },
            onImported = { imported ->
                entries = imported
                repository.save(imported)
                ClassReminderScheduler.reschedule(context)
                showLmsImport = false
            }
        )
    }
}

@Preview(showBackground = true, widthDp = 360, heightDp = 780)
@Composable
private fun ScheduleScreenPreview() {
    TravelUtilityAppTheme {
        ScheduleScreen()
    }
}
