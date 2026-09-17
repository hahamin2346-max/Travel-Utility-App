package com.example.travelutilityapp.ui.budget

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Check
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.travelutilityapp.R
import com.example.travelutilityapp.ui.theme.YwBorderSoft
import com.example.travelutilityapp.ui.theme.YwPrimary
import com.example.travelutilityapp.ui.theme.YwSurface
import com.example.travelutilityapp.ui.theme.YwTextPrimary
import com.example.travelutilityapp.ui.theme.YwTextSecondary

/**
 * Lets the user pick exactly two of the supported currencies to pin onto the Budget
 * screen's display toggle. Picking a third evicts whichever was picked first, so the
 * selection always settles back to two without needing a disabled/blocked state.
 */
@Composable
fun CurrencySettingsDialog(
    currentPair: Pair<Currency, Currency>,
    onDismiss: () -> Unit,
    onConfirm: (Pair<Currency, Currency>) -> Unit
) {
    val selected = remember { mutableStateListOf(currentPair.first, currentPair.second) }

    fun toggle(currency: Currency) {
        if (selected.contains(currency)) {
            selected.remove(currency)
        } else {
            if (selected.size >= 2) selected.removeAt(0)
            selected.add(currency)
        }
    }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(stringResource(R.string.budget_settings_title)) },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                Text(
                    text = stringResource(R.string.budget_settings_description),
                    color = YwTextSecondary,
                    fontSize = 13.sp
                )
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .heightIn(max = 280.dp)
                        .verticalScroll(rememberScrollState())
                        .background(YwSurface, RoundedCornerShape(16.dp))
                        .border(1.dp, YwBorderSoft, RoundedCornerShape(16.dp))
                ) {
                    Currency.entries.forEachIndexed { index, currency ->
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable { toggle(currency) }
                                .padding(horizontal = 16.dp, vertical = 14.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(text = currency.displayName(), color = YwTextPrimary, fontSize = 14.sp)
                            if (selected.contains(currency)) {
                                Icon(
                                    imageVector = Icons.Outlined.Check,
                                    contentDescription = null,
                                    tint = YwPrimary,
                                    modifier = Modifier.size(18.dp)
                                )
                            }
                        }
                        if (index != Currency.entries.lastIndex) {
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
        },
        confirmButton = {
            TextButton(
                onClick = { onConfirm(selected[0] to selected[1]) },
                enabled = selected.size == 2
            ) {
                Text(stringResource(R.string.action_confirm))
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text(stringResource(R.string.action_cancel))
            }
        }
    )
}
