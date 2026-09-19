package com.example.companion

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.companion.data.AppDatabase
import com.example.companion.data.SentimentAnalyzer
import com.example.companion.viewmodel.MoodViewModel
import java.text.SimpleDateFormat
import java.util.*

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) { super.onCreate(savedInstanceState); enableEdgeToEdge()
        val dao = AppDatabase.get(this).moodDao()
        setContent { MaterialTheme { CompanionApp(dao) } }
    }
}

@Composable
fun CompanionApp(dao: com.example.companion.data.MoodDao) {
    val vm: MoodViewModel = viewModel(factory = object : ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T = MoodViewModel(dao) as T
    })
    var tab by remember { mutableIntStateOf(0) }
    Scaffold(bottomBar = { NavigationBar {
        NavigationBarItem(tab == 0, { tab = 0 }, { Icon(Icons.Default.Home, null) }, label = { Text("Home") })
        NavigationBarItem(tab == 1, { tab = 1 }, { Icon(Icons.Default.Edit, null) }, label = { Text("Check-in") })
        NavigationBarItem(tab == 2, { tab = 2 }, { Icon(Icons.Default.Insights, null) }, label = { Text("Insights") })
    }}) { pad -> Box(Modifier.padding(pad)) { when(tab) {
        0 -> Home(vm); 1 -> CheckIn(vm); 2 -> Insights(vm)
    } } }
}

@Composable private fun Home(vm: MoodViewModel) {
    val entries by vm.entries.collectAsState(); LazyColumn(Modifier.fillMaxSize().padding(20.dp), verticalArrangement = Arrangement.spacedBy(14.dp)) {
        item { Text("Companion", style = MaterialTheme.typography.headlineLarge, fontWeight = FontWeight.Bold); Text("A private space for daily reflection", color = MaterialTheme.colorScheme.onSurfaceVariant) }
        item { Card { Column(Modifier.padding(18.dp)) { Text("Today’s gentle reminder", fontWeight = FontWeight.Bold); Spacer(Modifier.height(8.dp)); Text("You can check in without judging how you feel. Small, consistent steps matter.") } } }
        item { Text("Recent check-ins", style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold) }
        if (entries.isEmpty()) item { Text("No entries yet. Start your first check-in.") }
        items(entries.take(5)) { e -> Card { Column(Modifier.padding(16.dp)) { Text("${e.mood} • Stress ${e.stress}/10 • Anxiety ${e.anxiety}/10", fontWeight = FontWeight.SemiBold); Text(SimpleDateFormat("dd MMM, hh:mm a", Locale.getDefault()).format(Date(e.timestamp))); if(e.journal.isNotBlank()) Text(e.journal, maxLines = 2) } } }
        item { Text("Companion is a wellness tool, not a medical diagnosis or replacement for professional care.", style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant) }
    }
}

@Composable private fun CheckIn(vm: MoodViewModel) {
    var mood by remember { mutableStateOf("Calm") }; var stress by remember { mutableFloatStateOf(3f) }; var anxiety by remember { mutableFloatStateOf(3f) }; var journal by remember { mutableStateOf("") }; var saved by remember { mutableStateOf(false) }
    Column(Modifier.fillMaxSize().padding(20.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
        Text("Daily check-in", style = MaterialTheme.typography.headlineMedium, fontWeight = FontWeight.Bold)
        Text("How are you feeling right now?")
        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) { listOf("Happy","Calm","Sad","Anxious","Stressed").forEach { m -> FilterChip(selected=mood==m,onClick={mood=m},label={Text(m)}) } }
        Text("Stress: ${stress.toInt()}/10"); Slider(value=stress,onValueChange={stress=it},valueRange=0f..10f,steps=9)
        Text("Anxiety: ${anxiety.toInt()}/10"); Slider(value=anxiety,onValueChange={anxiety=it},valueRange=0f..10f,steps=9)
        OutlinedTextField(journal,{journal=it},Modifier.fillMaxWidth().height(140.dp),label={Text("Journal (optional)")},placeholder={Text("Write a few words about your day...")})
        if(journal.isNotBlank()) Text("Reflection: ${SentimentAnalyzer.classify(journal)}", style=MaterialTheme.typography.bodySmall)
        Button(onClick={ vm.save(mood,stress.toInt(),anxiety.toInt(),journal); journal=""; saved=true },Modifier.fillMaxWidth()) { Text(if(saved) "Saved ✓" else "Save check-in") }
        Text("This reflection is a simple wellness aid and should not be interpreted as a clinical assessment.", style=MaterialTheme.typography.bodySmall)
    }
}

@Composable private fun Insights(vm: MoodViewModel) {
    val entries by vm.entries.collectAsState(); val avgStress = if(entries.isEmpty()) 0.0 else entries.map{it.stress}.average(); val avgAnxiety=if(entries.isEmpty())0.0 else entries.map{it.anxiety}.average()
    Column(Modifier.fillMaxSize().padding(20.dp),verticalArrangement=Arrangement.spacedBy(14.dp)) { Text("Insights",style=MaterialTheme.typography.headlineMedium,fontWeight=FontWeight.Bold); Text("Based on your logged check-ins")
        Card { Column(Modifier.padding(18.dp)) { Text("Average stress"); Text("%.1f / 10".format(avgStress),style=MaterialTheme.typography.headlineSmall) } }
        Card { Column(Modifier.padding(18.dp)) { Text("Average anxiety"); Text("%.1f / 10".format(avgAnxiety),style=MaterialTheme.typography.headlineSmall) } }
        Card { Column(Modifier.padding(18.dp)) { Text("Entries recorded"); Text("${entries.size}",style=MaterialTheme.typography.headlineSmall) } }
        Text("These summaries describe your logged data; they do not diagnose or predict a mental health condition.",style=MaterialTheme.typography.bodySmall)
    }
}
