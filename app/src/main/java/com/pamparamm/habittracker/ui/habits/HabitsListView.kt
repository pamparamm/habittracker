package com.pamparamm.habittracker.ui.habits

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.Card
import androidx.compose.material.DropdownMenu
import androidx.compose.material.DropdownMenuItem
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.pamparamm.habittracker.domain.Habit
import com.pamparamm.habittracker.domain.HabitPriority
import com.pamparamm.habittracker.domain.HabitType
import java.util.UUID

@Composable
fun HabitsListView(
    habits: SnapshotStateList<Habit>,
    selectedIndex: MutableState<UUID?>,
    modifier: Modifier = Modifier
) {
    val viewMode = remember {
        mutableStateOf(HabitsViewMode.ALL)
    }
    val onItemClick =
        { index: UUID -> selectedIndex.value = if (selectedIndex.value != index) index else null }
    Row {
        HabitListViewModeView(viewMode)
    }
    LazyColumn(modifier) {
        items(habits.filter { habit ->
            when (viewMode.value) {
                HabitsViewMode.ALL -> true
                HabitsViewMode.BAD -> habit.type == HabitType.BAD
                HabitsViewMode.GOOD -> habit.type == HabitType.GOOD
            }

        }) { habit ->
            HabitCardView(habit, selectedIndex.value == habit.id, onItemClick)
        }
    }
}

@Composable
fun HabitListViewModeView(viewMode: MutableState<HabitsViewMode>) {
    val expanded = remember { mutableStateOf(false) }
    val priorities = enumValues<HabitsViewMode>()
    Column {
        Card(elevation = 5.dp) {
            Text(
                viewMode.value.toString(),
                modifier = Modifier.clickable(onClick = { expanded.value = true })
            )
        }
        DropdownMenu(expanded = expanded.value, onDismissRequest = { expanded.value = true }) {
            priorities.map {
                DropdownMenuItem(onClick = {
                    viewMode.value = it; expanded.value = false
                }) {
                    Text(text = it.toString())
                }
            }
        }
    }
}