package com.example.travelutilityapp.ui.home

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.EditCalendar
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.travelutilityapp.R
import com.example.travelutilityapp.ui.theme.YwBackground
import com.example.travelutilityapp.ui.theme.YwPrimary

/**
 * D-Day hero card shown at the top of the Home screen.
 * @param progress remaining-to-total ratio in [0f, 1f], drives the progress bar fill.
 */
@Composable
fun DDayHeroCard(
    dateRangeLabel: String,
    dDayLabel: String,
    weeksLeftLabel: String,
    progress: Float,
    onEditDatesClick: () -> Unit = {},
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .background(YwPrimary, RoundedCornerShape(24.dp))
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = stringResource(R.string.dday_label),
                color = Color.White.copy(alpha = 0.8f),
                fontSize = 13.sp,
                fontWeight = FontWeight.Medium
            )
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                Text(
                    text = dateRangeLabel,
                    color = Color.White.copy(alpha = 0.7f),
                    fontSize = 12.sp
                )
                Icon(
                    imageVector = Icons.Outlined.EditCalendar,
                    contentDescription = stringResource(R.string.cd_edit_period),
                    tint = Color.White.copy(alpha = 0.85f),
                    modifier = Modifier
                        .size(16.dp)
                        .clickable(onClick = onEditDatesClick)
                )
            }
        }

        Text(
            text = dDayLabel,
            color = Color.White,
            fontSize = 44.sp,
            fontWeight = FontWeight.Bold
        )

        Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
            Text(
                text = weeksLeftLabel,
                color = Color.White.copy(alpha = 0.9f),
                fontSize = 13.sp,
                fontWeight = FontWeight.Medium
            )
            Text(
                text = stringResource(R.string.dday_progress, (progress * 100).toInt()),
                color = Color.White.copy(alpha = 0.9f),
                fontSize = 13.sp,
                fontWeight = FontWeight.Medium
            )
        }

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(8.dp)
                .background(Color.White.copy(alpha = 0.2f), RoundedCornerShape(4.dp))
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth(progress.coerceIn(0f, 1f))
                    .height(8.dp)
                    .background(Color.White, RoundedCornerShape(4.dp))
            )
        }
    }
}

@Preview(showBackground = true, backgroundColor = 0xFFFBF6F1)
@Composable
private fun DDayHeroCardPreview() {
    Box(modifier = Modifier.background(YwBackground).padding(20.dp)) {
        DDayHeroCard(
            dateRangeLabel = "9.6 - 11.28",
            dDayLabel = "D-49",
            weeksLeftLabel = "7주 남음",
            progress = 0.42f
        )
    }
}
