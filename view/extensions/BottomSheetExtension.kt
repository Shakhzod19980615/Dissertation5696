package ru.arzonpay.android.ui.view.extensions

import android.widget.FrameLayout
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import ru.arzonpay.android.base_feature.R

private val BOTTOM_SHEET_ID = R.id.design_bottom_sheet

/**
 * Получение root-контейнера [BottomSheetDialog]'а.
 */
fun BottomSheetDialog.getBottomSheetContainer(): FrameLayout? {
    return findViewById(BOTTOM_SHEET_ID)
}

/**
 * Получение [BottomSheetBehavior] из [BottomSheetDialog]'а.
 */
fun BottomSheetDialog.getBottomSheetBehavior(): BottomSheetBehavior<*>? {
    val bottomSheet = getBottomSheetContainer() ?: return null
    return BottomSheetBehavior.from(bottomSheet)
}