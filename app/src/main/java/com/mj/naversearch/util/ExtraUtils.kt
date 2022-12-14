package com.mj.naversearch.util

import android.content.Intent
import android.os.Bundle
import kotlin.reflect.KProperty


class StringExtra(name: String, val default: String = "") : ExtraBase<String?>(
    name, { getString(name, default) }, { putString(name, it) }
)

abstract class ExtraBase<T>(
    val name: String,
    private val getValue: Bundle.() -> T,
    private val setValue: Bundle.(T) -> Unit
) {
    operator fun getValue(intent: Intent, property: KProperty<*>): T {
        return (intent.extras ?: Bundle()).getValue()
    }

    operator fun setValue(intent: Intent, property: KProperty<*>, value: T) {
        intent.putExtras(Bundle().apply { setValue(value) })
    }

    operator fun getValue(bundle: Bundle, property: KProperty<*>): T {
        return bundle.getValue()
    }

    operator fun setValue(bundle: Bundle, property: KProperty<*>, value: T) {
        if (value == null) bundle.remove(name) else bundle.setValue(value)
    }
}