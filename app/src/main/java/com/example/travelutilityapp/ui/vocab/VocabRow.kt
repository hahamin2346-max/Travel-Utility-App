package com.example.travelutilityapp.ui.vocab

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.KeyboardArrowRight
import androidx.compose.material.icons.outlined.Delete
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.travelutilityapp.R
import com.example.travelutilityapp.ui.theme.YwBorderSoft
import com.example.travelutilityapp.ui.theme.YwTextPrimary
import com.example.travelutilityapp.ui.theme.YwTextSecondary
import androidx.compose.ui.tooling.preview.Preview
import com.example.travelutilityapp.ui.theme.TravelUtilityAppTheme

/**
 * Mirrors Pencil's reusable "Vocab Row" component. Tapping a row fires [onClick]; when
 * [onDelete] is provided a trailing trash-can button is shown in place of the chevron so the
 * row supports delete-only (no edit) interaction.
 */
@Composable
fun VocabRow(
    word: VocabWord,
    onClick: () -> Unit = {},
    onDelete: (() -> Unit)? = null,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .padding(vertical = 12.dp, horizontal = 4.dp),
        horizontalArrangement = Arrangement.spacedBy(12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(
            modifier = Modifier.weight(1f),
            verticalArrangement = Arrangement.spacedBy(2.dp)
        ) {
            Text(text = word.word, color = YwTextPrimary, fontSize = 15.sp, fontWeight = FontWeight.SemiBold)
            Text(text = word.meaning, color = YwTextSecondary, fontSize = 12.sp)
        }
        if (onDelete != null) {
            Icon(
                imageVector = Icons.Outlined.Delete,
                contentDescription = stringResource(R.string.cd_delete),
                tint = YwTextSecondary,
                modifier = Modifier
                    .clickable(onClick = onDelete)
                    .size(18.dp)
            )
        } else {
            Icon(
                imageVector = Icons.AutoMirrored.Outlined.KeyboardArrowRight,
                contentDescription = null,
                tint = YwBorderSoft,
                modifier = Modifier.size(16.dp)
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun VocabRowDeletePreview() {
    TravelUtilityAppTheme {
        VocabRow(
            word = VocabWord(
                word = "attendance",
                meaning = "출석"
            )

        )
    }
}