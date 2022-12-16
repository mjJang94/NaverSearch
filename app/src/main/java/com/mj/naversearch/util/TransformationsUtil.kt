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

data class ConsistInLiveData<X>(
    private val key: Any?,
    private val clazz: Class<X>,
) : MediatorLiveData<X>() {
    override fun equals(other: Any?): Boolean =
        other is ConsistInLiveData<*> && key == other.key && clazz == other.clazz

    override fun toString(): String = "ConsistInLiveData($key, $clazz)"

    override fun hashCode(): Int = toString().hashCode()
}

@MainThread
inline fun <reified X> LiveData<X>.consistIn(key: Any?): ConsistInLiveData<X> =
    ConsistInLiveData(key, X::class.java).apply {
        addSource(this@consistIn) { newValue ->
            value = newValue
        }
    }