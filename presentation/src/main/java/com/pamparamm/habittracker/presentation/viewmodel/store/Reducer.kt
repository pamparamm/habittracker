package com.pamparamm.habittracker.presentation.viewmodel.store

interface Reducer<S: State, A: Action> {
    operator fun invoke(state: S, action: A): S
}