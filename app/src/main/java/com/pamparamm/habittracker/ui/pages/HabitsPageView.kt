package com.pamparamm.habittracker.ui.pages

import androidx.compose.foundation.layout.*
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import com.pamparamm.habittracker.domain.Habit
import com.pamparamm.habittracker.ui.habits.HabitsListView

@Composable
fun HabitsPageView(
    habits: SnapshotStateList<Habit>,
    navController: NavController,
    modifier: Modifier = Modifier
) {
    val selectedHabit = remember { mutableStateOf(-1) }
    Column(modifier, verticalArrangement = Arrangement.SpaceBetween) {
        HabitsListView(habits = habits, selectedHabit)
        Spacer(Modifier.weight(1f))
        Row {
            Button(onClick = { navController.navigate("habits/${selectedHabit.value}") }, Modifier.fillMaxWidth()) {
                Text(if (selectedHabit.value == -1) "Create new habit" else "Edit selected habit")
            }
        }
    }
}