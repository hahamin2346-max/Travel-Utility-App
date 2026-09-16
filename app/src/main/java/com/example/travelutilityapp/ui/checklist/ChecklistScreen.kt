package com.example.travelutilityapp.ui.checklist

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
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.travelutilityapp.R
import com.example.travelutilityapp.data.ChecklistRepository
import com.example.travelutilityapp.ui.components.AppTab
import com.example.travelutilityapp.ui.components.AppTabBar
import com.example.travelutilityapp.ui.components.SegmentedControl
import com.example.travelutilityapp.ui.theme.YwBackground
import com.example.travelutilityapp.ui.theme.YwBorderSoft
import com.example.travelutilityapp.ui.theme.YwPrimary
import com.example.travelutilityapp.ui.theme.YwSurface
import com.example.travelutilityapp.ui.theme.YwTextPrimary
import com.example.travelutilityapp.ui.theme.YwTextSecondary

@Composable
fun ChecklistScreen(
    onNavigateHome: () -> Unit = {},
    onNavigateToSchedule: () -> Unit = {},
    onNavigateToVocab: () -> Unit = {},
    onNavigateToBudget: () -> Unit = {},
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val repository = remember { ChecklistRepository(context) }

    var selectedType by rememberSaveable { mutableStateOf(ChecklistType.TODAY) }
    var todayItems by remember { mutableStateOf(repository.load(ChecklistType.TODAY)) }
    var afterReturnItems by remember { mutableStateOf(repository.load(ChecklistType.AFTER_RETURN)) }
    var selectedTab by remember { mutableStateOf(AppTab.Checklist) }
    var showAddDialog by remember { mutableStateOf(false) }
    var editingItem by remember { mutableStateOf<ChecklistItem?>(null) }

    val currentItems = if (selectedType == ChecklistType.TODAY) todayItems else afterReturnItems

    fun updateCurrentItems(update: (List<ChecklistItem>) -> List<ChecklistItem>) {
        val updated = update(currentItems)
        if (selectedType == ChecklistType.TODAY) todayItems = updated else afterReturnItems = updated
        repository.save(selectedType, updated)
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
                    .padding(start = 20.dp, end = 20.dp, top = 12.dp),
                verticalArrangement = Arrangement.spacedBy(20.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column(verticalArrangement = Arrangement.spacedBy(2.dp)) {
                        Text(
                            text = stringResource(R.string.nav_checklist),
                            color = YwTextPrimary,
                            fontSize = 20.sp,
                            fontWeight = FontWeight.SemiBold
                        )
                        Text(
                            text = stringResource(R.string.checklist_subtitle),
                            color = YwTextSecondary,
                            fontSize = 13.sp
                        )
                    }
                    Box(
                        modifier = Modifier
                            .size(38.dp)
                            .background(YwPrimary, CircleShape)
                            .clickable { showAddDialog = true },
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Outlined.Add,
                            contentDescription = stringResource(R.string.cd_add_item),
                            tint = Color.White,
                            modifier = Modifier.size(18.dp)
                        )
                    }
                }

                val segmentLabels = listOf(
                    stringResource(R.string.checklist_segment_today),
                    stringResource(R.string.checklist_segment_after_return)
                )
                SegmentedControl(
                    options = segmentLabels,
                    selectedIndex = if (selectedType == ChecklistType.TODAY) 0 else 1,
                    onSelect = { index ->
                        selectedType = if (index == 0) ChecklistType.TODAY else ChecklistType.AFTER_RETURN
                    }
                )

                if (currentItems.isEmpty()) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(YwSurface, RoundedCornerShape(20.dp))
                            .border(1.dp, YwBorderSoft, RoundedCornerShape(20.dp))
                            .padding(24.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(text = stringResource(R.string.checklist_empty), color = YwTextSecondary, fontSize = 13.sp)
                    }
                } else {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(YwSurface, RoundedCornerShape(20.dp))
                            .border(1.dp, YwBorderSoft, RoundedCornerShape(20.dp))
                            .padding(horizontal = 16.dp)
                    ) {
                        currentItems.forEachIndexed { index, item ->
                            ChecklistRow(
                                item = item,
                                onToggleChecked = {
                                    updateCurrentItems { list ->
                                        list.map { if (it.id == item.id) it.copy(isChecked = !it.isChecked) else it }
                                    }
                                },
                                onEditClick = { editingItem = item }
                            )
                            if (index != currentItems.lastIndex) {
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
                        AppTab.Vocab -> onNavigateToVocab()
                        AppTab.Budget -> onNavigateToBudget()
                        else -> {}
                    }
                },
                modifier = Modifier.padding(horizontal = 16.dp, vertical = 12.dp)
            )
        }
    }

    if (showAddDialog) {
        ChecklistAddDialog(
            onDismiss = { showAddDialog = false },
            onConfirm = { text ->
                updateCurrentItems { list -> list + ChecklistItem(content = text) }
                showAddDialog = false
            }
        )
    }

    editingItem?.let { item ->
        ChecklistEditDialog(
            item = item,
            onDismiss = { editingItem = null },
            onSave = { newText ->
                updateCurrentItems { list ->
                    list.map { if (it.id == item.id) it.copy(content = newText) else it }
                }
                editingItem = null
            },
            onDelete = {
                updateCurrentItems { list -> list.filterNot { it.id == item.id } }
                editingItem = null
            }
        )
    }
}
