package com.example.companion.data

/** Lightweight offline fallback. It is not a clinical assessment or diagnosis. */
object SentimentAnalyzer {
    private val positive = setOf("happy", "calm", "grateful", "good", "peaceful", "excited", "relaxed")
    private val negative = setOf("sad", "stressed", "anxious", "angry", "tired", "overwhelmed", "lonely", "worried")

    fun classify(text: String): String {
        val words = text.lowercase().split(Regex("\\W+")).toSet()
        val p = words.count { it in positive }; val n = words.count { it in negative }
        return when { p > n -> "Positive"; n > p -> "Needs attention"; else -> "Neutral" }
    }
}
