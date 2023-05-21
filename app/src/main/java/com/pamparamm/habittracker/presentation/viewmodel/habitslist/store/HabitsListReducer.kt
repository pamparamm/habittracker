package com.pamparamm.habittracker.presentation.viewmodel.habitslist.store

import com.pamparamm.habittracker.presentation.viewmodel.store.Reducer

class HabitsListReducer : Reducer<HabitsListState, HabitsListAction> {
    override fun invoke(state: HabitsListState, action: HabitsListAction): HabitsListState =
        when (action) {
            is HabitsListAction.GetHabits -> state.copy(habits = action.habits)
            is HabitsListAction.SelectHabit -> state.copy(selectedHabit = action.habit)
            HabitsListAction.DeselectHabit -> state.copy(selectedHabit = null)
            is HabitsListAction.FilterHabits -> state.copy(filterMode = action.filterMode)
            is HabitsListAction.SearchHabits -> state.copy(searchQuery = action.searchQuery)
            else -> state
        }
}