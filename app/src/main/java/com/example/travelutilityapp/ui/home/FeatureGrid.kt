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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.travelutilityapp.R
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
                title = stringResource(R.string.nav_checklist),
                subtitle = stringResource(R.string.feature_checklist_subtitle),
                onClick = onChecklistClick,
                modifier = Modifier.weight(1f)
            )
            FeatureCard(
                icon = Icons.AutoMirrored.Outlined.MenuBook,
                iconBackgroundColor = YwAccentGoldSoft,
                iconTint = YwAccentGold,
                title = stringResource(R.string.nav_vocab),
                subtitle = stringResource(R.string.feature_vocab_subtitle),
                onClick = onVocabClick,
                modifier = Modifier.weight(1f)
            )
        }
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(12.dp)) {
            FeatureCard(
                icon = Icons.Outlined.AccountBalanceWallet,
                iconBackgroundColor = YwAccentGoldSoft,
                iconTint = YwAccentGold,
                title = stringResource(R.string.nav_budget),
                subtitle = stringResource(R.string.feature_budget_subtitle),
                onClick = onBudgetClick,
                modifier = Modifier.weight(1f)
            )
            FeatureCard(
                icon = Icons.Outlined.CalendarMonth,
                iconBackgroundColor = YwPrimarySoft,
                iconTint = YwPrimary,
                title = stringResource(R.string.nav_schedule),
                subtitle = stringResource(R.string.feature_schedule_subtitle),
                onClick = onScheduleClick,
                modifier = Modifier.weight(1f)
            )
        }
    }
}
