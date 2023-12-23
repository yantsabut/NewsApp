package com.aston.news.base

import androidx.lifecycle.ViewModel

@Suppress("UNCHECKED_CAST")
abstract class BaseViewModel<T: BaseIntent> : ViewModel() {

    private var _intent: BaseIntent? = null

    protected val event: T
        get() = _intent as T

    abstract fun onTriggerEvent(eventType: T)
}