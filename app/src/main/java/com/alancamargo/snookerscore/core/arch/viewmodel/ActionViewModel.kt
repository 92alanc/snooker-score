package com.alancamargo.snookerscore.core.arch.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

abstract class ActionViewModel<A : UiAction> : ViewModel() {

    private val actionLiveData = MutableLiveData<A>()

    val action: LiveData<A> = actionLiveData

    protected fun sendAction(block: () -> A) {
        actionLiveData.value = block()
    }

}
