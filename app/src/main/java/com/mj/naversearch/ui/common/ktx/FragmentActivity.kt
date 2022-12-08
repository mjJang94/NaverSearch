package com.mj.naversearch.ui.common.ktx

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LifecycleOwner
import com.mj.naversearch.ui.common.result.FragmentResultCallback
import com.mj.naversearch.ui.common.result.FragmentResultContract
import com.mj.naversearch.ui.common.result.FragmentResultLauncher
import java.util.*

fun FragmentActivity.setFragmentResultListener(
    requestKey: String,
    listener: ((requestKey: String, bundle: Bundle) -> Unit)
) {
    supportFragmentManager.setFragmentResultListener(requestKey, this, listener)
}

fun FragmentActivity.clearFragmentResultListener(
    requestKey: String
) {
    supportFragmentManager.clearFragmentResultListener(requestKey)
}

fun <I, O> FragmentActivity.registerForFragmentResult(
    requestKey: String,
    contract: FragmentResultContract<I, O>,
    callback: FragmentResultCallback<O>
): FragmentResultLauncher<I> {
    setFragmentResultListener(requestKey) { _, result ->
        callback.onFragmentResult(contract.parseResult(result))
    }
    return FragmentResultLauncher { input ->
        whenStarted {
            val arguments = (contract.createArguments(this, input) ?: Bundle())
                .apply { extraRequestKey = requestKey }
            runCatching {
                if (!supportFragmentManager.isDestroyed) {
                    contract.onLaunch(supportFragmentManager, arguments)
                }
            }.onFailure { tr ->
                Log.w("$tr", "onLaunch()")
            }
        }
    }
}

private fun FragmentActivity.whenStarted(action: () -> Unit) {
    lifecycle.addObserver(object : DefaultLifecycleObserver {
        override fun onStart(owner: LifecycleOwner) {
            lifecycle.removeObserver(this)
            action()
        }
    })
}
