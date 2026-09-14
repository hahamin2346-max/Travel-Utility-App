package com.example.travelutilityapp.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.MenuBook
import androidx.compose.material.icons.outlined.AccountBalanceWallet
import androidx.compose.material.icons.outlined.CalendarMonth
import androidx.compose.material.icons.outlined.CheckBox
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.travelutilityapp.ui.theme.YwBorderSoft
import com.example.travelutilityapp.ui.theme.YwPrimary
import com.example.travelutilityapp.ui.theme.YwPrimarySoft
import com.example.travelutilityapp.ui.theme.YwTextSecondary

/** The 5 top-level destinations of the app, shared by every screen's bottom tab bar. */
enum class AppTab(val label: String, val icon: ImageVector) {
    Home("홈", Icons.Outlined.Home),
    Schedule("시간표", Icons.Outlined.CalendarMonth),
    Checklist("체크리스트", Icons.Outlined.CheckBox),
    Vocab("단어장", Icons.AutoMirrored.Outlined.MenuBook),
    Budget("가계부", Icons.Outlined.AccountBalanceWallet)
}

@Composable
fun AppTabBar(
    selectedTab: AppTab,
    onTabSelected: (AppTab) -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .height(56.dp)
            .background(Color(0xB3FFFFFF), RoundedCornerShape(28.dp))
            .border(1.dp, YwBorderSoft, RoundedCornerShape(28.dp))
            .padding(6.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        AppTab.entries.forEach { tab ->
            val selected = tab == selectedTab
            Column(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxHeight()
                    .clip(RoundedCornerShape(22.dp))
                    .background(if (selected) YwPrimarySoft else Color.Transparent)
                    .clickable { onTabSelected(tab) },
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Icon(
                    imageVector = tab.icon,
                    contentDescription = tab.label,
                    tint = if (selected) YwPrimary else YwTextSecondary,
                    modifier = Modifier.size(18.dp)
                )
                Text(
                    text = tab.label,
                    color = if (selected) YwPrimary else YwTextSecondary,
                    fontSize = 9.sp,
                    fontWeight = if (selected) FontWeight.SemiBold else FontWeight.Normal
                )
            }
        }
    }
}
