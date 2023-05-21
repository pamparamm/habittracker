package com.pamparamm.habittracker.presentation.ui.pages

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.Button
import androidx.compose.material.Card
import androidx.compose.material.DropdownMenu
import androidx.compose.material.DropdownMenuItem
import androidx.compose.material.RadioButton
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.pamparamm.habittracker.presentation.NavRoutes
import com.pamparamm.habittracker.R
import com.pamparamm.habittracker.domain.entities.HabitPriority
import com.pamparamm.habittracker.domain.entities.HabitType
import com.pamparamm.habittracker.presentation.ui.extensions.Helpers.nameResource
import com.pamparamm.habittracker.presentation.ui.extensions.Helpers.normalize
import com.pamparamm.habittracker.presentation.viewmodel.habitseditor.HabitsEditorHabitData
import com.pamparamm.habittracker.presentation.viewmodel.habitseditor.HabitsEditorViewModel

@Composable
fun HabitsEditorPageView(vm: HabitsEditorViewModel, navController: NavController) {
    val state = vm.state.collectAsState()
    val stateValue = state.value

//    if (stateValue.isDoneEditing) {
//        navController.navigate(NavRoutes.HABITS_LIST) {
//            popUpTo(navController.graph.id) { inclusive = true }
//        }
//    }

    Column(
        verticalArrangement = Arrangement.SpaceBetween,
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Row {
            HabitsEditorContainerComponent(vm)
        }

        Spacer(modifier = Modifier.weight(1f))

        Row(Modifier.padding(horizontal = 8.dp)) {
            Button(
                onClick = { vm.saveHabit(); navController.navigate(NavRoutes.HABITS_LIST) },
                modifier = Modifier
                    .weight(1f)
                    .padding(end = 8.dp)
            ) {
                Text(text = stringResource(R.string.habit_save))
            }
            Button(
                onClick = { vm.cancel(); navController.navigate(NavRoutes.HABITS_LIST) },
                modifier = Modifier
                    .weight(1f)
                    .padding(start = 8.dp)
            ) {
                Text(text = stringResource(R.string.habit_cancel))
            }
        }
    }
}

@Composable
fun HabitsEditorContainerComponent(vm: HabitsEditorViewModel) {
    val state = vm.state.collectAsState()
    val stateValue = state.value

    stateValue.habit?.let {
        val habitData = HabitsEditorHabitData.fromHabit(it)
        Column(Modifier.padding(16.dp)) {
            TextField(
                label = { Text("Title") },
                value = habitData.title,
                onValueChange = { vm.updateHabitData(habitData.copy(title = it.normalize(true))) },
                singleLine = true
            )

            TextField(
                label = { Text("Desc") },
                value = habitData.description,
                onValueChange = {
                    vm.updateHabitData(habitData.copy(description = it.normalize()))
                }
            )

            HabitsEditorPriorityComponent(vm, habitData)
            HabitsEditorTypeComponent(vm, habitData)

            TextField(
                label = { Text("Period") },
                value = habitData.period.toString(),
                onValueChange = {
                    it.toIntOrNull()?.let { vm.updateHabitData(habitData.copy(period = it)) }
                },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
            )

            TextField(
                label = { Text("Target Completions") },
                value = habitData.targetCompletions.toString(),
                onValueChange = {
                    it.toIntOrNull()
                        ?.let { vm.updateHabitData(habitData.copy(targetCompletions = it)) }
                },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
            )
        }
    }
}

@Composable
fun HabitsEditorPriorityComponent(vm: HabitsEditorViewModel, habitData: HabitsEditorHabitData) {
    val expanded = remember { mutableStateOf(false) }
    val priorities = enumValues<HabitPriority>()
    Column {
        Card(elevation = 5.dp) {
            Text(
                habitData.priority.nameResource(),
                modifier = Modifier.clickable(onClick = { expanded.value = true })
            )
        }
        DropdownMenu(expanded = expanded.value, onDismissRequest = { expanded.value = true }) {
            priorities.map {
                DropdownMenuItem(onClick = {
                    vm.updateHabitData(habitData.copy(priority = it)); expanded.value = false
                }) {
                    Text(it.nameResource())
                }
            }
        }
    }
}

@Composable
fun HabitsEditorTypeComponent(vm: HabitsEditorViewModel, habitData: HabitsEditorHabitData) {
    val types = enumValues<HabitType>()
    types.map {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(text = it.nameResource())
            RadioButton(selected = habitData.type == it,
                onClick = { vm.updateHabitData(habitData.copy(type = it)) })
        }
    }
}