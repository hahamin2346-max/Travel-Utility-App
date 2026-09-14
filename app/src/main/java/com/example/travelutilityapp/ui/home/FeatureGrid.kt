package com.example.travelutilityapp.ui.home

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.MenuBook
import androidx.compose.material.icons.outlined.AccountBalanceWallet
import androidx.compose.material.icons.outlined.CalendarMonth
import androidx.compose.material.icons.outlined.CheckBox
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.travelutilityapp.ui.theme.YwAccentGold
import com.example.travelutilityapp.ui.theme.YwAccentGoldSoft
import com.example.travelutilityapp.ui.theme.YwPrimary
import com.example.travelutilityapp.ui.theme.YwPrimarySoft

@Composable
fun FeatureGrid(
    onChecklistClick: () -> Unit = {},
    onVocabClick: () -> Unit = {},
    onBudgetClick: () -> Unit = {},
    onScheduleClick: () -> Unit = {},
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier.fillMaxWidth(), verticalArrangement = Arrangement.spacedBy(12.dp)) {
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(12.dp)) {
            FeatureCard(
                icon = Icons.Outlined.CheckBox,
                iconBackgroundColor = YwPrimarySoft,
                iconTint = YwPrimary,
                title = "체크리스트",
                subtitle = "오늘 할 일 3개",
                onClick = onChecklistClick,
                modifier = Modifier.weight(1f)
            )
            FeatureCard(
                icon = Icons.AutoMirrored.Outlined.MenuBook,
                iconBackgroundColor = YwAccentGoldSoft,
                iconTint = YwAccentGold,
                title = "단어장",
                subtitle = "복습 카드 12개",
                onClick = onVocabClick,
                modifier = Modifier.weight(1f)
            )
        }
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(12.dp)) {
            FeatureCard(
                icon = Icons.Outlined.AccountBalanceWallet,
                iconBackgroundColor = YwAccentGoldSoft,
                iconTint = YwAccentGold,
                title = "가계부",
                subtitle = "이번 주 42만원",
                onClick = onBudgetClick,
                modifier = Modifier.weight(1f)
            )
            FeatureCard(
                icon = Icons.Outlined.CalendarMonth,
                iconBackgroundColor = YwPrimarySoft,
                iconTint = YwPrimary,
                title = "시간표",
                subtitle = "다음 수업 09:00",
                onClick = onScheduleClick,
                modifier = Modifier.weight(1f)
            )
        }
    }
}
