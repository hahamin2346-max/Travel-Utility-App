package com.example.travelutilityapp.ui.vocab

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
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
import androidx.compose.ui.unit.dp
import com.example.travelutilityapp.R

/** Prompts for a new word and its meaning, opened from the Vocab screen's "+" button. */
@Composable
fun VocabAddDialog(
    onDismiss: () -> Unit,
    onConfirm: (word: String, meaning: String) -> Unit
) {
    var word by remember { mutableStateOf("") }
    var meaning by remember { mutableStateOf("") }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(stringResource(R.string.vocab_add_title)) },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                OutlinedTextField(
                    value = word,
                    onValueChange = { word = it },
                    placeholder = { Text(stringResource(R.string.vocab_word_placeholder)) },
                    singleLine = true
                )
                OutlinedTextField(
                    value = meaning,
                    onValueChange = { meaning = it },
                    placeholder = { Text(stringResource(R.string.vocab_meaning_placeholder)) },
                    singleLine = true
                )
            }
        },
        confirmButton = {
            TextButton(
                onClick = { onConfirm(word.trim(), meaning.trim()) },
                enabled = word.isNotBlank() && meaning.isNotBlank()
            ) {
                Text(stringResource(R.string.vocab_add_confirm))
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text(stringResource(R.string.action_cancel))
            }
        }
    )
}
