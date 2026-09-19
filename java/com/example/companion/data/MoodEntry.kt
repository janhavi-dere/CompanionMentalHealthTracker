package com.example.companion.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "mood_entries")
data class MoodEntry(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val mood: String,
    val stress: Int,
    val anxiety: Int,
    val journal: String,
    val timestamp: Long = System.currentTimeMillis()
)
