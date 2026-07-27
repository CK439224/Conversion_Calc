package com.numconversion.app.domain.history

enum class HistoryEntryType { CALCULATOR, CONVERTER }

data class HistoryEntry(
    val id: Long,
    val type: HistoryEntryType,
    val summary: String,
    val resultText: String,
    val timestampMillis: Long
)
