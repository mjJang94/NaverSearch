package com.mj.naversearch.ui.common.ktx

import android.os.Bundle
import androidx.fragment.app.DialogFragment
import com.mj.naversearch.base.BaseDialog

var Bundle.extraRequestKey: String? by BaseDialog.StringExtra("${DialogFragment::class.qualifiedName}.EXTRA_REQUEST_KEY")