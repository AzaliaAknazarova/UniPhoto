package com.example.uniphoto.base.lifecycle

import androidx.annotation.MainThread
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.Observer

class LiveArgEvent <T> : (T) -> Unit {

    private val liveData = SingleLiveEvent<T>()

    fun observe(owner: LifecycleOwner, observer: Observer<in T>) = liveData.observe(owner, observer)

    @MainThread
    override fun invoke(arg: T) { liveData.value = arg }
}