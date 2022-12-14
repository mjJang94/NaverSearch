package com.mj.naversearch.util

import android.os.Build
import android.text.Html
import android.view.View
import android.view.inputmethod.EditorInfo
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.widget.AppCompatImageView
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DecodeFormat
import com.mj.naversearch.R


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

    @JvmStatic
    @BindingAdapter("thumbnailUrl")
    fun AppCompatImageView.setListThumbnailUrl(url: String?) {
        if (url == null) {
            setImageResource(R.drawable.ic_browser_not_supported)
        } else {
            Glide.with(context)
                .load(url)
                .error(R.drawable.ic_browser_not_supported)
                .centerCrop()
                .skipMemoryCache(false)
                .format(DecodeFormat.PREFER_RGB_565)
                .into(this)
        }
    }
}

