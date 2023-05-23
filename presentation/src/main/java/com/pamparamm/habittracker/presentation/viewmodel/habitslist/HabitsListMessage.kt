package com.pamparamm.habittracker.presentation.viewmodel.habitslist

sealed class HabitsListMessage {
    data class GoodNotCompleted(val remaining: Int) : HabitsListMessage()
    object GoodCompleted : HabitsListMessage()
    data class BadNotCompleted(val remaining: Int) : HabitsListMessage()
    object BadCompleted : HabitsListMessage()
    object Error : HabitsListMessage()
}