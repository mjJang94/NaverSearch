package com.mj.naversearch.ui.main

import android.os.Bundle
import androidx.activity.viewModels
import com.mj.naversearch.R
import com.mj.naversearch.base.BaseActivity
import com.mj.naversearch.databinding.ActivityMainBinding
import com.mj.naversearch.ui.common.ktx.invoke
import com.mj.naversearch.ui.common.ktx.registerForFragmentResult
import com.mj.naversearch.ui.main.MainViewModel.MainEvent.Searching
import com.mj.naversearch.ui.result.ResultActivity
import com.mj.naversearch.ui.search.SearchDialog
import timber.log.Timber

class MainActivity : BaseActivity<ActivityMainBinding, MainViewModel>() {

    override val viewModel: MainViewModel by viewModels()

    override val layoutResourceId: Int
        get() = R.layout.activity_main

    override fun initOnCreate(savedInstanceState: Bundle?) {
        binding.vm = viewModel

        repeatOnCreated {
            viewModel.uiEvent.collect { event ->
                when(event){
                    Searching -> {
                        showSearchPopup()
                    }
                }
            }
        }
    }

    private val showSearchPopup = registerForFragmentResult(
        "POPUP_SEARCH", SearchDialog.Contract()
    ) { result ->
        if (result == null) return@registerForFragmentResult
        Timber.e("** result = ${result.keyword}")
        ResultActivity.start(this, result.keyword)
    }
}