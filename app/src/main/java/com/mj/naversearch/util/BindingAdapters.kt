package com.mj.naversearch.util

import android.os.Build
import android.text.Html
import android.text.Spanned
import android.view.View
import android.view.inputmethod.EditorInfo
import android.widget.EditText
import android.widget.TextView
import androidx.databinding.BindingAdapter


object BindingAdapters {

    @JvmStatic
    @BindingAdapter("visible")
    fun View.setVisibility(visible: Boolean?) {
        if (visible == null) return

        val visibility = when (visible) {
            true -> View.VISIBLE
            else -> View.GONE
        }

        this.visibility = visibility
    }

    @JvmStatic
    @BindingAdapter("searchAction")
    fun EditText.setSearchActionEvent(event: () -> Unit?) {

        setOnEditorActionListener { _, id, _ ->
            var handled = false

            if (id == EditorInfo.IME_ACTION_SEARCH) {
                event.invoke()
                handled = true
            }
            return@setOnEditorActionListener handled
        }
    }

    @JvmStatic
    @BindingAdapter("htmlText")
    fun TextView.fromHtml(source: String?) {
        if (source == null) return

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            text = Html.fromHtml(source, Html.FROM_HTML_MODE_LEGACY)
        } else {
            @Suppress("DEPRECATION")
            text = Html.fromHtml(source)
        }
    }
}

