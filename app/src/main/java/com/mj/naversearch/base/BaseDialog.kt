package com.mj.naversearch.base

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatDialogFragment
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.Fragment
import androidx.fragment.app.setFragmentResult
import androidx.fragment.app.viewModels
import androidx.lifecycle.*
import com.mj.naversearch.ui.common.ktx.extraRequestKey
import kotlinx.coroutines.CoroutineScope
import java.util.concurrent.atomic.AtomicBoolean
import kotlin.coroutines.CoroutineContext
import kotlin.reflect.KProperty

abstract class BaseDialog<T : ViewDataBinding, R : ViewModel> : AppCompatDialogFragment(), CoroutineScope {
    override val coroutineContext: CoroutineContext = lifecycleScope.coroutineContext

    lateinit var binding: T

    abstract val layoutResourceId: Int
    abstract val viewModel: R

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = DataBindingUtil.inflate(inflater, layoutResourceId, container, false)
        binding.lifecycleOwner = viewLifecycleOwner
        return binding.root
    }

    fun toast(text: String) {
        Toast.makeText(requireContext(), text, Toast.LENGTH_SHORT).show()
    }

    fun Fragment.dispatchResult(result: Bundle.() -> Unit = {}) {
        val arguments = arguments ?: return
        val key = arguments.extraRequestKey ?: return
        val vm: ResultViewModel by viewModels { ResultViewModel }
        if (!vm.resultDispatched.compareAndSet(false, true)) return
        setFragmentResult(key, Bundle().apply(result))
    }

    private class ResultViewModel : ViewModel() {
        val resultDispatched = AtomicBoolean(false)

        companion object Factory : ViewModelProvider.Factory {
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                return modelClass.newInstance()
            }
        }
    }

    class StringExtra(name: String) : ExtraBase<String?>(
        name, { getString(name) }, { putString(name, it) }
    )

    abstract class ExtraBase<T>(
        private val name: String,
        private val getValue: Bundle.() -> T,
        private val putValue: Bundle.(T) -> Unit
    ) {
        operator fun getValue(intent: Intent, property: KProperty<*>): T {
            return (intent.extras ?: Bundle()).getValue()
        }

        operator fun setValue(intent: Intent, property: KProperty<*>, value: T) {
            intent.putExtras(Bundle().apply { putValue(value) })
        }

        operator fun getValue(bundle: Bundle, property: KProperty<*>): T {
            return bundle.getValue()
        }

        operator fun setValue(bundle: Bundle, property: KProperty<*>, value: T) {
            if (value == null) bundle.remove(name) else bundle.putValue(value)
        }
    }
}