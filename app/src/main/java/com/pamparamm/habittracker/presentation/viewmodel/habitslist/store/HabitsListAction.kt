package com.pamparamm.habittracker.presentation.viewmodel.habitslist.store

import com.pamparamm.habittracker.domain.entities.Habit
import com.pamparamm.habittracker.presentation.viewmodel.habitslist.HabitsListFilterMode
import com.pamparamm.habittracker.presentation.viewmodel.habitslist.HabitsListMessage
import com.pamparamm.habittracker.presentation.viewmodel.habitslist.HabitsListSearchQuery
import com.pamparamm.habittracker.presentation.viewmodel.store.Action

sealed class HabitsListAction : Action {
    object ObserveHabits : HabitsListAction()
    data class UpdateClock(val timestamp: Long) : HabitsListAction()
    data class GetHabits(val habits: List<Habit>) : HabitsListAction()
    data class SelectHabit(val habit: Habit) : HabitsListAction()
    object DeselectHabit : HabitsListAction()
    object DeleteSelectedHabit : HabitsListAction()
    object CompleteSelectedHabit : HabitsListAction()
    data class FilterHabits(val filterMode: HabitsListFilterMode) : HabitsListAction()
    data class SearchHabits(val searchQuery: HabitsListSearchQuery) : HabitsListAction()
    object ClearSearch : HabitsListAction()
    object SyncWithRemote : HabitsListAction()
    data class PushCompletionMessage(val habit: Habit) : HabitsListAction()
    data class PushMessage(val message: HabitsListMessage) : HabitsListAction()
    object PopMessage : HabitsListAction()
}