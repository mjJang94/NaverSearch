package com.mj.naversearch.ui.common.util

import android.app.Dialog
import android.content.DialogInterface
import android.view.KeyEvent
import androidx.appcompat.app.AlertDialog

fun interface OnBackPressedListener {
    fun onBackPressed(): Boolean
}

fun Dialog.doOnBackPressed(listener: OnBackPressedListener) {
    setOnKeyListener(BackPressedCapture(listener))
}

private class BackPressedCapture(
    private val listener: OnBackPressedListener
) : DialogInterface.OnKeyListener {
    override fun onKey(dialog: DialogInterface?, keyCode: Int, event: KeyEvent?): Boolean =
        if (event?.action == KeyEvent.ACTION_UP &&
            (keyCode == KeyEvent.KEYCODE_BACK || keyCode == KeyEvent.KEYCODE_ESCAPE)
        ) listener.onBackPressed() else false
}