package ru.arzonpay.android.ui.util

import android.content.Context
import android.graphics.drawable.Drawable
import androidx.annotation.ColorInt
import androidx.core.content.ContextCompat
import ru.arzonpay.android.base_feature.R
import ru.arzonpay.android.domain.payment.Status
import ru.arzonpay.android.ui.view.extensions.getThemeColor


fun Status.getDrawable(context: Context): Drawable? {
    val res = when (this) {
        Status.CREATED -> R.drawable.ic_sad
        Status.DECLINED -> R.drawable.ic_sad
        Status.SUCCESS -> R.drawable.ic_check_circle
        Status.PROCESSING -> R.drawable.ic_clock
    }
    return ContextCompat.getDrawable(context, res)
}

@ColorInt
fun Status.getColor(context: Context): Int {
    val res = when (this) {
        Status.CREATED -> R.attr.colorTransactionDeclined
        Status.DECLINED -> R.attr.colorTransactionDeclined
        Status.PROCESSING -> R.attr.colorTransactionProcessing
        Status.SUCCESS -> R.attr.colorTransactionSuccess
    }

    return context.getThemeColor(res)
}

fun Status.getText(context: Context): String {
    val res = when (this) {
        Status.CREATED -> R.string.status_created
        Status.DECLINED -> R.string.status_declined
        Status.PROCESSING -> R.string.status_processing
        Status.SUCCESS -> R.string.status_success
    }

    return context.getString(res)
}