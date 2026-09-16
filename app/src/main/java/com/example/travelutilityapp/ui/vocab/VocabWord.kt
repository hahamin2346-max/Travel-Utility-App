package com.example.travelutilityapp.ui.vocab

import java.util.UUID

/**
 * A single vocabulary entry: an English word/phrase and its Korean meaning.
 *
 * [isKnown] is null until the (not-yet-built) vocab test feature classifies the word as
 * known or unknown; the filter views only ever show words with a non-null value.
 */
data class VocabWord(
    val id: String = UUID.randomUUID().toString(),
    val word: String = "",
    val meaning: String = "",
    val isKnown: Boolean? = null
)
