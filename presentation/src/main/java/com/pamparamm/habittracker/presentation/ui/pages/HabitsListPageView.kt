package com.pamparamm.habittracker.presentation.ui.pages

import android.widget.Toast
import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.BottomSheetScaffold
import androidx.compose.material.BottomSheetValue
import androidx.compose.material.Button
import androidx.compose.material.Card
import androidx.compose.material.Divider
import androidx.compose.material.DropdownMenu
import androidx.compose.material.DropdownMenuItem
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowLeft
import androidx.compose.material.icons.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material.rememberBottomSheetScaffoldState
import androidx.compose.material.rememberBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.pamparamm.habittracker.presentation.NavRoutes
import com.pamparamm.habittracker.presentation.R
import com.pamparamm.habittracker.domain.entities.Habit
import com.pamparamm.habittracker.domain.entities.HabitPriority
import com.pamparamm.habittracker.domain.entities.HabitType
import com.pamparamm.habittracker.presentation.ui.extensions.Helpers.nameResource
import com.pamparamm.habittracker.presentation.ui.extensions.Helpers.normalize
import com.pamparamm.habittracker.presentation.ui.extensions.Helpers.timestampToString
import com.pamparamm.habittracker.presentation.ui.extensions.ModifierExtensions.noRippleClickable
import com.pamparamm.habittracker.presentation.ui.theme.Icons
import com.pamparamm.habittracker.presentation.viewmodel.habitslist.HabitsListFilterMode
import com.pamparamm.habittracker.presentation.viewmodel.habitslist.HabitsListMessage
import com.pamparamm.habittracker.presentation.viewmodel.habitslist.HabitsListSearchQuery
import com.pamparamm.habittracker.presentation.viewmodel.habitslist.HabitsListViewModel
import com.pamparamm.habittracker.presentation.viewmodel.habitslist.store.HabitsListState
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun HabitsListPageView(vm: HabitsListViewModel, navController: NavController) {
    val state = vm.state.collectAsStateWithLifecycle()
    val stateValue = state.value

    val isSelected = stateValue.selectedHabit?.let { true } ?: false

    val sheetState = rememberBottomSheetState(initialValue = BottomSheetValue.Collapsed)
    val scaffoldState = rememberBottomSheetScaffoldState(bottomSheetState = sheetState)
    val coroutineScope = rememberCoroutineScope()
    val context = LocalContext.current

    val closeSearchMenu: () -> Unit =
        { coroutineScope.launch { if (sheetState.isExpanded) sheetState.collapse() } }

    val goodHabitNotCompletedMessage = stringResource(R.string.habits_list_message_good_less)
    val goodHabitCompletedMessage = stringResource(R.string.habits_list_message_good_ge)
    val badHabitNotCompletedMessage = stringResource(R.string.habits_list_message_bad_less)
    val badHabitCompletedMessage = stringResource(R.string.habits_list_message_bad_ge)
    LaunchedEffect(stateValue) {
        if (stateValue.messages.any()) {
            val toastText = when (val firstMessage = stateValue.messages.first()) {
                is HabitsListMessage.GoodNotCompleted -> "${goodHabitNotCompletedMessage}: ${firstMessage.remaining}"
                HabitsListMessage.GoodCompleted -> goodHabitCompletedMessage
                is HabitsListMessage.BadNotCompleted -> "${badHabitNotCompletedMessage}: ${firstMessage.remaining}"
                HabitsListMessage.BadCompleted -> badHabitCompletedMessage
            }
            Toast.makeText(context, toastText, Toast.LENGTH_SHORT).show()

            vm.popMessage()
        }
    }

    BottomSheetScaffold(
        scaffoldState = scaffoldState,
        sheetContent = { HabitsListSearchComponent(vm, stateValue, closeSearchMenu) },
        sheetPeekHeight = 0.dp,
        sheetBackgroundColor = MaterialTheme.colors.surface
    ) {
        Column(
            Modifier.pointerInput(Unit) {
                detectTapGestures(onTap = { closeSearchMenu() })
            },
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Row {
                Text(text = timestampToString(stateValue.currentTimestamp))
            }
            Divider()
            HabitsListTypePager(vm)
            Divider()
            Row(Modifier.weight(1f)) {
                HabitsListContainerComponent(vm, stateValue)
            }
            Column {
                Row {
                    Button(
                        onClick = {
                            navController.navigate(NavRoutes.HABITS_EDITOR) {
                                popUpTo(NavRoutes.HABITS_LIST) {
                                    inclusive = true
                                }
                            }
                        },
                        Modifier
                            .weight(1f)
                            .fillMaxWidth()
                    ) {
                        Text(text = stringResource(R.string.habits_list_create))
                    }
                }
                Row {
                    Button(
                        onClick = { vm.syncWithRemote(); },
                        Modifier
                            .weight(1f)
                            .fillMaxWidth()
                    ) {
                        Text(text = stringResource(R.string.habits_list_sync))
                    }
                }
                Row(Modifier.padding(horizontal = 8.dp)) {
                    Button(
                        enabled = isSelected,
                        onClick = {
                            if (isSelected) navController.navigate("${NavRoutes.HABITS_EDITOR}?${NavRoutes.HABITS_EDITOR_HABIT_UUID}=${stateValue.selectedHabit?.id}") {
                                popUpTo(NavRoutes.HABITS_LIST) {
                                    inclusive = true
                                }
                            }
                        },
                        modifier = Modifier
                            .weight(1f)
                            .padding(end = 8.dp)
                    ) {
                        Text(text = stringResource(R.string.habits_list_edit))
                    }
                    Button(
                        enabled = isSelected,
                        onClick = { if (isSelected) vm.deleteSelectedHabit() },
                        modifier = Modifier
                            .weight(1f)
                            .padding(start = 8.dp)
                    ) {
                        Text(text = stringResource(R.string.habits_list_delete))
                    }
                }
                Row(Modifier.padding(horizontal = 8.dp)) {
                    Button(
                        onClick = {
                            coroutineScope.launch {
                                if (sheetState.isCollapsed) sheetState.expand()
                                else sheetState.collapse()
                            }
                        },
                        Modifier
                            .weight(1f)
                            .padding(end = 8.dp)
                    ) {
                        Text(text = stringResource(R.string.habits_list_search))
                    }
                    Button(
                        enabled = stateValue.searchQuery != HabitsListSearchQuery.empty(),
                        onClick = { vm.clearSearch() },
                        modifier = Modifier
                            .weight(1f)
                            .padding(start = 8.dp)
                    ) {
                        Text(text = stringResource(R.string.habits_list_search_clear))
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun HabitsListTypePager(vm: HabitsListViewModel) {
    val pagerState = rememberPagerState()
    val pagerValues = HabitsListFilterMode.values()

    LaunchedEffect(pagerState) {
        snapshotFlow { pagerState.currentPage }.collect { page ->
            vm.filterHabits(pagerValues[page])
        }
    }

    Row(
        Modifier.padding(16.dp)
    ) {
        HorizontalPager(
            pageCount = pagerValues.count(), state = pagerState
        ) { page ->
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(30.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                if (page > 0)
                    Icon(Icons.KeyboardArrowLeft, contentDescription = "")
                Spacer(Modifier.weight(1f))
                Card(
                    Modifier
                        .wrapContentHeight()
                        .height(30.dp),
                ) {
                    Text(
                        text = when (pagerValues[page]) {
                            HabitsListFilterMode.ALL -> stringResource(R.string.habits_list_filter_all)
                            HabitsListFilterMode.GOOD -> stringResource(R.string.habits_list_filter_good)
                            HabitsListFilterMode.BAD -> stringResource(R.string.habits_list_filter_bad)
                        },
                        textAlign = TextAlign.Center
                    )
                }
                Spacer(Modifier.weight(1f))
                if (page < pagerValues.count() - 1)
                    Icon(Icons.KeyboardArrowRight, contentDescription = "")
            }
        }
    }
}

@Composable
fun HabitsListContainerComponent(vm: HabitsListViewModel, stateValue: HabitsListState) {
    val habitsToShow = stateValue.habits
        .filter {
            val isMatchingFilter = when (stateValue.filterMode) {
                HabitsListFilterMode.ALL -> true
                HabitsListFilterMode.GOOD -> it.type == HabitType.GOOD
                HabitsListFilterMode.BAD -> it.type == HabitType.BAD
            }
            val isMatchingSearch = stateValue.searchQuery.matchesHabit(it)
            isMatchingFilter && isMatchingSearch
        }
        .map { habit ->
            habit.copy(completions = habit.completions.filter {
                vm.isTimestampInPeriod(stateValue.currentTimestamp, it, habit.period)
            })
        }

    LazyColumn(
        Modifier
            .noRippleClickable { vm.deselectHabit() }
            .padding(16.dp)
    ) {
        items(habitsToShow, key = { it.id }) { habit ->
            HabitCardComponent(habit, vm, stateValue)
            Divider()
        }

    }
}

@Composable
fun HabitCardComponent(habit: Habit, vm: HabitsListViewModel, stateValue: HabitsListState) {
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
            elevation = 5.dp,
            backgroundColor = when (habit.type) {
                HabitType.GOOD -> colorResource(R.color.good_habit_tint)
                HabitType.BAD -> colorResource(R.color.bad_habit_tint)
            }
        ) {
            Column {
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
                        Text(text = "${stringResource(R.string.habits_list_completions)}: ${habit.completions.count()} / ${habit.targetCompletions}")
                        Text(text = "${stringResource(R.string.habits_list_period)}: ${habit.period}")
                        Spacer(modifier = Modifier.height(8.dp))
                        if (isSelected && habit.description.isNotBlank()) {
                            Text(
                                text = habit.description.take(30),
                                style = MaterialTheme.typography.body1
                            )
                            Divider()
                        }
                    }
                    if (isSelected) {
                        Column(Modifier.padding(8.dp, 2.dp)) {
                            IconButton(onClick = { vm.completeSelectedHabit() }) {
                                Icon(
                                    Icons.CheckCircle,
                                    "",
                                    tint = MaterialTheme.colors.primary
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun HabitsListSearchComponent(
    vm: HabitsListViewModel,
    stateValue: HabitsListState,
    closeSearchMenu: () -> Unit
) {
    val query = stateValue.searchQuery

    Column(
        Modifier
            .fillMaxWidth()
            .padding(8.dp),
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = stringResource(R.string.habits_list_search),
            style = MaterialTheme.typography.h6
        )
        Divider()
        Spacer(modifier = Modifier.height(16.dp))
        Row(
            Modifier
                .verticalScroll(rememberScrollState())
                .weight(weight = 1f, fill = false)
        ) {
            Column(
                Modifier
                    .padding(16.dp),
                Arrangement.spacedBy(8.dp)
            ) {
                TextField(
                    modifier = Modifier.fillMaxWidth(),
                    label = { Text(stringResource(R.string.habits_list_search_title)) },
                    value = query.title ?: "",
                    onValueChange = {
                        vm.searchHabits(query.copy(title = it.normalize(true).let {
                            it.ifBlank { null }
                        }))
                    },
                    singleLine = true
                )
                HabitsListSearchPriorityComponent(vm, query)
                HabitsListSearchCompletionComponent(vm, query)
            }
        }
        Spacer(modifier = Modifier.height(16.dp))
        Row(Modifier.padding(horizontal = 8.dp)) {
            Button(
                onClick = { closeSearchMenu() },
                Modifier
                    .weight(1f)
                    .fillMaxWidth()
            ) {
                Text(text = stringResource(R.string.habits_list_search_close))
            }
        }
    }
}

@Composable
fun HabitsListSearchPriorityComponent(vm: HabitsListViewModel, query: HabitsListSearchQuery) {
    val expanded = remember { mutableStateOf(false) }
    val priorities = enumValues<HabitPriority>()
    Row(modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
        Text(
            text = "${stringResource(R.string.habits_list_search_priority)}: ",
            Modifier.padding(end = 8.dp)
        )
        Card(
            elevation = 5.dp, modifier = Modifier
                .weight(1f)
                .clickable(onClick = { expanded.value = true })
        )
        {
            Row(Modifier.fillMaxWidth()) {
                Text(
                    query.priority?.nameResource()
                        ?: stringResource(R.string.habits_list_search_unselected)
                )
                Spacer(Modifier.weight(1f))
                when (expanded.value) {
                    true -> Icon(Icons.KeyboardArrowUp, contentDescription = "")
                    false -> Icon(Icons.KeyboardArrowDown, contentDescription = "")
                }

            }
            DropdownMenu(expanded = expanded.value, onDismissRequest = { expanded.value = false }) {
                priorities.map {
                    DropdownMenuItem(onClick = {
                        vm.searchHabits(query.copy(priority = it)); expanded.value = false
                    }) {
                        Text(it.nameResource())
                    }
                }
            }
        }
        IconButton(onClick = { vm.searchHabits(query.copy(priority = null)) }) {
            Icon(Icons.Clear, contentDescription = "")
        }
    }
}

@Composable
fun HabitsListSearchCompletionComponent(vm: HabitsListViewModel, query: HabitsListSearchQuery) {
    val expanded = remember { mutableStateOf(false) }

    Row(modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
        Text(
            text = "${stringResource(R.string.habits_list_search_completion_status)}: ",
            Modifier.padding(end = 8.dp)
        )
        Card(
            elevation = 5.dp, modifier = Modifier
                .weight(1f)
                .clickable(onClick = { expanded.value = true })
        )
        {
            Row(Modifier.fillMaxWidth()) {
                Text(
                    query.isCompleted?.let {
                        when (it) {
                            true -> stringResource(R.string.habits_list_search_complete)
                            false -> stringResource(R.string.habits_list_search_incomplete)
                        }
                    }
                        ?: stringResource(R.string.habits_list_search_unselected)
                )
                Spacer(Modifier.weight(1f))
                when (expanded.value) {
                    true -> Icon(Icons.KeyboardArrowUp, contentDescription = "")
                    false -> Icon(Icons.KeyboardArrowDown, contentDescription = "")
                }

            }
            DropdownMenu(expanded = expanded.value, onDismissRequest = { expanded.value = false }) {
                DropdownMenuItem(onClick = {
                    vm.searchHabits(query.copy(isCompleted = false)); expanded.value = false
                }) {
                    Text(stringResource(R.string.habits_list_search_incomplete))
                }
                DropdownMenuItem(onClick = {
                    vm.searchHabits(query.copy(isCompleted = true)); expanded.value = false
                }) {
                    Text(stringResource(R.string.habits_list_search_complete))
                }
            }
        }
        IconButton(onClick = { vm.searchHabits(query.copy(isCompleted = null)) }) {
            Icon(Icons.Clear, contentDescription = "")
        }
    }
}