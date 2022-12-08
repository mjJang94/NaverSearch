package com.mj.naversearch.util

import androidx.annotation.MainThread
import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData

@MainThread
fun <X> LiveData<X>.hide(): LiveData<X> =
    MediatorLiveData<X>().apply {
        addSource(this@hide) { newValue ->
            value = newValue
        }
    }

@MainThread
fun <X> LiveData<X>.event(): LiveData<X> =
    EventLiveData<X>().apply {
        addSource(this@event) { newValue ->
            setValue(newValue)
        }
    }