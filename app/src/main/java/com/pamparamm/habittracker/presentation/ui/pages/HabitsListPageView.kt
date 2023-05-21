package com.pamparamm.habittracker.presentation.ui.pages

import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.Card
import androidx.compose.material.Divider
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.pamparamm.habittracker.presentation.NavRoutes
import com.pamparamm.habittracker.R
import com.pamparamm.habittracker.domain.entities.Habit
import com.pamparamm.habittracker.presentation.ui.extensions.Helpers.nameResource
import com.pamparamm.habittracker.presentation.ui.extensions.ModifierExtensions.noRippleClickable
import com.pamparamm.habittracker.presentation.ui.theme.Icons
import com.pamparamm.habittracker.presentation.viewmodel.habitslist.HabitsListViewModel

@Composable
fun HabitsListPageView(vm: HabitsListViewModel, navController: NavController) {
    Column(
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        Row {
            HabitsListContainerComponent(vm, navController)
        }
    }
}

@Composable
fun HabitsListContainerComponent(vm: HabitsListViewModel, navController: NavController) {
    val state = vm.state.collectAsState()
    val stateValue = state.value

    val isSelected = stateValue.selectedHabit?.let { true } ?: false

    Column(
        Modifier
            .fillMaxSize()
    ) {
        LazyColumn(
            Modifier
                .weight(1f)
                .noRippleClickable { vm.deselectHabit() }
                .padding(16.dp)
        ) {
            items(stateValue.habits, key = { it.id }) { habit ->
                HabitCardComponent(habit, vm)
                Divider()
            }
        }
        Column {
            Row {
                Button(
                    onClick = { "${navController.navigate(NavRoutes.HABITS_EDITOR)}" },
                    Modifier
                        .weight(1f)
                        .fillMaxWidth()
                ) {
                    Text(text = stringResource(R.string.habit_create))
                }
            }
            Row(Modifier.padding(horizontal = 8.dp)) {
                Button(
                    enabled = isSelected,
                    onClick = { if (isSelected) navController.navigate("${NavRoutes.HABITS_EDITOR}?${NavRoutes.HABITS_EDITOR_HABIT_UUID}=${stateValue.selectedHabit?.id}") },
                    modifier = Modifier
                        .weight(1f)
                        .padding(end = 8.dp)
                ) {
                    Text(text = stringResource(R.string.habit_edit))
                }
                Button(
                    enabled = isSelected,
                    onClick = { if (isSelected) vm.deleteSelectedHabit() },
                    modifier = Modifier
                        .weight(1f)
                        .padding(start = 8.dp)
                ) {
                    Text(text = stringResource(R.string.habit_delete))
                }
            }
        }
    }
}

@Composable
fun HabitCardComponent(habit: Habit, vm: HabitsListViewModel) {
    val state = vm.state.collectAsState()
    val stateValue = state.value

    val isSelected = stateValue.selectedHabit?.let { it == habit } ?: false

    Box(
        if (isSelected) {
            Modifier
                .background(
                    brush = Brush.horizontalGradient(
                        colors = listOf(
                            Color.Transparent,
                            Color(MaterialTheme.colors.primary.value), // Replace with desired glow color
                            Color.Transparent
                        )
                    ),
                    shape = RoundedCornerShape(8.dp)
                )
                .animateContentSize()
        } else Modifier,
        contentAlignment = Alignment.Center
    ) {
        Card(
            Modifier
                .fillMaxWidth()
                .padding(5.dp)
                .clickable(onClick = { if (isSelected) vm.deselectHabit() else vm.selectHabit(habit) }),
            elevation = 5.dp
        ) {
            Row(
                Modifier.animateContentSize(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(
                    modifier = Modifier
                        .weight(1f)
                        .padding(16.dp)
                ) {
                    Text(
                        text = habit.title,
                        style = MaterialTheme.typography.h6
                    )
                    Divider()
                    Text(text = "${habit.type.nameResource()} ${habit.priority.nameResource()}")
                    Spacer(modifier = Modifier.height(8.dp))
                    if (isSelected) {
                        Text(
                            text = habit.description.take(30),
                            style = MaterialTheme.typography.body1
                        )
                        Divider()
                    }
                }
                if (isSelected) {
                    Column(
                        Modifier
                            .padding(8.dp, 2.dp)
                    ) {
                        IconButton(
                            { vm.completeSelectedHabit() }
                        ) {
                            Icon(
                                Icons.CheckCircle,
                                "Завершить",
                                tint = MaterialTheme.colors.primary
                            )
                        }
                    }
                }
            }
        }
    }
}