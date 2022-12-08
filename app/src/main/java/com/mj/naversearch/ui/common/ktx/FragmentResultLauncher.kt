package com.mj.naversearch.ui.common.ktx

import com.mj.naversearch.ui.common.result.FragmentResultLauncher

@Suppress("NOTHING_TO_INLINE")
inline operator fun FragmentResultLauncher<Unit>.invoke() = launch(Unit)

@Suppress("NOTHING_TO_INLINE")
inline operator fun <I> FragmentResultLauncher<I>.invoke(input: I) = launch(input)
