package com.pamparamm.habittracker.ui.habits

import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.runtime.*
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Modifier
import com.pamparamm.habittracker.domain.Habit

@Composable
fun HabitsListView(habits: SnapshotStateList<Habit>, selectedIndex: MutableState<Int>, modifier: Modifier = Modifier) {
    val onItemClick = { index: Int -> selectedIndex.value = if (selectedIndex.value != index) index else -1 }
    LazyColumn(modifier) {
        itemsIndexed(habits) { index, habit ->
            HabitCardView(habit, index, selectedIndex.value == index, onItemClick)
        }
    }
}