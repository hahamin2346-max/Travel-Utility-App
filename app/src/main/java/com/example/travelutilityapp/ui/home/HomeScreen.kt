package com.example.travelutilityapp.ui.home

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.Saver
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.travelutilityapp.R
import com.example.travelutilityapp.data.ScheduleRepository
import com.example.travelutilityapp.data.TrainingPeriodPreferences
import com.example.travelutilityapp.ui.components.AppTab
import com.example.travelutilityapp.ui.components.AppTabBar
import com.example.travelutilityapp.ui.theme.TravelUtilityAppTheme
import com.example.travelutilityapp.ui.theme.YwBackground
import kotlinx.coroutines.delay
import java.time.LocalDate
import java.time.LocalDateTime
import kotlin.math.ceil

private val LocalDateSaver = Saver<LocalDate, Long>(
    save = { it.toEpochDay() },
    restore = { LocalDate.ofEpochDay(it) }
)

@Composable
fun HomeScreen(
    onNavigateToSchedule: () -> Unit = {},
    onNavigateToChecklist: () -> Unit = {},
    onNavigateToVocab: () -> Unit = {},
    onNavigateToBudget: () -> Unit = {},
    onNavigateToSettings: () -> Unit = {},
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val preferences = remember { TrainingPeriodPreferences(context) }
    val savedPeriod = remember { preferences.load() }
    val scheduleEntries = remember { ScheduleRepository(context).load() }
    val today = remember { LocalDate.now() }
    val defaultStart = remember { LocalDate.of(today.year, 9, 6) }
    val defaultEnd = remember { LocalDate.of(today.year, 11, 28) }

    var startDate by rememberSaveable(stateSaver = LocalDateSaver) {
        mutableStateOf(savedPeriod?.first ?: defaultStart)
    }
    var endDate by rememberSaveable(stateSaver = LocalDateSaver) {
        mutableStateOf(savedPeriod?.second ?: defaultEnd)
    }
    var showDatePicker by rememberSaveable { mutableStateOf(false) }
    var selectedTab by remember { mutableStateOf(AppTab.Home) }

    val stats = remember(startDate, endDate) { computeTrainingPeriodStats(startDate, endDate) }

    var now by remember { mutableStateOf(LocalDateTime.now()) }
    LaunchedEffect(Unit) {
        while (true) {
            delay(30_000)
            now = LocalDateTime.now()
        }
    }
    val nextClassStatus = remember(scheduleEntries, now) { computeNextClassStatus(scheduleEntries, now) }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(YwBackground)
    ) {
        Column(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
                .verticalScroll(rememberScrollState())
                .padding(start = 20.dp, end = 20.dp, top = 12.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            HomeHeader(
                greeting = stringResource(R.string.home_greeting),
                subtitle = stringResource(R.string.home_subtitle),
                onSettingsClick = onNavigateToSettings
            )
            val dDayLabel = when {
                stats.remainingDays > 0 -> "D-${stats.remainingDays}"
                stats.remainingDays == 0L -> "D-DAY"
                else -> stringResource(R.string.dday_finished)
            }
            val weeksLeftLabel = when {
                stats.remainingDays > 0 ->
                    stringResource(R.string.dday_weeks_left, ceil(stats.remainingDays / 7.0).toInt())
                stats.remainingDays == 0L -> stringResource(R.string.dday_today)
                else -> stringResource(R.string.dday_finished)
            }
            DDayHeroCard(
                dateRangeLabel = stats.dateRangeLabel,
                dDayLabel = dDayLabel,
                weeksLeftLabel = weeksLeftLabel,
                progress = stats.progress,
                onEditDatesClick = { showDatePicker = true }
            )
            NextClassPanel(status = nextClassStatus)
            FeatureGrid(
                onScheduleClick = onNavigateToSchedule,
                onChecklistClick = onNavigateToChecklist,
                onVocabClick = onNavigateToVocab,
                onBudgetClick = onNavigateToBudget
            )
        }
        AppTabBar(
            selectedTab = selectedTab,
            onTabSelected = { tab ->
                selectedTab = tab
                when (tab) {
                    AppTab.Schedule -> onNavigateToSchedule()
                    AppTab.Checklist -> onNavigateToChecklist()
                    AppTab.Vocab -> onNavigateToVocab()
                    AppTab.Budget -> onNavigateToBudget()
                    else -> {}
                }
            },
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 12.dp)
        )
    }

    if (showDatePicker) {
        TrainingPeriodPickerDialog(
            initialStart = startDate,
            initialEnd = endDate,
            onDismiss = { showDatePicker = false },
            onConfirm = { newStart, newEnd ->
                startDate = newStart
                endDate = newEnd
                preferences.save(newStart, newEnd)
                showDatePicker = false
            }
        )
    }
}

@Preview(showBackground = true, widthDp = 360, heightDp = 780)
@Composable
private fun HomeScreenPreview() {
    TravelUtilityAppTheme {
        HomeScreen()
    }
}
