package com.example.travelutilityapp.ui.vocab

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.MenuBook
import androidx.compose.material.icons.outlined.SentimentDissatisfied
import androidx.compose.material.icons.outlined.WorkspacePremium
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.travelutilityapp.R
import com.example.travelutilityapp.ui.theme.YwAccentGold
import com.example.travelutilityapp.ui.theme.YwAccentGoldSoft
import com.example.travelutilityapp.ui.theme.YwPrimary
import com.example.travelutilityapp.ui.theme.YwPrimarySoft
import com.example.travelutilityapp.ui.theme.YwSurface
import com.example.travelutilityapp.ui.theme.YwTextPrimary
import com.example.travelutilityapp.ui.theme.YwTextSecondary

/** Bottom sheet opened from the Vocab screen's "시작하기" button: pick which word pool to test. */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun VocabTestSourceSheet(
    onDismiss: () -> Unit,
    onSelect: (VocabTestSource) -> Unit,
    modifier: Modifier = Modifier
) {
    val sheetState = rememberModalBottomSheetState()

    ModalBottomSheet(
        onDismissRequest = onDismiss,
        sheetState = sheetState,
        containerColor = YwSurface,
        modifier = modifier
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp)
                .padding(bottom = 28.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Column(verticalArrangement = Arrangement.spacedBy(2.dp)) {
                Text(
                    text = stringResource(R.string.vocab_test_source_title),
                    color = YwTextPrimary,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.SemiBold
                )
                Text(
                    text = stringResource(R.string.vocab_test_source_subtitle),
                    color = YwTextSecondary,
                    fontSize = 13.sp
                )
            }

            VocabTestSourceOptionRow(
                icon = Icons.AutoMirrored.Outlined.MenuBook,
                iconTint = YwPrimary,
                iconBackground = YwPrimarySoft,
                title = stringResource(R.string.vocab_test_source_my_title),
                subtitle = stringResource(R.string.vocab_test_source_my_subtitle),
                onClick = { onSelect(VocabTestSource.MY) }
            )
            VocabTestSourceOptionRow(
                icon = Icons.Outlined.SentimentDissatisfied,
                iconTint = YwAccentGold,
                iconBackground = YwAccentGoldSoft,
                title = stringResource(R.string.vocab_test_source_dont_know_title),
                subtitle = stringResource(R.string.vocab_test_source_dont_know_subtitle),
                onClick = { onSelect(VocabTestSource.DONT_KNOW) }
            )
            VocabTestSourceOptionRow(
                icon = Icons.Outlined.WorkspacePremium,
                iconTint = YwPrimary,
                iconBackground = YwPrimarySoft,
                title = stringResource(R.string.vocab_test_source_cefr_title),
                subtitle = stringResource(R.string.vocab_test_source_cefr_subtitle),
                onClick = { onSelect(VocabTestSource.CEFR) }
            )
        }
    }
}
