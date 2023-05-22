package com.pamparamm.habittracker.presentation.viewmodel.habitslist.store

import com.pamparamm.habittracker.presentation.viewmodel.habitslist.HabitsListSearchQuery
import com.pamparamm.habittracker.presentation.viewmodel.store.Reducer

class HabitsListReducer : Reducer<HabitsListState, HabitsListAction> {
    override fun invoke(state: HabitsListState, action: HabitsListAction): HabitsListState =
        when (action) {
            is HabitsListAction.UpdateClock -> state.copy(currentTimestamp = action.timestamp)
            is HabitsListAction.GetHabits -> state.copy(habits = action.habits)
            is HabitsListAction.SelectHabit -> state.copy(selectedHabit = action.habit)
            HabitsListAction.DeselectHabit -> state.copy(selectedHabit = null)
            is HabitsListAction.FilterHabits -> state.copy(filterMode = action.filterMode)
            is HabitsListAction.SearchHabits -> state.copy(searchQuery = action.searchQuery)
            HabitsListAction.ClearSearch -> state.copy(searchQuery = HabitsListSearchQuery.empty())
            is HabitsListAction.PushMessage -> state.copy(messages = state.messages.plus(action.message))
            HabitsListAction.PopMessage -> state.copy(messages = state.messages.drop(1))
            else -> state
        }
}