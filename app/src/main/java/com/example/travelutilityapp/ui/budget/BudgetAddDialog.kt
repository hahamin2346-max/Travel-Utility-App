package com.example.travelutilityapp.ui.budget

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.example.travelutilityapp.R

/**
 * Prompts for a new expense's description and amount, opened from the Budget screen's "+"
 * button. [currentCurrency] is whichever currency the screen's toggle is currently on; the
 * dialog surfaces a notice line so the user knows which unit they're entering in.
 */
@Composable
fun BudgetAddDialog(
    currentCurrency: Currency,
    onDismiss: () -> Unit,
    onConfirm: (description: String, amount: Long) -> Unit
) {
    var description by remember { mutableStateOf("") }
    var amountText by remember { mutableStateOf("") }

    val noticeRes = if (currentCurrency == Currency.PESO) {
        R.string.budget_add_notice_peso
    } else {
        R.string.budget_add_notice_krw
    }
    val amount = amountText.toLongOrNull()

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(stringResource(R.string.budget_add_title)) },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                Text(stringResource(noticeRes))
                OutlinedTextField(
                    value = description,
                    onValueChange = { description = it },
                    placeholder = { Text(stringResource(R.string.budget_desc_placeholder)) },
                    singleLine = true
                )
                OutlinedTextField(
                    value = amountText,
                    onValueChange = { input -> amountText = input.filter { it.isDigit() } },
                    placeholder = { Text(stringResource(R.string.budget_amount_placeholder)) },
                    singleLine = true,
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
                )
            }
        },
        confirmButton = {
            TextButton(
                onClick = { onConfirm(description.trim(), amount ?: 0L) },
                enabled = description.isNotBlank() && amount != null && amount > 0
            ) {
                Text(stringResource(R.string.budget_add_confirm))
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text(stringResource(R.string.action_cancel))
            }
        }
    )
}
