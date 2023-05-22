package com.pamparamm.habittracker.presentation.ui.pages

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Button
import androidx.compose.material.Card
import androidx.compose.material.DropdownMenu
import androidx.compose.material.DropdownMenuItem
import androidx.compose.material.Icon
import androidx.compose.material.RadioButton
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.pamparamm.habittracker.presentation.NavRoutes
import com.pamparamm.habittracker.R
import com.pamparamm.habittracker.domain.entities.HabitPriority
import com.pamparamm.habittracker.domain.entities.HabitType
import com.pamparamm.habittracker.presentation.ui.extensions.Helpers.nameResource
import com.pamparamm.habittracker.presentation.ui.extensions.Helpers.normalize
import com.pamparamm.habittracker.presentation.ui.theme.Icons
import com.pamparamm.habittracker.presentation.viewmodel.habitseditor.HabitsEditorHabitData
import com.pamparamm.habittracker.presentation.viewmodel.habitseditor.HabitsEditorViewModel
import com.pamparamm.habittracker.presentation.viewmodel.habitseditor.store.HabitsEditorState

@Composable
fun HabitsEditorPageView(vm: HabitsEditorViewModel, navController: NavController) {
    val state = vm.state.collectAsStateWithLifecycle()
    val stateValue = state.value

    LaunchedEffect(stateValue) {
        if (stateValue.isDoneEditing) {
            vm.resetEditor()
            val currentRoute = navController.currentBackStackEntry?.destination?.route!!
            navController.navigate(NavRoutes.HABITS_LIST) {
                popUpTo(currentRoute) {
                    inclusive = true
                }
            }
        }
    }

    Column(
        Modifier
            .fillMaxSize()
            .padding(8.dp),
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        Row(
            Modifier
                .verticalScroll(rememberScrollState())
                .weight(weight = 1f, fill = false)
        ) {
            HabitsEditorContainerComponent(vm, stateValue)
        }
        Row(Modifier.padding(horizontal = 8.dp)) {
            Button(
                onClick = { vm.saveHabit() },
                modifier = Modifier
                    .weight(1f)
                    .padding(end = 8.dp)
            ) {
                Text(text = stringResource(R.string.habits_editor_save))
            }
            Button(
                onClick = { vm.cancel() },
                modifier = Modifier
                    .weight(1f)
                    .padding(start = 8.dp)
            ) {
                Text(text = stringResource(R.string.habits_editor_cancel))
            }
        }
    }
}

@Composable
fun HabitsEditorContainerComponent(vm: HabitsEditorViewModel, stateValue: HabitsEditorState) {
    stateValue.habit?.let {
        val habitData = HabitsEditorHabitData.fromHabit(it)
        Column(
            Modifier
                .padding(16.dp),
            Arrangement.spacedBy(8.dp)
        ) {
            TextField(
                modifier = Modifier.fillMaxWidth(),
                label = { Text(stringResource(R.string.habits_editor_title)) },
                value = habitData.title,
                onValueChange = { vm.updateHabitData(habitData.copy(title = it.normalize(true))) },
                singleLine = true
            )

            TextField(
                modifier = Modifier.fillMaxWidth(),
                label = { Text(stringResource(R.string.habits_editor_description)) },
                value = habitData.description,
                onValueChange = {
                    vm.updateHabitData(habitData.copy(description = it.normalize()))
                }
            )

            HabitsEditorPriorityComponent(vm, habitData)
            HabitsEditorTypeComponent(vm, habitData)

            TextField(
                modifier = Modifier.fillMaxWidth(),
                label = { Text(stringResource(R.string.habits_editor_period)) },
                value = habitData.period.toString(),
                onValueChange = {
                    it.toIntOrNull()
                        ?.let { if (it > 0) vm.updateHabitData(habitData.copy(period = it)) }
                },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
            )

            TextField(
                modifier = Modifier.fillMaxWidth(),
                label = { Text(stringResource(R.string.habits_editor_target_completions)) },
                value = habitData.targetCompletions.toString(),
                onValueChange = {
                    it.toIntOrNull()
                        ?.let { if (it > 0) vm.updateHabitData(habitData.copy(targetCompletions = it)) }
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
    Row(modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
        Text(
            text = "${stringResource(R.string.habits_editor_priority)}: ",
            Modifier.padding(end = 8.dp)
        )
        Card(
            elevation = 5.dp, modifier = Modifier
                .weight(1f)
                .clickable(onClick = { expanded.value = true })
        ) {
            Row(Modifier.fillMaxWidth())
            {
                Text(habitData.priority.nameResource())
                Spacer(Modifier.weight(1f))
                when (expanded.value) {
                    true -> Icon(Icons.KeyboardArrowUp, contentDescription = "")
                    false -> Icon(Icons.KeyboardArrowDown, contentDescription = "")
                }
            }
            DropdownMenu(expanded = expanded.value, onDismissRequest = { expanded.value = false }) {
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
}

@Composable
fun HabitsEditorTypeComponent(vm: HabitsEditorViewModel, habitData: HabitsEditorHabitData) {
    val types = enumValues<HabitType>()
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceEvenly
    ) {
        types.map {
            Column()
            {
                Text(text = it.nameResource())
                RadioButton(selected = habitData.type == it,
                    onClick = { vm.updateHabitData(habitData.copy(type = it)) })
            }
        }
    }
}