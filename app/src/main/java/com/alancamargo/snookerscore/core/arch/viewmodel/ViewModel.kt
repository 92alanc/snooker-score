package com.alancamargo.snookerscore.core.arch.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

abstract class ViewModel<S : UiState, A : UiAction>(initialState: S) : ViewModel() {

    private val stateLiveData = MutableLiveData(initialState)
    private val actionLiveData = MutableLiveData<A>()

    val state: LiveData<S> = stateLiveData
    val action: LiveData<A> = actionLiveData

    protected fun setState(block: (S) -> S) {
        stateLiveData.value = block(stateLiveData.value!!)
    }

    protected fun sendAction(block: () -> A) {
        actionLiveData.value = block()
    }

}
