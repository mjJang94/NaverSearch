package com.mj.naversearch.ui.result.encyc

import android.os.Bundle
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import com.mj.naversearch.R
import com.mj.naversearch.base.BaseFragment
import com.mj.naversearch.databinding.FragmentEncycBinding
import com.mj.naversearch.ui.result.ResultViewModel
import com.mj.naversearch.ui.result.encyc.EncycViewModel.EncycEvent.*
import com.mj.naversearch.util.StringExtra
import com.mj.naversearch.util.hide
import com.mj.naversearch.util.visible
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class EncycFragment : BaseFragment<FragmentEncycBinding>() {

    companion object {
        var Bundle.extraQuery by StringExtra("EXTRA_QUERY")
    }

    private val parentModel: ResultViewModel by activityViewModels()
    private val viewModel: EncycViewModel by viewModels()

    override val layoutResourceId: Int
        get() = R.layout.fragment_encyc

    override fun initOnViewCreated(savedInstanceState: Bundle?) {

        with(binding) {
            vm = viewModel

            if (savedInstanceState == null) {
                val query = arguments?.extraQuery
                viewModel.configure(query)
            }

            repeatOnOwnerCreated {
                launch {
                    viewModel.uiEvent.collect { event ->
                        when (event) {
                            is LoadSuccess, is Loading -> emptyDescription.hide()
                            is ItemsEmpty -> emptyDescription.visible()
                            is OpenLink -> parentModel.openLink(event.link)
                            is LoadError -> toast(event.error.message ?: "알 수 없는 에러가 발생했습니다.")
                        }
                    }
                }
            }
        }
    }
}