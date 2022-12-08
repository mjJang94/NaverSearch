package com.mj.naversearch.base

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import kotlin.coroutines.CoroutineContext

abstract class BaseFragment<T : ViewDataBinding, R : ViewModel> : Fragment(), CoroutineScope {
    override val coroutineContext: CoroutineContext = lifecycleScope.coroutineContext

    lateinit var binding: T

    abstract val layoutResourceId: Int
    abstract val viewModel: R
    abstract fun initOnCreateView()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = DataBindingUtil.inflate(inflater, layoutResourceId, container, false)
        binding.lifecycleOwner = viewLifecycleOwner
        initOnCreateView()
        return binding.root
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