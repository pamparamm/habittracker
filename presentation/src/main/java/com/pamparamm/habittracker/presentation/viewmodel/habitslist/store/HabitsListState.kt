package com.pamparamm.habittracker.presentation.viewmodel.habitslist.store

import com.pamparamm.habittracker.domain.entities.Habit
import com.pamparamm.habittracker.presentation.viewmodel.habitslist.HabitsListFilterMode
import com.pamparamm.habittracker.presentation.viewmodel.habitslist.HabitsListMessage
import com.pamparamm.habittracker.presentation.viewmodel.habitslist.HabitsListSearchQuery
import com.pamparamm.habittracker.presentation.viewmodel.store.State

data class HabitsListState(
    val habits: List<Habit>,
    val selectedHabit: Habit?,
    val filterMode: HabitsListFilterMode,
    val searchQuery: HabitsListSearchQuery,
    val currentTimestamp: Long,
    val messages: List<HabitsListMessage>
) : State {
    companion object {
        fun initialState(): HabitsListState = HabitsListState(
            habits = emptyList(),
            selectedHabit = null,
            filterMode = HabitsListFilterMode.ALL,
            searchQuery = HabitsListSearchQuery.empty(),
            currentTimestamp = 0,
            messages = emptyList()
        )
    }
}