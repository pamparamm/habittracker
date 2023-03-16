package com.pamparamm.habittracker.ui.habits

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.pamparamm.habittracker.domain.Habit
import com.pamparamm.habittracker.domain.HabitPriority
import com.pamparamm.habittracker.domain.HabitType

@Composable
fun HabitEditorView(
    habits: SnapshotStateList<Habit>,
    selectedIndex: String?,
    navController: NavController,
    modifier: Modifier = Modifier
) {
    val selectedIndex = selectedIndex?.toInt()
    val existingHabit = selectedIndex != null && selectedIndex != -1
    val habitToEdit =
        remember { mutableStateOf(if (existingHabit) habits[selectedIndex!!] else Habit()) }
    Column(modifier) {
        Row(Modifier.fillMaxWidth()) {
            TextField(
                value = habitToEdit.value.title,
                onValueChange = { habitToEdit.value = habitToEdit.value.copy(title = it) })
        }
        Row(Modifier.fillMaxWidth()) {
            TextField(
                value = habitToEdit.value.description,
                onValueChange = { habitToEdit.value = habitToEdit.value.copy(description = it) })
        }
        Row(Modifier.fillMaxWidth()) {
            HabitEditorPriorityView(habitToEdit)
        }
        Row(Modifier.fillMaxWidth()) {
            HabitEditorTypeView(habitToEdit)
        }
        Row(Modifier.fillMaxWidth()) {
            HabitEditorFrequencyView(habitToEdit)
        }
        Spacer(Modifier.weight(1f))
        Row {
            Button(onClick = {
                if (existingHabit) habits.set(selectedIndex!!, habitToEdit.value) else habits.add(
                    habitToEdit.value
                )
                navController.navigate("habits")
            }, Modifier.fillMaxWidth()) {
                Text("Save habit")
            }
        }
    }
}

@Composable
fun HabitEditorPriorityView(habit: MutableState<Habit>) {
    val expanded = remember { mutableStateOf(false) }
    val priorities = enumValues<HabitPriority>()
    Column {
        Card(elevation = 5.dp) {
            Text(
                habit.value.priority.toString(),
                modifier = Modifier.clickable(onClick = { expanded.value = true })
            )
        }
        DropdownMenu(expanded = expanded.value, onDismissRequest = { expanded.value = true }) {
            priorities
                .map {
                    DropdownMenuItem(onClick = {
                        habit.value = habit.value.copy(priority = it); expanded.value = false
                    }) {
                        Text(text = it.toString())
                    }
                }
        }
    }
}

@Composable
fun HabitEditorTypeView(habit: MutableState<Habit>) {
    val types = enumValues<HabitType>()
    Column {
        Row {
            types.map {
                Text(text = it.toString())
                RadioButton(
                    selected = habit.value.type == it,
                    onClick = { habit.value = habit.value.copy(type = it) })
            }
        }
    }
}

@Composable
fun HabitEditorFrequencyView(habit: MutableState<Habit>) {
    Column(modifier = Modifier.fillMaxWidth()) {
        Text(text = "Frequency: ")
        TextField(
            value = habit.value.frequency.toString(),
            onValueChange = { habit.value = habit.value.copy(frequency = it.toInt()) },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            modifier = Modifier.fillMaxWidth(0.5f)
        )
        Text(text = "Repeats: ")
        TextField(
            value = habit.value.repeats.toString(),
            onValueChange = { habit.value = habit.value.copy(repeats = it.toInt()) },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            modifier = Modifier.fillMaxWidth(0.5f)
        )
    }
}