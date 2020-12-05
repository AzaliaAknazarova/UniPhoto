package com.example.uniphoto.base.extensions

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations

fun <T> LiveData<T?>.filterNotNull(): LiveData<T> = MediatorLiveData<T>().also { mediator ->
    mediator.addSource(this) { if (it != null) mediator.value = it }
}

fun <T, R> LiveData<T>.map(mapper: (T) -> R): LiveData<R> = Transformations.map(this, mapper)

fun <T, R> LiveData<T>.mapNotNull(mapper: (T) -> R?): LiveData<R> = map(mapper).filterNotNull()

fun <T> LiveData<T>.mutable() = this as MutableLiveData<T>

fun <T> MutableLiveData<T>.notifyObserver() {
    this.value = this.value
}
