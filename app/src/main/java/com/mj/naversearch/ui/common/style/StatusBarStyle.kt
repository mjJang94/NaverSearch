package com.mj.naversearch.ui.common.style

import android.app.Activity
import android.graphics.Color
import android.view.Window
import androidx.annotation.ColorInt
import androidx.annotation.ColorRes
import androidx.core.content.ContextCompat
import androidx.core.graphics.luminance
import androidx.core.view.WindowInsetsControllerCompat
import androidx.fragment.app.DialogFragment
import com.mj.naversearch.R
import kotlin.math.max
import kotlin.math.min

enum class StatusBarStyle(
    @ColorRes private val color: Int,
    private val isLight: Boolean? = null,
) {
    DEFAULT(R.color.default_background, null),
    SEARCH(R.color.default_background, null),
    ;

    fun apply(owner: Activity) = apply(owner, color, isLight)

    fun apply(owner: DialogFragment) = apply(owner, color, isLight)

    fun apply(owner: Window) = apply(owner, color, isLight)

    companion object {
        fun apply(owner: Activity, @ColorRes color: Int, isLight: Boolean? = null) {
            owner.window.applyImpl(color, isLight)
        }

        fun apply(owner: DialogFragment, @ColorRes color: Int, isLight: Boolean? = null) {
            owner.dialog?.window?.applyImpl(color, isLight)
        }

        fun apply(owner: Window, @ColorRes color: Int, isLight: Boolean? = null) {
            owner.applyImpl(color, isLight)
        }

        // 너무 어둡지만 않으면 Light Status Bar로 판단
        // Color.BLACK과 휘도 차이가 큰(>= 4.5f) 경우 Light Status Bar
        private fun Window.applyImpl(@ColorRes color: Int, isLight: Boolean?) {
            statusBarColor = ContextCompat.getColor(context, color)

            // Determine with Contrast Ratio on Black (ref: https://webaim.org/articles/contrast)
            WindowInsetsControllerCompat(this, decorView).isAppearanceLightStatusBars =
                isLight ?: (statusBarColor.contrastRatioBy(Color.BLACK) >= 4.5f)
        }

        private fun @receiver:ColorInt Int.contrastRatioBy(@ColorInt other: Int): Float =
            luminance.contrastRatioBy(other.luminance)

        // Add 0.05f to prevent divide by zero
        private fun Float.contrastRatioBy(other: Float) =
            (max(this, other) + 0.05f) / (min(this, other) + 0.05f)
    }
}
