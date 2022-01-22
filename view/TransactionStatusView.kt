package ru.arzonpay.android.ui.view

import android.content.Context
import android.util.AttributeSet
import com.google.android.material.textview.MaterialTextView
import ru.arzonpay.android.domain.payment.Status
import ru.arzonpay.android.ui.util.getColor
import ru.arzonpay.android.ui.util.getDrawable
import ru.arzonpay.android.ui.util.getText

class TransactionStatusView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    style: Int = android.R.attr.textViewStyle,
    defStyleRes: Int = 0
) : MaterialTextView(context, attrs, style, defStyleRes) {

    var status: Status = Status.DECLINED
        set(value) {
            field = value
            setCompoundDrawablesRelativeWithIntrinsicBounds(
                value.getDrawable(context),
                null,
                null,
                null
            )
            setTextColor(value.getColor(context))
            text = value.getText(context)
        }
}