package com.mj.naversearch.ui.result.books

import android.os.Bundle
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import com.mj.naversearch.R
import com.mj.naversearch.base.BaseFragment
import com.mj.naversearch.databinding.FragmentBooksBinding
import com.mj.naversearch.ui.result.ResultViewModel
import com.mj.naversearch.util.StringExtra
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class BooksFragment : BaseFragment<FragmentBooksBinding>() {

    companion object {
        var Bundle.extraQuery by StringExtra("EXTRA_QUERY")
    }

    private val parentModel: ResultViewModel by activityViewModels()
    private val viewModel: BooksViewModel by viewModels()

    override val layoutResourceId: Int
        get() = R.layout.fragment_books

    override fun initOnCreateView(savedInstanceState: Bundle?) {
        binding.vm = viewModel

        if (savedInstanceState == null) {
            val query = arguments?.extraQuery
            viewModel.configure(query)
        }

        viewModel.contentClick.observe(viewLifecycleOwner) { link ->
            parentModel.openLink(link)
        }

        viewModel.errorEvent.observe(viewLifecycleOwner) { tr ->
            toast(tr.message ?: "알 수 없는 에러가 발생했습니다.")
        }
    }
}