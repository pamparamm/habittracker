package com.pamparamm.habittracker.presentation.viewmodel.store

interface Middleware<S : State, A : Action> {
    suspend operator fun invoke(state: S, action: A, store: Store<S, A>)
}