package com.example.travelutilityapp.ui.budget

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
import androidx.compose.material.icons.outlined.Add
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
import com.example.travelutilityapp.data.BudgetRepository
import com.example.travelutilityapp.ui.components.AppTab
import com.example.travelutilityapp.ui.components.AppTabBar
import com.example.travelutilityapp.ui.theme.TravelUtilityAppTheme
import com.example.travelutilityapp.ui.theme.YwBackground
import com.example.travelutilityapp.ui.theme.YwBorderSoft
import com.example.travelutilityapp.ui.theme.YwPrimary
import com.example.travelutilityapp.ui.theme.YwSurface
import com.example.travelutilityapp.ui.theme.YwTextPrimary
import com.example.travelutilityapp.ui.theme.YwTextSecondary

@Composable
fun BudgetScreen(
    onNavigateHome: () -> Unit = {},
    onNavigateToSchedule: () -> Unit = {},
    onNavigateToChecklist: () -> Unit = {},
    onNavigateToVocab: () -> Unit = {},
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val repository = remember { BudgetRepository(context) }

    var entries by remember { mutableStateOf(repository.load()) }
    var displayCurrency by remember { mutableStateOf(Currency.KRW) }
    var showAddDialog by remember { mutableStateOf(false) }
    var selectedTab by remember { mutableStateOf(AppTab.Budget) }

    fun addEntry(description: String, amount: Long) {
        val updated = entries + ExpenseEntry(description = description, amount = amount, currency = displayCurrency)
        entries = updated
        repository.save(updated)
    }

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
                    .padding(start = 20.dp, end = 20.dp, top = 4.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Column(verticalArrangement = Arrangement.spacedBy(2.dp)) {
                    Text(
                        text = stringResource(R.string.nav_budget),
                        color = YwTextPrimary,
                        fontSize = 20.sp,
                        fontWeight = FontWeight.SemiBold
                    )
                    Text(
                        text = stringResource(R.string.budget_screen_subtitle),
                        color = YwTextSecondary,
                        fontSize = 13.sp
                    )
                }

                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(YwPrimary, RoundedCornerShape(20.dp))
                        .padding(18.dp),
                    verticalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = stringResource(R.string.budget_total_label),
                            color = Color.White.copy(alpha = 0.8f),
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Medium
                        )
                        Row(
                            modifier = Modifier
                                .background(Color.White.copy(alpha = 0.2f), RoundedCornerShape(11.dp))
                                .padding(3.dp),
                            horizontalArrangement = Arrangement.spacedBy(2.dp)
                        ) {
                            CurrencySegment(
                                label = stringResource(R.string.budget_currency_peso),
                                selected = displayCurrency == Currency.PESO,
                                onClick = { displayCurrency = Currency.PESO }
                            )
                            CurrencySegment(
                                label = stringResource(R.string.budget_currency_krw),
                                selected = displayCurrency == Currency.KRW,
                                onClick = { displayCurrency = Currency.KRW }
                            )
                        }
                    }
                    Text(
                        text = formatAmount(entries.totalIn(displayCurrency), displayCurrency),
                        color = Color.White,
                        fontSize = 34.sp,
                        fontWeight = FontWeight.Bold
                    )
                }

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = stringResource(R.string.budget_section_title),
                        color = YwTextPrimary,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.SemiBold
                    )
                    Box(
                        modifier = Modifier
                            .size(28.dp)
                            .background(YwPrimary, CircleShape)
                            .clickable { showAddDialog = true },
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Outlined.Add,
                            contentDescription = stringResource(R.string.cd_add_expense),
                            tint = Color.White,
                            modifier = Modifier.size(13.dp)
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
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(text = stringResource(R.string.budget_empty), color = YwTextSecondary, fontSize = 13.sp)
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
                            ExpenseRow(
                                date = entry.dateLabel,
                                description = entry.description,
                                price = formatAmount(entry.amountIn(displayCurrency), displayCurrency)
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
                        AppTab.Schedule -> onNavigateToSchedule()
                        AppTab.Checklist -> onNavigateToChecklist()
                        AppTab.Vocab -> onNavigateToVocab()
                        else -> {}
                    }
                },
                modifier = Modifier.padding(horizontal = 16.dp, vertical = 12.dp)
            )
        }
    }

    if (showAddDialog) {
        BudgetAddDialog(
            currentCurrency = displayCurrency,
            onDismiss = { showAddDialog = false },
            onConfirm = { description, amount ->
                addEntry(description, amount)
                showAddDialog = false
            }
        )
    }
}

@Composable
private fun CurrencySegment(label: String, selected: Boolean, onClick: () -> Unit) {
    Box(
        modifier = Modifier
            .background(if (selected) Color.White else Color.Transparent, RoundedCornerShape(8.dp))
            .clickable(onClick = onClick)
            .padding(horizontal = 10.dp, vertical = 6.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = label,
            color = if (selected) YwPrimary else Color.White.copy(alpha = 0.8f),
            fontSize = 10.sp,
            fontWeight = FontWeight.SemiBold
        )
    }
}

@Preview(showBackground = true, widthDp = 360, heightDp = 780)
@Composable
private fun BudgetScreenPreview() {
    TravelUtilityAppTheme {
        BudgetScreen()
    }
}
