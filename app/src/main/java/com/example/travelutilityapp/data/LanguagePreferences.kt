package com.example.travelutilityapp.data

import android.content.Context

enum class AppLanguage(val tag: String?) {
    SYSTEM(null),
    KOREAN("ko"),
    ENGLISH("en");

    companion object {
        fun fromTag(tag: String?): AppLanguage = entries.firstOrNull { it.tag == tag } ?: SYSTEM
    }
}

private const val PREFS_NAME = "language_prefs"
private const val KEY_LANGUAGE_TAG = "language_tag"

/** Persists the user's in-app language override (system default, Korean, or English). */
class LanguagePreferences(context: Context) {
    private val prefs = context.applicationContext.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)

    fun load(): AppLanguage = AppLanguage.fromTag(prefs.getString(KEY_LANGUAGE_TAG, null))

    fun save(language: AppLanguage) {
        prefs.edit().putString(KEY_LANGUAGE_TAG, language.tag).apply()
    }
}
