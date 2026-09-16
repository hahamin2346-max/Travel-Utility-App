package com.example.travelutilityapp.ui.vocab

import android.content.Context

private const val MAX_TEST_WORDS = 25

/** Which word pool a vocab test is drawn from, selected from the "테스트 시작하기" sheet. */
enum class VocabTestSource(val routeKey: String) {
    MY("my"),
    DONT_KNOW("dont_know"),
    CEFR("cefr");

    companion object {
        fun fromRouteKey(key: String?): VocabTestSource =
            entries.firstOrNull { it.routeKey == key } ?: MY
    }
}

/** Resolves this source's word pool (capped at [MAX_TEST_WORDS]) given the user's registered words. */
fun VocabTestSource.testWords(
    context: Context,
    myWords: List<VocabWord>,
    dontKnowWords: List<VocabWord>,
    excludedCefrIds: Set<String> = emptySet()
): List<VocabWord> {
    val pool = when (this) {
        VocabTestSource.MY -> myWords
        VocabTestSource.DONT_KNOW -> dontKnowWords

        VocabTestSource.CEFR -> CefrVocabulary.words(context).filterNot { it.id in excludedCefrIds }
    }
    return pool.shuffled().take(MAX_TEST_WORDS)
}
