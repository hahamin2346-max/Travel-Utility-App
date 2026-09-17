package com.example.travelutilityapp.data

import com.example.travelutilityapp.ui.budget.Currency
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.json.JSONObject
import java.net.HttpURLConnection
import java.net.URL

private const val EXCHANGE_RATE_API_URL = "https://open.er-api.com/v6/latest/KRW"
private const val TIMEOUT_MS = 8000

/** Fetches live KRW-based exchange rates from a free, keyless public API. */
object ExchangeRateService {

    /** Returns won-per-unit rates for [Currency], or null if the network call or parsing fails. */
    suspend fun fetchRatesToKrw(): Map<Currency, Double>? = withContext(Dispatchers.IO) {
        runCatching {
            val connection = URL(EXCHANGE_RATE_API_URL).openConnection() as HttpURLConnection
            connection.connectTimeout = TIMEOUT_MS
            connection.readTimeout = TIMEOUT_MS
            val body = connection.inputStream.bufferedReader().use { it.readText() }
            connection.disconnect()

            val json = JSONObject(body)
            if (json.optString("result") != "success") return@runCatching null

            val rates = json.getJSONObject("rates")
            mapOf(
                Currency.KRW to 1.0,
                Currency.PESO to 1.0 / rates.getDouble("PHP"),
                Currency.JPY to 1.0 / rates.getDouble("JPY"),
                Currency.TWD to 1.0 / rates.getDouble("TWD"),
                Currency.CNY to 1.0 / rates.getDouble("CNY"),
                Currency.USD to 1.0 / rates.getDouble("USD")
            )
        }.getOrNull()
    }
}
