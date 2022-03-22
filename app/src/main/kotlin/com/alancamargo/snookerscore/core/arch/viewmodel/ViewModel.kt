package com.alancamargo.snookerscore.core.arch.viewmodel

import androidx.annotation.VisibleForTesting
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

abstract class ViewModel<S : UiState, A : UiAction>(initialState: S) : ViewModel() {

    private val stateLiveData = MutableLiveData(initialState)
    private val actionLiveData = MutableLiveData<A>()

    @VisibleForTesting
    val state: LiveData<S> = stateLiveData

    @VisibleForTesting
    val action: LiveData<A> = actionLiveData

    protected fun setState(block: (S) -> S) {
        stateLiveData.value = block(stateLiveData.value!!)
    }

    protected fun sendAction(block: () -> A) {
        actionLiveData.value = block()
    }

}
