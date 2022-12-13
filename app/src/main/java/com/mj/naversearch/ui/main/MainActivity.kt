package com.mj.naversearch.ui.main

import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import com.mj.naversearch.R
import com.mj.naversearch.base.BaseActivity
import com.mj.naversearch.databinding.ActivityMainBinding
import com.mj.naversearch.ui.common.ktx.invoke
import com.mj.naversearch.ui.common.ktx.registerForFragmentResult
import com.mj.naversearch.ui.main.MainViewModel.MainEvent.Searching
import com.mj.naversearch.ui.result.ResultActivity
import com.mj.naversearch.ui.search.SearchActivity
import timber.log.Timber

class MainActivity : BaseActivity<ActivityMainBinding, MainViewModel>() {

    override val viewModel: MainViewModel by viewModels()

    override val layoutResourceId: Int
        get() = R.layout.activity_main

    private var waitTime = 0L

    override fun initOnCreate(savedInstanceState: Bundle?) {
        binding.vm = viewModel

        repeatOnCreated {
            viewModel.uiEvent.collect { event ->
                when(event){
                    Searching -> SearchActivity.start(this@MainActivity, null)
                }
            }
        }
    }

    override fun onBackPressed() {
        if (supportFragmentManager.backStackEntryCount > 0) {
            supportFragmentManager.popBackStack()
        } else {
            if (System.currentTimeMillis() - waitTime >= 1500) {
                waitTime = System.currentTimeMillis()
                Toast.makeText(this, "뒤로가기 버튼을 한번 더 누르면 종료됩니다.", Toast.LENGTH_SHORT).show()
            } else finish()
        }
    }
}