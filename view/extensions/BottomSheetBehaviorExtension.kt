package ru.arzonpay.android.ui.view.extensions

import android.view.View
import com.google.android.material.bottomsheet.BottomSheetBehavior

/**
 * Устанавливает состояние STATE_EXPANDED для BottomSheetBehavior
 */
fun <T : View> BottomSheetBehavior<T>.expand() {
    state = BottomSheetBehavior.STATE_EXPANDED
}

/**
 * Устанавливает состояние STATE_COLLAPSED для BottomSheetBehavior
 */
fun <T : View> BottomSheetBehavior<T>.collapse() {
    state = BottomSheetBehavior.STATE_COLLAPSED
}

/**
 * Устанавливает состояние STATE_HIDDEN для BottomSheetBehavior
 */
fun <T : View> BottomSheetBehavior<T>.hide() {
    isHideable = true
    state = BottomSheetBehavior.STATE_HIDDEN
}
