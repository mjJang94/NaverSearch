package com.mj.naversearch.ui.common.result

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.FragmentManager

interface FragmentResultContract<I, O> {

    fun createArguments(context: Context, input: I): Bundle? = null

    fun onLaunch(fragmentManager: FragmentManager, arguments: Bundle)

    fun parseResult(bundle: Bundle): O
}