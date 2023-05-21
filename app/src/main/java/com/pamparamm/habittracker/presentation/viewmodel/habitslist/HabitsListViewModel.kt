package com.pamparamm.habittracker.presentation.viewmodel.habitslist

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.pamparamm.habittracker.domain.entities.Habit
import com.pamparamm.habittracker.domain.usecases.HabitsUseCase
import com.pamparamm.habittracker.presentation.viewmodel.habitslist.store.HabitsListAction
import com.pamparamm.habittracker.presentation.viewmodel.habitslist.store.HabitsListReducer
import com.pamparamm.habittracker.presentation.viewmodel.habitslist.store.HabitsListState
import com.pamparamm.habittracker.presentation.viewmodel.habitslist.store.HabitsListUseCaseMiddleware
import com.pamparamm.habittracker.presentation.viewmodel.store.Store
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HabitsListViewModel @Inject constructor(
    habitsUseCase: HabitsUseCase
) : ViewModel() {

    private val store = Store(
        initState = HabitsListState.initialState(),
        reducer = HabitsListReducer(),
        middlewares = listOf(HabitsListUseCaseMiddleware(habitsUseCase))
    )

    val state: StateFlow<HabitsListState> = store.state

    init {
        observeHabits()
    }

    private fun observeHabits() = dispatch(HabitsListAction.ObserveHabits)

    fun selectHabit(habit: Habit) = dispatch(HabitsListAction.SelectHabit(habit))
    fun deselectHabit() = dispatch(HabitsListAction.DeselectHabit)
    fun deleteSelectedHabit() = dispatch(HabitsListAction.DeleteSelectedHabit)
    fun completeSelectedHabit() = dispatch(HabitsListAction.CompleteSelectedHabit)

    fun filterHabits(viewMode: HabitsListFilterMode) = HabitsListAction.FilterHabits(viewMode)
    fun searchHabits(searchQuery: HabitsListSearchQuery) =
        HabitsListAction.SearchHabits(searchQuery)

    private fun dispatch(action: HabitsListAction) {
        viewModelScope.launch(Dispatchers.IO) {
            store.dispatch(action)
        }
    }
}