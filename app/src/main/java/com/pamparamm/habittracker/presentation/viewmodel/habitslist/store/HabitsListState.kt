package com.pamparamm.habittracker.presentation.viewmodel.habitslist.store

import com.pamparamm.habittracker.domain.entities.Habit
import com.pamparamm.habittracker.presentation.viewmodel.habitslist.HabitsListFilterMode
import com.pamparamm.habittracker.presentation.viewmodel.habitslist.HabitsListSearchQuery
import com.pamparamm.habittracker.presentation.viewmodel.store.State

data class HabitsListState(
    val habits: List<Habit>,
    val selectedHabit: Habit?,
    val filterMode: HabitsListFilterMode,
    val searchQuery: HabitsListSearchQuery
) : State {
    companion object {
        fun initialState(): HabitsListState = HabitsListState(
            habits = emptyList(),
            selectedHabit = null,
            filterMode = HabitsListFilterMode.ALL,
            searchQuery = HabitsListSearchQuery(null, null, null)
        )
    }
}