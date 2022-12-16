package com.mj.naversearch.base

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import timber.log.Timber
import kotlin.coroutines.CoroutineContext

abstract class BaseFragment<T : ViewDataBinding> : Fragment(), CoroutineScope {
    override val coroutineContext: CoroutineContext = lifecycleScope.coroutineContext

    lateinit var binding: T

    abstract val layoutResourceId: Int
    abstract fun initOnViewCreated(savedInstanceState: Bundle?)

    override fun onAttach(context: Context) {
        super.onAttach(context)
        Timber.d("fragment onAttach()")
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Timber.d("fragment onCreate()")
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = DataBindingUtil.inflate(inflater, layoutResourceId, container, false)
        binding.lifecycleOwner = viewLifecycleOwner
        Timber.d("fragment onCreateView()")
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initOnViewCreated(savedInstanceState)
        Timber.d("fragment onViewCreated()")
    }

    override fun onStart() {
        super.onStart()
        Timber.d("fragment onStart()")
    }

    override fun onPause() {
        super.onPause()
        Timber.d("fragment onPause()")
    }

    override fun onStop() {
        super.onStop()
        Timber.d("fragment onStop()")
    }

    override fun onDestroy() {
        super.onDestroy()
        Timber.d("fragment onDestroy()")
    }

    override fun onDetach() {
        super.onDetach()
        Timber.d("fragment onDetach()")
    }

    fun repeatOnOwnerCreated(block: suspend CoroutineScope.() -> Unit){
        launch{
            viewLifecycleOwner.lifecycle.repeatOnLifecycle(Lifecycle.State.CREATED, block)
        }
    }

    fun repeatOnOwnerStarted(block: suspend CoroutineScope.() -> Unit) {
        launch {
            viewLifecycleOwner.lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED, block)
        }
    }

    fun toast(text: String){
        Toast.makeText(requireContext(), text, Toast.LENGTH_SHORT).show()
    }
}