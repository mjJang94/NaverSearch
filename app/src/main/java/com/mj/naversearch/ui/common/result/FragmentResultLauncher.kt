package com.mj.naversearch.ui.common.result

fun interface FragmentResultLauncher<I> {
    fun launch(input: I)
}