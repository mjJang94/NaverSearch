package com.mj.naversearch.util

import android.view.View
import android.view.inputmethod.EditorInfo
import android.widget.EditText
import androidx.databinding.BindingAdapter


object BindingAdapters{

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
    fun EditText.setSearchActionEvent(event : () -> Unit?){

        setOnEditorActionListener { _, id, _ ->
            var handled = false

            if (id == EditorInfo.IME_ACTION_SEARCH){
                event.invoke()
                handled = true
            }
            return@setOnEditorActionListener handled
        }
    }
}

