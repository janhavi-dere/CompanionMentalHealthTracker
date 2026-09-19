package com.example.companion.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.companion.data.MoodDao
import com.example.companion.data.MoodEntry
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class MoodViewModel(private val dao: MoodDao) : ViewModel() {
    val entries = dao.observeAll().stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), emptyList())
    fun save(mood: String, stress: Int, anxiety: Int, journal: String) = viewModelScope.launch {
        dao.insert(MoodEntry(mood = mood, stress = stress, anxiety = anxiety, journal = journal))
    }
}
