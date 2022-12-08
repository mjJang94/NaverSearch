package com.mj.naversearch.ui.result

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.viewModels
import com.mj.naversearch.R
import com.mj.naversearch.base.BaseActivity
import com.mj.naversearch.databinding.ActivityResultBinding

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
        if (savedInstanceState == null) {
            val param = intent?.getStringExtra("EXTRA_QUERY") ?: return finish()
            // api 조회

        }
    }
}