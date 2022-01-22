package ru.arzonpay.android.ui.util.back_press

import androidx.fragment.app.Fragment
import ru.surfstudio.android.utilktx.ktx.ui.activity.hideKeyboard

fun Fragment.addDefaultOnBackPressedCallback(onBackPressed: () -> Unit) {
    requireActivity().onBackPressedDispatcher.addCallback(
        this,
        DefaultBackPressedCallback(onBackPressed)
    )
}

fun Fragment.hideKeyboard() {
    requireActivity().hideKeyboard()
}