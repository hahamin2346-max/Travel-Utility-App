package com.example.travelutilityapp.ui.settings

import android.Manifest
import android.content.pm.PackageManager
import android.os.Build
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.ArrowBack
import androidx.compose.material.icons.outlined.Check
import androidx.compose.material3.Icon
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat
import com.example.travelutilityapp.R
import com.example.travelutilityapp.data.AppLanguage
import com.example.travelutilityapp.data.LanguagePreferences
import com.example.travelutilityapp.data.NotificationPreferences
import com.example.travelutilityapp.notification.ClassReminderScheduler
import com.example.travelutilityapp.ui.theme.YwBackground
import com.example.travelutilityapp.ui.theme.YwBorderSoft
import com.example.travelutilityapp.ui.theme.YwPrimary
import com.example.travelutilityapp.ui.theme.YwSurface
import com.example.travelutilityapp.ui.theme.YwTextPrimary
import com.example.travelutilityapp.ui.theme.YwTextSecondary
import androidx.compose.ui.tooling.preview.Preview
import com.example.travelutilityapp.ui.theme.TravelUtilityAppTheme

/** Home screen's gear icon opens this to let the user pick the app language and manage notifications. */
@Composable
fun SettingsScreen(
    onBack: () -> Unit,
    onLanguageChanged: () -> Unit,
    onRequestNotificationPermission: () -> Unit = {},
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val preferences = remember { LanguagePreferences(context) }
    var selected by remember { mutableStateOf(preferences.load()) }
    val notificationPreferences = remember { NotificationPreferences(context) }
    var reminderEnabled by remember { mutableStateOf(notificationPreferences.isReminderEnabled()) }
    val hasNotificationPermission = remember {
        Build.VERSION.SDK_INT < Build.VERSION_CODES.TIRAMISU ||
            ContextCompat.checkSelfPermission(context, Manifest.permission.POST_NOTIFICATIONS) ==
            PackageManager.PERMISSION_GRANTED
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(YwBackground)
            .padding(start = 20.dp, end = 20.dp, top = 12.dp),
        verticalArrangement = Arrangement.spacedBy(20.dp)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Box(
                modifier = Modifier
                    .size(38.dp)
                    .background(YwSurface, CircleShape)
                    .border(1.dp, YwBorderSoft, CircleShape)
                    .clickable(onClick = onBack),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.AutoMirrored.Outlined.ArrowBack,
                    contentDescription = stringResource(R.string.cd_back),
                    tint = YwTextSecondary,
                    modifier = Modifier.size(18.dp)
                )
            }
            Text(
                text = stringResource(R.string.settings_title),
                color = YwTextPrimary,
                fontSize = 20.sp,
                fontWeight = FontWeight.SemiBold
            )
        }

        Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
            Text(
                text = stringResource(R.string.settings_language_label),
                color = YwTextSecondary,
                fontSize = 13.sp,
                fontWeight = FontWeight.Medium
            )
            val options = listOf(
                AppLanguage.SYSTEM to stringResource(R.string.settings_language_system),
                AppLanguage.KOREAN to stringResource(R.string.settings_language_korean),
                AppLanguage.ENGLISH to stringResource(R.string.settings_language_english)
            )
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(YwSurface, RoundedCornerShape(16.dp))
                    .border(1.dp, YwBorderSoft, RoundedCornerShape(16.dp))
            ) {
                options.forEachIndexed { index, (language, label) ->
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable {
                                if (selected != language) {
                                    selected = language
                                    preferences.save(language)
                                    onLanguageChanged()
                                }
                            }
                            .padding(horizontal = 16.dp, vertical = 14.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(text = label, color = YwTextPrimary, fontSize = 14.sp)
                        if (selected == language) {
                            Icon(
                                imageVector = Icons.Outlined.Check,
                                contentDescription = null,
                                tint = YwPrimary,
                                modifier = Modifier.size(18.dp)
                            )
                        }
                    }
                    if (index != options.lastIndex) {
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

        Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
            Text(
                text = stringResource(R.string.settings_notification_label),
                color = YwTextSecondary,
                fontSize = 13.sp,
                fontWeight = FontWeight.Medium
            )
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(YwSurface, RoundedCornerShape(16.dp))
                    .border(1.dp, YwBorderSoft, RoundedCornerShape(16.dp))
                    .padding(horizontal = 16.dp, vertical = 14.dp),
                verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = stringResource(R.string.settings_notification_reminder_title),
                            color = YwTextPrimary,
                            fontSize = 14.sp
                        )
                        Text(
                            text = stringResource(R.string.settings_notification_reminder_subtitle),
                            color = YwTextSecondary,
                            fontSize = 12.sp
                        )
                    }
                    Switch(
                        checked = reminderEnabled,
                        onCheckedChange = { checked ->
                            reminderEnabled = checked
                            notificationPreferences.setReminderEnabled(checked)
                            if (checked && !hasNotificationPermission) {
                                onRequestNotificationPermission()
                            }
                            ClassReminderScheduler.reschedule(context)
                        },
                        colors = SwitchDefaults.colors(checkedTrackColor = YwPrimary)
                    )
                }
                if (reminderEnabled && !hasNotificationPermission) {
                    Text(
                        text = stringResource(R.string.settings_notification_permission_denied),
                        color = YwPrimary,
                        fontSize = 12.sp
                    )
                }
            }
        }
        Spacer(modifier = Modifier.weight(1f))

        Column{
            Text(
                text = "Travel Utility v1.0.0",
                color = YwTextSecondary,
                fontSize = 11.sp
            )
            Text(
                text = "Developed by Shin Hamin\n" +
                            "© 2026 Shin Hamin",
                color = YwTextSecondary,
                fontSize = 9.sp,
                lineHeight = 11.sp,
                modifier = Modifier.padding(bottom = 100 .dp)
            )
        }
    }
}
@Preview(showBackground = true, showSystemUi = true)
@Composable
fun SettingsScreenPreview() {
    TravelUtilityAppTheme {
        SettingsScreen(
            onBack = {},
            onLanguageChanged = {},
            onRequestNotificationPermission = {}
        )
    }
}