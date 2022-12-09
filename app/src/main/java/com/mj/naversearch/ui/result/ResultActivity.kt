package com.mj.naversearch.ui.result

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.viewModels
import com.mj.naversearch.R
import com.mj.naversearch.base.BaseActivity
import com.mj.naversearch.databinding.ActivityResultBinding
import com.mj.naversearch.ui.common.ktx.invoke
import com.mj.naversearch.ui.common.ktx.registerForFragmentResult
import com.mj.naversearch.ui.result.ResultViewModel.ResultEvent.*
import com.mj.naversearch.ui.search.SearchActivity
import timber.log.Timber

class ResultActivity : BaseActivity<ActivityResultBinding, ResultViewModel>() {

    companion object {
        fun start(context: Context, query: String) {
            val intent = Intent(context, ResultActivity::class.java).apply {
                putExtra("EXTRA_QUERY", query)
            }
            context.startActivity(intent)
        }
    }

    override val layoutResourceId: Int
        get() = R.layout.activity_result

    override val viewModel: ResultViewModel by viewModels()

    override fun initOnCreate(savedInstanceState: Bundle?) {

        binding.vm = viewModel

        if (savedInstanceState == null) {
            val query = intent?.getStringExtra("EXTRA_QUERY") ?: return finish()
            viewModel.configure(query)
        }

        repeatOnCreated {
            viewModel.uiEvent.collect { event ->
                when (event) {
                    is Back -> finish()
                    is Searching -> SearchActivity.start(this@ResultActivity, event.query)
                }
            }
        }
    }
}