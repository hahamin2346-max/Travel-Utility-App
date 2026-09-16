package com.example.travelutilityapp.ui.navigation

object AppRoutes {
    const val HOME = "home"
    const val SCHEDULE = "schedule"
    const val CHECKLIST = "checklist"
    const val VOCAB = "vocab"
    const val BUDGET = "budget"
    const val SETTINGS = "settings"
    const val VOCAB_TEST = "vocab_test/{source}"

    fun vocabTest(source: String) = "vocab_test/$source"
}
