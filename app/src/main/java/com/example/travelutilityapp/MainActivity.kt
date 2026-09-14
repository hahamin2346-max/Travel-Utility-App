package com.example.travelutilityapp

import android.content.Context
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.travelutilityapp.data.withAppLanguage
import com.example.travelutilityapp.ui.checklist.ChecklistScreen
import com.example.travelutilityapp.ui.home.HomeScreen
import com.example.travelutilityapp.ui.navigation.AppRoutes
import com.example.travelutilityapp.ui.schedule.ScheduleScreen
import com.example.travelutilityapp.ui.settings.SettingsScreen
import com.example.travelutilityapp.ui.theme.TravelUtilityAppTheme

class MainActivity : ComponentActivity() {
    override fun attachBaseContext(newBase: Context) {
        super.attachBaseContext(newBase.withAppLanguage())
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
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
                                onNavigateToSettings = { navController.navigate(AppRoutes.SETTINGS) }
                            )
                        }
                        composable(AppRoutes.SCHEDULE) {
                            ScheduleScreen(
                                onNavigateHome = {
                                    navController.popBackStack(AppRoutes.HOME, inclusive = false)
                                },
                                onNavigateToChecklist = { navController.navigate(AppRoutes.CHECKLIST) }
                            )
                        }
                        composable(AppRoutes.CHECKLIST) {
                            ChecklistScreen(
                                onNavigateHome = {
                                    navController.popBackStack(AppRoutes.HOME, inclusive = false)
                                },
                                onNavigateToSchedule = { navController.navigate(AppRoutes.SCHEDULE) }
                            )
                        }
                        composable(AppRoutes.SETTINGS) {
                            SettingsScreen(
                                onBack = { navController.popBackStack() },
                                onLanguageChanged = { recreate() }
                            )
                        }
                    }
                }
            }
        }
    }
}
