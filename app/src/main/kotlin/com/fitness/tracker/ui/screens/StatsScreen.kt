package com.fitness.tracker.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.fitness.tracker.data.model.WorkoutStats
import com.fitness.tracker.ui.components.ImprovementChart
import com.fitness.tracker.viewmodel.StatsViewModel

@Composable
fun StatsScreen(viewModel: StatsViewModel) {
    val dailyStats by viewModel.dailyStats.collectAsState()
    val weeklyStats by viewModel.weeklyStats.collectAsState()
    val monthlyStats by viewModel.monthlyStats.collectAsState()
    val dailySessions by viewModel.dailySessions.collectAsState()
    val weeklySessions by viewModel.weeklySessions.collectAsState()
    val monthlySessions by viewModel.monthlySessions.collectAsState()

    var selectedPeriod by remember { mutableStateOf("Daily") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 16.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                "Statistics",
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.weight(1f)
            )
            IconButton(onClick = { viewModel.refreshStats() }) {
                Icon(Icons.Filled.Refresh, contentDescription = "Refresh")
            }
        }

        PeriodSelector(
            selectedPeriod = selectedPeriod,
            onPeriodChange = { selectedPeriod = it },
            modifier = Modifier.padding(bottom = 16.dp)
        )

        val stats = when (selectedPeriod) {
            "Daily" -> dailyStats
            "Weekly" -> weeklyStats
            else -> monthlyStats
        }

        val sessions = when (selectedPeriod) {
            "Daily" -> dailySessions
            "Weekly" -> weeklySessions
            else -> monthlySessions
        }

        if (stats.isEmpty()) {
            Text(
                "No workouts recorded for this period",
                fontSize = 16.sp,
                color = Color.Gray,
                modifier = Modifier.padding(top = 24.dp)
            )
        } else {
            LazyColumn {
                items(stats.values.toList()) { stat ->
                    StatCard(stat)
                    
                    ImprovementChart(
                        workoutId = stat.workoutId,
                        workoutName = stat.workoutName,
                        sessions = sessions
                    )
                }
            }
        }
    }
}

@Composable
fun PeriodSelector(
    selectedPeriod: String,
    onPeriodChange: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        listOf("Daily", "Weekly", "Monthly").forEach { period ->
            Button(
                onClick = { onPeriodChange(period) },
                modifier = Modifier
                    .weight(1f)
                    .padding(horizontal = 4.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = if (selectedPeriod == period) 
                        MaterialTheme.colorScheme.primary 
                    else 
                        MaterialTheme.colorScheme.surfaceVariant
                )
            ) {
                Text(period, fontSize = 12.sp)
            }
        }
    }
}

@Composable
fun StatCard(stat: WorkoutStats) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Text(
                stat.workoutName,
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold
            )

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 12.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                StatItem("Sessions", stat.totalSessions.toString())
                StatItem("Total kg", "%.1f".format(stat.totalKg))
                StatItem("Avg kg", "%.1f".format(stat.avgKg))
            }

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 8.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                StatItem("Total Reps", stat.totalReps.toString())
                StatItem("Avg Reps", "%.1f".format(stat.avgReps))
                StatItem("Max kg", "%.1f".format(stat.maxKg))
            }
        }
    }
}

@Composable
fun StatItem(label: String, value: String) {
    Column {
        Text(label, fontSize = 12.sp, color = Color.Gray)
        Text(value, fontSize = 16.sp, fontWeight = FontWeight.Bold)
    }
}
