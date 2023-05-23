package com.pamparamm.habittracker.presentation.viewmodel.habitslist

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.pamparamm.habittracker.domain.entities.Habit
import com.pamparamm.habittracker.domain.services.TimestampService
import com.pamparamm.habittracker.domain.usecases.HabitsUseCase
import com.pamparamm.habittracker.presentation.viewmodel.habitslist.store.HabitsListAction
import com.pamparamm.habittracker.presentation.viewmodel.habitslist.store.HabitsListClockMiddleware
import com.pamparamm.habittracker.presentation.viewmodel.habitslist.store.HabitsListReducer
import com.pamparamm.habittracker.presentation.viewmodel.habitslist.store.HabitsListState
import com.pamparamm.habittracker.presentation.viewmodel.habitslist.store.HabitsListUseCaseMiddleware
import com.pamparamm.habittracker.presentation.viewmodel.store.Store
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HabitsListViewModel @Inject constructor(
    habitsUseCase: HabitsUseCase,
    private val timestampService: TimestampService
) : ViewModel() {

    private val store = Store(
        initState = HabitsListState.initialState(),
        reducer = HabitsListReducer(),
        middlewares = listOf(
            HabitsListUseCaseMiddleware(habitsUseCase, viewModelScope),
            HabitsListClockMiddleware(timestampService, viewModelScope)
        ),
    )

    val state: StateFlow<HabitsListState> = store.state

    init {
        observeHabits()
    }

    fun isTimestampInPeriod(now: Long, then: Long, period: Int) =
        timestampService.periodsBetween(now, then) < period

    private fun observeHabits() = dispatch(HabitsListAction.ObserveHabits)

    fun selectHabit(habit: Habit) = dispatch(HabitsListAction.SelectHabit(habit))
    fun deselectHabit() = dispatch(HabitsListAction.DeselectHabit)
    fun deleteSelectedHabit() = dispatch(HabitsListAction.DeleteSelectedHabit)
    fun completeSelectedHabit() = dispatch(HabitsListAction.CompleteSelectedHabit)

    fun filterHabits(viewMode: HabitsListFilterMode) =
        dispatch(HabitsListAction.FilterHabits(viewMode))

    fun searchHabits(searchQuery: HabitsListSearchQuery) =
        dispatch(HabitsListAction.SearchHabits(searchQuery))

    fun clearSearch() = dispatch(HabitsListAction.ClearSearch)

    fun syncWithRemote() = dispatch(HabitsListAction.SyncWithRemote)

    fun popMessage() = dispatch(HabitsListAction.PopMessage)

    private fun dispatch(action: HabitsListAction) {
        viewModelScope.launch {
            try {
                store.dispatch(action)
            } catch (e: Exception) {
                store.dispatch(HabitsListAction.PushMessage(HabitsListMessage.Error))
            }
        }
    }
}