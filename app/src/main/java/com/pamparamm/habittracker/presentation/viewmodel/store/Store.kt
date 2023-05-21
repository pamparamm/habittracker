package com.pamparamm.habittracker.presentation.viewmodel.store

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class Store<S : State, A : Action>(
    initState: S,
    private val reducer: Reducer<S, A>,
    private val middlewares: List<Middleware<S, A>> = emptyList()
) {
    private val mutState = MutableStateFlow(initState)
    val state: StateFlow<S> = mutState

    suspend fun dispatch(action: A) {
        middlewares.forEach { middleware -> middleware(state.value, action, this) }

        val newState = reducer(state.value, action)
        mutState.value = newState
    }
}