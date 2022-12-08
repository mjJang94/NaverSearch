package com.mj.naversearch.ui.common.result

fun interface FragmentResultCallback<O> {
    fun onFragmentResult(result: O)
}