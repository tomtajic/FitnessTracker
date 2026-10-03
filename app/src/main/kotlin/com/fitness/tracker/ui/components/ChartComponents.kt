package com.fitness.tracker.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.fitness.tracker.data.model.WorkoutSession
import kotlin.math.max

@Composable
fun ImprovementChart(
    workoutId: Int,
    workoutName: String,
    sessions: List<WorkoutSession>
) {
    val relevantSessions = sessions
        .filter { it.workoutId == workoutId }
        .sortedBy { it.timestamp }
        .takeLast(10)

    if (relevantSessions.isEmpty()) {
        return
    }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 12.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Text(
                "$workoutName - Weight Progression",
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(bottom = 16.dp)
            )

            // Simple bar chart
            SimpleBarChart(
                sessions = relevantSessions,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp)
            )

            // Session list below chart
            Text(
                "Recent Sessions",
                fontSize = 14.sp,
                fontWeight = FontWeight.SemiBold,
                modifier = Modifier.padding(top = 16.dp, bottom = 8.dp)
            )

            LazyColumn(
                modifier = Modifier.heightIn(max = 150.dp)
            ) {
                items(relevantSessions.reversed()) { session ->
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 4.dp),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(
                            "${session.kg}kg x ${session.reps} reps",
                            fontSize = 12.sp
                        )
                        Text(
                            formatTime(session.timestamp),
                            fontSize = 11.sp,
                            color = Color.Gray
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun SimpleBarChart(
    sessions: List<WorkoutSession>,
    modifier: Modifier = Modifier
) {
    val maxWeight = sessions.maxOfOrNull { it.kg } ?: 100.0
    val minWeight = sessions.minOfOrNull { it.kg } ?: 0.0
    val range = if (maxWeight == minWeight) 100.0 else maxWeight - minWeight + 20

    Column(modifier = modifier) {
        // Chart bars
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
                .padding(bottom = 8.dp),
            horizontalArrangement = Arrangement.SpaceEvenly,
            verticalAlignment = Alignment.Bottom
        ) {
            sessions.forEach { session ->
                Column(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxHeight()
                        .padding(horizontal = 2.dp),
                    verticalArrangement = Arrangement.Bottom,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    val barHeight = ((session.kg - minWeight) / range) * 0.95f
                    
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .fillMaxHeight(barHeight)
                            .background(
                                color = MaterialTheme.colorScheme.primary,
                                shape = androidx.compose.foundation.shape.RoundedCornerShape(
                                    topStart = 4.dp,
                                    topEnd = 4.dp
                                )
                            )
                    )
                }
            }
        }

        // X-axis labels
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(20.dp),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            sessions.forEachIndexed { index, _ ->
                Text(
                    "${index + 1}",
                    fontSize = 10.sp,
                    color = Color.Gray,
                    modifier = Modifier.weight(1f),
                    textAlign = androidx.compose.ui.text.style.TextAlign.Center
                )
            }
        }
    }
}

private fun formatTime(timestamp: Long): String {
    val date = java.time.Instant.ofEpochMilli(timestamp)
        .atZone(java.time.ZoneId.systemDefault())
        .toLocalDateTime()
    
    return "${date.month.name.take(3)} ${date.dayOfMonth}"
}
