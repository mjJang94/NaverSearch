package com.mj.naversearch.ui.search

import android.app.Dialog
import android.os.Bundle
import android.os.Parcelable
import android.view.View
import android.view.WindowManager
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.viewModels
import androidx.lifecycle.whenCreated
import com.mj.naversearch.R
import com.mj.naversearch.base.BaseDialog
import com.mj.naversearch.databinding.DialogSearchBinding
import com.mj.naversearch.ui.common.result.FragmentResultContract
import com.mj.naversearch.ui.common.style.StatusBarStyle
import com.mj.naversearch.ui.common.util.OnBackPressedListener
import com.mj.naversearch.ui.common.util.doOnBackPressed
import com.mj.naversearch.ui.search.SearchViewModel.SearchEvent.Dismiss
import com.mj.naversearch.ui.search.SearchViewModel.SearchEvent.Search
import com.mj.naversearch.util.showKeyboard
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import kotlinx.parcelize.Parcelize

class SearchDialog : BaseDialog<DialogSearchBinding, SearchViewModel>(), CoroutineScope, OnBackPressedListener {


    @Parcelize
    data class Result(
        val keyword: String
    ) : Parcelable

    class Contract : FragmentResultContract<Unit, Result?> {
        override fun onLaunch(fragmentManager: FragmentManager, arguments: Bundle) {
            SearchDialog().apply {
                this.arguments = arguments
            }.show(fragmentManager, SearchDialog::class.simpleName)
        }

        override fun parseResult(bundle: Bundle): Result? = bundle.getParcelable("EXTRA_RESULT")
    }

    override val layoutResourceId: Int
        get() = R.layout.dialog_search

    override val viewModel: SearchViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(STYLE_NO_FRAME, R.style.Theme_NaverSearch)
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        isCancelable = false
        return super.onCreateDialog(savedInstanceState)
            .apply {
                doOnBackPressed(this@SearchDialog)
                window?.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE)
            }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        StatusBarStyle.SEARCH.apply(this)
        binding.vm = viewModel

        launch {
            viewLifecycleOwner.whenCreated {
                viewModel.uiEvent.collect { event ->
                    when(event){
                        is Search -> {
                            dispatchResult { putParcelable("EXTRA_RESULT", Result(event.query)) }
                            dismissAllowingStateLoss()
                        }
                        is Dismiss -> {
                            dispatchResult()
                            dismissAllowingStateLoss()
                        }
                    }
                }
            }
        }
    }

    override fun onResume() {
        super.onResume()
        binding.editQuery.showKeyboard()
    }

    override fun onBackPressed(): Boolean {
        dispatchResult()
        dismissAllowingStateLoss()
        return true
    }
}