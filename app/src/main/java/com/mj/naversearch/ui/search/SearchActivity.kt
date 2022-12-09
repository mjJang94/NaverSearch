package com.mj.naversearch.ui.search

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.WindowManager
import androidx.activity.viewModels
import com.mj.naversearch.R
import com.mj.naversearch.base.BaseActivity
import com.mj.naversearch.databinding.ActivitySearchBinding
import com.mj.naversearch.ui.common.style.StatusBarStyle
import com.mj.naversearch.ui.result.ResultActivity
import com.mj.naversearch.ui.search.SearchViewModel.SearchEvent.Dismiss
import com.mj.naversearch.ui.search.SearchViewModel.SearchEvent.Search
import com.mj.naversearch.util.showKeyboard
import kotlinx.coroutines.CoroutineScope

class SearchActivity : BaseActivity<ActivitySearchBinding, SearchViewModel>(), CoroutineScope {

    companion object {
        fun start(context: Context, query: String?) {
            val intent = Intent(context, SearchActivity::class.java).apply {
                putExtra("EXTRA_QUERY", query)
            }
            context.startActivity(intent)
        }
    }

    override val layoutResourceId: Int
        get() = R.layout.activity_search

    override val viewModel: SearchViewModel by viewModels()


    override fun initOnCreate(savedInstanceState: Bundle?) {
        StatusBarStyle.SEARCH.apply(this)
        window?.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE)

        binding.vm = viewModel

        if (savedInstanceState == null) {
            val query = intent?.getStringExtra("EXTRA_QUERY")
            viewModel.configure(query)
        }

        repeatOnCreated {
            viewModel.uiEvent.collect { event ->
                when (event) {
                    is Search -> ResultActivity.start(this@SearchActivity, event.query)
                    is Dismiss -> finish()
                }
            }
        }
    }

    override fun onResume() {
        super.onResume()
        binding.editQuery.showKeyboard()
    }
}