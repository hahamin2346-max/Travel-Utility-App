package com.example.travelutilityapp.data

import android.content.Context
import android.content.res.Configuration
import java.util.Locale

/**
 * Wraps [this] context so its resources resolve against the user's saved [AppLanguage],
 * falling back to the device locale when SYSTEM is selected. Call from
 * Activity.attachBaseContext so the override applies before any resource is read.
 */
fun Context.withAppLanguage(): Context {
    val tag = LanguagePreferences(this).load().tag ?: return this
    val locale = Locale.forLanguageTag(tag)
    Locale.setDefault(locale)
    val config = Configuration(resources.configuration)
    config.setLocale(locale)
    return createConfigurationContext(config)
}
