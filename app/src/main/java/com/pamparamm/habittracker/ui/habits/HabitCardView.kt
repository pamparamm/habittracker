package com.pamparamm.habittracker.ui.habits

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.Card
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.pamparamm.habittracker.domain.Habit

@Composable
fun HabitCardView(
    habit: Habit,
    index: Int,
    selected: Boolean,
    onClick: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier
            .fillMaxWidth()
            .padding(5.dp)
            .clickable { onClick.invoke(index) },
        elevation = 5.dp,
    ) {
        Column(modifier = Modifier.padding(10.dp)) {
            Row {
                Text(text = habit.title)
            }
            Row {
                AnimatedVisibility(visible = selected) {
                    Text(text = habit.description)
                }
            }
            Row {
                Text(text = "Habit Type: ${habit.type.name}")
                Spacer(modifier = Modifier.weight(1.0f))
                Text(text = "Priority: ${habit.priority.name}")
            }
            Row {
                Text(text = "Frequency: ${habit.frequency}")
                Spacer(modifier = Modifier.weight(1.0f))
                Text(text = "Repeats: ${habit.repeats}")
            }
        }
    }
}