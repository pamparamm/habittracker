package com.pamparamm.habittracker.presentation.viewmodel.habitseditor

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.pamparamm.habittracker.presentation.NavRoutes
import com.pamparamm.habittracker.domain.usecases.HabitsUseCase
import com.pamparamm.habittracker.presentation.viewmodel.habitseditor.store.HabitsEditorAction
import com.pamparamm.habittracker.presentation.viewmodel.habitseditor.store.HabitsEditorReducer
import com.pamparamm.habittracker.presentation.viewmodel.habitseditor.store.HabitsEditorState
import com.pamparamm.habittracker.presentation.viewmodel.habitseditor.store.HabitsEditorUseCaseMiddleware
import com.pamparamm.habittracker.presentation.viewmodel.store.Store
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import java.util.UUID
import javax.inject.Inject

@HiltViewModel
class HabitsEditorViewModel @Inject constructor(
    habitsUseCase: HabitsUseCase,
    savedStateHandle: SavedStateHandle
) : ViewModel() {
    private val store = Store(
        initState = HabitsEditorState.initialState(),
        reducer = HabitsEditorReducer(),
        middlewares = listOf(HabitsEditorUseCaseMiddleware(habitsUseCase))
    )
    private val selectedHabitUUID =
        savedStateHandle.get<String>(NavRoutes.HABITS_EDITOR_HABIT_UUID)?.let {
            UUID.fromString(it)
        }

    val state: StateFlow<HabitsEditorState> = store.state

    init {
        initHabitEditor(selectedHabitUUID)
    }

    private fun initHabitEditor(habitUUID: UUID?) =
        dispatch(HabitsEditorAction.InitHabitEditor(habitUUID))

    fun updateHabitData(habitData: HabitsEditorHabitData) =
        dispatch(HabitsEditorAction.UpdateHabitData(habitData))

    fun saveHabit() = dispatch(HabitsEditorAction.SaveHabit)
    fun cancel() = dispatch(HabitsEditorAction.Cancel)

    private fun dispatch(action: HabitsEditorAction) {
        viewModelScope.launch(Dispatchers.IO) {
            store.dispatch(action)
        }
    }
}