package com.mj.naversearch.ui.result

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.activity.viewModels
import androidx.fragment.app.Fragment
import com.mj.naversearch.R
import com.mj.naversearch.base.BaseActivity
import com.mj.naversearch.databinding.ActivityResultBinding
import com.mj.naversearch.ui.result.ResultViewModel.Category.*
import com.mj.naversearch.ui.result.ResultViewModel.ResultEvent.Back
import com.mj.naversearch.ui.result.ResultViewModel.ResultEvent.Searching
import com.mj.naversearch.ui.result.books.BooksFragment
import com.mj.naversearch.ui.result.encyc.EncycFragment
import com.mj.naversearch.ui.result.news.NewsFragment
import com.mj.naversearch.ui.result.news.NewsFragment.Companion.extraQuery
import com.mj.naversearch.ui.search.SearchActivity
import com.mj.naversearch.util.StringExtra
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ResultActivity : BaseActivity<ActivityResultBinding, ResultViewModel>() {

    companion object {
        var Intent.extraQuery by StringExtra("EXTRA_QUERY")

        fun start(context: Context, query: String) {
            val intent = Intent(context, ResultActivity::class.java).apply {
                extraQuery = query
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
            val query = intent?.extraQuery ?: return finish()
            viewModel.configure(query)
        }

        viewModel.selectedCategoryInfo.observe(this) { (query, id) ->
            with(supportFragmentManager) {
                val compositions: Fragment = when (ResultViewModel.Category[id]) {
                    News -> NewsFragment().apply { putQuery(query) }
                    Books -> BooksFragment().apply { putQuery(query) }
                    Encyclopedia -> EncycFragment().apply { putQuery(query) }
                    QnA -> NewsFragment()
                    Image -> NewsFragment()
                }

                with(beginTransaction()) {
                    replace(binding.container.id, compositions)
                    commitAllowingStateLoss()
                }
            }
        }

        viewModel.openLink.observe(this) { link ->
            startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(link)))
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

    private fun Fragment.putQuery(data: String) {
        arguments = Bundle().apply {
            extraQuery = data
        }
    }
}