package com.pamparamm.habittracker.presentation.viewmodel.habitslist.store

import com.pamparamm.habittracker.domain.entities.HabitType
import com.pamparamm.habittracker.domain.services.TimestampService
import com.pamparamm.habittracker.presentation.viewmodel.habitslist.HabitsListMessage
import com.pamparamm.habittracker.presentation.viewmodel.store.Middleware
import com.pamparamm.habittracker.presentation.viewmodel.store.Store
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.launch

class HabitsListClockMiddleware(
    private val timestampService: TimestampService,
    private val scope: CoroutineScope
) : Middleware<HabitsListState, HabitsListAction> {
    override suspend fun invoke(
        state: HabitsListState,
        action: HabitsListAction,
        store: Store<HabitsListState, HabitsListAction>
    ) {
        when (action) {
            HabitsListAction.ObserveHabits -> scope.launch {
                timestampService.clock().distinctUntilChanged().collect {
                    store.dispatch(HabitsListAction.UpdateClock(it))
                }
            }

            is HabitsListAction.PushCompletionMessage -> {
                val currentCompletions = action.habit.completions.filter {
                    timestampService.periodsBetween(
                        state.currentTimestamp,
                        it
                    ) < action.habit.period
                }
                val remaining = action.habit.targetCompletions - currentCompletions.count() - 1
                val message = when (action.habit.type) {
                    HabitType.GOOD ->
                        if (remaining >= 0) HabitsListMessage.GoodNotCompleted(remaining)
                        else HabitsListMessage.GoodCompleted

                    HabitType.BAD ->
                        if (remaining >= 0) HabitsListMessage.BadNotCompleted(remaining)
                        else HabitsListMessage.BadCompleted
                }
                store.dispatch(HabitsListAction.PushMessage(message))
            }

            else -> {}
        }
    }
}