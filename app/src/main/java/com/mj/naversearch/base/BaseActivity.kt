package com.mj.naversearch.base

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.lifecycle.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import timber.log.Timber
import kotlin.coroutines.CoroutineContext

abstract class BaseActivity<T : ViewDataBinding, V : ViewModel> : AppCompatActivity(), CoroutineScope {

    override val coroutineContext: CoroutineContext = lifecycleScope.coroutineContext

    lateinit var binding: T

    abstract val layoutResourceId: Int
    abstract val viewModel: V
    abstract fun initOnCreate(savedInstanceState: Bundle?)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, layoutResourceId)
        binding.lifecycleOwner = this
        initOnCreate(savedInstanceState)
        Timber.e("onCreate")
    }

    override fun onStart() {
        super.onStart()
        Timber.e("onStart")
    }

    override fun onResume() {
        super.onResume()
        Timber.e("onResume")
    }

    override fun onPause() {
        super.onPause()
        Timber.e("onPause")
    }

    override fun onStop() {
        super.onStop()
        Timber.e("onStop")
    }

    override fun onDestroy() {
        super.onDestroy()
        Timber.e("onDestroy")
    }

    override fun onRestart() {
        super.onRestart()
        Timber.e("onRestart")
    }

    protected fun shortShowToast(msg: String) =
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show()

    fun LifecycleOwner.repeatOnCreated(block: suspend CoroutineScope.() -> Unit) {
        launch {
            lifecycle.repeatOnLifecycle(Lifecycle.State.CREATED, block)
        }
    }

    fun LifecycleOwner.repeatOnStarted(block: suspend CoroutineScope.() -> Unit) {
        launch {
            lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED, block)
        }
    }
}