package com.example.travelutilityapp

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.ui.Modifier
import androidx.core.content.ContextCompat
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.travelutilityapp.data.withAppLanguage
import com.example.travelutilityapp.notification.ClassReminderScheduler
import com.example.travelutilityapp.ui.checklist.ChecklistScreen
import com.example.travelutilityapp.ui.home.HomeScreen
import com.example.travelutilityapp.ui.navigation.AppRoutes
import com.example.travelutilityapp.ui.schedule.ScheduleScreen
import com.example.travelutilityapp.ui.settings.SettingsScreen
import com.example.travelutilityapp.ui.theme.TravelUtilityAppTheme
import com.example.travelutilityapp.ui.vocab.VocabScreen
import com.example.travelutilityapp.ui.vocab.VocabTestScreen
import com.example.travelutilityapp.ui.vocab.VocabTestSource

class MainActivity : ComponentActivity() {
    private val requestNotificationPermission =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) {
            ClassReminderScheduler.reschedule(this)
        }

    override fun attachBaseContext(newBase: Context) {
        super.attachBaseContext(newBase.withAppLanguage())
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        requestNotificationPermissionIfNeeded()
        ClassReminderScheduler.reschedule(this)
        setContent {
            TravelUtilityAppTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    val navController = rememberNavController()
                    NavHost(
                        navController = navController,
                        startDestination = AppRoutes.HOME,
                        modifier = Modifier.padding(innerPadding)
                    ) {
                        composable(AppRoutes.HOME) {
                            HomeScreen(
                                onNavigateToSchedule = { navController.navigate(AppRoutes.SCHEDULE) },
                                onNavigateToChecklist = { navController.navigate(AppRoutes.CHECKLIST) },
                                onNavigateToVocab = { navController.navigate(AppRoutes.VOCAB) },
                                onNavigateToSettings = { navController.navigate(AppRoutes.SETTINGS) }
                            )
                        }
                        composable(AppRoutes.SCHEDULE) {
                            ScheduleScreen(
                                onNavigateHome = {
                                    navController.popBackStack(AppRoutes.HOME, inclusive = false)
                                },
                                onNavigateToChecklist = { navController.navigate(AppRoutes.CHECKLIST) },
                                onNavigateToVocab = { navController.navigate(AppRoutes.VOCAB) }
                            )
                        }
                        composable(AppRoutes.CHECKLIST) {
                            ChecklistScreen(
                                onNavigateHome = {
                                    navController.popBackStack(AppRoutes.HOME, inclusive = false)
                                },
                                onNavigateToSchedule = { navController.navigate(AppRoutes.SCHEDULE) },
                                onNavigateToVocab = { navController.navigate(AppRoutes.VOCAB) }
                            )
                        }
                        composable(AppRoutes.VOCAB) {
                            VocabScreen(
                                onNavigateHome = {
                                    navController.popBackStack(AppRoutes.HOME, inclusive = false)
                                },
                                onNavigateToSchedule = { navController.navigate(AppRoutes.SCHEDULE) },
                                onNavigateToChecklist = { navController.navigate(AppRoutes.CHECKLIST) },
                                onStartTest = { source -> navController.navigate(AppRoutes.vocabTest(source.routeKey)) }
                            )
                        }
                        composable(
                            AppRoutes.VOCAB_TEST,
                            arguments = listOf(navArgument("source") { type = NavType.StringType })
                        ) { backStackEntry ->
                            val source = VocabTestSource.fromRouteKey(backStackEntry.arguments?.getString("source"))
                            VocabTestScreen(
                                source = source,
                                onClose = { navController.popBackStack() }
                            )
                        }
                        composable(AppRoutes.SETTINGS) {
                            SettingsScreen(
                                onBack = { navController.popBackStack() },
                                onLanguageChanged = { recreate() },
                                onRequestNotificationPermission = { requestNotificationPermissionIfNeeded() }
                            )
                        }
                    }
                }
            }
        }
    }

    override fun onResume() {
        super.onResume()
        ClassReminderScheduler.reschedule(this)
    }

    private fun requestNotificationPermissionIfNeeded() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU &&
            ContextCompat.checkSelfPermission(this, Manifest.permission.POST_NOTIFICATIONS) !=
            PackageManager.PERMISSION_GRANTED
        ) {
            requestNotificationPermission.launch(Manifest.permission.POST_NOTIFICATIONS)
        }
    }
}
